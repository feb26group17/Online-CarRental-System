import { useState, useEffect } from 'react';
import { bookingApi, crudApi } from '../../api/axios';

// VehicleResponse: vehicleId, modelName, brandName, registrationNumber, ...
// BookingResponse: bookingId, status (Pending/Confirmed/Cancelled/Completed), totalAmount, ...

function Overview({ onNavigate }) {
  const [stats, setStats] = useState({
    totalEarnings: 0,
    vehicleCount: 0,
    activeRentals: 0,
    pendingRequests: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOverviewData();
  }, []);

  const fetchOverviewData = async () => {
    setLoading(true);
    try {
      // 1. Fetch owner's vehicles from crud-service on port 8082
      const vRes = await crudApi.get('/vehicles/my');
      const vehicles = vRes.data || [];

      let totalEarnings = 0;
      let activeCount = 0;
      let pendingCount = 0;

      for (const car of vehicles) {
        try {
          // 2. Fetch bookings for this vehicle from ocrs-booking-service on port 8083
          const bRes = await bookingApi.get(`/bookings/vehicle/${car.vehicleId}`);
          if (bRes.data) {
            bRes.data.forEach(b => {
              if (b.status === 'Confirmed' || b.status === 'Completed') {
                totalEarnings += parseFloat(b.totalAmount || 0);
              }
              if (b.status === 'Confirmed') activeCount++;
              if (b.status === 'Pending')   pendingCount++;
            });
          }
        } catch (e) {
          console.error(`Failed to fetch bookings for vehicle ${car.vehicleId}`, e);
        }
      }

      setStats({
        totalEarnings,
        vehicleCount: vehicles.length,
        activeRentals: activeCount,
        pendingRequests: pendingCount
      });
    } catch (err) {
      console.error('Failed to load overview metrics', err);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    { icon: '💰', label: 'Total Earnings',    value: `₹${stats.totalEarnings.toLocaleString()}`, color: '#16a34a', bg: '#f0fdf4' },
    { icon: '🚗', label: 'My Vehicles',       value: stats.vehicleCount.toString(),              color: '#2563eb', bg: '#eff6ff' },
    { icon: '🔑', label: 'Active Rentals',    value: stats.activeRentals.toString(),             color: '#d97706', bg: '#fffbeb' },
    { icon: '🕐', label: 'Pending Requests',  value: stats.pendingRequests.toString(),           color: '#dc2626', bg: '#fef2f2' },
  ];

  return (
    <div className="page-container">
      <h2 className="page-heading">Overview</h2>
      <p className="page-sub">Your fleet dashboard summary at a glance</p>

      {/* Stats cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 16, marginBottom: 28 }}>
        {statCards.map((st, i) => (
          <div key={i} style={{
            background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
            padding: '20px 18px', display: 'flex', alignItems: 'center', gap: 14,
            boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
          }}>
            <div style={{
              width: 46, height: 46, borderRadius: 10, background: st.bg,
              display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 22, flexShrink: 0
            }}>
              {st.icon}
            </div>
            <div>
              <div style={{ fontSize: 22, fontWeight: 700, color: st.color }}>{loading ? '...' : st.value}</div>
              <div style={{ fontSize: 12, color: '#64748b', marginTop: 2 }}>{st.label}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Quick actions */}
      <div style={{
        background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12,
        padding: '20px 22px', marginBottom: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <h3 style={{ fontSize: 15, fontWeight: 600, color: '#1e293b', marginBottom: 14 }}>Quick Actions</h3>
        <div style={{ display: 'flex', gap: 10, flexWrap: 'wrap' }}>
          {[
            { label: '+ Add New Vehicle', key: 'vehicles', color: '#2563eb' },
            { label: '📋 View Requests',  key: 'requests', color: '#d97706' },
            { label: '📊 View Earnings',  key: 'earnings', color: '#16a34a' },
          ].map(btn => (
            <button key={btn.key} onClick={() => onNavigate(btn.key)}
              style={{
                padding: '9px 18px', background: btn.color, color: '#fff',
                border: 'none', borderRadius: 8, fontSize: 13, fontWeight: 600,
                cursor: 'pointer'
              }}>
              {btn.label}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Overview;
