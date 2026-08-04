import { useState } from 'react';
import { bookingApi } from '../../api/axios';

// BookingRequest: { vehicleId, pickupDate, returnDate, dropCity }
// VehicleResponse: { vehicleId, userId, modelId, modelName, brandName,
//                    seatingCapacity, registrationNumber, fuelType, rentPerDay, status }

function CarDetail({ car, onBookSuccess }) {
  const [pickupDate, setPickupDate] = useState('');
  const [returnDate, setReturnDate] = useState('');
  const [dropCity, setDropCity] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  if (!car) {
    return (
      <div className="page-container">
        <h2 className="page-heading">Car Detail</h2>
        <div className="coming-soon-card" style={{ background: '#fff' }}>
          <div className="cs-icon">🚗</div>
          <h3>No Car Selected</h3>
          <p>Please browse available cars and click "Book Now" on a car to view details.</p>
        </div>
      </div>
    );
  }

  // Calculate rental duration in days (same-day rental counts as minimum 1 day)
  const calculateDays = () => {
    if (!pickupDate || !returnDate) return 0;
    const start = new Date(pickupDate);
    const end = new Date(returnDate);
    if (end < start) return 0; // invalid range
    const diffTime = end - start;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays > 0 ? diffDays : 1; // Same-day rental = 1 day minimum
  };

  const days = calculateDays();
  const totalAmount = days * (car.rentPerDay || 0);

  const handleBooking = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');

    if (!pickupDate || !returnDate) {
      return setError('Please select both Pickup Date and Return Date');
    }

    if (new Date(returnDate) < new Date(pickupDate)) {
      return setError('Return date cannot be before pickup date');
    }

    setLoading(true);
    try {
      // Send booking request to ocrs-booking-service on port 8083
      await bookingApi.post('/bookings', {
        vehicleId: car.vehicleId,
        pickupDate: pickupDate,
        returnDate: returnDate,
        dropCity: dropCity || null
      });

      setSuccessMsg('Booking created successfully! Redirecting to your bookings...');
      setTimeout(() => {
        if (onBookSuccess) onBookSuccess();
      }, 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to process booking request');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <h2 className="page-heading">{car.brandName} {car.modelName || car.registrationNumber}</h2>
      <p className="page-sub">View specifications and complete your reservation</p>

      {error && <div className="alert-error" style={{ marginBottom: 20 }}>{error}</div>}
      {successMsg && <div style={{
        padding: '12px 16px', background: '#f0fdf4', color: '#16a34a', border: '1px solid #bbf7d0',
        borderRadius: 8, fontSize: 14, fontWeight: 600, marginBottom: 20
      }}>{successMsg}</div>}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: 24 }}>
        
        {/* Car Details Card */}
        <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 14, padding: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <div style={{
            height: 220, background: '#f8fafc', borderRadius: 10, display: 'flex', alignItems: 'center',
            justifyContent: 'center', overflow: 'hidden', marginBottom: 20, border: '1px solid #e2e8f0'
          }}>
            <span style={{ fontSize: 72 }}>🚗</span>
          </div>

          <h3 style={{ fontSize: 20, fontWeight: 700, color: '#1e293b', marginBottom: 12 }}>
            {car.brandName} {car.modelName}
          </h3>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 18 }}>
            <div style={{ background: '#f8fafc', padding: '10px 14px', borderRadius: 8 }}>
              <div style={{ fontSize: 11, color: '#64748b' }}>Registration No.</div>
              <div style={{ fontSize: 13, fontWeight: 600, color: '#1e293b' }}>{car.registrationNumber || 'N/A'}</div>
            </div>
            <div style={{ background: '#f8fafc', padding: '10px 14px', borderRadius: 8 }}>
              <div style={{ fontSize: 11, color: '#64748b' }}>Fuel Type</div>
              <div style={{ fontSize: 13, fontWeight: 600, color: '#1e293b' }}>{car.fuelType || 'N/A'}</div>
            </div>
            <div style={{ background: '#f8fafc', padding: '10px 14px', borderRadius: 8 }}>
              <div style={{ fontSize: 11, color: '#64748b' }}>Seats</div>
              <div style={{ fontSize: 13, fontWeight: 600, color: '#1e293b' }}>{car.seatingCapacity || 5} Persons</div>
            </div>
            <div style={{ background: '#f8fafc', padding: '10px 14px', borderRadius: 8 }}>
              <div style={{ fontSize: 11, color: '#64748b' }}>Status</div>
              <div style={{ fontSize: 13, fontWeight: 600, color: car.status === 'Available' ? '#16a34a' : '#dc2626' }}>
                {car.status}
              </div>
            </div>
          </div>

          <div style={{ background: '#eff6ff', padding: '14px 16px', borderRadius: 10, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div>
              <div style={{ fontSize: 12, color: '#2563eb', fontWeight: 600 }}>Daily Rate</div>
              <div style={{ fontSize: 22, fontWeight: 800, color: '#1e293b' }}>₹{car.rentPerDay}</div>
            </div>
            <span style={{ fontSize: 12, fontWeight: 600, padding: '4px 12px', borderRadius: 20, background: '#dbeafe', color: '#1e40af' }}>
              Verified Listing
            </span>
          </div>
        </div>

        {/* Booking Form Card */}
        <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 14, padding: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
          <h3 style={{ fontSize: 18, fontWeight: 700, color: '#1e293b', marginBottom: 6 }}>Book This Vehicle</h3>
          <p style={{ fontSize: 13, color: '#64748b', marginBottom: 20 }}>Select your rental dates to proceed</p>

          {car.status !== 'Available' && (
            <div style={{ padding: '12px 16px', background: '#fef2f2', color: '#dc2626', border: '1px solid #fca5a5', borderRadius: 8, marginBottom: 16, fontSize: 13, fontWeight: 600 }}>
              This vehicle is currently {car.status} and cannot be booked.
            </div>
          )}

          <form onSubmit={handleBooking}>
            <div className="form-group">
              <label>Pickup Date *</label>
              <input type="date" value={pickupDate} min={new Date().toISOString().split('T')[0]}
                onChange={e => setPickupDate(e.target.value)} required disabled={car.status !== 'Available'} />
            </div>

            <div className="form-group">
              <label>Return Date *</label>
              <input type="date" value={returnDate} min={pickupDate || new Date().toISOString().split('T')[0]}
                onChange={e => setReturnDate(e.target.value)} required disabled={car.status !== 'Available'} />
            </div>

            <div className="form-group">
              <label>Drop City (Optional)</label>
              <input type="text" placeholder="e.g. Pune" value={dropCity}
                onChange={e => setDropCity(e.target.value)} disabled={car.status !== 'Available'} />
            </div>

            <div style={{ background: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: 10, padding: '16px', margin: '20px 0' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 13, color: '#64748b', marginBottom: 8 }}>
                <span>Rental Duration</span>
                <span style={{ fontWeight: 600, color: '#1e293b' }}>{days} Day{days !== 1 ? 's' : ''}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 13, color: '#64748b', marginBottom: 8 }}>
                <span>Price per Day</span>
                <span style={{ fontWeight: 600, color: '#1e293b' }}>₹{car.rentPerDay}</span>
              </div>
              <hr style={{ border: 'none', borderTop: '1px dashed #cbd5e1', margin: '10px 0' }} />
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 16, fontWeight: 700, color: '#1e293b' }}>
                <span>Total Amount</span>
                <span style={{ color: '#2563eb', fontSize: 20 }}>₹{totalAmount}</span>
              </div>
            </div>

            <button type="submit" className="btn-primary" disabled={loading || days <= 0 || car.status !== 'Available'}>
              {loading ? 'Submitting Reservation...' : 'Confirm & Reserve'}
            </button>
          </form>
        </div>

      </div>
    </div>
  );
}

export default CarDetail;
