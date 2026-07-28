import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

// Usage: <ProtectedRoute allowedRoles={['admin']}><AdminDashboard /></ProtectedRoute>
function ProtectedRoute({ children, allowedRoles }) {
  const { user, isAuthenticated } = useSelector((state) => state.auth);

  // 1. Not logged in -> go to login
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // 2. Logged in but role not allowed for this route -> go to home
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />;
  }

  // 3. All good -> render the page
  return children;
}

export default ProtectedRoute;
