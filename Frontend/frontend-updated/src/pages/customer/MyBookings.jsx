import { useState, useEffect } from 'react';
import { bookingApi } from '../../api/axios';

// BookingStatus enum (Pascal case): Pending | Confirmed | Cancelled | Completed
// PaymentMethod display strings (from @JsonValue): UPI | Credit Card | Debit Card | Net Banking | Cash
const statusColors = {
  Pending:   { bg: '#fffbeb', text: '#d97706' },
  Confirmed: { bg: '#f0fdf4', text: '#16a34a' },
  Completed: { bg: '#eff6ff', text: '#2563eb' },
  Cancelled: { bg: '#fef2f2', text: '#dc2626' },
};

function PaymentModal({ booking, onClose, onSuccess }) {
  const [method, setMethod] = useState('UPI');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handlePay = async () => {
    setLoading(true);
    setError('');
    try {
      // PaymentRequest to ocrs-booking-service: { bookingId, paymentMethod }
      await bookingApi.post('/payments', {
        bookingId: booking.bookingId,
        paymentMethod: method
      });
      onSuccess();
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Payment processing failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.5)', display: 'flex',
      alignItems: 'center', justifyContent: 'center', zIndex: 1000
    }}>
      <div style={{ background: '#fff', borderRadius: 14, padding: 24, maxWidth: 420, width: '100%', boxShadow: '0 8px 32px rgba(0,0,0,0.18)' }}>
        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 6 }}>Complete Payment</h3>
        <p style={{ fontSize: 13, color: '#64748b', marginBottom: 16 }}>
          Booking #{booking.bookingId} — {booking.modelName || 'Vehicle Rental'}
        </p>

        {error && <div className="alert-error" style={{ marginBottom: 14 }}>{error}</div>}

        <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, marginBottom: 16 }}>
          <div style={{ fontSize: 12, color: '#64748b' }}>Total Payable Amount</div>
          <div style={{ fontSize: 24, fontWeight: 800, color: '#2563eb' }}>₹{booking.totalAmount}</div>
        </div>

        <div className="form-group">
          <label>Select Payment Method</label>
          <select value={method} onChange={e => setMethod(e.target.value)}
            style={{ width: '100%', padding: '10px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 14 }}>
            <option value="Credit Card">Credit Card</option>
            <option value="Debit Card">Debit Card</option>
            <option value="UPI">UPI / GPay / PhonePe</option>
            <option value="Net Banking">Net Banking</option>
            <option value="Cash">Cash on Pickup</option>
          </select>
        </div>

        <div style={{ display: 'flex', gap: 10, marginTop: 20 }}>
          <button className="btn-primary" onClick={handlePay} disabled={loading}>
            {loading ? 'Processing...' : `Pay ₹${booking.totalAmount}`}
          </button>
          <button onClick={onClose} style={{
            flex: 1, padding: '12px', background: '#f1f5f9', border: 'none', borderRadius: 8,
            fontSize: 14, fontWeight: 600, cursor: 'pointer', color: '#475569'
          }}>Cancel</button>
        </div>
      </div>
    </div>
  );
}

function MyBookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedBookingForPay, setSelectedBookingForPay] = useState(null);

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    setLoading(true);
    setError('');
    try {
      // Fetch bookings from ocrs-booking-service on port 8083
      const res = await bookingApi.get('/bookings/my');
      setBookings(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch your bookings');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (bookingId) => {
    if (!window.confirm('Are you sure you want to cancel this booking?')) return;
    try {
      await bookingApi.patch(`/bookings/${bookingId}/cancel`);
      fetchBookings();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to cancel booking');
    }
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">My Bookings</h2>
      <p className="page-sub">View active bookings and rental history</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading bookings...</div>
      ) : bookings.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">📅</div>
          <h3>No Bookings Yet</h3>
          <p>You haven't made any reservations. Browse available cars to make your first booking!</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {bookings.map(b => {
            const st = statusColors[b.status] || { bg: '#f1f5f9', text: '#475569' };
            const canCancel = b.status === 'Pending';
            const canPay = b.status === 'Pending' || b.status === 'Confirmed';

            return (
              <div key={b.bookingId} style={{
                background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, padding: '18px 22px',
                display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 16,
                boxShadow: '0 1px 3px rgba(0,0,0,0.04)'
              }}>
                <div style={{ flex: 1, minWidth: 240 }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 6 }}>
                    <h3 style={{ fontSize: 16, fontWeight: 700, color: '#1e293b', margin: 0 }}>
                      {b.brandName ? `${b.brandName} ` : ''}{b.modelName || `Vehicle #${b.vehicleId}`}
                    </h3>
                    <span style={{
                      fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                      background: st.bg, color: st.text
                    }}>
                      {b.status}
                    </span>
                  </div>

                  <div style={{ fontSize: 13, color: '#64748b', display: 'flex', gap: 16, flexWrap: 'wrap', marginBottom: 4 }}>
                    <span>📅 <strong>{b.pickupDate}</strong> → <strong>{b.returnDate}</strong></span>
                    {b.dropCity && <span>📍 Drop: {b.dropCity}</span>}
                    {b.vehicleRegistrationNumber && <span>🔖 {b.vehicleRegistrationNumber}</span>}
                  </div>

                  <div style={{ fontSize: 12, color: '#94a3b8' }}>
                    Booking ID: #{b.bookingId} | Booked on: {b.bookingDate ? new Date(b.bookingDate).toLocaleDateString() : 'N/A'}
                  </div>
                </div>

                <div style={{ textAlign: 'right', display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
                  <div style={{ fontSize: 20, fontWeight: 800, color: '#2563eb' }}>
                    ₹{b.totalAmount}
                  </div>

                  <div style={{ display: 'flex', gap: 8 }}>
                    {canPay && b.status !== 'Confirmed' && (
                      <button className="btn-primary" style={{ width: 'auto', padding: '6px 14px', fontSize: 12, marginTop: 0 }}
                        onClick={() => setSelectedBookingForPay(b)}>
                        💳 Pay Now
                      </button>
                    )}

                    {canCancel && (
                      <button onClick={() => handleCancel(b.bookingId)} style={{
                        padding: '6px 12px', background: '#fef2f2', color: '#dc2626', border: '1px solid #fca5a5',
                        borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer'
                      }}>
                        Cancel Booking
                      </button>
                    )}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {selectedBookingForPay && (
        <PaymentModal
          booking={selectedBookingForPay}
          onClose={() => setSelectedBookingForPay(null)}
          onSuccess={fetchBookings}
        />
      )}
    </div>
  );
}

export default MyBookings;
