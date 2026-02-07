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

### Using Docker (Recommended)

1. Start SQL Server with Docker:
   ```bash
   docker-compose up -d
   ```

2. Create the database (see [DOCKER.md](DOCKER.md) for details)

3. Build and run the application:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.grainindustries.invoice.Main"
   ```

### Manual Setup

1. Install SQL Server locally
2. Configure your database connection in `src/main/resources/database.properties`
3. Create the database schema
4. Build the project with Maven: `mvn clean install`
5. Run the application

## Development Progress

Track the development progress through Git commits as features are implemented.
