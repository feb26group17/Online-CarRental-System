import { useState } from 'react';

const monthlyData = [
  { month: 'Jan', amount: 2400 },
  { month: 'Feb', amount: 3200 },
  { month: 'Mar', amount: 1800 },
  { month: 'Apr', amount: 4100 },
  { month: 'May', amount: 3600 },
  { month: 'Jun', amount: 3300 },
];

const perVehicle = [
  { car: 'Maruti Swift', trips: 6, revenue: 7200,  paid: 6800  },
  { car: 'Honda City',   trips: 4, revenue: 8400,  paid: 8400  },
  { car: 'Tata Nexon',   trips: 2, revenue: 4500,  paid: 3200  },
];

function BarChart({ data }) {
  const max = Math.max(...data.map(d => d.amount));
  return (
    <div style={{ display: 'flex', alignItems: 'flex-end', gap: 10, height: 160, padding: '0 4px' }}>
      {data.map((d, i) => (
        <div key={i} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6 }}>
          <div style={{ fontSize: 10, color: '#64748b', fontWeight: 500 }}>
            ₹{(d.amount / 1000).toFixed(1)}k
          </div>
          <div style={{ width: '100%', background: '#2563eb', borderRadius: '4px 4px 0 0',
            height: `${(d.amount / max) * 110}px`, transition: 'height 0.3s',
            opacity: 0.75 + (i / data.length) * 0.25 }} />
          <div style={{ fontSize: 11, color: '#94a3b8' }}>{d.month}</div>
        </div>
      ))}
    </div>
  );
}

function Earnings() {
  const [filter, setFilter] = useState('all');

  const totalRevenue = perVehicle.reduce((s, v) => s + v.revenue, 0);
  const totalPaid    = perVehicle.reduce((s, v) => s + v.paid, 0);
  const totalPending = totalRevenue - totalPaid;

  return (
    <div className="page-container">
      <h2 className="page-heading">Earnings</h2>
      <p className="page-sub">Track your revenue across all vehicles</p>

      {/* Summary cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 14, marginBottom: 24 }}>
        {[
          { label: 'Total Revenue',  value: `₹${totalRevenue.toLocaleString()}`, color: '#2563eb', bg: '#eff6ff' },
          { label: 'Paid Out',       value: `₹${totalPaid.toLocaleString()}`,    color: '#16a34a', bg: '#f0fdf4' },
          { label: 'Pending',        value: `₹${totalPending.toLocaleString()}`, color: '#d97706', bg: '#fffbeb' },
          { label: 'Total Trips',    value: `${perVehicle.reduce((s,v) => s + v.trips, 0)}`, color: '#7c3aed', bg: '#f5f3ff' },
        ].map((c, i) => (
          <div key={i} style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
            padding: '16px 18px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
            <div style={{ fontSize: 12, color: '#64748b', marginBottom: 6 }}>{c.label}</div>
            <div style={{ fontSize: 20, fontWeight: 700, color: c.color }}>{c.value}</div>
          </div>
        ))}
      </div>

      {/* Monthly chart */}
      <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', marginBottom: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b' }}>Monthly Revenue (2026)</h3>
          <span style={{ fontSize: 12, color: '#94a3b8' }}>Jan – Jun</span>
        </div>
        <BarChart data={monthlyData} />
      </div>

      {/* Per vehicle breakdown */}
      <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
          <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b' }}>Revenue by Vehicle</h3>
          <select value={filter} onChange={e => setFilter(e.target.value)}
            style={{ padding: '6px 12px', border: '1px solid #e2e8f0', borderRadius: 6,
              fontSize: 13, background: '#fff', color: '#475569' }}>
            <option value="all">All vehicles</option>
            {perVehicle.map(v => <option key={v.car} value={v.car}>{v.car}</option>)}
          </select>
        </div>

        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: '#f8fafc' }}>
              {['Vehicle', 'Trips', 'Revenue', 'Paid Out', 'Pending'].map(h => (
                <th key={h} style={{ padding: '10px 14px', fontSize: 12, fontWeight: 600,
                  color: '#64748b', textAlign: 'left', borderBottom: '1px solid #e2e8f0' }}>
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {perVehicle.filter(v => filter === 'all' || v.car === filter).map((v, i) => (
              <tr key={i} style={{ borderBottom: '1px solid #f1f5f9' }}>
                <td style={{ padding: '12px 14px', fontSize: 13, fontWeight: 600, color: '#1e293b' }}>{v.car}</td>
                <td style={{ padding: '12px 14px', fontSize: 13, color: '#374151' }}>{v.trips}</td>
                <td style={{ padding: '12px 14px', fontSize: 13, fontWeight: 600, color: '#2563eb' }}>₹{v.revenue.toLocaleString()}</td>
                <td style={{ padding: '12px 14px', fontSize: 13, color: '#16a34a', fontWeight: 600 }}>₹{v.paid.toLocaleString()}</td>
                <td style={{ padding: '12px 14px' }}>
                  <span style={{ fontSize: 12, fontWeight: 600, padding: '3px 10px', borderRadius: 20,
                    background: v.revenue - v.paid > 0 ? '#fffbeb' : '#f0fdf4',
                    color: v.revenue - v.paid > 0 ? '#d97706' : '#16a34a' }}>
                    ₹{(v.revenue - v.paid).toLocaleString()}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default Earnings;
