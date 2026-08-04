import { useState, useEffect } from 'react';
import { crudApi } from '../../api/axios';

// Fuel type values match the backend FuelType enum exactly (Pascal case)
const FUEL_TYPES = ['Diesel', 'Petrol', 'CNG', 'Battery'];

function BrowseCars({ onSelectCar }) {
  const [vehicles, setVehicles] = useState([]);
  const [brands, setBrands] = useState([]);
  const [models, setModels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Search Filters
  const [selectedBrand, setSelectedBrand] = useState('');
  const [selectedModel, setSelectedModel] = useState('');
  const [fuelType, setFuelType] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');

  useEffect(() => {
    fetchBrands();
    fetchVehicles();
  }, []);

  const fetchBrands = async () => {
    try {
      const res = await crudApi.get('/brands');
      setBrands(res.data || []);
    } catch (err) {
      console.error('Failed to fetch brands', err);
    }
  };

  // Backend BrandResponse: { brandId, bname }
  // Backend ModelResponse: { modelId, brandId, brandName, modelName, seatingCapacity }
  const handleBrandChange = async (brandId) => {
    setSelectedBrand(brandId);
    setSelectedModel('');
    if (brandId) {
      try {
        const res = await crudApi.get(`/models?brandId=${brandId}`);
        setModels(res.data || []);
      } catch (err) {
        console.error('Failed to fetch models', err);
      }
    } else {
      setModels([]);
    }
  };

  const fetchVehicles = async (e) => {
    if (e) e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const params = new URLSearchParams();
      if (selectedBrand) params.append('brandId', selectedBrand);
      if (selectedModel) params.append('modelId', selectedModel);
      if (fuelType) params.append('fuelType', fuelType);
      if (minPrice) params.append('minPrice', minPrice);
      if (maxPrice) params.append('maxPrice', maxPrice);

      const res = await crudApi.get(`/vehicles?${params.toString()}`);
      setVehicles(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load vehicles');
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setSelectedBrand('');
    setSelectedModel('');
    setFuelType('');
    setMinPrice('');
    setMaxPrice('');
    setModels([]);
    crudApi.get('/vehicles').then(res => setVehicles(res.data || []));
  };

  // VehicleResponse: { vehicleId, userId, modelId, modelName, brandName,
  //                    seatingCapacity, registrationNumber, fuelType, rentPerDay, status }
  // status enum: Available | Booked | Maintenance
  const statusColor = (status) => {
    if (status === 'Available') return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Booked')    return { bg: '#eff6ff', text: '#2563eb' };
    return { bg: '#fff7ed', text: '#ea580c' }; // Maintenance
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">Browse Cars</h2>
      <p className="page-sub">Search and filter available cars for rent</p>

      {/* Filter Bar */}
      <form onSubmit={fetchVehicles} style={{
        background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '18px 20px', marginBottom: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 14, marginBottom: 14 }}>
          
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: '#475569', marginBottom: 4, display: 'block' }}>Brand</label>
            <select value={selectedBrand} onChange={e => handleBrandChange(e.target.value)}
              style={{ width: '100%', padding: '9px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13 }}>
              <option value="">All Brands</option>
              {/* BrandResponse fields: brandId, bname */}
              {brands.map(b => (
                <option key={b.brandId} value={b.brandId}>{b.bname}</option>
              ))}
            </select>
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: '#475569', marginBottom: 4, display: 'block' }}>Model</label>
            <select value={selectedModel} onChange={e => setSelectedModel(e.target.value)}
              disabled={!selectedBrand}
              style={{ width: '100%', padding: '9px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13, opacity: !selectedBrand ? 0.6 : 1 }}>
              <option value="">All Models</option>
              {/* ModelResponse fields: modelId, modelName */}
              {models.map(m => (
                <option key={m.modelId} value={m.modelId}>{m.modelName}</option>
              ))}
            </select>
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: '#475569', marginBottom: 4, display: 'block' }}>Fuel Type</label>
            <select value={fuelType} onChange={e => setFuelType(e.target.value)}
              style={{ width: '100%', padding: '9px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13 }}>
              <option value="">All Fuels</option>
              {/* FuelType enum: Diesel | Petrol | CNG | Battery */}
              {FUEL_TYPES.map(f => (
                <option key={f} value={f}>{f}</option>
              ))}
            </select>
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: '#475569', marginBottom: 4, display: 'block' }}>Min Price (₹)</label>
            <input type="number" placeholder="e.g. 500" value={minPrice} onChange={e => setMinPrice(e.target.value)}
              style={{ width: '100%', padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13 }} />
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label style={{ fontSize: 12, fontWeight: 600, color: '#475569', marginBottom: 4, display: 'block' }}>Max Price (₹)</label>
            <input type="number" placeholder="e.g. 5000" value={maxPrice} onChange={e => setMaxPrice(e.target.value)}
              style={{ width: '100%', padding: '8px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13 }} />
          </div>
        </div>

        <div style={{ display: 'flex', gap: 10, justifyContent: 'flex-end' }}>
          <button type="button" onClick={clearFilters} style={{
            padding: '8px 16px', background: '#f1f5f9', color: '#475569', border: 'none', borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer'
          }}>Clear</button>
          <button type="submit" className="btn-primary" style={{ width: 'auto', padding: '8px 24px', marginTop: 0 }}>
            🔍 Search Vehicles
          </button>
        </div>
      </form>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: '40px', color: '#64748b' }}>Loading available cars...</div>
      ) : vehicles.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">🚗</div>
          <h3>No Vehicles Found</h3>
          <p>Try adjusting your search filters or check back later.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 20 }}>
          {vehicles.map(car => {
            // VehicleResponse: vehicleId, modelName, brandName, seatingCapacity,
            //                  registrationNumber, fuelType, rentPerDay, status
            const sc = statusColor(car.status);
            return (
              <div key={car.vehicleId} style={{
                background: '#fff', border: '1px solid #e2e8f0', borderRadius: 14, overflow: 'hidden',
                boxShadow: '0 2px 8px rgba(0,0,0,0.06)', display: 'flex', flexDirection: 'column'
              }}>
                <div style={{
                  height: 140, background: '#f8fafc', display: 'flex', alignItems: 'center', justifyContent: 'center',
                  overflow: 'hidden', borderBottom: '1px solid #e2e8f0', position: 'relative'
                }}>
                  <span style={{ fontSize: 56 }}>🚗</span>
                  <span style={{
                    position: 'absolute', top: 10, right: 10, fontSize: 11, fontWeight: 700, padding: '4px 10px',
                    borderRadius: 20, background: sc.bg, color: sc.text
                  }}>
                    {car.status}
                  </span>
                </div>

                <div style={{ padding: '16px', flex: 1, display: 'flex', flexDirection: 'column' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, textTransform: 'uppercase', color: '#2563eb' }}>
                    {car.brandName || 'CAR'}
                  </div>
                  <h3 style={{ fontSize: 17, fontWeight: 700, color: '#1e293b', margin: '2px 0 8px' }}>
                    {car.modelName || car.registrationNumber}
                  </h3>

                  <div style={{ display: 'flex', gap: 12, fontSize: 12, color: '#64748b', marginBottom: 12 }}>
                    <span>⛽ {car.fuelType || 'N/A'}</span>
                    <span>🪑 {car.seatingCapacity || 5} Seats</span>
                    <span>🔖 {car.registrationNumber}</span>
                  </div>

                  <div style={{ marginTop: 'auto', display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingTop: 12, borderTop: '1px solid #f1f5f9' }}>
                    <div>
                      <span style={{ fontSize: 18, fontWeight: 800, color: '#1e293b' }}>₹{car.rentPerDay}</span>
                      <span style={{ fontSize: 12, color: '#94a3b8' }}>/day</span>
                    </div>
                    <div style={{ display: 'flex', gap: 8 }}>
                      <button
                        onClick={() => onSelectCar && onSelectCar(car)}
                        style={{
                          padding: '8px 12px', background: '#f1f5f9', color: '#334155', border: '1px solid #cbd5e1',
                          borderRadius: 8, fontSize: 12, fontWeight: 600, cursor: 'pointer'
                        }}
                      >
                        🔍 Verify Details
                      </button>
                      <button className="btn-primary" style={{ width: 'auto', padding: '8px 16px', marginTop: 0 }}
                        onClick={() => onSelectCar && onSelectCar(car)}
                        disabled={car.status !== 'Available'}>
                        {car.status === 'Available' ? 'Book Now' : car.status}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default BrowseCars;
