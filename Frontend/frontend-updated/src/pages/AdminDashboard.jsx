import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../redux/slices/authSlice';
import { crudApi } from '../api/axios';

// UserResponse:    id, name, email, phone, role, status (ACTIVE|BLOCKED), address, adharCard, createdAt
// BookingResponse: bookingId, customerId, vehicleId, vehicleRegistrationNumber, modelName,
//                  bookingDate, pickupDate, returnDate, dropCity, status, totalAmount
// PaymentResponse: paymentId, bookingId, amt, paymentMethod, paymentStatus, paymentDate
// RefundResponse:  refundId, paymentId, refAmount, reason, status (Pending|Approved|Rejected|Completed), refundDate

function AdminDashboard() {
  const { user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [activeTab, setActiveTab] = useState('users');
  const [usersList, setUsersList] = useState([]);
  const [bookingsList, setBookingsList] = useState([]);
  const [paymentsList, setPaymentsList] = useState([]);
  const [refundsList, setRefundsList] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogout = () => {
    dispatch(logout());
    navigate('/');
  };

  useEffect(() => {
    if (activeTab === 'users')    fetchUsers();
    else if (activeTab === 'bookings') fetchBookings();
    else if (activeTab === 'payments') fetchPayments();
    else if (activeTab === 'refunds')  fetchRefunds();
  }, [activeTab]);

  const fetchUsers = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/users');
      setUsersList(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch users');
    } finally {
      setLoading(false);
    }
  };

  const fetchBookings = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/bookings');
      setBookingsList(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch system bookings');
    } finally {
      setLoading(false);
    }
  };

  const fetchPayments = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/payments');
      setPaymentsList(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch payments');
    } finally {
      setLoading(false);
    }
  };

  const fetchRefunds = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/refunds');
      setRefundsList(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch refunds');
    } finally {
      setLoading(false);
    }
  };

  const toggleUserStatus = async (targetUser) => {
    // UserStatus enum: ACTIVE | BLOCKED  (not INACTIVE)
    const newStatus = targetUser.status === 'ACTIVE' ? 'BLOCKED' : 'ACTIVE';
    try {
      await crudApi.patch(`/users/${targetUser.id}/status`, { status: newStatus });
      fetchUsers();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update user status');
    }
  };

  // Booking status style — BookingStatus: Pending|Confirmed|Cancelled|Completed
  const bookingStatusStyle = (status) => {
    if (status === 'Confirmed') return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Completed') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Cancelled') return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  // Payment status style — PaymentStatus: Pending|Paid|Failed|Refunded
  const paymentStatusStyle = (status) => {
    if (status === 'Paid')     return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Refunded') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Failed')   return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  // Refund status style — RefundStatus: Pending|Approved|Rejected|Completed
  const refundStatusStyle = (status) => {
    if (status === 'Approved')  return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Completed') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Rejected')  return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  return (
    <div className="dashboard-wrapper">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-brand">🚗 CarRental Admin</div>
        <div className="sidebar-user">
          <div className="sidebar-avatar">{user?.name?.charAt(0).toUpperCase() || 'A'}</div>
          <div>
            <div className="sidebar-username">{user?.name || 'Administrator'}</div>
            <div className="sidebar-role">Super Admin</div>
          </div>
        </div>

        <nav className="sidebar-nav">
          {[
            { key: 'users',    label: 'User Management', icon: '👥', sub: 'Accounts & status' },
            { key: 'bookings', label: 'All Bookings',     icon: '📅', sub: 'Global reservation list' },
            { key: 'payments', label: 'Payments',         icon: '💳', sub: 'Financial transactions' },
            { key: 'refunds',  label: 'Refund Requests',  icon: '↩️', sub: 'Processed & pending' }
          ].map((item) => (
            <button key={item.key} className={`nav-btn ${activeTab === item.key ? 'active' : ''}`}
              onClick={() => setActiveTab(item.key)}>
              <span className="nav-icon">{item.icon}</span>
              <span>
                <div className="nav-label">{item.label}</div>
                <div className="nav-sub">{item.sub}</div>
              </span>
            </button>
          ))}
        </nav>

        <button className="sidebar-logout" onClick={handleLogout}>🚪 Logout</button>
      </aside>

      {/* Main Content */}
      <main className="dashboard-main">
        <header className="dashboard-topbar">
          <h1>🛠️ System Administration</h1>
          <span className="topbar-greeting">Welcome, {user?.name} 👋</span>
        </header>

        <div className="dashboard-content">
          <div className="page-container">
            {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}

            {/* ─── User Management Tab ─── */}
            {activeTab === 'users' && (
              <div>
                <h2 className="page-heading">Registered Users</h2>
                <p className="page-sub">Manage active accounts, customer & car owner profiles</p>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading users...</div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>User ID</th>
                          <th style={{ padding: '14px 18px' }}>Full Name</th>
                          <th style={{ padding: '14px 18px' }}>Email</th>
                          <th style={{ padding: '14px 18px' }}>Phone</th>
                          <th style={{ padding: '14px 18px' }}>Role</th>
                          <th style={{ padding: '14px 18px' }}>Status</th>
                          <th style={{ padding: '14px 18px', textAlign: 'right' }}>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {usersList.map((u, idx) => (
                          <tr key={u.id} style={{ borderBottom: idx < usersList.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                            <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{u.id}</td>
                            {/* UserResponse uses 'name' (not 'fullName') */}
                            <td style={{ padding: '14px 18px', fontWeight: 600 }}>{u.name}</td>
                            <td style={{ padding: '14px 18px', color: '#64748b' }}>{u.email}</td>
                            <td style={{ padding: '14px 18px', color: '#64748b' }}>{u.phone || 'N/A'}</td>
                            <td style={{ padding: '14px 18px' }}>
                              <span style={{ fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                                background: u.role === 'ADMIN' ? '#f5f3ff' : u.role === 'OWNER' ? '#eff6ff' : '#f0fdf4',
                                color: u.role === 'ADMIN' ? '#7c3aed' : u.role === 'OWNER' ? '#2563eb' : '#16a34a' }}>
                                {u.role}
                              </span>
                            </td>
                            <td style={{ padding: '14px 18px' }}>
                              {/* UserStatus: ACTIVE | BLOCKED */}
                              <span style={{ fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                                background: u.status === 'ACTIVE' ? '#f0fdf4' : '#fef2f2',
                                color: u.status === 'ACTIVE' ? '#16a34a' : '#dc2626' }}>
                                {u.status || 'ACTIVE'}
                              </span>
                            </td>
                            <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                              {u.role !== 'ADMIN' && (
                                <button onClick={() => toggleUserStatus(u)} style={{
                                  padding: '6px 12px',
                                  background: u.status === 'ACTIVE' ? '#fef2f2' : '#f0fdf4',
                                  color: u.status === 'ACTIVE' ? '#dc2626' : '#16a34a',
                                  border: '1px solid #e2e8f0', borderRadius: 6, fontSize: 12, fontWeight: 600, cursor: 'pointer'
                                }}>
                                  {/* Correct toggle: ACTIVE → BLOCKED (not INACTIVE) */}
                                  {u.status === 'ACTIVE' ? 'Block' : 'Activate'}
                                </button>
                              )}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {/* ─── Bookings Tab ─── */}
            {activeTab === 'bookings' && (
              <div>
                <h2 className="page-heading">System Reservations</h2>
                <p className="page-sub">Monitor all customer bookings across the platform</p>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading bookings...</div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>Booking ID</th>
                          <th style={{ padding: '14px 18px' }}>Customer ID</th>
                          <th style={{ padding: '14px 18px' }}>Vehicle</th>
                          <th style={{ padding: '14px 18px' }}>Registration</th>
                          <th style={{ padding: '14px 18px' }}>Dates</th>
                          <th style={{ padding: '14px 18px' }}>Total</th>
                          <th style={{ padding: '14px 18px' }}>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {bookingsList.map((b, idx) => {
                          // BookingResponse: bookingId, customerId, modelName,
                          //   vehicleRegistrationNumber, pickupDate, returnDate, status, totalAmount
                          const ss = bookingStatusStyle(b.status);
                          return (
                            <tr key={b.bookingId} style={{ borderBottom: idx < bookingsList.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{b.bookingId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{b.customerId}</td>
                              {/* modelName is the vehicle name in BookingResponse */}
                              <td style={{ padding: '14px 18px', fontWeight: 600 }}>{b.modelName || `Vehicle #${b.vehicleId}`}</td>
                              {/* vehicleRegistrationNumber is the RC field */}
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>{b.vehicleRegistrationNumber || 'N/A'}</td>
                              {/* pickupDate / returnDate are the correct field names */}
                              <td style={{ padding: '14px 18px', color: '#64748b', fontSize: 13 }}>{b.pickupDate} → {b.returnDate}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#2563eb' }}>₹{b.totalAmount}</td>
                              <td style={{ padding: '14px 18px' }}>
                                {/* status is Pascal case: Pending|Confirmed|Cancelled|Completed */}
                                <span style={{ fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20, background: ss.bg, color: ss.text }}>
                                  {b.status}
                                </span>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {/* ─── Payments Tab ─── */}
            {activeTab === 'payments' && (
              <div>
                <h2 className="page-heading">Payment Transactions</h2>
                <p className="page-sub">View global payment logs and transaction methods</p>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading payments...</div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>Payment ID</th>
                          <th style={{ padding: '14px 18px' }}>Booking ID</th>
                          <th style={{ padding: '14px 18px' }}>Method</th>
                          <th style={{ padding: '14px 18px' }}>Amount</th>
                          <th style={{ padding: '14px 18px' }}>Date</th>
                          <th style={{ padding: '14px 18px' }}>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {paymentsList.map((p, idx) => {
                          // PaymentResponse: paymentId, bookingId, amt, paymentMethod, paymentStatus, paymentDate
                          const ss = paymentStatusStyle(p.paymentStatus);
                          return (
                            <tr key={p.paymentId} style={{ borderBottom: idx < paymentsList.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              {/* paymentId is the correct id field */}
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{p.paymentId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{p.bookingId}</td>
                              {/* paymentMethod is the @JsonValue display string: "Credit Card", "UPI", etc. */}
                              <td style={{ padding: '14px 18px' }}>{p.paymentMethod || 'N/A'}</td>
                              {/* amt is the correct field name (not amount) */}
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#16a34a' }}>₹{p.amt}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>
                                {p.paymentDate ? new Date(p.paymentDate).toLocaleDateString() : 'N/A'}
                              </td>
                              <td style={{ padding: '14px 18px' }}>
                                {/* PaymentStatus: Pending|Paid|Failed|Refunded */}
                                <span style={{ fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20, background: ss.bg, color: ss.text }}>
                                  {p.paymentStatus || 'Pending'}
                                </span>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {/* ─── Refunds Tab ─── */}
            {activeTab === 'refunds' && (
              <div>
                <h2 className="page-heading">Refund Requests</h2>
                <p className="page-sub">Track customer refund submissions and status</p>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading refunds...</div>
                ) : refundsList.length === 0 ? (
                  <div className="coming-soon-card" style={{ background: '#fff' }}>
                    <div className="cs-icon">↩️</div>
                    <h3>No Refund Requests</h3>
                    <p>There are currently no customer refund requests registered.</p>
                  </div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>Refund ID</th>
                          <th style={{ padding: '14px 18px' }}>Payment ID</th>
                          <th style={{ padding: '14px 18px' }}>Refund Amount</th>
                          <th style={{ padding: '14px 18px' }}>Reason</th>
                          <th style={{ padding: '14px 18px' }}>Status</th>
                          <th style={{ padding: '14px 18px' }}>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        {refundsList.map((r, idx) => {
                          // RefundResponse: refundId, paymentId, refAmount, reason, status, refundDate
                          const ss = refundStatusStyle(r.status);
                          return (
                            <tr key={r.refundId} style={{ borderBottom: idx < refundsList.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              {/* refundId is the correct id field */}
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{r.refundId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{r.paymentId}</td>
                              {/* refAmount is the correct field (not refundAmount) */}
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#dc2626' }}>₹{r.refAmount}</td>
                              <td style={{ padding: '14px 18px', color: '#475569' }}>{r.reason || 'N/A'}</td>
                              <td style={{ padding: '14px 18px' }}>
                                {/* RefundStatus: Pending|Approved|Rejected|Completed */}
                                {/* r.status is the correct field (not r.refundStatus) */}
                                <span style={{ fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20, background: ss.bg, color: ss.text }}>
                                  {r.status || 'Pending'}
                                </span>
                              </td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>
                                {r.refundDate ? new Date(r.refundDate).toLocaleDateString() : 'N/A'}
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

          </div>
        </div>
      </main>
    </div>
  );
}

export default AdminDashboard;
