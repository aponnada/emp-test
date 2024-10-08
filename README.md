# Requirements

Before setting up the project, make sure you have the following installed:

Java 1.8 

Maven 3+

MySQL (or any other preferred relational database)

Git:

An IDE (like Spring Tool Suite or IntelliJ IDEA)

# Installation:

Clone the Repository:

Open your terminal and clone the project from GitHub:

git clone git@github.com:aponnada/emp-test.git

cd emp-test

Open the Project:

Open the project in your preferred IDE (e.g., Spring Tool Suite or IntelliJ IDEA).

Build the Project:

Use Maven to build the project:

mvn clean install

# Database Setup

Install MySQL:

If you don't have MySQL installed, download it from here and follow the installation instructions.

Create the Database:

Open MySQL and create a new database:


CREATE DATABASE employee_management;

Configure the Application:

Open src/main/resources/application.properties and update the database configuration:

properties
spring.application.name=employee


# MySQL Database configuration
spring.datasource.url=jdbc:mysql://localhost/employee_management?useSSL=false&serverTimezone=UTC

spring.datasource.username=root

spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


# JPA and Hibernate settings
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

server.port=8081

security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b

security.jwt.expiration-time=3600000

Run the Application:
You can run the application through your IDE or use Maven:

mvn spring-boot:run

# Access the Application:

Once the application starts, it will be accessible at http://localhost:8081.

API Documentation
The application uses Swagger for API documentation.

 After running the application, you can access the Swagger UI at: http://localhost:8081/swagger-ui.html
 
You can use the Swagger UI to interact with the available APIs.

Running Tests

To run the unit and integration tests, execute the following command:

mvn test