function Overview({ onNavigate }) {
  const stats = [
    { icon: '💰', label: 'Total Earnings',    value: '₹18,400', color: '#16a34a', bg: '#f0fdf4' },
    { icon: '🚗', label: 'My Vehicles',       value: '3',        color: '#2563eb', bg: '#eff6ff' },
    { icon: '📅', label: 'Active Rentals',    value: '1',        color: '#d97706', bg: '#fffbeb' },
    { icon: '🕐', label: 'Pending Requests',  value: '2',        color: '#dc2626', bg: '#fef2f2' },
  ];

  const recentActivity = [
    { type: 'request',  msg: 'New booking request for Maruti Swift',   time: '10 mins ago',  icon: '🔔' },
    { type: 'rental',   msg: 'Honda City returned by Priya S.',         time: '2 hours ago',  icon: '✅' },
    { type: 'payment',  msg: 'Payment of ₹2,400 received',             time: 'Yesterday',    icon: '💳' },
    { type: 'request',  msg: 'New booking request for Tata Nexon',     time: '2 days ago',   icon: '🔔' },
  ];

  return (
    <div className="page-container">
      <h2 className="page-heading">Overview</h2>
      <p className="page-sub">Your dashboard summary at a glance</p>

      {/* Stats cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 16, marginBottom: 28 }}>
        {stats.map((st, i) => (
          <div key={i} style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
            padding: '20px 18px', display: 'flex', alignItems: 'center', gap: 14,
            boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
            <div style={{ width: 46, height: 46, borderRadius: 10, background: st.bg,
              display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 22, flexShrink: 0 }}>
              {st.icon}
            </div>
            <div>
              <div style={{ fontSize: 22, fontWeight: 700, color: st.color }}>{st.value}</div>
              <div style={{ fontSize: 12, color: '#64748b', marginTop: 2 }}>{st.label}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Quick actions */}
      <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', marginBottom: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
        <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b', marginBottom: 14 }}>Quick Actions</h3>
        <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
          {[
            { label: '+ Add New Vehicle', key: 'add-vehicle', color: '#2563eb' },
            { label: '📋 View Requests',  key: 'requests',    color: '#d97706' },
            { label: '📊 View Earnings',  key: 'earnings',    color: '#16a34a' },
          ].map(btn => (
            <button key={btn.key} onClick={() => onNavigate(btn.key)}
              style={{ padding: '9px 18px', background: btn.color, color: '#fff',
                border: 'none', borderRadius: 8, fontSize: 13, fontWeight: 600,
                cursor: 'pointer' }}>
              {btn.label}
            </button>
          ))}
        </div>
      </div>

      {/* Recent activity */}
      <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
        <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b', marginBottom: 14 }}>Recent Activity</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 0 }}>
          {recentActivity.map((a, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 14,
              padding: '12px 0', borderBottom: i < recentActivity.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
              <div style={{ width: 36, height: 36, borderRadius: '50%', background: '#f1f5f9',
                display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 16, flexShrink: 0 }}>
                {a.icon}
              </div>
              <div style={{ flex: 1, fontSize: 13, color: '#374151' }}>{a.msg}</div>
              <div style={{ fontSize: 11, color: '#94a3b8', whiteSpace: 'nowrap' }}>{a.time}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Overview;
