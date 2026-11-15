# Service API Test Organization

This project contains two complementary test families for the service API:

- Acceptance tests, implemented with Cucumber
- Robustness tests, implemented with JUnit Jupiter

Both families run together through `RunServiceTests`. They remain separated under the `acceptance` and `robustness` packages so their different purposes stay explicit.

The rest of this document explains the organization of the Cucumber acceptance tests, specifically designed to overcome common Cucumber testing challenges while maintaining clean, maintainable code.

## Overview

Our testing framework is built around a feature-centric approach where each `.feature` file represents scenarios for a specific operation, with dedicated scenario classes and a robust support layer to minimize code duplication and simplify context management.

## Test Organization Structure

### Feature Files Organization
- Each `.feature` file groups scenarios related to a single operation
- Each scenario within a `.feature` file has its own unique encoding to make each step description unique, even when conceptually repeated across different scenarios
- This encoding approach allows gathering all steps of a scenario into a dedicated class

### Scenario Classes
Each scenario is implemented in its own dedicated class. This approach provides several key benefits:

1. **Simplified Context Management**: Scenario context becomes class attributes, eliminating the typical Cucumber problem known as **"Step Definition Sprawl"** or **"Scattered Step Definitions"**
2. **Clear Separation of Concerns**: Each scenario class focuses solely on its specific test case
3. **Enhanced Maintainability**: Changes to a scenario only affect its dedicated class

### Code Reuse Strategy

To avoid code repetition that would naturally arise from having dedicated classes for each scenario, we implement a comprehensive support layer consisting of:

#### 1. DbFixtures Class
Generates all possible initial data configurations for test scenarios.

#### 2. Db Class
Provides database insertion and querying capabilities without requiring detailed SQL knowledge (acts as a GenericDao).

#### 3. Record Builder Classes
Generate records for database tables with default values using builder pattern:
- Format: `<table_name>RecordBuilder`
- Provide fluent API for creating test data

#### 4. DTO Builder Classes
Generate DTOs for various operations:
- Insert operations (`<service>Add`)
- Expected values for query services
- Services that return DTOs

## Syntax Conventions

### Builder Pattern Conventions

Our builders follow consistent naming conventions:

- **`with`** prefix: Adjusts an attribute
  ```java
  builder.withName("John").withAge(30)
  ```

- **`for`** prefix: Establishes a foreign key relationship
  ```java
  builder.forClient(clientId).forVehicle(vehicleId)
  ```

- **`build`** method: Generates the final object
  ```java
  Entity entity = builder.build()
  ```

### DbFixtures Naming Conventions

The `DbFixtures` class uses descriptive method names following this pattern:

#### Basic Patterns:
- **`a<Entity>`**: Returns a single record of type `<Entity>`
- **`some<Entity>`**: Returns a list of records of type `<Entity>`

#### Relationship Indicators:
- **`With`**: Indicates the record will be connected to others, forming a graph
- **`For`**: Indicates an ID must be provided to establish a foreign key
- **`Of`** (terminal): Indicates functional arguments must be provided
- **`And`**: Extends a "With" or "For" clause

#### Examples:

```java
// Single entities with relationships
aClientWithVehicleAndVehicleType()
aVehicleWithClientAndClientType()
```
*Note: Both examples generate the same object graph but return different root entities*

```java
// Multiple entities with relationships
somePayrollsWithContractsForProfessionalGroup(pgId)
```

```java
// Parameterized entity creation
aContractTypeOf(compensationDays)
```
*The "Of" terminal indicates parameters follow*
