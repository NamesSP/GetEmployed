# GetEmployed
Spring boot Project



linkedin-clone-backend/
├── config-server/
├── discovery-server/
├── gateway-service/
├── auth-service/           <-- MySQL
├── user-service/           <-- MongoDB
├── company-service/        <-- MySQL
├── job-service/            <-- MySQL
├── application-service/    <-- MongoDB
├── experience-service/     <-- MongoDB
└── common/                 <-- Shared DTOs, annotations, utils

----------------------------------------------------------------

🧭 1. config-server/
config-server/
├── src/main/java/com/linkedinclone/config/
│   ├── ConfigServerApplication.java       // Main class with @EnableConfigServer
│   └── config/
│       └── GitConfig.java                 // Optional Git config setup
├── src/main/resources/
│   ├── application.yml                    // Points to config repo
│   └── bootstrap.yml                      // For Spring Cloud config

🧭 2. discovery-server/
discovery-server/
├── src/main/java/com/linkedinclone/discovery/
│   └── DiscoveryServerApplication.java    // Main class with @EnableEurekaServer
├── src/main/resources/
│   └── application.yml                    // Eureka server config

🧭 3. gateway-service/
gateway-service/
├── src/main/java/com/linkedinclone/gateway/
│   ├── GatewayServiceApplication.java     // Main class
│   └── config/
│       └── GatewayConfig.java             // Route definitions
│   └── security/
│       └── JwtAuthenticationFilter.java   // JWT filter for auth
├── src/main/resources/
│   └── application.yml                    // Route and Eureka config


🧭 4. auth-service/ (MySQL)
auth-service/
├── controller/
│   └── AuthController.java                // Login, register, token endpoints
├── service/
│   └── AuthService.java                   // Business logic
├── entity/
│   └── UserCredential.java                // MySQL entity
├── repository/
│   └── UserCredentialRepository.java      // JPA repository
├── security/
│   ├── JwtUtil.java                       // Token generation/validation
│   ├── SecurityConfig.java                // Spring Security setup
│   └── CustomUserDetailsService.java      // Loads user details
├── exception/
│   └── AuthExceptionHandler.java          // Global exception handler


🧭 5. user-service/ (MongoDB)
user-service/
├── controller/
│   └── UserController.java                // CRUD for users
├── service/
│   ├── UserService.java
│   └── impl/UserServiceImpl.java
├── entity/
│   ├── User.java                          // MongoDB document
│   └── Skill.java                         // Embedded or separate document
├── repository/
│   └── UserRepository.java                // MongoRepository
├── annotation/
│   └── LogExecutionTime.java              // Custom annotation
├── aspect/
│   └── LoggingAspect.java                 // AOP logic
├── feign/
│   └── JobServiceClient.java              // Feign client to job-service
├── exception/
│   └── GlobalExceptionHandler.java


🧭 6. company-service/ (MySQL)
company-service/
├── controller/
│   └── CompanyController.java
├── service/
│   └── CompanyService.java
├── entity/
│   ├── Company.java
│   └── Recruiter.java
├── repository/
│   ├── CompanyRepository.java
│   └── RecruiterRepository.java
├── feign/
│   └── UserServiceClient.java             // Optional


🧭 7. job-service/ (MySQL)
job-service/
├── controller/
│   └── JobController.java
├── service/
│   └── JobService.java
├── entity/
│   ├── Job.java
│   └── JobSkill.java                      // Many-to-many mapping
├── repository/
│   ├── JobRepository.java
│   └── JobSkillRepository.java
├── feign/
│   └── CompanyServiceClient.java


🧭 8. application-service/ (MongoDB)
application-service/
├── controller/
│   └── ApplicationController.java
├── service/
│   └── ApplicationService.java
├── entity/
│   └── Application.java                   // MongoDB document
├── repository/
│   └── ApplicationRepository.java
├── feign/
│   └── JobServiceClient.java


🧭 9. experience-service/ (MongoDB)
experience-service/
├── controller/
│   └── ExperienceController.java
├── service/
│   └── ExperienceService.java
├── entity/
│   └── Experience.java                    // MongoDB document
├── repository/
│   └── ExperienceRepository.java


🧭 10. common/ (Shared Module)
common/
├── dto/
│   ├── UserDTO.java
│   ├── JobDTO.java
│   └── SkillDTO.java
├── annotation/
│   ├── LogExecutionTime.java
│   └── ValidateUserRole.java
├── aspect/
│   ├── LoggingAspect.java
│   └── RoleValidationAspect.java
├── util/
│   └── JwtUtil.java


🧪 Testing Structure (Per Service)
src/test/java/com/linkedinclone/<service>/
├── controller/
├── service/
├── repository/
└── integration/

------------------------------------------------------------------------

🧭 1. config-server/
File/Folder	Purpose
ConfigServerApplication.java	Main class with @EnableConfigServer to start the config server.
application.yml	Basic Spring Boot config (e.g., port, Eureka registration).
bootstrap.yml	Points to the Git repo or local folder where config files for all services are stored.


🧭 2. discovery-server/
File/Folder	Purpose
DiscoveryServerApplication.java	Main class with @EnableEurekaServer to enable service discovery.
application.yml	Configures Eureka server settings (port, instance info, etc.).


🧭 3. gateway-service/
File/Folder	Purpose
GatewayServiceApplication.java	Main class to run the API Gateway.
config/GatewayConfig.java	Optional Java-based route definitions (can also be done in YAML).
security/JwtAuthenticationFilter.java	Intercepts requests to validate JWT tokens.
application.yml	Defines routes to microservices, security filters, and Eureka config.


🧭 4. auth-service/ (MySQL)
File/Folder	Purpose
controller/AuthController.java	Exposes login, register, and token refresh endpoints.
service/AuthService.java	Contains business logic for authentication and token generation.
entity/UserCredential.java	JPA entity representing user credentials in MySQL.
repository/UserCredentialRepository.java	Interface for DB operations on credentials.
security/JwtUtil.java	Utility class for generating and validating JWTs.
security/SecurityConfig.java	Configures Spring Security (password encoder, filters, etc.).
security/CustomUserDetailsService.java	Loads user details from DB for authentication.
exception/AuthExceptionHandler.java	Handles exceptions like invalid credentials.



🧭 5. user-service/ (MongoDB)
File/Folder	Purpose
controller/UserController.java	REST endpoints for user CRUD operations.
service/UserService.java	Interface defining user-related operations.
service/impl/UserServiceImpl.java	Implements the business logic.
entity/User.java	MongoDB document for user profile.
entity/Skill.java	Embedded or referenced document for skills.
repository/UserRepository.java	Interface extending MongoRepository.
annotation/LogExecutionTime.java	Custom annotation to log method execution time.
aspect/LoggingAspect.java	AOP logic to handle @LogExecutionTime.
feign/JobServiceClient.java	Feign client to call job-service APIs.
exception/GlobalExceptionHandler.java	Handles exceptions globally.


🧭 6. company-service/ (MySQL)
File/Folder	Purpose
controller/CompanyController.java	Endpoints for managing companies and recruiters.
service/CompanyService.java	Business logic for company operations.
entity/Company.java	JPA entity for company data.
entity/Recruiter.java	JPA entity for recruiter data.
repository/CompanyRepository.java	JPA repository for companies.
repository/RecruiterRepository.java	JPA repository for recruiters.
feign/UserServiceClient.java	Optional client to fetch user info.


🧭 7. job-service/ (MySQL)
File/Folder	Purpose
controller/JobController.java	Endpoints for job posting and retrieval.
service/JobService.java	Business logic for job operations.
entity/Job.java	JPA entity for job postings.
entity/JobSkill.java	Entity for many-to-many mapping between jobs and skills.
repository/JobRepository.java	JPA repository for jobs.
repository/JobSkillRepository.java	JPA repository for job-skill mapping.
feign/CompanyServiceClient.java	Feign client to fetch company info.


🧭 8. application-service/ (MongoDB)
File/Folder	Purpose
controller/ApplicationController.java	Endpoints for applying to jobs and tracking status.
service/ApplicationService.java	Business logic for applications.
entity/Application.java	MongoDB document for job applications.
repository/ApplicationRepository.java	MongoRepository for applications.
feign/JobServiceClient.java	Feign client to fetch job info.


🧭 9. experience-service/ (MongoDB)
File/Folder	Purpose
controller/ExperienceController.java	Endpoints for managing user experience.
service/ExperienceService.java	Business logic for experience records.
entity/Experience.java	MongoDB document for experience.
repository/ExperienceRepository.java	MongoRepository for experience data.


🧭 10. common/ (Shared Module)
File/Folder	Purpose
dto/	Shared DTOs like UserDTO, JobDTO, SkillDTO.
annotation/LogExecutionTime.java	Custom annotation for logging.
annotation/ValidateUserRole.java	Custom annotation for role validation.
aspect/LoggingAspect.java	AOP logic for @LogExecutionTime.
aspect/RoleValidationAspect.java	AOP logic for @ValidateUserRole.
util/JwtUtil.java	Shared JWT utility class.
