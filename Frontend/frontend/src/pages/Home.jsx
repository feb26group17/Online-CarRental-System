import { Link } from 'react-router-dom';

function Home() {
  return (
    <div className="home-page">
      <div className="home-icon">🚗</div>
      <h1 className="home-title">Car<span>Rental</span></h1>
      <p className="home-desc">
        Rent a car easily and affordably. Choose from a wide range of vehicles,
        book instantly, and hit the road.
      </p>
      <div className="home-buttons">
        <Link to="/login" className="btn-white">Login</Link>
        <Link to="/register" className="btn-outline-white">Register as Customer</Link>
        <Link to="/register/owner" className="btn-outline-white">Register as Car Owner</Link>
      </div>
      <div className="home-features">
        <div className="feature-card">
          <div className="icon">🔑</div>
          <h4>Easy Booking</h4>
          <p>Book your car in minutes</p>
        </div>
        <div className="feature-card">
          <div className="icon">🛡️</div>
          <h4>Verified Owners</h4>
          <p>All car owners are admin verified</p>
        </div>
        <div className="feature-card">
          <div className="icon">💳</div>
          <h4>Secure Payments</h4>
          <p>Safe and hassle-free payments</p>
        </div>
      </div>
    </div>
  );
}

export default Home;
