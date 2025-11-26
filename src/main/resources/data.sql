-- Enable UUID extension (required for gen_random_uuid())
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Roles are stored as strings: 'ADMIN', 'EMPLOYEE', 'MEMBER'

-- 1. Location
INSERT INTO locations (id, name, address)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'Andrea360 Belgrade', 'Bulevar Kralja Aleksandra 123, Belgrade');

-- 2. Admin user (no location)
INSERT INTO users (id, first_name, last_name, email, password, role, location_id)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Admin', 'User', 'admin@andrea360.com', '$2a$12$jThw9c5IzbOZr9kYw739E.RiEjutxIBuCTrwiyASo9R4d8GEgK5Fe', 'ADMIN', NULL);

-- 3. Employee (linked to location)
INSERT INTO users (id, first_name, last_name, email, password, role, location_id)
VALUES
  ('22222222-2222-2222-2222-222222222222', 'Jelena', 'Petrović', 'jelena@andrea360.com', '$2a$12$jThw9c5IzbOZr9kYw739E.RiEjutxIBuCTrwiyASo9R4d8GEgK5Fe', 'EMPLOYEE', '550e8400-e29b-41d4-a716-446655440000');

-- 4. Member (linked to same location)
INSERT INTO users (id, first_name, last_name, email, password, role, location_id)
VALUES
  ('33333333-3333-3333-3333-333333333333', 'Marko', 'Marković', 'marko@andrea360.com', '$2a$12$jThw9c5IzbOZr9kYw739E.RiEjutxIBuCTrwiyASo9R4d8GEgK5Fe', 'MEMBER', '550e8400-e29b-41d4-a716-446655440000');

-- 5. Training Services
INSERT INTO services (id, name, price)
VALUES
  ('44444444-4444-4444-4444-444444444444', 'Yoga Flow', 15.00),
  ('55555555-5555-5555-5555-555555555555', 'CrossFit Blast', 20.00);

-- 6. Appointments
-- Future appointment with capacity
INSERT INTO appointments (id, start_time, end_time, max_capacity, location_id, service_id)
VALUES
  ('66666666-6666-6666-6666-666666666666', '2025-12-10 18:00:00', '2025-12-10 19:00:00', 5, '550e8400-e29b-41d4-a716-446655440000', '44444444-4444-4444-4444-444444444444');

-- Full appointment (5 reservations for max 5)
INSERT INTO appointments (id, start_time, end_time, max_capacity, location_id, service_id)
VALUES
  ('77777777-7777-7777-7777-777777777777', '2025-12-11 19:00:00', '2025-12-11 20:00:00', 5, '550e8400-e29b-41d4-a716-446655440000', '55555555-5555-5555-5555-555555555555');

-- 7. Purchase (successful, for member)
INSERT INTO purchases (id, member_id, service_id, amount, stripe_payment_intent_id, purchased_at, status, uses_left)
VALUES
  ('88888888-8888-8888-8888-888888888888', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444444', 15.00, 'pi_test_123456789', '2025-11-25 10:00:00', 'SUCCEEDED', 1);

-- 8. Reservation (uses the purchase, for the full appointment)
INSERT INTO reservations (id, member_id, appointment_id, purchase_id, reserved_at)
VALUES
  ('99999999-9999-9999-9999-999999999999', '33333333-3333-3333-3333-333333333333', '77777777-7777-7777-7777-777777777777', '88888888-8888-8888-8888-888888888888', '2025-11-25 10:05:00');

-- Additional data for Marko Marković

-- A new, available purchase for Marko (CrossFit Blast) - This will show up in "My Available Purchases"
INSERT INTO purchases (id, member_id, service_id, amount, stripe_payment_intent_id, purchased_at, status, uses_left)
VALUES
  ('10101010-1010-1010-1010-101010101010', '33333333-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555555', 20.00, 'pi_test_987654321', '2025-11-26 11:00:00', 'SUCCEEDED', 1);

-- A second purchase for Yoga Flow, which will be used for the reservation below
INSERT INTO purchases (id, member_id, service_id, amount, stripe_payment_intent_id, purchased_at, status, uses_left)
VALUES
  ('13131313-1313-1313-1313-131313131313', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444444', 15.00, 'pi_test_112233445', '2025-11-26 11:04:00', 'SUCCEEDED', 1);

-- A new reservation for Marko for the upcoming Yoga Flow - This will show up in "My Reservations"
INSERT INTO reservations (id, member_id, appointment_id, purchase_id, reserved_at)
VALUES
  ('12121212-1212-1212-1212-121212121212', '33333333-3333-3333-3333-333333333333', '66666666-6666-6666-6666-666666666666', '13131313-1313-1313-1313-131313131313', '2025-11-26 11:05:00');
