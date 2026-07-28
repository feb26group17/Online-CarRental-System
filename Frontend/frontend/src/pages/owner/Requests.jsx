import { useState } from 'react';

const INITIAL = [
  { id: 'RQ001', customer: 'Rahul Sharma',  email: 'rahul@email.com', phone: '9876543210',
    car: 'Maruti Swift', from: '2026-06-20', to: '2026-06-22', amount: 1600, status: 'Pending' },
  { id: 'RQ002', customer: 'Priya Mehta',   email: 'priya@email.com', phone: '9123456780',
    car: 'Tata Nexon',  from: '2026-06-21', to: '2026-06-24', amount: 4500, status: 'Pending' },
  { id: 'RQ003', customer: 'Amit Verma',    email: 'amit@email.com',  phone: '9988776655',
    car: 'Maruti Swift', from: '2026-06-05', to: '2026-06-07', amount: 1600, status: 'Approved' },
  { id: 'RQ004', customer: 'Sneha Patil',   email: 'sneha@email.com', phone: '9012345678',
    car: 'Tata Nexon',  from: '2026-05-20', to: '2026-05-21', amount: 1500, status: 'Rejected' },
];

const statusColor = { Pending: '#d97706', Approved: '#16a34a', Rejected: '#dc2626' };
const statusBg    = { Pending: '#fffbeb', Approved: '#f0fdf4', Rejected: '#fef2f2' };

function Requests() {
  const [requests, setRequests] = useState(INITIAL);
  const [filter, setFilter]     = useState('all');
  const [rejectId, setRejectId] = useState(null);
  const [reason, setReason]     = useState('');

  const filtered = filter === 'all' ? requests : requests.filter(r => r.status.toLowerCase() === filter);

  const approve = (id) =>
    setRequests(r => r.map(req => req.id === id ? { ...req, status: 'Approved' } : req));

  const reject = (id) => {
    setRequests(r => r.map(req => req.id === id ? { ...req, status: 'Rejected' } : req));
    setRejectId(null); setReason('');
  };

  const days = (from, to) => Math.ceil((new Date(to) - new Date(from)) / (1000 * 60 * 60 * 24));

  return (
    <div className="page-container">
      <h2 className="page-heading">Rental Requests</h2>
      <p className="page-sub">Approve or reject incoming booking requests from customers</p>

      {/* Filter tabs */}
      <div style={{ display: 'flex', gap: 8, marginBottom: 20 }}>
        {['all', 'pending', 'approved', 'rejected'].map(f => (
          <button key={f} onClick={() => setFilter(f)}
            style={{ padding: '7px 16px', border: '1px solid #e2e8f0', borderRadius: 20,
              fontSize: 13, fontWeight: 500, cursor: 'pointer', textTransform: 'capitalize',
              background: filter === f ? '#1e293b' : '#fff',
              color: filter === f ? '#fff' : '#475569' }}>
            {f}
            {f === 'pending' && (
              <span style={{ marginLeft: 6, background: '#ef4444', color: '#fff',
                borderRadius: 10, fontSize: 10, padding: '1px 6px', fontWeight: 700 }}>
                {requests.filter(r => r.status === 'Pending').length}
              </span>
            )}
          </button>
        ))}
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        {filtered.length === 0 && (
          <div className="coming-soon-card">
            <div className="cs-icon">📭</div>
            <h3>No {filter} requests</h3>
          </div>
        )}
        {filtered.map(req => (
          <div key={req.id} style={{ background: '#fff', border: '1px solid #e2e8f0',
            borderRadius: 12, padding: '18px 20px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 10 }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginBottom: 6 }}>
                  <div style={{ width: 36, height: 36, borderRadius: '50%', background: '#eff6ff',
                    display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 700,
                    color: '#2563eb', fontSize: 15 }}>
                    {req.customer[0]}
                  </div>
                  <div>
                    <div style={{ fontSize: 14, fontWeight: 600, color: '#1e293b' }}>{req.customer}</div>
                    <div style={{ fontSize: 12, color: '#64748b' }}>{req.email} · {req.phone}</div>
                  </div>
                </div>
                <div style={{ fontSize: 13, color: '#374151', marginBottom: 4 }}>
                  🚗 <strong>{req.car}</strong> &nbsp;·&nbsp;
                  📅 {req.from} → {req.to} &nbsp;·&nbsp;
                  🕐 {days(req.from, req.to)} days
                </div>
                <div style={{ fontSize: 13, color: '#374151' }}>
                  💰 Total: <strong style={{ color: '#16a34a' }}>₹{req.amount}</strong>
                  &nbsp;&nbsp;<span style={{ fontSize: 11, color: '#94a3b8' }}>ID: {req.id}</span>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
                <span style={{ fontSize: 11, fontWeight: 600, padding: '4px 12px', borderRadius: 20,
                  background: statusBg[req.status], color: statusColor[req.status] }}>
                  {req.status}
                </span>
                {req.status === 'Pending' && (
                  <div style={{ display: 'flex', gap: 8 }}>
                    <button onClick={() => approve(req.id)}
                      style={{ padding: '7px 16px', background: '#16a34a', color: '#fff',
                        border: 'none', borderRadius: 7, fontSize: 13, fontWeight: 600, cursor: 'pointer' }}>
                      ✅ Approve
                    </button>
                    <button onClick={() => setRejectId(req.id)}
                      style={{ padding: '7px 16px', background: '#fef2f2', color: '#dc2626',
                        border: '1px solid #fca5a5', borderRadius: 7, fontSize: 13, fontWeight: 600, cursor: 'pointer' }}>
                      ❌ Reject
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Reject modal */}
      {rejectId && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
          display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
          <div style={{ background: '#fff', borderRadius: 12, padding: '24px 28px', maxWidth: 400, width: '100%' }}>
            <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 8 }}>Reject Request</h3>
            <p style={{ fontSize: 13, color: '#64748b', marginBottom: 14 }}>Add a reason for rejection (optional):</p>
            <textarea rows={3} value={reason} onChange={e => setReason(e.target.value)}
              placeholder="e.g. Vehicle not available on those dates"
              style={{ width: '100%', padding: '10px', border: '1.5px solid #d1d5db', borderRadius: 8,
                fontSize: 13, resize: 'vertical', boxSizing: 'border-box', marginBottom: 14 }} />
            <div style={{ display: 'flex', gap: 10 }}>
              <button onClick={() => reject(rejectId)}
                style={{ flex: 1, padding: '10px', background: '#dc2626', color: '#fff',
                  border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>
                Confirm Reject
              </button>
              <button onClick={() => { setRejectId(null); setReason(''); }}
                style={{ flex: 1, padding: '10px', background: '#f1f5f9', color: '#475569',
                  border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Requests;
