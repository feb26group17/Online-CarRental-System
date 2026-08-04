import { useState, useEffect } from 'react';
import { crudApi } from '../../api/axios';

// VehicleRequest (POST /api/vehicles):
//   { modelId*, registrationNumber*, fuelType*, rentPerDay*, status }
//   NOTE: brandId, color, description, imageUrl are NOT accepted by backend
//
// FuelType enum: Diesel | Petrol | CNG | Battery
// VehicleStatus enum: Available | Booked | Maintenance
//
// BrandResponse: { brandId, bname }
// ModelResponse: { modelId, brandId, brandName, modelName, seatingCapacity }
// VehicleResponse: { vehicleId, userId, modelId, modelName, brandName,
//                    seatingCapacity, registrationNumber, fuelType, rentPerDay, status }

const FUEL_TYPES = ['Diesel', 'Petrol', 'CNG', 'Battery'];

function AddVehicleModal({ onClose, onAddSuccess }) {
  const [brands, setBrands] = useState([]);
  const [models, setModels] = useState([]);
  const [form, setForm] = useState({
    brandId: '',
    modelId: '',
    registrationNumber: '',  // correct field name
    fuelType: '',            // required by backend
    rentPerDay: '',          // correct field name
  });
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState('');

  useEffect(() => {
    fetchBrands();
  }, []);

  const fetchBrands = async () => {
    try {
      const res = await crudApi.get('/brands');
      setBrands(res.data || []);
    } catch (e) {
      console.error('Failed to load brands', e);
    }
  };

  const handleBrandChange = async (brandId) => {
    setForm(f => ({ ...f, brandId, modelId: '' }));
    if (brandId) {
      try {
        const res = await crudApi.get(`/models?brandId=${brandId}`);
        setModels(res.data || []);
      } catch (e) {
        console.error('Failed to load models', e);
      }
    } else {
      setModels([]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.modelId || !form.registrationNumber || !form.fuelType || !form.rentPerDay) {
      return setErr('Model, Registration Number, Fuel Type, and Price per day are required.');
    }

    setLoading(true);
    setErr('');
    try {
      // VehicleRequest: modelId, registrationNumber, fuelType, rentPerDay, status
      await crudApi.post('/vehicles', {
        modelId: parseInt(form.modelId),
        registrationNumber: form.registrationNumber,
        fuelType: form.fuelType,                         // must match FuelType enum exactly
        rentPerDay: parseFloat(form.rentPerDay),
        status: 'Available'                              // VehicleStatus.Available
      });
      onAddSuccess();
      onClose();
    } catch (error) {
      setErr(error.response?.data?.message || 'Failed to add vehicle');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', display: 'flex',
      alignItems: 'center', justifyContent: 'center', zIndex: 1000
    }}>
      <div style={{
        background: '#fff', borderRadius: 14, padding: '28px', width: '100%', maxWidth: 480,
        boxShadow: '0 8px 32px rgba(0,0,0,0.18)', maxHeight: '90vh', overflowY: 'auto'
      }}>
        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 18 }}>Add New Vehicle</h3>

        {err && <div className="alert-error" style={{ marginBottom: 14 }}>{err}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Car Brand *</label>
            {/* BrandResponse: brandId, bname */}
            <select value={form.brandId} onChange={e => handleBrandChange(e.target.value)} required
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
              <option value="">Select Brand</option>
              {brands.map(b => <option key={b.brandId} value={b.brandId}>{b.bname}</option>)}
            </select>
          </div>

          <div className="form-group">
            <label>Car Model *</label>
            {/* ModelResponse: modelId, modelName, seatingCapacity */}
            <select value={form.modelId} onChange={e => setForm({ ...form, modelId: e.target.value })} required disabled={!form.brandId}
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14, opacity: !form.brandId ? 0.6 : 1 }}>
              <option value="">Select Model</option>
              {models.map(m => <option key={m.modelId} value={m.modelId}>{m.modelName} ({m.seatingCapacity} seats)</option>)}
            </select>
          </div>

          <div className="form-group">
            {/* registrationNumber is the correct field name (not vehicleNumber) */}
            <label>Registration Number *</label>
            <input type="text" placeholder="e.g. MH12AB1234" value={form.registrationNumber}
              onChange={e => setForm({ ...form, registrationNumber: e.target.value })} required />
          </div>

          <div className="form-group">
            {/* fuelType is required — FuelType enum: Diesel | Petrol | CNG | Battery */}
            <label>Fuel Type *</label>
            <select value={form.fuelType} onChange={e => setForm({ ...form, fuelType: e.target.value })} required
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
              <option value="">Select Fuel Type</option>
              {FUEL_TYPES.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
          </div>

          <div className="form-group">
            {/* rentPerDay is the correct field name (not rentalPricePerDay) */}
            <label>Daily Rental Rate (₹) *</label>
            <input type="number" step="0.01" placeholder="e.g. 1500" value={form.rentPerDay}
              onChange={e => setForm({ ...form, rentPerDay: e.target.value })} required />
          </div>

          <div style={{ background: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: 8, padding: '10px 14px', marginBottom: 16, fontSize: 13, color: '#64748b' }}>
            ℹ️ Vehicle will be listed as <strong>Available</strong> immediately after submission.
          </div>

          <div style={{ display: 'flex', gap: 10, marginTop: 20 }}>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Submitting...' : 'Add Vehicle'}
            </button>
            <button type="button" onClick={onClose} style={{
              flex: 1, padding: '12px', background: '#f1f5f9', border: 'none', borderRadius: 8,
              fontSize: 14, fontWeight: 600, cursor: 'pointer', color: '#475569'
            }}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}

function MyVehicles() {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [deleteId, setDeleteId] = useState(null);

  useEffect(() => {
    fetchMyVehicles();
  }, []);

  const fetchMyVehicles = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/vehicles/my');
      setVehicles(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch your listed vehicles');
    } finally {
      setLoading(false);
    }
  };

  const toggleStatus = async (car) => {
    // VehicleStatus enum: Available | Booked | Maintenance
    const newStatus = car.status === 'Available' ? 'Maintenance' : 'Available';
    try {
      // vehicleId is the correct id field
      await crudApi.patch(`/vehicles/${car.vehicleId}/status`, { status: newStatus });
      fetchMyVehicles();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update vehicle status');
    }
  };

  const handleDelete = async (vehicleId) => {
    try {
      await crudApi.delete(`/vehicles/${vehicleId}`);
      fetchMyVehicles();
      setDeleteId(null);
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to delete vehicle');
    }
  };

  // VehicleStatus: Available | Booked | Maintenance
  const statusStyle = (status) => {
    if (status === 'Available')   return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Booked')      return { bg: '#eff6ff', text: '#2563eb' };
    return { bg: '#fff7ed', text: '#ea580c' }; // Maintenance
  };

  return (
    <div className="page-container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 className="page-heading">My Vehicles</h2>
          <p className="page-sub">Manage your listed rental fleet</p>
        </div>
        <button className="btn-primary" style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}
          onClick={() => setShowModal(true)}>
          + Add Vehicle
        </button>
      </div>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading your fleet...</div>
      ) : vehicles.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">🚗</div>
          <h3>No Vehicles Listed Yet</h3>
          <p>Click "+ Add Vehicle" to list your first car for rental.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
          {vehicles.map(car => {
            // VehicleResponse: vehicleId, modelId, modelName, brandName,
            //                  seatingCapacity, registrationNumber, fuelType, rentPerDay, status
            const ss = statusStyle(car.status);
            return (
              <div key={car.vehicleId} style={{
                background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden',
                boxShadow: '0 1px 3px rgba(0,0,0,0.05)', display: 'flex', flexDirection: 'column'
              }}>
                <div style={{
                  background: '#f8fafc', height: 130, display: 'flex', alignItems: 'center',
                  justifyContent: 'center', borderBottom: '1px solid #e2e8f0', overflow: 'hidden'
                }}>
                  <span style={{ fontSize: 52 }}>🚗</span>
                </div>
                <div style={{ padding: '14px 16px', flex: 1, display: 'flex', flexDirection: 'column' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
                    <div>
                      <div style={{ fontSize: 11, fontWeight: 700, color: '#2563eb', textTransform: 'uppercase' }}>{car.brandName}</div>
                      <div style={{ fontSize: 15, fontWeight: 700, color: '#1e293b' }}>{car.modelName || car.registrationNumber}</div>
                    </div>
                    <span style={{
                      fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                      background: ss.bg, color: ss.text
                    }}>
                      {car.status}
                    </span>
                  </div>

                  <div style={{ fontSize: 12, color: '#64748b', display: 'flex', gap: 10, flexWrap: 'wrap', marginBottom: 8 }}>
                    <span>⛽ {car.fuelType || 'N/A'}</span>
                    <span>🪑 {car.seatingCapacity || 5} seats</span>
                    {/* registrationNumber is the correct field */}
                    <span>🔖 {car.registrationNumber}</span>
                  </div>

                  <div style={{ fontSize: 17, fontWeight: 800, color: '#2563eb', marginBottom: 14 }}>
                    {/* rentPerDay is the correct field */}
                    ₹{car.rentPerDay}<span style={{ fontSize: 12, fontWeight: 400, color: '#94a3b8' }}>/day</span>
                  </div>

                  <div style={{ display: 'flex', gap: 8, marginTop: 'auto' }}>
                    {/* Only allow toggling if not Booked (can't un-book manually) */}
                    <button onClick={() => toggleStatus(car)} disabled={car.status === 'Booked'} style={{
                      flex: 1, padding: '7px', fontSize: 12, fontWeight: 600, cursor: car.status === 'Booked' ? 'not-allowed' : 'pointer',
                      border: '1px solid #e2e8f0', borderRadius: 7,
                      background: car.status === 'Booked' ? '#f1f5f9' : '#f8fafc', color: '#475569',
                      opacity: car.status === 'Booked' ? 0.6 : 1
                    }}>
                      {car.status === 'Available' ? '⏸ Set Maintenance' : car.status === 'Maintenance' ? '✅ Set Available' : '🔒 Booked'}
                    </button>
                    <button onClick={() => setDeleteId(car.vehicleId)} style={{
                      padding: '7px 12px', fontSize: 12, fontWeight: 600, cursor: 'pointer',
                      border: '1px solid #fca5a5', borderRadius: 7, background: '#fef2f2', color: '#dc2626'
                    }}>
                      🗑
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {showModal && (
        <AddVehicleModal
          onClose={() => setShowModal(false)}
          onAddSuccess={fetchMyVehicles}
        />
      )}

      {deleteId && (
        <div style={{
          position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', display: 'flex',
          alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{ background: '#fff', borderRadius: 12, padding: '24px 28px', maxWidth: 360, width: '100%' }}>
            <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 10 }}>Remove vehicle?</h3>
            <p style={{ fontSize: 14, color: '#64748b', marginBottom: 20 }}>
              This will permanently remove this vehicle from your rental listings.
            </p>
            <div style={{ display: 'flex', gap: 10 }}>
              <button onClick={() => handleDelete(deleteId)} style={{
                flex: 1, padding: '10px', background: '#dc2626', color: '#fff', border: 'none',
                borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14
              }}>Remove</button>
              <button onClick={() => setDeleteId(null)} style={{
                flex: 1, padding: '10px', background: '#f1f5f9', color: '#475569', border: 'none',
                borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14
              }}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default MyVehicles;
