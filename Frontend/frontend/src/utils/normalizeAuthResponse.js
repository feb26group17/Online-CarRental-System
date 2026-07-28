// user-service (Spring Boot) returns:
//   { token, id, userId, fullName, email, role: "CUSTOMER" | "OWNER" | "ADMIN", message }
//   - id     = role-scoped id (customer_id / owner_id, or users.id for admin)
//   - userId = the master users.id, always present regardless of role
//
// The rest of the app (authSlice, ProtectedRoute, dashboards) expects:
//   { user: { id, userId, name, email, role: "customer" | "owner" | "admin" }, token }
//
// This is the one place that translation happens, so if the backend
// response shape changes again, only this file needs to change.
export function normalizeAuthResponse(data) {
  return {
    token: data.token || null,
    user: {
      id: data.id,
      userId: data.userId,
      name: data.fullName,
      email: data.email,
      role: data.role ? data.role.toLowerCase() : null
    }
  };
}
