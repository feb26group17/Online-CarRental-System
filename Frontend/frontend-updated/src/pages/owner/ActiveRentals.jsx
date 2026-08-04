import { useState, useEffect } from 'react';
import { crudApi } from '../../api/axios';

// BookingResponse: bookingId, customerId, vehicleId, vehicleRegistrationNumber, modelName,
//                  bookingDate, pickupDate, returnDate, dropCity, status, totalAmount
// BookingStatus enum: Pending | Confirmed | Cancelled | Completed
// Active = Confirmed (not APPROVED — that status doesn't exist in backend)

function ActiveRentals() {
  const [rentals, setRentals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [reportId, setReportId] = useState(null);
  const [report, setReport] = useState('');
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    fetchActiveRentals();
  }, []);

  const fetchActiveRentals = async () => {
    setLoading(true);
    setError('');
    try {
      // VehicleResponse: vehicleId (not vehicle.id)
      const vRes = await crudApi.get('/vehicles/my');
      const ownerVehicles = vRes.data || [];

      let activeBookings = [];
      for (const vehicle of ownerVehicles) {
        try {
          const bRes = await crudApi.get(`/bookings/vehicle/${vehicle.vehicleId}`);
          if (bRes.data) {
            // Active = Confirmed (BookingStatus.Confirmed)
            const active = bRes.data.filter(b => b.status === 'Confirmed');
            activeBookings.push(...active);
          }
        } catch (e) {
          console.error(e);
        }
      }
      setRentals(activeBookings);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load active rentals');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">Active Rentals</h2>
      <p className="page-sub">Vehicles currently rented out (status: Confirmed)</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading active rentals...</div>
      ) : rentals.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">🔑</div>
          <h3>No Active Rentals</h3>
          <p>All your listed vehicles are currently unrented or available for new bookings.</p>
        </div>
      ) : rentals.map(r => (
        <div key={r.bookingId} style={{
          background: '#fff', border: '1px solid #e2e8f0',
          borderRadius: 12, padding: '20px 22px', marginBottom: 14,
          boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
        }}>

          <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 10, marginBottom: 16 }}>
            <div>
              {/* modelName is the vehicle name in BookingResponse */}
              <div style={{ fontSize: 16, fontWeight: 700, color: '#1e293b' }}>
                🚗 {r.modelName || `Vehicle #${r.vehicleId}`}
              </div>
              {r.vehicleRegistrationNumber && (
                <div style={{ fontSize: 12, color: '#64748b', marginTop: 2 }}>
                  RC: {r.vehicleRegistrationNumber}
                </div>
              )}
              {/* bookingId is the correct id field */}
              <div style={{ fontSize: 12, color: '#64748b', marginTop: 2 }}>Booking ID: #{r.bookingId}</div>
            </div>
            <span style={{
              fontSize: 11, fontWeight: 700, padding: '4px 14px', borderRadius: 20,
              background: '#f0fdf4', color: '#16a34a'
            }}>
              🟢 Confirmed
            </span>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12, marginBottom: 16 }}>
            {[
              // customerId since customerName is not in BookingResponse
              { label: 'Customer ID', value: `#${r.customerId}` },
              // pickupDate / returnDate are the correct field names
              { label: 'Pickup Date', value: r.pickupDate },
              { label: 'Return Date', value: r.returnDate },
              { label: 'Total Amount', value: `₹${r.totalAmount}`, green: true },
              ...(r.dropCity ? [{ label: 'Drop City', value: r.dropCity }] : []),
            ].map((item, i) => (
              <div key={i} style={{ background: '#f8fafc', borderRadius: 8, padding: '10px 12px' }}>
                <div style={{ fontSize: 11, color: '#94a3b8', marginBottom: 3 }}>{item.label}</div>
                <div style={{ fontSize: 13, fontWeight: 600, color: item.green ? '#16a34a' : '#1e293b' }}>
                  {item.value}
                </div>
              </div>
            ))}
          </div>

          <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
            <button onClick={() => setReportId(r.bookingId)}
              style={{
                padding: '8px 18px', background: '#fef2f2', color: '#dc2626',
                border: '1px solid #fca5a5', borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer'
              }}>
              ⚠️ Report Issue
            </button>
          </div>
        </div>
      ))}

      {reportId && (
        <div style={{
          position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
          display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{ background: '#fff', borderRadius: 12, padding: '24px 28px', maxWidth: 420, width: '100%' }}>
            {submitted ? (
              <div style={{ textAlign: 'center', padding: '16px 0' }}>
                <div style={{ fontSize: 48, marginBottom: 12 }}>✅</div>
                <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 8 }}>Report submitted</h3>
                <p style={{ fontSize: 13, color: '#64748b', marginBottom: 20 }}>Admin will review and take action.</p>
                <button onClick={() => { setReportId(null); setSubmitted(false); setReport(''); }}
                  className="btn-primary">Close</button>
              </div>
            ) : (
              <>
                <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 8 }}>⚠️ Report Damage / Issue</h3>
                <p style={{ fontSize: 13, color: '#64748b', marginBottom: 14 }}>
                  Describe the issue observed for Booking #{reportId}:
                </p>
                <textarea rows={4} value={report} onChange={e => setReport(e.target.value)}
                  placeholder="e.g. Vehicle returned with damage, late return..."
                  style={{
                    width: '100%', padding: '10px', border: '1.5px solid #d1d5db', borderRadius: 8,
                    fontSize: 13, resize: 'vertical', boxSizing: 'border-box', marginBottom: 14
                  }} />
                <div style={{ display: 'flex', gap: 10 }}>
                  <button onClick={() => setSubmitted(true)}
                    style={{
                      flex: 1, padding: '10px', background: '#dc2626', color: '#fff',
                      border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14
                    }}>
                    Submit Report
                  </button>
                  <button onClick={() => { setReportId(null); setReport(''); }}
                    style={{
                      flex: 1, padding: '10px', background: '#f1f5f9', color: '#475569',
                      border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14
                    }}>
                    Cancel
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default ActiveRentals;
