# Spring Boot Assignment

This is a Spring Boot application serving as a backend for a photo album gallery. It includes functionalities such as importing users' galleries, CRUD operations on photos and albums, and more.

## Prerequisites
- Java 21
- Maven

## Running the Application
1. Clone this repository to your local machine.
2. Configure the database by updating the DB connection details in `application.properties`.
3. **Set up the Database**:
   - Locate the `db.sql` file in the [db.sql](src/main/resources/sql/db.sql) directory of the project.
   - Copy the contents of `db.sql` and run the SQL commands in your preferred SQL client or tool to create the necessary database schema and tables.
4. Run Python Scripts to Clone Data:
   - Before running these scripts, ensure you have Python installed on your machine.
   - Open a terminal and navigate to the project directory.
   - Run the following commands to clone data from external sources into your database:
     - [users.py](src/main/resources/python/users.py) to clone data from [https://jsonplaceholder.typicode.com/users](https://jsonplaceholder.typicode.com/users)
     - [albums.py](src/main/resources/python/albums.py) to clone data from [https://jsonplaceholder.typicode.com/albums](https://jsonplaceholder.typicode.com/albums)
     - [photos.py](src/main/resources/python/photos.py)` to clone data from [https://jsonplaceholder.typicode.com/photos](https://jsonplaceholder.typicode.com/photos)
5. Build the application using Maven:
   - Maven: `mvn clean package`
6. Run the application.

The application will start on port 8090 by default. You can configure the port in the `application.properties` file if needed.

## Additional Notes
- For authentication, the application uses JWT with LDAP.
- Ensure that only users with the ADMIN role can access the APIs.
