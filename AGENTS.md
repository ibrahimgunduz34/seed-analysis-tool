# AGENTS.md

This file contains guidelines and commands for agentic coding agents working in this repository.

## Project Overview

This is a Spring Boot Java application for analyzing TEFAS fund performance. It's a financial analysis tool that calculates various metrics like Sharpe ratio, Sortino ratio, MDD, and other statistical measures for investment funds.

## Build/Test Commands

### Maven Commands
- **Build**: `mvn clean compile`
- **Package**: `mvn clean package`
- **Run application**: `mvn spring-boot:run`
- **Run tests**: `mvn test`
- **Run single test**: `mvn test -Dtest=ClassName` (e.g., `mvn test -Dtest=MeanTest`)
- **Skip tests**: Add `-DskipTests=true` to any Maven command

### Docker Commands
- **Build image**: `./scripts/build.sh` or `docker build -f Dockerfile -t seed-analysis:latest .`
- **Run with dependencies**: `docker compose up -d`

### Application Tasks
The application supports various tasks via JVM arguments:
- **MetaDataListSync**: `-Dtask=MetaDataListSync`
- **HistoricalDataListSync**: `-Dtask=HistoricalDataListSync`
- **PeriodComparisonReport**: `-Dtask=PeriodComparisonReport`
- **PeriodFundTypeComparisonReport**: `-Dtask=PeriodFundTypeComparisonReport`

Example: `mvn spring-boot:run -DskipTests=true -Dspring-boot.run.profiles=prod -Dspring-boot.run.jvmArguments="-Dtask=PeriodComparisonReport" -Dspring-boot.run.arguments="BDS,AFT,MAC"`

## Code Style Guidelines

### Package Structure
- Base package: `com.seed`
- Core functionality: `com.seed.core.*`
- Fund-specific: `com.seed.fund.*`
- Web layer: `com.seed.fund.web.*`
- Configuration: `com.seed.configuration.*`

### Import Organization
1. Java standard library imports
2. Third-party library imports (Spring, JUnit, etc.)
3. Project imports (com.seed.*)
4. Static imports (at the bottom)

Use wildcard imports sparingly, only for frequently used classes like `java.util.List` or `java.util.Map`.

### Naming Conventions
- **Classes**: PascalCase (e.g., `FundController`, `SharpeRatio`)
- **Methods**: camelCase (e.g., `calculate`, `analyzeFund`)
- **Variables**: camelCase (e.g., `metaData`, `analysisContext`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `SHARPE_RATIO`, `DEFAULT_DATE`)
- **Packages**: lowercase with dots (e.g., `com.seed.core.calculator`)

### Type Safety
- Use generics properly: `Calculator<H extends HistoricalData>`
- Prefer `Optional<T>` over null returns
- Use `@Nullable` annotations where appropriate
- Declare method return types explicitly

### Error Handling
- Use custom exceptions from `com.seed.core.exception.*`
- Return `ResponseEntity<?>` for REST endpoints with proper HTTP status codes
- Use `ErrorModel` and `ErrorCategory` for API error responses
- Validate inputs and throw meaningful exceptions

### Testing Guidelines
- Use JUnit 5 with `@Test`, `@BeforeEach`, `@DisplayName`
- Use AssertJ for assertions: `assertThat(result).isEqualTo(expected)`
- Use Mockito for mocking: `Mockito.mock(MetaData.class)`
- Test naming: `methodName() should expectedBehavior`
- Use `DummyCandle` for test data where applicable

### Configuration
- Use Spring Boot's `@ConfigurationProperties` for type-safe configuration
- Environment-specific configs in separate profiles (prod, test)
- Configuration properties in `application.yaml`

### Database & JPA
- Use JPA entities in `storage.entity.*` packages
- Enable JPA auditing with `@EnableJpaAuditing`
- Use Spring Data repositories: `JpaRepository<Entity, Id>`
- Batch size configured to 50 for performance

### Dependency Injection
- Use constructor injection (no field injection)
- Mark injected fields as `final`
- Use `@Component`, `@Service`, `@Repository`, `@Controller` appropriately

### BigDecimal Usage
- Use `BigDecimal` for all financial calculations
- Specify scale and rounding mode: `BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP)`
- Use `BigDecimalMath` utility for complex operations

### REST API Guidelines
- Use `@RestController` with `@RequestMapping`
- Return `ResponseEntity<?>` for flexibility
- Use DTOs in `web.dto` package for request/response
- Validate request bodies and path variables
- Use appropriate HTTP status codes

### Calculator Pattern
- Implement `Calculator<T>` interface
- Define `requires()` and `produces()` methods
- Use `ResultKey<T>` constants for result keys
- Return `Map<ResultKey<?>, Object>` from calculate method

### Performance Considerations
- Use streaming API for collections: `.stream().map().filter().toList()`
- Batch database operations (batch size: 50)
- Use Redis for caching where applicable
- Optimize database queries with proper indexing

### Code Quality
- Keep methods under 20 lines when possible
- Use meaningful variable names
- Add TODO comments for future improvements
- Use `@SuppressWarnings("unused")` sparingly

## Development Workflow

1. Run tests before committing: `mvn test`
2. Check code style and formatting
3. Ensure all calculator tests pass
4. Test REST endpoints manually or with integration tests
5. Verify Docker build works: `./scripts/build.sh`

## Environment Setup

- Java 21 required
- Maven for dependency management
- Docker and Docker Compose for local development
- PostgreSQL and Redis for production environment
- H2 for testing