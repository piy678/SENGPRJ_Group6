# SENGPRJ_Group6

# Topic: LEO based assessment
### Features
- creation of LEO structure and grade calculation for teachers
- recording and editing of assessment results for teachers
- visualization of current standings for learners
- suggestion of possible next LEOs to prepare for student

## Additional Requirements for Learning Outcomes (LEOs)

Some learning outcomes (LEOs) may imply others.  
Example: "Can multiply 3-digit numbers" automatically implies "Can multiply 2-digit numbers".  
In this case, if a student demonstrates mastery of 3-digit multiplication, the 2-digit multiplication outcome can be marked as reached.  

<u>Grading Scale for LEOs:</u>  

Not Reached  
Partially Reached  
Reached  

<u>Cascade Logic:</u>  

When a higher-level LEO is marked as Reached, all dependent (implied) lower-level LEOs must also be updated accordingly (set to Partially Reached or Reached).

### Main Steps
- [ ] Project setup: Java 17+, Maven/Gradle; modules/packages (model, service, web, test)
- [ ] Create core classes and fields: LEO, Student, Assessment, User, Role, Status enum; dependencies
- [ ] DB schema & migrations: tables, PK/FK, unique indexes;
- [ ] Data access: JPA/Hibernate (or JDBC) entities/repositories to read/write LEOs, students, and assessments; mapping between DB and domain
- [ ] Core logic (services):
- GradingService with transactional cascade
- AssessmentService (CRUD)  
- RecommendationService 
- [ ] Validation & error model: Bean Validation; global exception handler; consistent error body (code, message, optional traceId)
- [ ] Security: login with hashed passwords (bcrypt/Argon2), RBAC (Admin/Teacher/Student), protect write endpoints
- [ ] REST API (or JavaFX handlers): /leos, /students, /assessments, /recommendations with pagination/filtering/sorting; 
- [ ]  UI (JavaFX or Web): LEO table, Teacher grading view, Student progress view (progress bars/filters), basic accessibility
- [ ]  Import/Export: JSON import for LEO catalog; CSV/JSON export of results (define formats)
- [ ]  Logging: structured logs; basic metrics if using Spring Boot Actuator
- [ ]  Build & run: app config (application-dev.yml), profiles (dev/test), Dockerfile + docker-compose (app + DB)
- [ ]  Testing:
 - Unit tests
 - Integration tests 
 - API tests (REST-assured) and one E2E test 
- [ ] Performance sanity: quick local check that key endpoints respond

## Tools used for the Project:  
Java 17  
Spring Boot (Web, Data JPA)  
Maven oder Gradle  
PostgreSQL   
Hibernate (JPA)    
UI: JavaFX  
Testing: JUnit 5, Spring Boot Test, Testcontainers (Postgres), REST-assured  
Docker + docker-compose; Actuator für Health/Metrics  

## Questions:
Should the tool be web-based (browser) or desktop-based (JavaFX, Electron, etc.)? 

Do we need to implement authentication or can we assume a single teacher and student?  

How detailed should the LEO structure be (tree hierarchy vs. simple list)?  

Should the visualization be graphical (like a dependency tree) or textual (list with status)?  

Should we store the data locally or in a database (e.g. SQLite, MySQL)?  

Is suggestion logic mandatory or optional (for “next possible LEOs”)?  

Should grading levels (“not reached”, “partially reached”, “reached”) be customizable?  
