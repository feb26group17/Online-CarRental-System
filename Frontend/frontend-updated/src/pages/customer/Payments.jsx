import { useState, useEffect } from 'react';
import { bookingApi } from '../../api/axios';

// PaymentResponse fields: paymentId, bookingId, amt, paymentMethod, paymentStatus, paymentDate, bookingDetails
// PaymentStatus enum: Pending | Paid | Failed | Refunded

function RefundModal({ payment, onClose, onSuccess }) {
  const [reason, setReason] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleRefund = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Send refund request to ocrs-booking-service on port 8083
      await bookingApi.post('/refunds', {
        paymentId: payment.paymentId,
        reason: reason
      });
      onSuccess();
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit refund request');
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
        <h3 style={{ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 6 }}>Request Refund</h3>
        <p style={{ fontSize: 13, color: '#64748b', marginBottom: 16 }}>
          Payment #{payment.paymentId} — Paid Amount: ₹{payment.amt}
        </p>

        {error && <div className="alert-error" style={{ marginBottom: 14 }}>{error}</div>}

        <div style={{ background: '#f8fafc', padding: '10px 14px', borderRadius: 8, marginBottom: 16 }}>
          <div style={{ fontSize: 12, color: '#64748b' }}>Refund will be processed for the full paid amount</div>
          <div style={{ fontSize: 20, fontWeight: 700, color: '#dc2626' }}>₹{payment.amt}</div>
        </div>

        <form onSubmit={handleRefund}>
          <div className="form-group">
            <label>Reason for Refund *</label>
            <textarea rows={3} placeholder="Explain why you are requesting a refund..."
              value={reason} onChange={e => setReason(e.target.value)} required
              style={{ width: '100%', padding: '10px 12px', border: '1px solid #d1d5db', borderRadius: 8, fontSize: 13 }} />
          </div>

          <div style={{ display: 'flex', gap: 10, marginTop: 20 }}>
            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Submitting...' : 'Submit Refund Request'}
            </button>
            <button type="button" onClick={onClose} style={{
              flex: 1, padding: '12px', background: '#f1f5f9', border: 'none', borderRadius: 8,
              fontSize: 14, fontWeight: 600, cursor: 'cursor', color: '#475569'
            }}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}

function Payments() {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedPaymentForRefund, setSelectedPaymentForRefund] = useState(null);
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    fetchPayments();
  }, []);

  const fetchPayments = async () => {
    setLoading(true);
    setError('');
    try {
      // Fetch payment history from ocrs-booking-service on port 8083
      const res = await bookingApi.get('/payments/my');
      setPayments(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load payment history');
    } finally {
      setLoading(false);
    }
  };

  const handleRefundSuccess = () => {
    setSuccessMsg('Refund request submitted successfully!');
    fetchPayments();
    setTimeout(() => setSuccessMsg(''), 4000);
  };

  const statusStyle = (status) => {
    if (status === 'Paid')     return { bg: '#f0fdf4', color: '#16a34a' };
    if (status === 'Refunded') return { bg: '#eff6ff', color: '#2563eb' };
    if (status === 'Failed')   return { bg: '#fef2f2', color: '#dc2626' };
    return { bg: '#fffbeb', color: '#d97706' };
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">Payments</h2>
      <p className="page-sub">Payment history, transaction receipts, and refund requests</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}
      {successMsg && <div style={{
        padding: '12px 16px', background: '#f0fdf4', color: '#16a34a', border: '1px solid #bbf7d0',
        borderRadius: 8, fontSize: 14, fontWeight: 600, marginBottom: 20
      }}>{successMsg}</div>}

      {loading ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading payments...</div>
      ) : payments.length === 0 ? (
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">💳</div>
          <h3>No Payment History</h3>
          <p>You haven't completed any payment transactions yet.</p>
        </div>
      ) : (
        <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
            <thead>
              <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                <th style={{ padding: '14px 18px' }}>Payment ID</th>
                <th style={{ padding: '14px 18px' }}>Booking ID</th>
                <th style={{ padding: '14px 18px' }}>Method</th>
                <th style={{ padding: '14px 18px' }}>Date</th>
                <th style={{ padding: '14px 18px' }}>Amount</th>
                <th style={{ padding: '14px 18px' }}>Status</th>
                <th style={{ padding: '14px 18px', textAlign: 'right' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {payments.map((p, idx) => {
                const ss = statusStyle(p.paymentStatus);
                return (
                  <tr key={p.paymentId} style={{ borderBottom: idx < payments.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                    <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{p.paymentId}</td>
                    <td style={{ padding: '14px 18px', color: '#64748b' }}>#{p.bookingId}</td>
                    <td style={{ padding: '14px 18px', color: '#475569' }}>
                      {p.paymentMethod || 'N/A'}
                    </td>
                    <td style={{ padding: '14px 18px', color: '#64748b' }}>
                      {p.paymentDate ? new Date(p.paymentDate).toLocaleDateString() : 'N/A'}
                    </td>
                    <td style={{ padding: '14px 18px', fontWeight: 700, color: '#16a34a' }}>
                      ₹{p.amt}
                    </td>
                    <td style={{ padding: '14px 18px' }}>
                      <span style={{
                        fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                        background: ss.bg, color: ss.color
                      }}>
                        {p.paymentStatus || 'Pending'}
                      </span>
                    </td>
                    <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                      {p.paymentStatus === 'Paid' && (
                        <button onClick={() => setSelectedPaymentForRefund(p)} style={{
                          padding: '6px 12px', background: '#eff6ff', color: '#2563eb', border: '1px solid #bfdbfe',
                          borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer'
                        }}>
                          Request Refund
                        </button>
                      )}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      {selectedPaymentForRefund && (
        <RefundModal
          payment={selectedPaymentForRefund}
          onClose={() => setSelectedPaymentForRefund(null)}
          onSuccess={handleRefundSuccess}
        />
      )}
    </div>
  );
}

export default Payments;
