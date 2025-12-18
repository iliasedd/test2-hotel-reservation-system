## Test 2 - Hotel Reservation System

the required screenshot for printAll() and printAllUsers() can be found in the screenshot folder

#### Design Questions (Bonus)

1/- A single service is acceptable for this test app because we have fewer files, everything is visible in one place. It also matches the test constraints. But it’s not the recommended approach for production or any non-trivial extension for the following reasons :

- HotelService does user management, room management, booking rules, availability checks, printing, and validation. Each of those is a separate concern. Splitting responsibilities makes the code easier to understand, test and extend.
- Smaller focused services are easier to unit-test in isolation.
- Adding features becomes harder when logic is tangled, which is not good for maintainability.
