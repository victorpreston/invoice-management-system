# Invoice Management System

A desktop application for invoice management with SQL Server database integration.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/grainindustries/invoice/
│   │       ├── model/       # Entity classes (Customer, Invoice, Item, etc.)
│   │       ├── dao/         # Data Access Objects
│   │       ├── service/     # Business logic layer
│   │       ├── ui/          # Swing UI components
│   │       └── util/        # Utility classes (DatabaseConnection, etc.)
│   └── resources/
│       ├── database.properties    # Database configuration
│       └── application.properties # Application settings
└── test/
    └── java/                # Unit tests
```

## Technologies

- Java 17
- SQL Server
- Swing with FlatLaf Look and Feel
- Maven for dependency management

## Getting Started

1. Configure your SQL Server database connection in `src/main/resources/database.properties`
2. Create the database schema in SQL Server
3. Build the project with Maven: `mvn clean install`
4. Run the application

## Development Progress

Track the development progress through Git commits as features are implemented.
