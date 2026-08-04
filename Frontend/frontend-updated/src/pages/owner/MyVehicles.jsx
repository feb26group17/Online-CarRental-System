import { useState, useEffect } from 'react';
import { crudApi } from '../../api/axios';

// VehicleRequest (POST /api/vehicles):
//   { modelId*, registrationNumber*, fuelType*, rentPerDay*, status }
//
// FuelType enum: Diesel | Petrol | CNG | Battery
// VehicleStatus enum: Available | Booked | Maintenance
//
// BrandResponse: { brandId, bname }
// ModelResponse: { modelId, brandId, brandName, modelName, seatingCapacity }
// VehicleResponse: { vehicleId, userId, modelId, modelName, brandName,
//                    seatingCapacity, registrationNumber, fuelType, rentPerDay, status }

const FUEL_TYPES = ['Diesel', 'Petrol', 'CNG', 'Battery'];

function AddEditVehicleModal({ onClose, onSuccess, initialData = null }) {
  const [brands, setBrands] = useState([]);
  const [allModels, setAllModels] = useState([]);
  const [models, setModels] = useState([]);
  const [form, setForm] = useState({
    brandId: '',
    modelId: initialData?.modelId ? String(initialData.modelId) : '',
    registrationNumber: initialData?.registrationNumber || '',
    fuelType: initialData?.fuelType || '',
    rentPerDay: initialData?.rentPerDay !== undefined ? String(initialData.rentPerDay) : '',
    status: initialData?.status || 'Available'
  });
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState('');

  useEffect(() => {
    fetchBrandsAndInitialModels();
  }, []);

  const fetchBrandsAndInitialModels = async () => {
    try {
      const res = await crudApi.get('/brands');
      const brandList = res.data || [];
      setBrands(brandList);

      const mRes = await crudApi.get('/models');
      const modelList = mRes.data || [];
      setAllModels(modelList);

      if (initialData) {
        let currentBrandId = null;
        if (initialData.brandName) {
          const matchedBrand = brandList.find(b => b.bname === initialData.brandName);
          if (matchedBrand) {
            currentBrandId = String(matchedBrand.brandId);
          }
        }

        if (currentBrandId) {
          setForm(f => ({ ...f, brandId: currentBrandId }));
          const filtered = modelList.filter(m => String(m.brandId) === currentBrandId);
          setModels(filtered.length > 0 ? filtered : modelList);
        } else {
          setModels(modelList);
        }
      } else {
        setModels(modelList);
      }
    } catch (e) {
      console.error('Failed to load brands/models', e);
    }
  };

  const handleBrandChange = async (brandId) => {
    setForm(f => ({ ...f, brandId, modelId: '' }));
    if (brandId) {
      try {
        const res = await crudApi.get(`/models?brandId=${brandId}`);
        setModels(res.data || []);
      } catch (e) {
        console.error('Failed to load models for brand', e);
      }
    } else {
      setModels(allModels);
    }
  };

  const handleModelChange = (modelId) => {
    const selected = allModels.find(m => String(m.modelId) === String(modelId));
    setForm(f => ({
      ...f,
      modelId,
      brandId: selected ? String(selected.brandId) : f.brandId
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.modelId || !form.registrationNumber || !form.fuelType || !form.rentPerDay) {
      return setErr('Model, Registration Number, Fuel Type, and Price per day are required.');
    }

    setLoading(true);
    setErr('');
    try {
      const payload = {
        modelId: parseInt(form.modelId),
        registrationNumber: form.registrationNumber,
        fuelType: form.fuelType,
        rentPerDay: parseFloat(form.rentPerDay),
        status: form.status
      };

      if (initialData?.vehicleId) {
        await crudApi.put(`/vehicles/${initialData.vehicleId}`, payload);
      } else {
        await crudApi.post('/vehicles', payload);
      }

      onSuccess();
      onClose();
    } catch (error) {
      setErr(error.response?.data?.message || 'Failed to save vehicle details');
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
        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 18 }}>
          {initialData ? '✏️ Edit Vehicle Details' : '🚗 Add New Vehicle'}
        </h3>

        {err && <div className="alert-error" style={{ marginBottom: 14 }}>{err}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Car Brand *</label>
            <select value={form.brandId} onChange={e => handleBrandChange(e.target.value)} required
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
              <option value="">Select Brand</option>
              {brands.map(b => <option key={b.brandId} value={b.brandId}>{b.bname}</option>)}
            </select>
          </div>

          <div className="form-group">
            <label>Car Model *</label>
            <select value={form.modelId} onChange={e => handleModelChange(e.target.value)} required
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
              <option value="">Select Model</option>
              {models.map(m => (
                <option key={m.modelId} value={m.modelId}>
                  {m.brandName ? `${m.brandName} ` : ''}{m.modelName} ({m.seatingCapacity} seats)
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Registration Number *</label>
            <input type="text" placeholder="e.g. MH12AB1234" value={form.registrationNumber}
              onChange={e => setForm({ ...form, registrationNumber: e.target.value })} required />
          </div>

          <div className="form-group">
            <label>Fuel Type *</label>
            <select value={form.fuelType} onChange={e => setForm({ ...form, fuelType: e.target.value })} required
              style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
              <option value="">Select Fuel Type</option>
              {FUEL_TYPES.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
          </div>

          <div className="form-group">
            <label>Daily Rental Rate (₹) *</label>
            <input type="number" step="0.01" placeholder="e.g. 1500" value={form.rentPerDay}
              onChange={e => setForm({ ...form, rentPerDay: e.target.value })} required />
          </div>

          {initialData && (
            <div className="form-group">
              <label>Availability Status</label>
              <select value={form.status} onChange={e => setForm({ ...form, status: e.target.value })}
                style={{ width: '100%', padding: '10px 12px', border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
                <option value="Available">Available</option>
                <option value="Maintenance">Maintenance</option>
                <option value="Booked" disabled>Booked (Controlled by Customer Bookings)</option>
              </select>
            </div>
          )}

          <div style={{ display: 'flex', gap: 10, marginTop: 20 }}>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Saving...' : (initialData ? 'Save Changes' : 'Add Vehicle')}
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
  const [editingVehicle, setEditingVehicle] = useState(null);
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
    const newStatus = car.status === 'Available' ? 'Maintenance' : 'Available';
    try {
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

  const statusStyle = (status) => {
    if (status === 'Available')   return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Booked')      return { bg: '#eff6ff', text: '#2563eb' };
    return { bg: '#fff7ed', text: '#ea580c' };
  };

  return (
    <div className="page-container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 className="page-heading">My Vehicles</h2>
          <p className="page-sub">Manage and edit your listed rental fleet</p>
        </div>
        <button className="btn-primary" style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}
          onClick={() => { setEditingVehicle(null); setShowModal(true); }}>
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
                    <span>🔖 {car.registrationNumber}</span>
                  </div>

                  <div style={{ fontSize: 17, fontWeight: 800, color: '#2563eb', marginBottom: 14 }}>
                    ₹{car.rentPerDay}<span style={{ fontSize: 12, fontWeight: 400, color: '#94a3b8' }}>/day</span>
                  </div>

                  <div style={{ display: 'flex', gap: 8, marginTop: 'auto' }}>
                    <button onClick={() => setEditingVehicle(car)} style={{
                      padding: '7px 12px', fontSize: 12, fontWeight: 600, cursor: 'pointer',
                      border: '1px solid #cbd5e1', borderRadius: 7, background: '#f1f5f9', color: '#334155'
                    }}>
                      ✏️ Edit
                    </button>
                    <button onClick={() => toggleStatus(car)} disabled={car.status === 'Booked'} style={{
                      flex: 1, padding: '7px', fontSize: 12, fontWeight: 600, cursor: car.status === 'Booked' ? 'not-allowed' : 'pointer',
                      border: '1px solid #e2e8f0', borderRadius: 7,
                      background: car.status === 'Booked' ? '#f1f5f9' : '#f8fafc', color: '#475569',
                      opacity: car.status === 'Booked' ? 0.6 : 1
                    }}>
                      {car.status === 'Available' ? '⏸ Maintenance' : car.status === 'Maintenance' ? '✅ Available' : '🔒 Booked'}
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

      {(showModal || editingVehicle) && (
        <AddEditVehicleModal
          initialData={editingVehicle}
          onClose={() => { setShowModal(false); setEditingVehicle(null); }}
          onSuccess={fetchMyVehicles}
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
