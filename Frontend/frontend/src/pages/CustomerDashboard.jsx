import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../redux/slices/authSlice';
import BrowseCars from './customer/BrowseCars';
import CarDetail  from './customer/CarDetail';
import MyBookings from './customer/MyBookings';
import Payments   from './customer/Payments';
import MyReviews  from './customer/MyReviews';

const menuItems = [
  { key: 'browse',   label: 'Browse Cars',  icon: '🚗', sub: 'Search + filter'   },
  { key: 'detail',   label: 'Car Detail',   icon: '📋', sub: 'Photos + specs'    },
  { key: 'bookings', label: 'My Bookings',  icon: '📅', sub: 'Active + history'  },
  { key: 'payments', label: 'Payments',     icon: '💳', sub: 'History + refunds' },
  { key: 'reviews',  label: 'My Reviews',   icon: '⭐', sub: 'Submit feedback'   },
];

function CustomerDashboard() {
  const { user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [active, setActive] = useState('browse');

  const handleLogout = () => { dispatch(logout()); navigate('/'); };

  const renderContent = () => {
    switch (active) {
      case 'browse':   return <BrowseCars />;
      case 'detail':   return <CarDetail />;
      case 'bookings': return <MyBookings />;
      case 'payments': return <Payments />;
      case 'reviews':  return <MyReviews />;
      default:         return <BrowseCars />;
    }
  };

  return (
    <div className="dashboard-wrapper">
      <aside className="sidebar">
        <div className="sidebar-brand">🚗 CarRental</div>
        <div className="sidebar-user">
          <div className="sidebar-avatar">{user?.name?.charAt(0).toUpperCase()}</div>
          <div>
            <div className="sidebar-username">{user?.name}</div>
            <div className="sidebar-role">Customer</div>
          </div>
        </div>
        <nav className="sidebar-nav">
          {menuItems.map((item) => (
            <button
              key={item.key}
              className={`nav-btn ${active === item.key ? 'active' : ''}`}
              onClick={() => setActive(item.key)}
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

      <main className="dashboard-main">
        <header className="dashboard-topbar">
          <h1>{menuItems.find((m) => m.key === active)?.label}</h1>
          <span className="topbar-greeting">Hi, {user?.name} 👋</span>
        </header>
        <div className="dashboard-content">
          {renderContent()}
        </div>
      </main>
    </div>
  );
}

export default CustomerDashboard;
