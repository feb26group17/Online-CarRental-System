import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../redux/slices/authSlice';
import { crudApi } from '../api/axios';
import Profile from './common/Profile';

function AdminDashboard() {
  const { user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [activeTab, setActiveTab] = useState('overview');
  
  // Data lists
  const [usersList, setUsersList] = useState([]);
  const [vehiclesList, setVehiclesList] = useState([]);
  const [bookingsList, setBookingsList] = useState([]);
  const [paymentsList, setPaymentsList] = useState([]);
  const [refundsList, setRefundsList] = useState([]);

  // Loading & Error handling
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [actionMsg, setActionMsg] = useState('');

  // Filters & Search
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('ALL');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [userStatusFilter, setUserStatusFilter] = useState('ALL');

  // Selected User Modal for Registration Approval & Review
  const [selectedUserModal, setSelectedUserModal] = useState(null);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/');
  };

  const showNotification = (msg) => {
    setActionMsg(msg);
    setTimeout(() => setActionMsg(''), 3000);
  };

  useEffect(() => {
    setSearchTerm('');
    setStatusFilter('ALL');
    setRoleFilter('ALL');
    
    if (activeTab === 'overview') {
      fetchAllData();
    } else if (activeTab === 'users') {
      fetchUsers();
    } else if (activeTab === 'vehicles') {
      fetchVehicles();
    } else if (activeTab === 'bookings') {
      fetchBookings();
    } else if (activeTab === 'payments') {
      fetchPayments();
    } else if (activeTab === 'refunds') {
      fetchRefunds();
    }
  }, [activeTab]);

  const fetchAllData = async () => {
    setLoading(true);
    setError('');
    try {
      const [uRes, vRes, bRes, pRes, rRes] = await Promise.allSettled([
        crudApi.get('/users'),
        crudApi.get('/vehicles'),
        crudApi.get('/bookings'),
        crudApi.get('/payments'),
        crudApi.get('/refunds')
      ]);

      if (uRes.status === 'fulfilled') setUsersList(uRes.value.data || []);
      if (vRes.status === 'fulfilled') setVehiclesList(vRes.value.data || []);
      if (bRes.status === 'fulfilled') setBookingsList(bRes.value.data || []);
      if (pRes.status === 'fulfilled') setPaymentsList(pRes.value.data || []);
      if (rRes.status === 'fulfilled') setRefundsList(rRes.value.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch dashboard metadata');
    } finally {
      setLoading(false);
    }
  };

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

  const fetchVehicles = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await crudApi.get('/vehicles');
      setVehiclesList(res.data || []);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch vehicles');
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
      setError(err.response?.data?.message || 'Failed to fetch bookings');
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

  // Change user status (ACTIVE / BLOCKED)
  const changeUserStatus = async (targetUser, newStatus) => {
    try {
      await crudApi.patch(`/users/${targetUser.id}/status`, { status: newStatus });
      showNotification(`User #${targetUser.id} (${targetUser.name}) status updated to ${newStatus}`);
      fetchUsers();
      if (selectedUserModal && selectedUserModal.id === targetUser.id) {
        setSelectedUserModal(prev => prev ? { ...prev, status: newStatus } : null);
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update user status');
    }
  };

  // Admin update booking status
  const updateBookingStatus = async (bookingId, newStatus) => {
    try {
      await crudApi.patch(`/bookings/${bookingId}/status`, { status: newStatus });
      showNotification(`Booking #${bookingId} updated to ${newStatus}`);
      fetchBookings();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update booking status');
    }
  };

  // Admin update vehicle status
  const updateVehicleStatus = async (vehicleId, newStatus) => {
    try {
      await crudApi.patch(`/vehicles/${vehicleId}/status`, { status: newStatus });
      showNotification(`Vehicle #${vehicleId} status updated to ${newStatus}`);
      fetchVehicles();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to update vehicle status');
    }
  };

  // Status Styling Helpers
  const bookingStatusStyle = (status) => {
    if (status === 'Confirmed') return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Completed') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Cancelled') return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  const paymentStatusStyle = (status) => {
    if (status === 'Paid')     return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Refunded') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Failed')   return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  const refundStatusStyle = (status) => {
    if (status === 'Approved')  return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'Completed') return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'Rejected')  return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#fffbeb', text: '#d97706' }; // Pending
  };

  const vehicleStatusStyle = (status) => {
    if (status === 'AVAILABLE')   return { bg: '#f0fdf4', text: '#16a34a' };
    if (status === 'RENTED')      return { bg: '#eff6ff', text: '#2563eb' };
    if (status === 'MAINTENANCE') return { bg: '#fef2f2', text: '#dc2626' };
    return { bg: '#f8fafc', text: '#64748b' };
  };

  // Revenue calculation
  const totalRevenue = paymentsList
    .filter(p => p.paymentStatus === 'Paid')
    .reduce((sum, p) => sum + (p.amt || 0), 0);

  // Filtered Users
  const filteredUsers = usersList.filter(u => {
    const matchesSearch = (u.name || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (u.email || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (u.phone || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (u.adharCard || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (u.drivingLicense || '').toLowerCase().includes(searchTerm.toLowerCase());
    const matchesRole = roleFilter === 'ALL' || u.role === roleFilter;
    const matchesStatus = userStatusFilter === 'ALL' || (u.status || 'ACTIVE') === userStatusFilter;
    return matchesSearch && matchesRole && matchesStatus;
  });

  // Filtered Bookings
  const filteredBookings = bookingsList.filter(b => {
    const matchesSearch = String(b.bookingId).includes(searchTerm) ||
                          String(b.customerId).includes(searchTerm) ||
                          (b.modelName || '').toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || b.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  // Filtered Vehicles
  const filteredVehicles = vehiclesList.filter(v => {
    const matchesSearch = (v.modelName || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (v.registrationNumber || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (v.city || '').toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || v.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  // Filtered Payments
  const filteredPayments = paymentsList.filter(p => {
    const matchesSearch = String(p.paymentId).includes(searchTerm) ||
                          String(p.bookingId).includes(searchTerm);
    const matchesStatus = statusFilter === 'ALL' || p.paymentStatus === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="dashboard-wrapper">
      {/* Sidebar Navigation */}
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
            { key: 'overview', label: 'Dashboard Overview', icon: '📊', sub: 'System KPI & stats' },
            { key: 'users',    label: 'User Management',   icon: '👥', sub: 'Accounts & roles' },
            { key: 'vehicles', label: 'Fleet Vehicles',     icon: '🚘', sub: 'Cars & availability' },
            { key: 'bookings', label: 'All Bookings',       icon: '📅', sub: 'Global reservations' },
            { key: 'payments', label: 'Payment Logs',      icon: '💳', sub: 'Financial transactions' },
            { key: 'refunds',  label: 'Refund Requests',    icon: '↩️', sub: 'Customer refunds' },
            { key: 'profile',  label: 'My Profile',         icon: '👤', sub: 'View & edit details' }
          ].map((item) => (
            <button
              key={item.key}
              className={`nav-btn ${activeTab === item.key ? 'active' : ''}`}
              onClick={() => setActiveTab(item.key)}
            >
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

      {/* Main Content Area */}
      <main className="dashboard-main">
        <header className="dashboard-topbar">
          <h1>🛠️ Admin Control Center</h1>
          <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
            <span className="topbar-greeting">Welcome back, <strong>{user?.name}</strong> 👋</span>
          </div>
        </header>

        <div className="dashboard-content">
          <div className="page-container">
            {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}
            {actionMsg && <div className="alert-success" style={{ marginBottom: 20 }}>{actionMsg}</div>}

            {/* ─── TAB 1: OVERVIEW DASHBOARD ─── */}
            {activeTab === 'overview' && (
              <div>
                <h2 className="page-heading">System KPI Overview</h2>
                <p className="page-sub">Live real-time operational indicators from OCRS CRUD Microservice</p>

                <div className="stats-grid">
                  <div className="stat-card">
                    <div className="stat-header">
                      <span className="stat-title">Total Users</span>
                      <div className="stat-icon" style={{ background: '#eff6ff', color: '#2563eb' }}>👥</div>
                    </div>
                    <div className="stat-value">{usersList.length}</div>
                    <div className="stat-desc">Registered Accounts</div>
                  </div>

                  <div className="stat-card">
                    <div className="stat-header">
                      <span className="stat-title">Fleet Cars</span>
                      <div className="stat-icon" style={{ background: '#f0fdf4', color: '#16a34a' }}>🚘</div>
                    </div>
                    <div className="stat-value">{vehiclesList.length}</div>
                    <div className="stat-desc">Active Vehicles Listed</div>
                  </div>

                  <div className="stat-card">
                    <div className="stat-header">
                      <span className="stat-title">Total Bookings</span>
                      <div className="stat-icon" style={{ background: '#f5f3ff', color: '#7c3aed' }}>📅</div>
                    </div>
                    <div className="stat-value">{bookingsList.length}</div>
                    <div className="stat-desc">Customer Reservations</div>
                  </div>

                  <div className="stat-card">
                    <div className="stat-header">
                      <span className="stat-title">Total Revenue</span>
                      <div className="stat-icon" style={{ background: '#ecfdf5', color: '#059669' }}>💰</div>
                    </div>
                    <div className="stat-value">₹{totalRevenue.toLocaleString()}</div>
                    <div className="stat-desc">Paid Transactions Sum</div>
                  </div>
                </div>

                {/* Recent Bookings Quick Table */}
                <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, padding: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
                    <h3 style={{ fontSize: 16, fontWeight: 700, color: '#1e293b' }}>Recent System Bookings</h3>
                    <button
                      onClick={() => setActiveTab('bookings')}
                      style={{ background: 'none', border: 'none', color: '#2563eb', fontWeight: 600, fontSize: 13, cursor: 'pointer' }}
                    >
                      View All →
                    </button>
                  </div>

                  {bookingsList.length === 0 ? (
                    <div style={{ textAlign: 'center', padding: 20, color: '#64748b' }}>No bookings recorded yet.</div>
                  ) : (
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '12px 16px' }}>Booking ID</th>
                          <th style={{ padding: '12px 16px' }}>Vehicle</th>
                          <th style={{ padding: '12px 16px' }}>Dates</th>
                          <th style={{ padding: '12px 16px' }}>Amount</th>
                          <th style={{ padding: '12px 16px' }}>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {bookingsList.slice(0, 5).map((b, idx) => {
                          const ss = bookingStatusStyle(b.status);
                          return (
                            <tr key={b.bookingId} style={{ borderBottom: idx < 4 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '12px 16px', fontWeight: 600 }}>#{b.bookingId}</td>
                              <td style={{ padding: '12px 16px' }}>{b.modelName || `Vehicle #${b.vehicleId}`}</td>
                              <td style={{ padding: '12px 16px', color: '#64748b', fontSize: 13 }}>{b.pickupDate} → {b.returnDate}</td>
                              <td style={{ padding: '12px 16px', fontWeight: 700, color: '#2563eb' }}>₹{b.totalAmount}</td>
                              <td style={{ padding: '12px 16px' }}>
                                <span className="badge-pill" style={{ background: ss.bg, color: ss.text }}>{b.status}</span>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  )}
                </div>
              </div>
            )}

            {/* ─── TAB 2: USER MANAGEMENT ─── */}
            {activeTab === 'users' && (
              <div>
                <h2 className="page-heading">User Accounts & Registration Approval</h2>
                <p className="page-sub">Review customer & car owner registration details, verify IDs, and approve access</p>

                <div className="table-toolbar">
                  <div className="search-box">
                    <span>🔍</span>
                    <input
                      type="text"
                      placeholder="Search by name, email, phone, Aadhar or license..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>

                  <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
                    <div className="filter-group">
                      <label style={{ fontSize: 13, fontWeight: 600, color: '#64748b' }}>Filter Role:</label>
                      <select
                        className="filter-select"
                        value={roleFilter}
                        onChange={(e) => setRoleFilter(e.target.value)}
                      >
                        <option value="ALL">All Roles</option>
                        <option value="CUSTOMER">Customer</option>
                        <option value="OWNER">Car Owner</option>
                        <option value="ADMIN">Admin</option>
                      </select>
                    </div>

                    <div className="filter-group">
                      <label style={{ fontSize: 13, fontWeight: 600, color: '#64748b' }}>Status:</label>
                      <select
                        className="filter-select"
                        value={userStatusFilter}
                        onChange={(e) => setUserStatusFilter(e.target.value)}
                      >
                        <option value="ALL">All Statuses</option>
                        <option value="ACTIVE">Active / Approved</option>
                        <option value="BLOCKED">Blocked / Pending</option>
                      </select>
                    </div>
                  </div>
                </div>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading user profiles...</div>
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
                          <th style={{ padding: '14px 18px' }}>Account Status</th>
                          <th style={{ padding: '14px 18px', textAlign: 'right' }}>Actions & Approval</th>
                        </tr>
                      </thead>
                      <tbody>
                        {filteredUsers.map((u, idx) => (
                          <tr key={u.id} style={{ borderBottom: idx < filteredUsers.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                            <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{u.id}</td>
                            <td style={{ padding: '14px 18px', fontWeight: 600 }}>{u.name}</td>
                            <td style={{ padding: '14px 18px', color: '#64748b' }}>{u.email}</td>
                            <td style={{ padding: '14px 18px', color: '#64748b' }}>{u.phone || 'N/A'}</td>
                            <td style={{ padding: '14px 18px' }}>
                              <span className="badge-pill" style={{
                                background: u.role === 'ADMIN' ? '#f5f3ff' : u.role === 'OWNER' ? '#eff6ff' : '#f0fdf4',
                                color: u.role === 'ADMIN' ? '#7c3aed' : u.role === 'OWNER' ? '#2563eb' : '#16a34a'
                              }}>
                                {u.role}
                              </span>
                            </td>
                            <td style={{ padding: '14px 18px' }}>
                              <span className="badge-pill" style={{
                                background: u.status === 'ACTIVE' ? '#f0fdf4' : '#fef2f2',
                                color: u.status === 'ACTIVE' ? '#16a34a' : '#dc2626'
                              }}>
                                {u.status === 'ACTIVE' ? 'APPROVED' : 'BLOCKED'}
                              </span>
                            </td>
                            <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                              <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                                <button
                                  onClick={() => setSelectedUserModal(u)}
                                  style={{
                                    padding: '6px 12px',
                                    background: '#f1f5f9',
                                    color: '#334155',
                                    border: '1px solid #cbd5e1',
                                    borderRadius: 6,
                                    fontSize: 12,
                                    fontWeight: 600,
                                    cursor: 'pointer'
                                  }}
                                >
                                  👁️ Details
                                </button>
                                {u.role !== 'ADMIN' && (
                                  u.status === 'ACTIVE' ? (
                                    <button
                                      onClick={() => changeUserStatus(u, 'BLOCKED')}
                                      style={{
                                        padding: '6px 12px',
                                        background: '#fef2f2',
                                        color: '#dc2626',
                                        border: '1px solid #fca5a5',
                                        borderRadius: 6,
                                        fontSize: 12,
                                        fontWeight: 600,
                                        cursor: 'pointer'
                                      }}
                                    >
                                      🚫 Block
                                    </button>
                                  ) : (
                                    <button
                                      onClick={() => changeUserStatus(u, 'ACTIVE')}
                                      style={{
                                        padding: '6px 12px',
                                        background: '#f0fdf4',
                                        color: '#16a34a',
                                        border: '1px solid #86efac',
                                        borderRadius: 6,
                                        fontSize: 12,
                                        fontWeight: 600,
                                        cursor: 'pointer'
                                      }}
                                    >
                                      ✅ Approve
                                    </button>
                                  )
                                )}
                              </div>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {/* ─── TAB 3: FLEET & VEHICLE MANAGEMENT ─── */}
            {activeTab === 'vehicles' && (
              <div>
                <h2 className="page-heading">Registered Vehicles Fleet</h2>
                <p className="page-sub">Monitor all owner cars, daily rental prices, and availability</p>

                <div className="table-toolbar">
                  <div className="search-box">
                    <span>🔍</span>
                    <input
                      type="text"
                      placeholder="Search car model, registration, or city..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>

                  <div className="filter-group">
                    <label style={{ fontSize: 13, fontWeight: 600, color: '#64748b' }}>Availability:</label>
                    <select
                      className="filter-select"
                      value={statusFilter}
                      onChange={(e) => setStatusFilter(e.target.value)}
                    >
                      <option value="ALL">All Statuses</option>
                      <option value="AVAILABLE">AVAILABLE</option>
                      <option value="RENTED">RENTED</option>
                      <option value="MAINTENANCE">MAINTENANCE</option>
                    </select>
                  </div>
                </div>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading vehicle catalog...</div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>Car ID</th>
                          <th style={{ padding: '14px 18px' }}>Model & Brand</th>
                          <th style={{ padding: '14px 18px' }}>Registration</th>
                          <th style={{ padding: '14px 18px' }}>Fuel Type</th>
                          <th style={{ padding: '14px 18px' }}>Daily Rate</th>
                          <th style={{ padding: '14px 18px' }}>City</th>
                          <th style={{ padding: '14px 18px' }}>Status</th>
                          <th style={{ padding: '14px 18px', textAlign: 'right' }}>Update Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {filteredVehicles.map((v, idx) => {
                          const vs = vehicleStatusStyle(v.status);
                          return (
                            <tr key={v.id} style={{ borderBottom: idx < filteredVehicles.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '14px 18px', fontWeight: 600 }}>#{v.id}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 600 }}>{v.brandName ? `${v.brandName} ${v.modelName}` : v.modelName}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>{v.registrationNumber || 'N/A'}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>{v.fuelType || 'Petrol'}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#16a34a' }}>₹{v.pricePerDay}/day</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>{v.city || 'N/A'}</td>
                              <td style={{ padding: '14px 18px' }}>
                                <span className="badge-pill" style={{ background: vs.bg, color: vs.text }}>{v.status}</span>
                              </td>
                              <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                                <select
                                  className="status-dropdown"
                                  value={v.status}
                                  onChange={(e) => updateVehicleStatus(v.id, e.target.value)}
                                >
                                  <option value="AVAILABLE">AVAILABLE</option>
                                  <option value="RENTED">RENTED</option>
                                  <option value="MAINTENANCE">MAINTENANCE</option>
                                </select>
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

            {/* ─── TAB 4: SYSTEM BOOKINGS ─── */}
            {activeTab === 'bookings' && (
              <div>
                <h2 className="page-heading">System Reservations</h2>
                <p className="page-sub">Monitor and update customer rental reservations platform-wide</p>

                <div className="table-toolbar">
                  <div className="search-box">
                    <span>🔍</span>
                    <input
                      type="text"
                      placeholder="Search by Booking ID, Customer ID, or vehicle..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>

                  <div className="filter-group">
                    <label style={{ fontSize: 13, fontWeight: 600, color: '#64748b' }}>Booking Status:</label>
                    <select
                      className="filter-select"
                      value={statusFilter}
                      onChange={(e) => setStatusFilter(e.target.value)}
                    >
                      <option value="ALL">All Statuses</option>
                      <option value="Pending">Pending</option>
                      <option value="Confirmed">Confirmed</option>
                      <option value="Cancelled">Cancelled</option>
                      <option value="Completed">Completed</option>
                    </select>
                  </div>
                </div>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading reservations...</div>
                ) : (
                  <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: 14 }}>
                      <thead>
                        <tr style={{ background: '#f8fafc', borderBottom: '1px solid #e2e8f0', color: '#475569', fontSize: 12, fontWeight: 700, textTransform: 'uppercase' }}>
                          <th style={{ padding: '14px 18px' }}>Booking ID</th>
                          <th style={{ padding: '14px 18px' }}>Customer ID</th>
                          <th style={{ padding: '14px 18px' }}>Vehicle</th>
                          <th style={{ padding: '14px 18px' }}>Dates</th>
                          <th style={{ padding: '14px 18px' }}>Total Amount</th>
                          <th style={{ padding: '14px 18px' }}>Current Status</th>
                          <th style={{ padding: '14px 18px', textAlign: 'right' }}>Admin Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {filteredBookings.map((b, idx) => {
                          const ss = bookingStatusStyle(b.status);
                          return (
                            <tr key={b.bookingId} style={{ borderBottom: idx < filteredBookings.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{b.bookingId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{b.customerId}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 600 }}>{b.modelName || `Vehicle #${b.vehicleId}`}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b', fontSize: 13 }}>{b.pickupDate} → {b.returnDate}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#2563eb' }}>₹{b.totalAmount}</td>
                              <td style={{ padding: '14px 18px' }}>
                                <span className="badge-pill" style={{ background: ss.bg, color: ss.text }}>{b.status}</span>
                              </td>
                              <td style={{ padding: '14px 18px', textAlign: 'right' }}>
                                <select
                                  className="status-dropdown"
                                  value={b.status}
                                  onChange={(e) => updateBookingStatus(b.bookingId, e.target.value)}
                                >
                                  <option value="Pending">Pending</option>
                                  <option value="Confirmed">Confirmed</option>
                                  <option value="Cancelled">Cancelled</option>
                                  <option value="Completed">Completed</option>
                                </select>
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

            {/* ─── TAB 5: PAYMENTS LOG ─── */}
            {activeTab === 'payments' && (
              <div>
                <h2 className="page-heading">Payment Transactions Log</h2>
                <p className="page-sub">Audit overall system payment methods, receipts, and amounts</p>

                <div className="table-toolbar">
                  <div className="search-box">
                    <span>🔍</span>
                    <input
                      type="text"
                      placeholder="Search Payment ID or Booking ID..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                  </div>

                  <div className="filter-group">
                    <label style={{ fontSize: 13, fontWeight: 600, color: '#64748b' }}>Payment Status:</label>
                    <select
                      className="filter-select"
                      value={statusFilter}
                      onChange={(e) => setStatusFilter(e.target.value)}
                    >
                      <option value="ALL">All Statuses</option>
                      <option value="Paid">Paid</option>
                      <option value="Pending">Pending</option>
                      <option value="Failed">Failed</option>
                      <option value="Refunded">Refunded</option>
                    </select>
                  </div>
                </div>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading payment logs...</div>
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
                        {filteredPayments.map((p, idx) => {
                          const ss = paymentStatusStyle(p.paymentStatus);
                          return (
                            <tr key={p.paymentId} style={{ borderBottom: idx < filteredPayments.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{p.paymentId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{p.bookingId}</td>
                              <td style={{ padding: '14px 18px' }}>
                                <span style={{ fontWeight: 600, color: '#475569' }}>💳 {p.paymentMethod || 'Card'}</span>
                              </td>
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#16a34a' }}>₹{p.amt}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>
                                {p.paymentDate ? new Date(p.paymentDate).toLocaleDateString() : 'N/A'}
                              </td>
                              <td style={{ padding: '14px 18px' }}>
                                <span className="badge-pill" style={{ background: ss.bg, color: ss.text }}>{p.paymentStatus || 'Pending'}</span>
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

            {/* ─── TAB 6: REFUND REQUESTS ─── */}
            {activeTab === 'refunds' && (
              <div>
                <h2 className="page-heading">Customer Refund Requests</h2>
                <p className="page-sub">Track customer refund submissions and processing status</p>

                {loading ? (
                  <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading refund requests...</div>
                ) : refundsList.length === 0 ? (
                  <div className="coming-soon-card" style={{ background: '#fff' }}>
                    <div className="cs-icon">↩️</div>
                    <h3>No Refund Requests Found</h3>
                    <p>There are currently no customer refund requests in the database.</p>
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
                          const ss = refundStatusStyle(r.status);
                          return (
                            <tr key={r.refundId} style={{ borderBottom: idx < refundsList.length - 1 ? '1px solid #f1f5f9' : 'none' }}>
                              <td style={{ padding: '14px 18px', fontWeight: 600, color: '#1e293b' }}>#{r.refundId}</td>
                              <td style={{ padding: '14px 18px', color: '#64748b' }}>#{r.paymentId}</td>
                              <td style={{ padding: '14px 18px', fontWeight: 700, color: '#dc2626' }}>₹{r.refAmount}</td>
                              <td style={{ padding: '14px 18px', color: '#475569' }}>{r.reason || 'N/A'}</td>
                              <td style={{ padding: '14px 18px' }}>
                                <span className="badge-pill" style={{ background: ss.bg, color: ss.text }}>{r.status || 'Pending'}</span>
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

            {/* ─── TAB 7: MY PROFILE ─── */}
            {activeTab === 'profile' && <Profile />}
          </div>
        </div>
      </main>

      {/* ─── REGISTRATION DETAILS APPROVAL MODAL ─── */}
      {selectedUserModal && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(15, 23, 42, 0.65)',
          backdropFilter: 'blur(4px)',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          zIndex: 1000,
          padding: 20
        }}>
          <div style={{
            background: '#ffffff',
            borderRadius: 16,
            width: '100%',
            maxWidth: 600,
            boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
            overflow: 'hidden'
          }}>
            {/* Modal Header */}
            <div style={{
              background: 'linear-gradient(135deg, #1e293b 0%, #0f172a 100%)',
              color: '#ffffff',
              padding: '20px 24px',
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <div style={{
                  width: 44,
                  height: 44,
                  borderRadius: '50%',
                  background: selectedUserModal.role === 'OWNER' ? '#2563eb' : selectedUserModal.role === 'ADMIN' ? '#7c3aed' : '#16a34a',
                  color: '#fff',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontWeight: 700,
                  fontSize: 18
                }}>
                  {selectedUserModal.name?.charAt(0).toUpperCase()}
                </div>
                <div>
                  <h3 style={{ margin: 0, fontSize: 18, fontWeight: 700 }}>Registration Verification</h3>
                  <div style={{ fontSize: 13, color: '#94a3b8' }}>User ID: #{selectedUserModal.id}</div>
                </div>
              </div>
              <button
                onClick={() => setSelectedUserModal(null)}
                style={{
                  background: 'rgba(255,255,255,0.1)',
                  border: 'none',
                  color: '#fff',
                  width: 32,
                  height: 32,
                  borderRadius: '50%',
                  cursor: 'pointer',
                  fontSize: 16
                }}
              >
                ✕
              </button>
            </div>

            {/* Modal Body */}
            <div style={{ padding: 24 }}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 20 }}>
                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>Full Name</div>
                  <div style={{ fontSize: 15, fontWeight: 600, color: '#1e293b', marginTop: 4 }}>{selectedUserModal.name}</div>
                </div>

                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>Account Role</div>
                  <div style={{ marginTop: 4 }}>
                    <span className="badge-pill" style={{
                      background: selectedUserModal.role === 'ADMIN' ? '#f5f3ff' : selectedUserModal.role === 'OWNER' ? '#eff6ff' : '#f0fdf4',
                      color: selectedUserModal.role === 'ADMIN' ? '#7c3aed' : selectedUserModal.role === 'OWNER' ? '#2563eb' : '#16a34a'
                    }}>
                      {selectedUserModal.role}
                    </span>
                  </div>
                </div>

                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>Email Address</div>
                  <div style={{ fontSize: 14, fontWeight: 600, color: '#1e293b', marginTop: 4 }}>{selectedUserModal.email}</div>
                </div>

                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>Phone Number</div>
                  <div style={{ fontSize: 14, fontWeight: 600, color: '#1e293b', marginTop: 4 }}>{selectedUserModal.phone || 'Not Provided'}</div>
                </div>

                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>🪪 Aadhar Card Number</div>
                  <div style={{ fontSize: 14, fontWeight: 700, color: '#2563eb', marginTop: 4 }}>{selectedUserModal.adharCard || 'N/A'}</div>
                </div>

                <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0' }}>
                  <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>🚗 Driving License</div>
                  <div style={{ fontSize: 14, fontWeight: 700, color: selectedUserModal.drivingLicense ? '#16a34a' : '#94a3b8', marginTop: 4 }}>
                    {selectedUserModal.drivingLicense || (selectedUserModal.role === 'OWNER' ? 'Not Applicable (Owner)' : 'N/A')}
                  </div>
                </div>
              </div>

              <div style={{ background: '#f8fafc', padding: 14, borderRadius: 8, border: '1px solid #e2e8f0', marginBottom: 20 }}>
                <div style={{ fontSize: 11, fontWeight: 700, color: '#64748b', textTransform: 'uppercase' }}>🏠 Address Details</div>
                <div style={{ fontSize: 14, color: '#334155', marginTop: 4, lineHeight: 1.4 }}>{selectedUserModal.address || 'No address details provided.'}</div>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#f1f5f9', padding: '12px 16px', borderRadius: 8, marginBottom: 24 }}>
                <span style={{ fontSize: 13, color: '#475569', fontWeight: 600 }}>Current Registration Status:</span>
                <span className="badge-pill" style={{
                  background: selectedUserModal.status === 'ACTIVE' ? '#f0fdf4' : '#fef2f2',
                  color: selectedUserModal.status === 'ACTIVE' ? '#16a34a' : '#dc2626',
                  fontSize: 13,
                  padding: '6px 14px'
                }}>
                  {selectedUserModal.status === 'ACTIVE' ? '✅ APPROVED / ACTIVE' : '🚫 BLOCKED / REJECTED'}
                </span>
              </div>

              {/* Action Buttons */}
              <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end' }}>
                <button
                  onClick={() => setSelectedUserModal(null)}
                  style={{
                    padding: '10px 18px',
                    background: '#ffffff',
                    border: '1px solid #cbd5e1',
                    borderRadius: 8,
                    fontSize: 14,
                    fontWeight: 600,
                    color: '#475569',
                    cursor: 'pointer'
                  }}
                >
                  Close
                </button>
                {selectedUserModal.role !== 'ADMIN' && (
                  selectedUserModal.status === 'ACTIVE' ? (
                    <button
                      onClick={() => changeUserStatus(selectedUserModal, 'BLOCKED')}
                      style={{
                        padding: '10px 18px',
                        background: '#dc2626',
                        border: 'none',
                        borderRadius: 8,
                        fontSize: 14,
                        fontWeight: 600,
                        color: '#ffffff',
                        cursor: 'pointer'
                      }}
                    >
                      🚫 Block / Reject Access
                    </button>
                  ) : (
                    <button
                      onClick={() => changeUserStatus(selectedUserModal, 'ACTIVE')}
                      style={{
                        padding: '10px 18px',
                        background: '#16a34a',
                        border: 'none',
                        borderRadius: 8,
                        fontSize: 14,
                        fontWeight: 600,
                        color: '#ffffff',
                        cursor: 'pointer'
                      }}
                    >
                      ✅ Approve Registration
                    </button>
                  )
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
