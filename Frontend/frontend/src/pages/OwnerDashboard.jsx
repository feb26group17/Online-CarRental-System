import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../redux/slices/authSlice';

import Overview     from './owner/Overview';
import MyVehicles   from './owner/MyVehicles';
import Requests     from './owner/Requests';
import ActiveRentals from './owner/ActiveRentals';
import Earnings     from './owner/Earnings';

const menuItems = [
  { key: 'overview',  label: 'Overview',       icon: '📊', sub: 'Stats + earnings'   },
  { key: 'vehicles',  label: 'My Vehicles',    icon: '🚗', sub: 'Manage listings'    },
  { key: 'requests',  label: 'Requests',       icon: '📋', sub: 'Approve / reject'   },
  { key: 'rentals',   label: 'Active Rentals', icon: '🔑', sub: 'Ongoing trips'      },
  { key: 'earnings',  label: 'Earnings',       icon: '💰', sub: 'Revenue report'     },
];

function OwnerDashboard() {
  const { user } = useSelector((state) => state.auth);
  const dispatch  = useDispatch();
  const navigate  = useNavigate();
  const [active, setActive] = useState('overview');

  const handleLogout = () => { dispatch(logout()); navigate('/'); };

  // Allow Overview's quick-action buttons to navigate sidebar
  const handleNavigate = (key) => setActive(key);

  const renderContent = () => {
    switch (active) {
      case 'overview': return <Overview onNavigate={handleNavigate} />;
      case 'vehicles': return <MyVehicles />;
      case 'requests': return <Requests />;
      case 'rentals':  return <ActiveRentals />;
      case 'earnings': return <Earnings />;
      default:         return <Overview onNavigate={handleNavigate} />;
    }
  };

  return (
    <div className="dashboard-wrapper">

      {/* ── Sidebar ── */}
      <aside className="sidebar">
        <div className="sidebar-brand">🚗 CarRental</div>

        <div className="sidebar-user">
          <div className="sidebar-avatar">{user?.name?.charAt(0).toUpperCase()}</div>
          <div>
            <div className="sidebar-username">{user?.name}</div>
            <div className="sidebar-role">Car Owner</div>
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

      {/* ── Main content ── */}
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

export default OwnerDashboard;
