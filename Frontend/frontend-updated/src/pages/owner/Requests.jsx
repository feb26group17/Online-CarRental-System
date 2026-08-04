import { useState, useEffect } from 'react';
import { bookingApi, crudApi } from '../../api/axios';

// BookingResponse fields:
//   bookingId, customerId, vehicleId, vehicleRegistrationNumber, modelName,
//   bookingDate, pickupDate, returnDate, dropCity, status, totalAmount
//
// BookingStatus enum (Pascal case): Pending | Confirmed | Cancelled | Completed
// Owner can: Confirmed (approve) or Cancelled (reject) a Pending booking

function Requests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchOwnerRequests();
  }, []);

  const fetchOwnerRequests = async () => {
    setLoading(true);
    setError('');
    try {
      // 1. Get owner's vehicles from crud-service on port 8082
      const vRes = await crudApi.get('/vehicles/my');
      const ownerVehicles = vRes.data || [];

      // 2. Fetch bookings for each vehicle from bookingApi on port 8083
      let allBookings = [];
      for (const vehicle of ownerVehicles) {
        try {
          const bRes = await bookingApi.get(`/bookings/vehicle/${vehicle.vehicleId}`);
          if (bRes.data && bRes.data.length > 0) {
            allBookings.push(...bRes.data);
          }
        } catch (e) {
          console.error(`Failed to load bookings for vehicle ${vehicle.vehicleId}`, e);
        }
      }

      setRequests(allBookings);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch booking requests');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateStatus = async (bookingId, newStatus) => {
    try {
      // PATCH /bookings/{id}/status to ocrs-booking-service on port 8083
      await bookingApi.patch(`/bookings/${bookingId}/status`, { status: newStatus });
      fetchOwnerRequests();
    } catch (err) {
      alert(err.response?.data?.message || `Failed to update booking status to ${newStatus}`);
    }
  };

  const statusStyle = (status) => {
    if (status === 'Pending')   return { bg: '#fffbeb', text: '#d97706' };
    if (status === 'Confirmed') return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Completed') return { bg: '#eff6ff', text: '#2563eb' };
    return { bg: '#fef2f2', text: '#dc2626' };
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">Booking Requests</h2>
      <p className="page-sub">Review and approve or reject incoming customer reservations</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading rental requests...</div>
      ) : requests.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">📋</div>
          <h3>No Booking Requests</h3>
          <p>You have no incoming rental requests for your vehicles at this time.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {requests.map(req => {
            const ss = statusStyle(req.status);
            return (
              <div key={req.bookingId} style={{
                background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, padding: '18px 22px',
                display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 16,
                boxShadow: '0 1px 3px rgba(0,0,0,0.04)'
              }}>
                <div style={{ flex: 1, minWidth: 260 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 6 }}>
                    <h3 style={{ fontSize: 16, fontWeight: 700, color: '#1e293b', margin: 0 }}>
                      {req.brandName ? `${req.brandName} ` : ''}{req.modelName || `Vehicle #${req.vehicleId}`}
                      {req.vehicleRegistrationNumber && (
                        <span style={{ fontSize: 13, fontWeight: 400, color: '#64748b', marginLeft: 6 }}>
                          ({req.vehicleRegistrationNumber})
                        </span>
                      )}
                    </h3>
                    <span style={{
                      fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                      background: ss.bg, color: ss.text
                    }}>
                      {req.status}
                    </span>
                  </div>

                  <div style={{ fontSize: 13, color: '#64748b', display: 'flex', gap: 16, flexWrap: 'wrap', marginBottom: 4 }}>
                    <span>👤 Customer ID: <strong>#{req.customerId}</strong></span>
                    <span>📅 <strong>{req.pickupDate}</strong> → <strong>{req.returnDate}</strong></span>
                    {req.dropCity && <span>📍 Drop: {req.dropCity}</span>}
                  </div>

                  <div style={{ fontSize: 12, color: '#94a3b8' }}>
                    Request ID: #{req.bookingId} | Submitted: {req.bookingDate ? new Date(req.bookingDate).toLocaleDateString() : 'N/A'}
                  </div>
                </div>

                <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 10 }}>
                  <div style={{ fontSize: 20, fontWeight: 800, color: '#2563eb' }}>
                    ₹{req.totalAmount}
                  </div>

                  {req.status === 'Pending' && (
                    <div style={{ display: 'flex', gap: 8 }}>
                      <button onClick={() => handleUpdateStatus(req.bookingId, 'Confirmed')} style={{
                        padding: '8px 16px', background: '#16a34a', color: '#fff', border: 'none',
                        borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer'
                      }}>
                        ✅ Approve
                      </button>
                      <button onClick={() => handleUpdateStatus(req.bookingId, 'Cancelled')} style={{
                        padding: '8px 16px', background: '#fef2f2', color: '#dc2626', border: '1px solid #fca5a5',
                        borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer'
                      }}>
                        ❌ Reject
                      </button>
                    </div>
                  )}

                  {req.status === 'Confirmed' && (
                    <button onClick={() => handleUpdateStatus(req.bookingId, 'Completed')} style={{
                      padding: '8px 16px', background: '#eff6ff', color: '#2563eb', border: '1px solid #bfdbfe',
                      borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer'
                    }}>
                      🏁 Mark Completed
                    </button>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default Requests;
