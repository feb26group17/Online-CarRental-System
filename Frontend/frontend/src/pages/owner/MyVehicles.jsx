import { useState } from 'react';

const INITIAL_VEHICLES = [
  { id: 1, model: 'Maruti Swift', fuel: 'Petrol', seats: 5, price: 800,  status: 'Available', rc: 'MH12AB1234' },
  { id: 2, model: 'Honda City',   fuel: 'Petrol', seats: 5, price: 1200, status: 'Rented',    rc: 'MH14CD5678' },
  { id: 3, model: 'Tata Nexon',   fuel: 'Electric', seats: 5, price: 1500, status: 'Inactive', rc: 'MH01EF9012' },
];

const statusColor = { Available: '#16a34a', Rented: '#2563eb', Inactive: '#dc2626' };
const statusBg    = { Available: '#f0fdf4', Rented: '#eff6ff', Inactive: '#fef2f2' };

function AddVehicleModal({ onClose, onAdd }) {
  const [form, setForm] = useState({ model: '', fuel: 'Petrol', seats: '5', price: '', rc: '' });
  const [err, setErr] = useState('');

  const handleSubmit = () => {
    if (!form.model || !form.price || !form.rc) return setErr('All fields are required');
    onAdd({ ...form, id: Date.now(), status: 'Available', seats: parseInt(form.seats), price: parseInt(form.price) });
    onClose();
  };

  return (
    <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.45)',
      display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
      <div style={{ background: '#fff', borderRadius: 14, padding: '28px 28px',
        width: '100%', maxWidth: 440, boxShadow: '0 8px 32px rgba(0,0,0,0.18)' }}>
        <h3 style={{ fontSize: 17, fontWeight: 700, color: '#1e293b', marginBottom: 20 }}>Add New Vehicle</h3>

        {err && <div className="alert-error">{err}</div>}

        {[
          ['Vehicle Model', 'model', 'text', 'e.g. Hyundai i20'],
          ['RC Number',     'rc',    'text', 'e.g. MH12AB1234'],
          ['Price per day (₹)', 'price', 'number', 'e.g. 1000'],
        ].map(([label, key, type, ph]) => (
          <div className="form-group" key={key}>
            <label>{label} *</label>
            <input type={type} placeholder={ph} value={form[key]}
              onChange={e => setForm({ ...form, [key]: e.target.value })} />
          </div>
        ))}

        <div className="form-group">
          <label>Fuel Type *</label>
          <select value={form.fuel} onChange={e => setForm({ ...form, fuel: e.target.value })}
            style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #d1d5db',
              borderRadius: 8, fontSize: 14, background: '#f9fafb' }}>
            {['Petrol', 'Diesel', 'Electric', 'CNG'].map(f => <option key={f}>{f}</option>)}
          </select>
        </div>

        <div className="form-group">
          <label>Seats *</label>
          <select value={form.seats} onChange={e => setForm({ ...form, seats: e.target.value })}
            style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #d1d5db',
              borderRadius: 8, fontSize: 14, background: '#f9fafb' }}>
            {['4','5','6','7','8'].map(n => <option key={n}>{n}</option>)}
          </select>
        </div>

        <div style={{ display: 'flex', gap: 10, marginTop: 8 }}>
          <button className="btn-primary" onClick={handleSubmit}>Add Vehicle</button>
          <button onClick={onClose} style={{ flex: 1, padding: '12px', background: '#f1f5f9',
            border: 'none', borderRadius: 8, fontSize: 15, fontWeight: 600, cursor: 'pointer', color: '#475569' }}>
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}

function MyVehicles() {
  const [vehicles, setVehicles]     = useState(INITIAL_VEHICLES);
  const [showModal, setShowModal]   = useState(false);
  const [deleteId, setDeleteId]     = useState(null);

  const toggleStatus = (id) => {
    setVehicles(v => v.map(car =>
      car.id === id
        ? { ...car, status: car.status === 'Inactive' ? 'Available' : 'Inactive' }
        : car
    ));
  };

  const handleDelete = (id) => {
    setVehicles(v => v.filter(c => c.id !== id));
    setDeleteId(null);
  };

  return (
    <div className="page-container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 className="page-heading">My Vehicles</h2>
          <p className="page-sub">Manage your listed vehicles</p>
        </div>
        <button className="btn-primary" style={{ width: 'auto', padding: '10px 20px', marginTop: 0 }}
          onClick={() => setShowModal(true)}>
          + Add Vehicle
        </button>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
        {vehicles.map(car => (
          <div key={car.id} style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
            overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
            <div style={{ background: '#f8fafc', height: 110, display: 'flex', alignItems: 'center',
              justifyContent: 'center', fontSize: 52, borderBottom: '1px solid #e2e8f0' }}>
              🚗
            </div>
            <div style={{ padding: '14px 16px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 8 }}>
                <div style={{ fontSize: 15, fontWeight: 700, color: '#1e293b' }}>{car.model}</div>
                <span style={{ fontSize: 11, fontWeight: 600, padding: '3px 10px', borderRadius: 20,
                  background: statusBg[car.status], color: statusColor[car.status] }}>
                  {car.status}
                </span>
              </div>
              <div style={{ fontSize: 12, color: '#64748b', display: 'flex', gap: 12, marginBottom: 4 }}>
                <span>{car.fuel}</span>
                <span>{car.seats} seats</span>
                <span>RC: {car.rc}</span>
              </div>
              <div style={{ fontSize: 16, fontWeight: 700, color: '#2563eb', marginBottom: 14 }}>
                ₹{car.price}<span style={{ fontSize: 12, fontWeight: 400, color: '#94a3b8' }}>/day</span>
              </div>
              <div style={{ display: 'flex', gap: 8 }}>
                <button onClick={() => toggleStatus(car.id)}
                  style={{ flex: 1, padding: '7px', fontSize: 12, fontWeight: 600, cursor: 'pointer',
                    border: '1px solid #e2e8f0', borderRadius: 7, background: '#f8fafc', color: '#475569' }}>
                  {car.status === 'Inactive' ? '✅ Activate' : '⏸ Deactivate'}
                </button>
                <button onClick={() => setDeleteId(car.id)}
                  style={{ padding: '7px 12px', fontSize: 12, fontWeight: 600, cursor: 'pointer',
                    border: '1px solid #fca5a5', borderRadius: 7, background: '#fef2f2', color: '#dc2626' }}>
                  🗑
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {showModal && (
        <AddVehicleModal
          onClose={() => setShowModal(false)}
          onAdd={v => setVehicles(prev => [...prev, v])}
        />
      )}

      {/* Delete confirm */}
      {deleteId && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
          display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
          <div style={{ background: '#fff', borderRadius: 12, padding: '24px 28px', maxWidth: 360, width: '100%' }}>
            <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 10 }}>Remove vehicle?</h3>
            <p style={{ fontSize: 14, color: '#64748b', marginBottom: 20 }}>
              This will permanently remove this vehicle from your listings.
            </p>
            <div style={{ display: 'flex', gap: 10 }}>
              <button onClick={() => handleDelete(deleteId)} style={{ flex: 1, padding: '10px',
                background: '#dc2626', color: '#fff', border: 'none', borderRadius: 8,
                fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>Remove</button>
              <button onClick={() => setDeleteId(null)} style={{ flex: 1, padding: '10px',
                background: '#f1f5f9', color: '#475569', border: 'none', borderRadius: 8,
                fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default MyVehicles;
