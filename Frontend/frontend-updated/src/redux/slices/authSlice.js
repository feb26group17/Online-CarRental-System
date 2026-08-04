import { createSlice } from '@reduxjs/toolkit';

// Initial state - no user logged in
const initialState = {
  user: null,      // { id, userId, name, email, role }
  token: null,
  isAuthenticated: false
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    // Called after successful login
    loginSuccess: (state, action) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.isAuthenticated = true;
    },
    // Called on logout
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
    },
    // Called when profile information is updated
    updateUser: (state, action) => {
      if (state.user) {
        state.user = { ...state.user, ...action.payload };
      }
    }
  }
});

export const { loginSuccess, logout, updateUser } = authSlice.actions;
export default authSlice.reducer;
