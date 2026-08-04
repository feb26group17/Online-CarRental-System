import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { crudApi } from '../../api/axios';
import { updateUser } from '../../redux/slices/authSlice';

function Profile() {
  const { user } = useSelector((state) => state.auth);
  const dispatch = useDispatch();

  const [profile, setProfile] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [msg, setMsg] = useState('');
  const [err, setErr] = useState('');

  const [form, setForm] = useState({
    name: '',
    phone: '',
    address: '',
    adharCard: '',
    drivingLicense: ''
  });

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    setLoading(true);
    setErr('');
    try {
      const res = await crudApi.get('/users/profile');
      const data = res.data || {};
      setProfile(data);
      setForm({
        name: data.name || '',
        phone: data.phone || '',
        address: data.address || '',
        adharCard: data.adharCard || '',
        drivingLicense: data.drivingLicense || ''
      });
    } catch (e) {
      console.error('Failed to load user profile', e);
      setErr(e.response?.data?.message || 'Failed to fetch user profile details');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setMsg('');
    setErr('');

    try {
      const res = await crudApi.put('/users/profile', form);
      const updated = res.data || {};
      setProfile(updated);
      setIsEditing(false);
      setMsg('Profile updated successfully!');
      
      // Update Redux state so headers refresh live
      dispatch(updateUser({ name: updated.name }));
      
      setTimeout(() => setMsg(''), 4000);
    } catch (error) {
      setErr(error.response?.data?.message || 'Failed to save profile changes');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div style={{ textAlign: 'center', padding: 40, color: '#64748b' }}>Loading profile information...</div>;
  }

  const roleColor = profile?.role === 'ADMIN' ? '#7c3aed' : profile?.role === 'OWNER' ? '#2563eb' : '#16a34a';
  const roleBg = profile?.role === 'ADMIN' ? '#f5f3ff' : profile?.role === 'OWNER' ? '#eff6ff' : '#f0fdf4';

  return (
    <div className="page-container" style={{ maxWidth: 800 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <div>
          <h2 className="page-heading">My Profile</h2>
          <p className="page-sub">View and manage your personal account details & credentials</p>
        </div>
        {!isEditing && (
          <button
            onClick={() => setIsEditing(true)}
            style={{
              padding: '10px 20px',
              background: '#2563eb',
              color: '#fff',
              border: 'none',
              borderRadius: 8,
              fontSize: 14,
              fontWeight: 600,
              cursor: 'pointer',
              boxShadow: '0 2px 4px rgba(37,99,235,0.2)'
            }}
          >
            ✏️ Edit Profile
          </button>
        )}
      </div>

      {msg && <div className="alert-success" style={{ marginBottom: 20 }}>{msg}</div>}
      {err && <div className="alert-error" style={{ marginBottom: 20 }}>{err}</div>}

      {/* Header Profile Card */}
      <div style={{
        background: '#fff', border: '1px solid #e2e8f0', borderRadius: 16, padding: 24,
        marginBottom: 24, boxShadow: '0 1px 3px rgba(0,0,0,0.05)', display: 'flex', gap: 20, alignItems: 'center'
      }}>
        <div style={{
          width: 72, height: 72, borderRadius: '50%', background: roleColor, color: '#fff',
          display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 28, fontWeight: 700
        }}>
          {profile?.name?.charAt(0).toUpperCase() || 'U'}
        </div>
        <div style={{ flex: 1 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10, flexWrap: 'wrap' }}>
            <h3 style={{ margin: 0, fontSize: 22, fontWeight: 700, color: '#1e293b' }}>{profile?.name}</h3>
            <span className="badge-pill" style={{ background: roleBg, color: roleColor, fontSize: 12, fontWeight: 700 }}>
              {profile?.role}
            </span>
            <span className="badge-pill" style={{
              background: profile?.status === 'ACTIVE' ? '#f0fdf4' : '#fef2f2',
              color: profile?.status === 'ACTIVE' ? '#16a34a' : '#dc2626',
              fontSize: 12, fontWeight: 700
            }}>
              {profile?.status === 'ACTIVE' ? '✅ ACTIVE & VERIFIED' : 'BLOCKED'}
            </span>
          </div>
          <div style={{ fontSize: 14, color: '#64748b', marginTop: 4 }}>{profile?.email}</div>
        </div>
      </div>

      {/* Profile Details & Form */}
      <div style={{ background: '#fff', border: '1px solid #e2e8f0', borderRadius: 16, padding: 28, boxShadow: '0 1px 3px rgba(0,0,0,0.05)' }}>
        <form onSubmit={handleSave}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20, marginBottom: 20 }}>
            
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>Full Name *</label>
              {isEditing ? (
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  required
                  style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #cbd5e1', borderRadius: 8, fontSize: 14 }}
                />
              ) : (
                <div style={{ padding: '10px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, fontWeight: 600, color: '#1e293b', border: '1px solid #e2e8f0' }}>
                  {profile?.name || 'N/A'}
                </div>
              )}
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>Email Address (Verified)</label>
              <div style={{ padding: '10px 14px', background: '#f1f5f9', borderRadius: 8, fontSize: 14, color: '#64748b', border: '1px solid #e2e8f0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span>{profile?.email}</span>
                <span style={{ fontSize: 11, fontWeight: 700, color: '#2563eb' }}>🔒 Locked</span>
              </div>
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>Phone Number</label>
              {isEditing ? (
                <input
                  type="text"
                  placeholder="e.g. +91 9876543210"
                  value={form.phone}
                  onChange={(e) => setForm({ ...form, phone: e.target.value })}
                  style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #cbd5e1', borderRadius: 8, fontSize: 14 }}
                />
              ) : (
                <div style={{ padding: '10px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, fontWeight: 600, color: '#1e293b', border: '1px solid #e2e8f0' }}>
                  {profile?.phone || 'Not Provided'}
                </div>
              )}
            </div>

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>🪪 Aadhar Card Number</label>
              {isEditing ? (
                <input
                  type="text"
                  placeholder="12-digit Aadhar Card No."
                  value={form.adharCard}
                  onChange={(e) => setForm({ ...form, adharCard: e.target.value })}
                  style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #cbd5e1', borderRadius: 8, fontSize: 14 }}
                />
              ) : (
                <div style={{ padding: '10px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, fontWeight: 600, color: '#2563eb', border: '1px solid #e2e8f0' }}>
                  {profile?.adharCard || 'Not Provided'}
                </div>
              )}
            </div>

            {profile?.role === 'CUSTOMER' && (
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>🚗 Driving License Number</label>
                {isEditing ? (
                  <input
                    type="text"
                    placeholder="Driving License No."
                    value={form.drivingLicense}
                    onChange={(e) => setForm({ ...form, drivingLicense: e.target.value })}
                    style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #cbd5e1', borderRadius: 8, fontSize: 14 }}
                  />
                ) : (
                  <div style={{ padding: '10px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, fontWeight: 600, color: '#16a34a', border: '1px solid #e2e8f0' }}>
                    {profile?.drivingLicense || 'Not Provided'}
                  </div>
                )}
              </div>
            )}

            <div className="form-group" style={{ marginBottom: 0 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>Registration Date</label>
              <div style={{ padding: '10px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, color: '#64748b', border: '1px solid #e2e8f0' }}>
                {profile?.createdAt ? new Date(profile.createdAt).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' }) : 'N/A'}
              </div>
            </div>

          </div>

          <div className="form-group">
            <label style={{ fontSize: 13, fontWeight: 700, color: '#475569', marginBottom: 6, display: 'block' }}>🏠 Residential / Business Address</label>
            {isEditing ? (
              <textarea
                rows={3}
                placeholder="Enter complete address..."
                value={form.address}
                onChange={(e) => setForm({ ...form, address: e.target.value })}
                style={{ width: '100%', padding: '10px 14px', border: '1.5px solid #cbd5e1', borderRadius: 8, fontSize: 14 }}
              />
            ) : (
              <div style={{ padding: '12px 14px', background: '#f8fafc', borderRadius: 8, fontSize: 14, color: '#334155', border: '1px solid #e2e8f0', lineHeight: 1.5 }}>
                {profile?.address || 'No address specified.'}
              </div>
            )}
          </div>

          {isEditing && (
            <div style={{ display: 'flex', gap: 12, justifyContent: 'flex-end', marginTop: 24, paddingTop: 16, borderTop: '1px solid #f1f5f9' }}>
              <button
                type="button"
                onClick={() => {
                  setIsEditing(false);
                  setForm({
                    name: profile?.name || '',
                    phone: profile?.phone || '',
                    address: profile?.address || '',
                    adharCard: profile?.adharCard || '',
                    drivingLicense: profile?.drivingLicense || ''
                  });
                }}
                style={{
                  padding: '10px 20px', background: '#f1f5f9', color: '#475569', border: 'none',
                  borderRadius: 8, fontSize: 14, fontWeight: 600, cursor: 'pointer'
                }}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="btn-primary"
                disabled={saving}
                style={{ width: 'auto', padding: '10px 24px', marginTop: 0 }}
              >
                {saving ? 'Saving Changes...' : '💾 Save Profile'}
              </button>
            </div>
          )}
        </form>
      </div>
    </div>
  );
}

export default Profile;
