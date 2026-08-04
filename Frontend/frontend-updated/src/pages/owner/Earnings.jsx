import { useState, useEffect } from 'react';
import { crudApi } from '../../api/axios';

// PaymentResponse: paymentId, bookingId, amt, paymentMethod, paymentStatus, paymentDate
// PaymentStatus enum: Pending | Paid | Failed | Refunded
// VehicleResponse: vehicleId, modelName, brandName, ...

function Earnings() {
  const [vehicleEarnings, setVehicleEarnings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchEarnings();
  }, []);

  const fetchEarnings = async () => {
    setLoading(true);
    setError('');
    try {
      // VehicleResponse uses vehicleId (not car.id)
      const vRes = await crudApi.get('/vehicles/my');
      const vehicles = vRes.data || [];

      let breakdown = [];
      for (const car of vehicles) {
        let revenue = 0;
        let paid = 0;
        let trips = 0;

        try {
          // Use vehicleId (not car.id)
          const pRes = await crudApi.get(`/payments/vehicle/${car.vehicleId}`);
          if (pRes.data) {
            pRes.data.forEach(p => {
              trips++;
              // amt is the correct field name (not amount)
              const amt = parseFloat(p.amt || 0);
              revenue += amt;
              // PaymentStatus.Paid is the successful status (not SUCCESS or COMPLETED)
              if (p.paymentStatus === 'Paid') {
                paid += amt;
              }
            });
          }
        } catch (e) {
          console.error(e);
        }

        breakdown.push({
          id: car.vehicleId,
          // modelName + brandName from VehicleResponse
          car: car.modelName ? `${car.brandName} ${car.modelName}` : car.registrationNumber,
          trips,
          revenue,
          paid,
          pending: revenue - paid
        });
      }

      setVehicleEarnings(breakdown);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to calculate earnings');
    } finally {
      setLoading(false);
    }
  };

  const totalRevenue = vehicleEarnings.reduce((s, v) => s + v.revenue, 0);
  const totalPaid = vehicleEarnings.reduce((s, v) => s + v.paid, 0);
  const totalPending = totalRevenue - totalPaid;
  const totalTrips = vehicleEarnings.reduce((s, v) => s + v.trips, 0);

  return (
    <div className="page-container">
      <h2 className="page-heading">Earnings</h2>
      <p className="page-sub">Track your revenue across all listed vehicles</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {/* Summary cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 14, marginBottom: 24 }}>
        {[
          { label: 'Total Revenue', value: `₹${totalRevenue.toLocaleString()}`, color: '#2563eb', bg: '#eff6ff' },
          { label: 'Paid Amount',   value: `₹${totalPaid.toLocaleString()}`,    color: '#16a34a', bg: '#f0fdf4' },
          { label: 'Pending',       value: `₹${totalPending.toLocaleString()}`, color: '#d97706', bg: '#fffbeb' },
          { label: 'Transactions',  value: `${totalTrips}`,                     color: '#7c3aed', bg: '#f5f3ff' },
        ].map((c, i) => (
          <div key={i} style={{
            background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
            padding: '16px 18px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
          }}>
            <div style={{ fontSize: 12, color: '#64748b', marginBottom: 6 }}>{c.label}</div>
            <div style={{ fontSize: 20, fontWeight: 700, color: c.color }}>{loading ? '...' : c.value}</div>
          </div>
        ))}
      </div>

      {/* Per vehicle breakdown */}
      <div style={{
        background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b' }}>Revenue by Vehicle</h3>
          <select value={filter} onChange={e => setFilter(e.target.value)}
            style={{
              padding: '6px 12px', border: '1px solid #e2e8f0', borderRadius: 6,
              fontSize: 13, background: '#fff', color: '#475569'
            }}>
            <option value="all">All vehicles</option>
            {vehicleEarnings.map(v => <option key={v.id} value={v.id}>{v.car}</option>)}
          </select>
        </div>

        {loading ? (
          <div style={{ textAlign: 'center', padding: 30, color: '#64748b' }}>Calculating vehicle earnings...</div>
        ) : vehicleEarnings.length === 0 ? (
          <div style={{ textAlign: 'center', padding: 30, color: '#64748b' }}>No earnings history available yet.</div>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ background: '#f8fafc' }}>
                {['Vehicle', 'Transactions', 'Total Revenue', 'Received (Paid)', 'Pending'].map(h => (
                  <th key={h} style={{
                    padding: '10px 14px', fontSize: 12, fontWeight: 600,
                    color: '#64748b', textAlign: 'left', borderBottom: '1px solid #e2e8f0'
                  }}>
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {vehicleEarnings.filter(v => filter === 'all' || v.id.toString() === filter).map((v, i) => (
                <tr key={i} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '12px 14px', fontSize: 13, fontWeight: 600, color: '#1e293b' }}>{v.car}</td>
                  <td style={{ padding: '12px 14px', fontSize: 13, color: '#374151' }}>{v.trips}</td>
                  <td style={{ padding: '12px 14px', fontSize: 13, fontWeight: 600, color: '#2563eb' }}>₹{v.revenue.toLocaleString()}</td>
                  <td style={{ padding: '12px 14px', fontSize: 13, color: '#16a34a', fontWeight: 600 }}>₹{v.paid.toLocaleString()}</td>
                  <td style={{ padding: '12px 14px' }}>
                    <span style={{
                      fontSize: 12, fontWeight: 600, padding: '3px 10px', borderRadius: 20,
                      background: v.pending > 0 ? '#fffbeb' : '#f0fdf4',
                      color: v.pending > 0 ? '#d97706' : '#16a34a'
                    }}>
                      ₹{v.pending.toLocaleString()}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default Earnings;
