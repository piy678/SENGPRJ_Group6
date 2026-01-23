# SENGPRJ_Group6

# Topic: LEO based assessment
## Overview

This repository contains the backend service for the LEO-Based Assessment Tool.
It implements RESTful APIs that support learning outcome–based assessment, user management, and progress tracking.

The backend handles authentication and authorization, manages LEO graphs, courses, and assessment states, and persists all relevant data in a PostgreSQL database.
It serves as the central logic layer between the frontend application and the database.

The system is built with Spring Boot and follows standard enterprise design principles to ensure maintainability, testability, and scalability.

This project is part of the Software Engineering Project course at FHTW.

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

End users do not interact directly with the backend application.
All user interactions are performed via the frontend interface.

Therefore, the user handbook is shared with the frontend project and
describes the usage of the system from the perspective of teachers and students.

This backend provides the underlying services and APIs required to support
the described user workflows.

*Teacher Account*

1.username: teacher1   
Password: password    
2.username: teacher2   
Password: password   
3.username: teacher3   
Password: password    

Role: Teacher   
Permissions: Course management, LEO graph creation, student assessment, progress monitoring   

*Student Account*

1.username:student1   
Password: password   
2.username:student2  
Password: password   
3.username.student3   
Password: password   
4.username:student4   
Password: password   
5.username:student5  
Password: password  

Role: Student  

Permissions: View courses, check LEO progress, view recommendations  

### Getting Started
- Start the application using Docker Compose
- Access the Electron UI
- Default roles: Teacher, Student

## Installation / Setup Guide
The application is deployed on an AWS server and can be started using Docker.

### Prerequisites
- AWS EC2 instance (Linux)
- Docker and Docker Compose installed
- Java 17 (for local development)
- Git

### Database Configuration

The backend uses a cloud-based PostgreSQL database provided by Neon.

Database credentials are **not stored in the repository**.
They must be provided via environment variables or a `.env` file.

Example variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### Setup on AWS Server
1. Clone the project repository on the AWS EC2 instance
 ```bash
git clone https://github.com/piy678/SENGPRJ_Group6
cd SENGPRJ_Group6
```
3. Configure environment variables (database credentials, ports)
 ```bash
 naono .env
```
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

SERVER_PORT=
SPRING_JPA_HIBERNATE_DDL_AUTO=
SPRING_JPA_SHOW_SQL=

VITE_API=
CORS_ALLOWED_ORIGINS=

CORS_ALLOWED_ORIGINS=

5. Start the application using Docker Compose
```bash
cd
cd SENGPRJ_Group6_FrontendPart
docker compose up -d
```
7. The backend and frontend services are started automatically
Production URL:
http://13.53.169.202:5174/

### Local Development (Optional)
1. Clone the repository
2. Start the database and backend using Docker Compose
```bash
docker compose up -d
```
### Start the Application locally

```bash
mvn clean spring-boot:run

```
To run the backend locally, a `.env` file with the required environment
variables must be provided (e.g. database credentials).

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

### Main Steps
- [ ] Project setup: Java 17, Maven; package structure (controller, service, repository, model, test)
- [ ] Domain model: LEO, Course, Student, Assessment; enums (Status/Level); relationships
- [ ] Persistence: PostgreSQL (Neon) + JPA/Hibernate entities and repositories
- [ ] Core services:
  - AssessmentService (create/update/read)
  - LeoService (calculation rules, next LEO suggestions)
- [ ] Validation & error handling: Bean Validation + global exception handler (consistent error response)
- [ ] Security & permissions: role-based authorization (Teacher/Student/Admin) + protected endpoints
- [ ] REST API: endpoints for LEOs, courses, students, assessments, recommendations
- [ ] Configuration: application properties + environment variables (.env); dev/prod profiles
- [ ] Docker deployment: Dockerfile + docker-compose for server deployment
- [ ] Testing:
  - Unit tests (services)
  - Integration tests (controllers/repositories)
  - Security/permission tests (e.g., 403 for forbidden actions)
- [ ] Documentation: API overview + setup guide + link to frontend repository

### Project Management (PM)
Project management was conducted using agile methods with iterative development
(sprints/iterations) and continuous progress tracking.

- A GitHub Project board was used for backlog management, task tracking, and iteration planning  
  (including requirements engineering, implementation tasks, testing, deployment, and documentation)
- Tasks were organized into iterations and tracked with status updates
- Responsibilities and progress were clearly assigned to team members

#### Task Planning & Implementation Tracking
- **GitHub Projects** were used for detailed task planning and implementation tracking
- Tasks were linked to concrete development activities such as backend implementation,
  frontend development, testing, deployment, and documentation
- Task states (e.g., To Do, In Progress, Done) were continuously updated

GitHub Project Board:  
https://github.com/users/piy678/projects/7

#### Time Tracking & Sprint Reviews
- Time was tracked per task within the project boards
- Sprint/iteration reviews were conducted after each iteration to evaluate progress,
  review completed work items, and plan subsequent tasks
- The backlog was continuously refined based on sprint outcomes and project progress

Additionally, backlog planning and task refinement were performed using **Microsoft Azure DevOps Boards**,
which were used to complement GitHub Projects for detailed backlog structuring and planning.

#### Backlog & Task Management
- The main product backlog and sprint backlogs were maintained using
  **Microsoft Azure DevOps Boards**
- User stories, functional requirements, and non-functional requirements
  were created.
- Each work item was assigned to team members and tracked until completion through GitHub Project Plan.

Azure DevOps Boards (Backlog & Sprints):  
https://dev.azure.com/BWI-25WS-SEPR-Team06/LEOBasedAssessment/_sprints/backlog/LEOBasedAssessment%20Team/LEOBasedAssessment/Sprint%201

## Evidence for Grading Criteria

Evidence for all grading criteria is provided through documented project artifacts,
repositories, and management tools. The evidence is structured according to the
three main grading categories defined in the course.

---

### Solution (40%)

**Functionality (30%)**
- Fully implemented frontend and backend applications
- Role-based access for teachers and students
- LEO creation, assessment recording, progress visualization, and recommendations
- Deployed and runnable system (AWS + Docker)

**Quality (10%)**
- Clean layered architecture (controller, service, repository)
- Use of Spring Boot, JPA, and React/Electron
- Validation, error handling, and security checks
- Unit and integration tests

Evidence:
- Frontend repository: https://github.com/piy678/SENGPRJ_Group6_FrontendPart
- Backend repository: https://github.com/piy678/SENGPRJ_Group6
- Deployed system on AWS

---
### Process (40%)

**Planning & Requirements Engineering**
- User stories and functional/non-functional requirements defined and tracked
- Backlog management using Azure DevOps Boards

**Analysis, Design, Implementation & Testing**
- Iterative development across multiple sprints
- Continuous implementation and testing
- Code reviews and refactoring during development

**Teamwork & Project Management**
- Task assignment and progress tracking
- Time tracking and sprint reviews
- Use of GitHub Projects for implementation planning

**Deployment**
- Docker-based deployment on AWS EC2
- Environment-based configuration and cloud database (Neon)

Evidence:
- Azure DevOps Boards (backlog & sprints): https://dev.azure.com/BWI-25WS-SEPR-Team06/LEOBasedAssessment/_sprints/backlog/LEOBasedAssessment%20Team/LEOBasedAssessment/Sprint%201
- GitHub Project board: https://github.com/users/piy678/projects/7
- Docker and deployment documentation in repositories

---

### Presentation (20%)

**Presentation (10%)**
- Structured presentation of the project and system architecture
- Live demonstration of core features

**Project Reflection (10%)**
- Written reflection covering challenges, solutions, and lessons learned
- Evaluation of teamwork, tools, and development process

Evidence:
- Presentation slides (submitted via Moodle also on Github)
- Written project reflection document

---


### Source Code

- Frontend and backend source code are maintained in two **separate GitHub repositories**
- Version control is handled via Git, with a complete commit history documenting
  the development process
- Commits and pull requests provide traceability of changes, bug fixes, and feature implementation

Repositories:
- Frontend: https://github.com/piy678/SENGPRJ_Group6_FrontendPart
- Backend: https://github.com/piy678/SENGPRJ_Group6

## Purpose and Background

The tool supports constructive alignment, where learning outcomes (LEOs) define what students must know or be able to do, and assessments measure mastery of these outcomes instead of grading isolated tasks.

Some LEOs depend on others—mastering a high-level outcome implies mastery of its supporting lower-level outcomes.
The frontend helps visualize these hierarchical structures and mastery levels.

## Requirements for Developers

To work on this project, it is recommended that you have:

- Basic programming skills

- Interest in education technology

- Fundamental web development knowledge (HTML, CSS, JavaScript/TypeScript)

- Familiarity with modern frontend frameworks (React/Vite recommended)

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

## Project Management

The project was developed using agile software development practices.

Project management included:
- Sprint planning and sprint reviews
- Backlog management and task distribution
- Time tracking and progress monitoring
- Regular team coordination

Backlogs and sprint planning were managed using project management tools
(e.g. Azure DevOps Boards and GitHub Projects), with relevant artifacts
submitted via Moodle.

## Personal Contribution

All group members contributed actively to the project across multiple areas.

Contributions included:
- Frontend and backend development
- API design and system integration
- Testing and debugging
- Deployment and documentation

Individual responsibilities and contributions are documented in the project plan
and task tracking system.

Project management and contribution evidence:
https://github.com/users/piy678/projects/7

For grading purposes, the GitHub Project board provides traceable evidence of
task assignments, progress, and completed work per team member.
Each task in the project board is assigned to specific team members,
allowing individual contributions to be clearly identified.


## Personal Development

Through this project, we developed both technical and personal skills.

### Technical Development
- Full-stack development (frontend and backend integration)
- Cloud-based deployment using AWS
- Use of a managed cloud PostgreSQL database (Neon)
- Working with client–server architectures and distributed components

### Personal Development
- Effective team communication and collaboration
- Time management and task prioritization
- Problem-solving in a collaborative development environment

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

## Related Repository

This backend service is consumed by the frontend application of the
LEO-Based Assessment Tool.

Frontend repository:  
https://github.com/piy678/SENGPRJ_Group6_FrontendPart

## Contributors

El Khadra Tareq  
Kovalko Markiian  
Yalcin Pinar  
Zhou Lukas  

### Group 6 — SENGPRJ

### Supervisor: Thomas Mandl
