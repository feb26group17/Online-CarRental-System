# user-service

First microservice of the Car Rental system. Owns identity + authentication
for all three roles: **Customer**, **Car Owner**, **Admin**. Built with
Spring Boot 3.3.4 / Java 17 / Spring Security / JWT / Spring Data JPA.

## Design (redesigned around the new schema)

- **`users` is now the single master identity/auth table** — `id`, `name`,
  `email`, `password`, `role` (`customer`/`owner`/`admin`), `status`
  (`active`/`pending_admin`/`blocked`). There is **no separate `admin`
  table anymore** — the seeded admin is just a `users` row with
  `role='admin'`.
- **`customer` and `car_owner` are now thin profile-extension tables**,
  each linked back to `users` via a unique `user_id` FK. They still hold
  the role-specific fields (`driving_license`, `address`, the `car_owner`
  approval `status`, etc.) and each has its own auto-increment id
  (`customer_id` / `owner_id`) — these are what booking/vehicle/payment
  already key on, so nothing downstream breaks.
- **One login endpoint, no role selector.** `POST /api/auth/login` takes
  only `email` + `password`. The backend looks the user up in `users`,
  checks the password and `status`, and reads the `role` straight off that
  row — the frontend never has to say which kind of account it's logging
  into. The `role` in the response is what the frontend uses to redirect
  to the right dashboard.
- **Account gating happens at the `users.status` level first**:
  - `blocked` → 403, "account blocked"
  - `pending_admin` → 403, "pending admin approval" (new car owners land
    here until an admin approves them)
  - `active` → login proceeds
  - `car_owner.status` (`Pending`/`Approved`/`Rejected`) is kept in sync
    with `users.status` and double-checked defensively in case they drift.
- `hibernate.ddl-auto=validate` — this service does **not** create/alter
  your schema. Run `db/00_schema.sql` yourself first (see Setup below),
  then Hibernate just validates the entity mappings match.

## Setup

1. Create the database and run:
   ```
   src/main/resources/db/00_schema.sql
   ```
   This single file replaces the old per-table dumps and the old
   `01_admin_table.sql` / `02_admin_seed.sql` — it creates `users`,
   `customer`, `car_owner`, `vehicle`, `vehicle_availability`, `booking`,
   `payment`, `refund`, and seeds the one admin account.

2. Set env vars (or edit `application.yml` directly):
   ```
   DB_USER=root
   DB_PASSWORD=your_mysql_password
   JWT_SECRET=<32+ char random string>
   ```

3. Run:
   ```
   mvn spring-boot:run
   ```
   Service starts on **http://localhost:8081**.

## Endpoints

| Method | Path | Body | Notes |
|---|---|---|---|
| POST | `/api/auth/register/customer` | `fullName, email, phone, address, drivingLicense, password` | Creates a `users` row (role=customer, status=active) + a linked `customer` row. Active immediately, returns JWT |
| POST | `/api/auth/register/owner` | `fullName, email, phone, address, drivingLicense, password` | Creates a `users` row (role=owner, status=pending_admin) + a linked `car_owner` row (status=Pending). **No JWT returned** — needs admin approval first |
| POST | `/api/auth/login` | `email, password` | **Single endpoint for all three roles** — no role field. Backend determines the role from `users.role` |

### Login response

```json
{
  "token": "eyJhbGciOi...",
  "id": 1,
  "userId": 7,
  "fullName": "ashish singh",
  "email": "ashish@gmail.com",
  "role": "CUSTOMER",
  "message": "Login successful"
}
```

- `id` is the role-scoped id (`customer_id` / `owner_id`, or `users.id` for
  admin) — what downstream services already key on.
- `userId` is always the master `users.id`.

JWT payload contains `sub` (email), `id`, `userId`, `role` — downstream
services (booking, vehicle, payment) can verify the token and read
`role`/`id` without calling back into user-service.

## Flagged for later (not blocking this service)

- **`refund.refund_status` enum has a typo**: `'Rejected,Completed'` is one
  malformed value instead of two (`'Rejected'`, `'Completed'`). Worth
  fixing in the DB before the payment/refund service is built.
- **`vehicle.model` stores a year** (e.g. `'2023'`) rather than a model
  name — `vehicle_name` seems to carry the actual model. Confirm intent
  when we get to vehicle-service.
- **No reviews table yet** — `MyReviews.jsx` on the frontend is still a
  placeholder; will need a table + service later.

## Next microservice

Ready to move on to **vehicle-service** (owns `vehicle` +
`vehicle_availability`) or **booking-service** whenever you are — just say
the word.
