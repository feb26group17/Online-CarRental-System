import { useState } from 'react';

const rentals = [
  { id: 'BK001', car: 'Honda City', customer: 'Priya Mehta',
    email: 'priya@email.com', phone: '9123456780',
    from: '2026-06-17', to: '2026-06-20', amount: 3600, daysLeft: 2 },
];

function ActiveRentals() {
  const [reportId, setReportId] = useState(null);
  const [report, setReport]     = useState('');
  const [submitted, setSubmitted] = useState(false);

  return (
    <div className="page-container">
      <h2 className="page-heading">Active Rentals</h2>
      <p className="page-sub">Vehicles currently rented out</p>

      {rentals.length === 0 ? (
        <div className="coming-soon-card">
          <div className="cs-icon">🎉</div>
          <h3>No active rentals right now</h3>
          <p>All your vehicles are available</p>
        </div>
      ) : rentals.map(r => (
        <div key={r.id} style={{ background: '#fff', border: '1px solid #e2e8f0',
          borderRadius: 12, padding: '20px 22px', marginBottom: 14,
          boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>

          {/* Header */}
          <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: 10, marginBottom: 16 }}>
            <div>
              <div style={{ fontSize: 16, fontWeight: 700, color: '#1e293b' }}>🚗 {r.car}</div>
              <div style={{ fontSize: 12, color: '#64748b', marginTop: 2 }}>Booking ID: {r.id}</div>
            </div>
            <span style={{ fontSize: 11, fontWeight: 700, padding: '4px 14px', borderRadius: 20,
              background: '#eff6ff', color: '#2563eb' }}>
              🟢 ACTIVE
            </span>
          </div>

          {/* Info grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12, marginBottom: 16 }}>
            {[
              { label: 'Renter',       value: r.customer },
              { label: 'Phone',        value: r.phone },
              { label: 'Email',        value: r.email },
              { label: 'From',         value: r.from },
              { label: 'Return date',  value: r.to },
              { label: 'Days left',    value: `${r.daysLeft} days`, highlight: true },
              { label: 'Total amount', value: `₹${r.amount}`, green: true },
            ].map((item, i) => (
              <div key={i} style={{ background: '#f8fafc', borderRadius: 8, padding: '10px 12px' }}>
                <div style={{ fontSize: 11, color: '#94a3b8', marginBottom: 3 }}>{item.label}</div>
                <div style={{ fontSize: 13, fontWeight: 600,
                  color: item.highlight ? '#d97706' : item.green ? '#16a34a' : '#1e293b' }}>
                  {item.value}
                </div>
              </div>
            ))}
          </div>

          {/* Progress bar */}
          <div style={{ marginBottom: 16 }}>
            <div style={{ fontSize: 12, color: '#64748b', marginBottom: 6 }}>
              Rental progress: {r.from} → {r.to}
            </div>
            <div style={{ background: '#e2e8f0', borderRadius: 99, height: 8, overflow: 'hidden' }}>
              <div style={{ background: '#2563eb', height: '100%', width: '60%', borderRadius: 99 }} />
            </div>
          </div>

          {/* Actions */}
          <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
            <a href={`tel:${r.phone}`}
              style={{ padding: '8px 18px', background: '#2563eb', color: '#fff',
                border: 'none', borderRadius: 8, fontSize: 13, fontWeight: 600,
                cursor: 'pointer', textDecoration: 'none' }}>
              📞 Call Renter
            </a>
            <a href={`mailto:${r.email}`}
              style={{ padding: '8px 18px', background: '#f1f5f9', color: '#475569',
                border: '1px solid #e2e8f0', borderRadius: 8, fontSize: 13, fontWeight: 600,
                cursor: 'pointer', textDecoration: 'none' }}>
              ✉️ Email Renter
            </a>
            <button onClick={() => setReportId(r.id)}
              style={{ padding: '8px 18px', background: '#fef2f2', color: '#dc2626',
                border: '1px solid #fca5a5', borderRadius: 8, fontSize: 13, fontWeight: 600, cursor: 'pointer' }}>
              ⚠️ Report Damage
            </button>
          </div>
        </div>
      ))}

      {/* Report damage modal */}
      {reportId && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)',
          display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
          <div style={{ background: '#fff', borderRadius: 12, padding: '24px 28px', maxWidth: 420, width: '100%' }}>
            {submitted ? (
              <div style={{ textAlign: 'center', padding: '16px 0' }}>
                <div style={{ fontSize: 48, marginBottom: 12 }}>✅</div>
                <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 8 }}>Damage report submitted</h3>
                <p style={{ fontSize: 13, color: '#64748b', marginBottom: 20 }}>Admin will review and take action.</p>
                <button onClick={() => { setReportId(null); setSubmitted(false); setReport(''); }}
                  className="btn-primary">Close</button>
              </div>
            ) : (
              <>
                <h3 style={{ fontSize: 16, fontWeight: 700, marginBottom: 8 }}>⚠️ Report Damage</h3>
                <p style={{ fontSize: 13, color: '#64748b', marginBottom: 14 }}>
                  Describe the damage observed:
                </p>
                <textarea rows={4} value={report} onChange={e => setReport(e.target.value)}
                  placeholder="e.g. Front bumper scratch, broken side mirror..."
                  style={{ width: '100%', padding: '10px', border: '1.5px solid #d1d5db', borderRadius: 8,
                    fontSize: 13, resize: 'vertical', boxSizing: 'border-box', marginBottom: 14 }} />
                <div style={{ display: 'flex', gap: 10 }}>
                  <button onClick={() => setSubmitted(true)}
                    style={{ flex: 1, padding: '10px', background: '#dc2626', color: '#fff',
                      border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>
                    Submit Report
                  </button>
                  <button onClick={() => { setReportId(null); setReport(''); }}
                    style={{ flex: 1, padding: '10px', background: '#f1f5f9', color: '#475569',
                      border: 'none', borderRadius: 8, fontWeight: 600, cursor: 'pointer', fontSize: 14 }}>
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
