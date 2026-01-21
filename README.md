# SENGPRJ_Group6

# Topic: LEO based assessment
### Features
- creation of LEO structure and grade calculation for teachers
- recording and editing of assessment results for teachers
- visualization of current standings for learners
- suggestion of possible next LEOs to prepare for student



## User Handbook / Guide
This section describes how to use the application from a user perspective.

### Teacher Guide
- Log in as a Teacher
- Create and manage courses
- Create and edit LEO graphs
- Record and update student assessments
- View student progress and recommendations

### Student Guide
- Log in as a Student
- View assigned courses
- Check LEO progress and current status
- View recommendations for next LEOs

### Getting Started
- Start the application using Docker Compose
- Access the Electron UI
- Default roles: Admin, Teacher, Student

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
- [ ] REST API: /leos, /students, /assessments, /recommendations with pagination/filtering/sorting; 
- [ ]  UI (Electron): LEO table, Teacher grading view, Student progress view (progress bars/filters), basic accessibility
- [ ]  Import/Export: JSON import for LEO catalog; CSV/JSON export of results (define formats)
- [ ]  Logging: structured logs; basic metrics if using Spring Boot Actuator
- [ ]  Build & run: app config (application-dev.yml), profiles (dev/test), Dockerfile + docker-compose (app + DB)
- [ ]  Testing:
 - Unit tests
 - Integration tests 
 - API tests (REST-assured) and one E2E test 
- [ ] Performance sanity: quick local check that key endpoints respond



## Installation / Setup Guide
The application is deployed on an AWS server and can be started using Docker.

### Prerequisites
- AWS EC2 instance (Linux)
- Docker and Docker Compose installed
- Java 17 (for local development)
- Git

### Setup on AWS Server
1. Clone the project repository on the AWS EC2 instance
2. Configure environment variables (database credentials, ports)
3. Start the application using Docker Compose
4. The backend and frontend services are started automatically

### Local Development (Optional)
1. Clone the repository
2. Start the database and backend using Docker Compose
3. Start the Electron frontend locally



## Technical Documentation
This section describes the technical architecture, components, and internal logic of the system.
The system consists of a Spring Boot backend, an Electron frontend, and a PostgreSQL database.
It follows a layered architecture (controller, service, persistence) with role-based security and automated grading cascade logic.

### Architecture
- Backend: Java 17 with Spring Boot (REST API)
- Frontend: Electron-based UI using web technologies
- Database: Neon (managed PostgreSQL cloud database)
- Deployment: Docker and Docker Compose on AWS EC2

### Database
The system uses Neon, a managed cloud-based PostgreSQL database.
Neon provides serverless PostgreSQL with automatic scaling and branching support.

- Database engine: PostgreSQL (via Neon)
- Secure cloud connection using connection string and credentials
- Used for storing users, LEOs, assessments, and relationships
- Schema managed via JPA/Hibernate

### Backend Structure
- Controller layer: REST endpoints for LEOs, students, assessments, and recommendations
- Service layer: business logic (grading cascade, validation, recommendation logic)
- Persistence layer: JPA/Hibernate entities and repositories
- Security: role-based access control (Admin, Teacher, Student)

### Core Domain Model
- LEO: represents a learning outcome node in a graph
- Assessment: stores the status of a LEO for a student
- User: authenticated system user with a role
- Relationships: LEO dependencies are stored as graph relations

### API Overview
- `/leos` – manage learning outcomes and dependencies
- `/students` – manage students and enrollments
- `/assessments` – record and update assessment results

### Grading & Cascade Logic
When a higher-level LEO is marked as *Reached*, all dependent lower-level LEOs are automatically updated according to cascade rules.

### Testing
- Unit tests using JUnit 5
- Integration tests using Spring Boot Test and Testcontainers
- API tests using REST-assured

### Additional Requirements for Learning Outcomes (LEOs)
Some learning outcomes (LEOs) may imply others.  
Example: "Can multiply 3-digit numbers" automatically implies "Can multiply 2-digit numbers".  
In this case, if a student demonstrates mastery of 3-digit multiplication, the 2-digit multiplication outcome can be marked as reached.  

<u>Grading Scale for LEOs:</u>  

Not Reached  
Partially Reached  
Reached  
Unmark

<u>Cascade Logic:</u>  

When a higher-level LEO is marked as Reached, all dependent (implied) lower-level LEOs must also be updated accordingly (set to Partially Reached or Reached).

### Tools used for the Project:  
Java 17  
Spring Boot (Web, Data JPA)  
Maven oder Gradle  
PostgreSQL   
Hibernate (JPA)    
UI: Electron  
Testing: JUnit 5, Spring Boot Test, Testcontainers (Postgres), REST-assured  
Docker + docker-compose; Actuator für Health/Metrics  



## Project Management (PM)
Project management was conducted using agile methods.

- A GitHub Project board was used for backlog management, task tracking, and iteration planning  
  (including requirements engineering, implementation tasks, testing, deployment, and documentation)
- Tasks were organized into iterations and tracked with status updates
- Responsibilities and progress were assigned to team members
- Sprint planning and sprint reviews
- Regular team meetings and coordination

All project management artifacts (backlog, sprint reviews, time tracking)
are provided as part of the Moodle submission.

Additionally, backlog planning and task refinement were performed using **Microsoft Azure DevOps Boards**,
which were used to complement GitHub Projects for detailed backlog structuring and planning.



## Evidence for Grading Criteria
Evidence for all grading criteria is provided and documented using multiple tools and artifacts.

### Source Code
- Frontend and backend source code are maintained in GitHub in two seperated repositories



## Reflection
During the development of this project, we gained practical experience in designing and implementing a full-stack application with a clear separation of frontend, backend, and database layers.

One of the main challenges was implementing the LEO dependency graph and the cascade grading logic, as changes to a single learning outcome could affect multiple dependent outcomes. Ensuring data consistency and correct transactional behavior required careful design and testing. Furthermore, we learned how to deploy our code on AWS EC2 Instance using Docker and how to connect our databse with neon so everything does not only run locally. In addition, it improved our understanding of modern deployment workflows and cloud infrastructure.

Overall, the project helped us strengthen our skills in backend development with Spring Boot, REST API design, role-based security, testing strategies, and collaborative software development.

### Deployment & Configuration
- Deployment was performed using Docker and Docker Compose
- The application is hosted on an AWS EC2 server
- Configuration includes cloud-based database access (Neon PostgreSQL)

### Documentation 
- Technical documentation and user handbook are provided in the project repositories
- Reflection, personal contribution, and personal development are documented and submitted via Moodle
All required evidence for grading criteria has been submitted via **Moodle** as specified.


## Personal Contribution

All group members contributed actively to the project.

Contributions included:
- Frontend and backend development
- API design and integration
- Testing and debugging
- Deployment and documentation

Detailed individual contributions are documented in the complete project plan and task overview are available here:  
https://github.com/users/piy678/projects/7

For grading purposes, the GitHub Project board serves as the primary source of
project management evidence.


## Personal Development

Through this project, we developed both technical and personal skills.

Technical growth included:
- Full-stack development
- Cloud deployment with AWS
- Running database over a server (neon)
- Working with distributed systems

Personal development included:
- Team communication
- Time management
- Problem-solving in a collaborative environment



## Questions:
Should the tool be web-based (browser) or desktop-based (JavaFX, Electron, etc.)?   
Electron desktop app. Built with web tech, packaged to run locally on Windows/macOS/Linux.   

Do we need to implement authentication or can we assume a single teacher and student?  
Yes. Support separate accounts and roles for multiple teachers and students.   

How detailed should the LEO structure be (tree hierarchy vs. simple list)?  
As a graph. Nodes and relationships (not just a simple tree or flat list).   

Should the visualization be graphical (like a dependency tree) or textual (list with status)?  
Text/list view. A graphic diagram is considered too complex; a list is more precise for this use case.   

Should we store the data locally or in a database (e.g. SQLite, MySQL)?  
In a database. Use a proper DB (e.g., SQLite/MySQL) rather than local files only.  

Is suggestion logic mandatory or optional (for “next possible LEOs”)?  
Mandatory. The system must generate recommendations (e.g., next applicable LEOs).   

Should grading levels (“not reached”, “partially reached”, “reached”) be customizable?  
No. Use fixed levels, with “Unmark” as the fourth level in addition to the existing ones.   



## Contributors

El Khadra Tareq  
Kovalko Markiian  
Yalcin Pinar  
Zhou Lukas  

### Group 6 — SENGPRJ

### Supervisor: Thomas Mandl
