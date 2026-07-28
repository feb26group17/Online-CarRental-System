import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, Link } from 'react-router-dom';
import API from '../api/axios';
import { loginSuccess } from '../redux/slices/authSlice';
import { normalizeAuthResponse } from '../utils/normalizeAuthResponse';

// Single login for every role — no selector. The backend looks the
// account up by email in the `users` table and tells us the role; we
// just redirect to whichever dashboard matches it.
function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await API.post('/auth/login', formData);
      const { user, token } = normalizeAuthResponse(res.data);
      dispatch(loginSuccess({ user, token }));
      if (user.role === 'customer')    navigate('/customer/dashboard');
      else if (user.role === 'owner')  navigate('/owner/dashboard');
      else if (user.role === 'admin')  navigate('/admin/dashboard');
      else                             navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-logo">🚗 Car<span>Rental</span></div>
        <p className="auth-subtitle">Sign in to your account</p>

        {error && <div className="alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} required autoFocus />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Signing in...' : 'Login'}
          </button>
        </form>

        <div className="auth-links">
          <span>New customer? <Link to="/register">Register here</Link></span>
          <span>Want to list your car? <Link to="/register/owner">Register as Car Owner</Link></span>
        </div>

        <div className="auth-hint">
          <strong>Seeded admin login</strong>
          <div>admin@carrental.com — Admin@123</div>
        </div>
      </div>
    </div>
  );
}

export default Login;
