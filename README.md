# Vehicle Rental Management System

A Java-based desktop application for managing vehicle rentals.  
The system supports manager and customer login, vehicle browsing, renting and returning vehicles, rental reminders, persistent storage, vehicle-specific pricing, and automated tests.

## Features

### Customer Features

- Customer login and logout
- View available vehicles
- View vehicle brand, model, type, and daily rate
- Rent a vehicle using a vehicle selection list
- Select rental dates using date controls instead of text input
- View personal rental history
- Return active rentals
- Calculate total rental cost
- Apply a late-return penalty
- Receive rental expiry reminders

### Manager Features

- Manager login and logout
- View available vehicles

## Vehicle Types

The system supports the following vehicle hierarchy:

- `Car`
- `SUV`
- `Truck`

Each vehicle type uses a different pricing strategy:

| Vehicle Type | Pricing Rule |
|---|---|
| Car | Normal daily rate |
| SUV | Normal cost + 10% |
| Truck | Normal cost + 20% |

## Late Return Penalty

A fixed penalty is added for every late day:

```text
Late penalty = Late days × 10.0
```

The total rental cost is calculated as:

```text
Total cost = Vehicle pricing strategy cost + Late penalty
```

## Design Patterns

### Strategy Pattern

The Strategy Pattern is used to calculate rental prices.

Main classes:

- `PricingStrategy`
- `CarPricingStrategy`
- `SuvPricingStrategy`
- `TruckPricingStrategy`

Each vehicle contains a pricing strategy and delegates rental-cost calculation to it.

### Observer Pattern

The Observer Pattern is used for rental expiry reminders.

Main classes:

- `NotificationObserver`
- `EmailNotificationObserver`
- `RentalReminderService`
- `NotificationService`
- `EmailNotificationService`

`RentalReminderService` notifies all registered observers when an active rental expires within two days.

## Project Structure

```text
src
├── main
│   └── java
│       └── najah
│           └── stu
│               ├── app
│               ├── domain
│               ├── exception
│               ├── notification
│               ├── observer
│               ├── repository
│               ├── service
│               ├── strategy
│               └── ui
└── test
    └── java
        └── najah
            └── stu
```

## Main Packages

| Package | Responsibility |
|---|---|
| `app` | Application entry point |
| `domain` | Core entities and enums |
| `repository` | File persistence and data access |
| `service` | Business logic |
| `strategy` | Vehicle pricing strategies |
| `observer` | Reminder observers |
| `notification` | Notification implementations |
| `exception` | Custom exceptions |
| `ui` | Swing user interface |

## Data Persistence

The application stores data in text files inside the `data` directory.

```text
data/
├── customers.txt
├── managers.txt
├── rentals.txt
└── vehicles.txt
```

### Vehicle File Format

```text
id,brand,model,available,dailyRate,type
```

Example:

```text
1,Toyota,Corolla,true,50.0,CAR
2,Toyota,RAV4,true,70.0,SUV
3,Volvo,FMX,true,100.0,TRUCK
```

The repository also supports the old five-field vehicle format. Vehicles without a stored type are treated as `CAR`.

### Rental File Format

```text
id,vehicleId,customerName,startDate,endDate,returnDate,status
```

Example active rental:

```text
1,1,Masoud,2026-07-20,2026-07-25,null,ACTIVE
```

Example returned rental:

```text
1,1,Masoud,2026-07-20,2026-07-25,2026-07-27,RETURNED
```

The repository also supports the old rental format without `returnDate`.

## Technologies

- Java
- Java Swing
- Maven
- JUnit 5
- Mockito
- JaCoCo
- PlantUML

## Requirements

- Java Development Kit
- Apache Maven
- VS Code, IntelliJ IDEA, Eclipse, or another Java IDE

## Running the Application

Run the following class from the IDE:

```text
najah.stu.app.App
```

Or compile the project first:

```bash
mvn clean package
```

Then run:

```bash
java -cp target/classes najah.stu.app.App
```

Login accounts are loaded from:

```text
data/customers.txt
data/managers.txt
```

## Running Tests

Run all automated tests:

```bash
mvn test
```

Current test result:

```text
Tests run: 116
Failures: 0
Errors: 0
Skipped: 0
```

## Code Coverage

Generate the JaCoCo report:

```bash
mvn clean test
```

Open:

```text
target/site/jacoco/index.html
```

The latest reported total instruction coverage is approximately:

```text
81%
```

The `ui` and `app` packages are excluded from JaCoCo coverage.

## Generate Javadocs

Run:

```bash
mvn javadoc:javadoc
```

Then open:

```text
target/site/apidocs/index.html
```

## UML Diagram

The project includes a PlantUML diagram:

```text
vehicle-rental-system-uml.puml
```

In VS Code, install the PlantUML extension, open the file, and use:

```text
Alt + D
```

## Validation Rules

- Customer must be logged in before renting or returning a vehicle
- Customer name must not be empty
- Start and end dates are required
- Start date cannot be in the past
- End date must be after the start date
- Rental duration must be between 1 and 30 days
- A vehicle cannot have more than one active rental
- A returned rental cannot be returned again
- Daily rate cannot be negative

## Custom Exceptions

- `InvalidRentalPeriodException`
- `VehicleNotAvailableException`
- `VehicleNotFoundException`

## Testing

The project includes tests for:

- Domain classes
- Repositories
- Services
- Exceptions
- Notifications
- Observer Pattern
- Strategy Pattern
- Vehicle availability
- Rental validation
- Renting and returning vehicles
- Cost and late-penalty calculation
- File persistence

## Author

Developed as a Vehicle Rental Management System course project.
