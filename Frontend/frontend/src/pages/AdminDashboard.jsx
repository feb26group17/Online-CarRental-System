import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../redux/slices/authSlice';

function AdminDashboard() {
  const { user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const handleLogout = () => { dispatch(logout()); navigate('/'); };

  return (
    <div className="simple-dashboard">
      <h1>🛠️ Admin Dashboard</h1>
      <p>Welcome, {user?.name} 👋 — You are logged in as Admin</p>
      <button className="btn-danger" onClick={handleLogout}>🚪 Logout</button>
    </div>
  );
}

export default AdminDashboard;
