# JPAwithJsonDataAndPostgres
This repository deals with json data handling using JPA and a RDBMS system PostgreSQL using a Springboot application.
In this example, we will run a Springboot app which will connect with a local database running in docker container.

## Steps to run the application

Firstly, lets spin up a docker container for postgres to connect our Springboot app with it.

1. Pull a docker container for postgres: `docker pull postgres:apline3.18`
2. Run postgres docker container to start postgres server: `docker run --rm --name postgres_0 -e POSTGRES_PASSWORD=password -d -p 5432:5432 docker.io/library/postgres:alpine3.18`
3. See that postgres container is still running in detached mode: `docker ps`
4. Connect to postgres server container: `docker exec -it postgres_0 bash`
5. After tunneling inside, type this command to connect to default postgres database and postgres user provided out of box by Postgresql: `psql -d postgres -U postgres -W` and enter password as `password`
6. Now, type this to list all databases in our container: `\l`
7. Observe that we have a postgres database already with postgres user. This is the default database and user provided by Postgresql out of box.

Now, lets run out Springboot app:

1. Go to the root directory of this project and run: `mvn clean install`
2. Run the application using command: `mvn spring-boot:run`
3. Observe that the application is running on port 8081
4. Type this in your command prompt that we opened in step (5) previously: `\dt` to see all tables in postgres database. Observe that there is a table named `student` in the database now.
   Type this to see records inside student table: `select * from student;`
   JPA has automatically created a table named `student` in our postgres database. This is because we have provided `spring.jpa.hibernate.ddl-auto=create` in our `application.properties` file. This property tells JPA to create tables automatically in our database.

## API CALLS:
Find the POSTMAN collection in the repository to see all API calls.

## Databse query examples with json format

//SELECT id, name, favs -> 'color' AS COLOR FROM student; This will return color values with double quotes around it. Null values are shown if the key doesnt exist.
//SELECT id, name, favs ->> 'color' AS COLOR FROM student; This will return color values without double quotes around it. Null values are shown if the key doesnt exist.
//SELECT id, name, favs from student where favs ->> 'color' = 'pink'; This will return all records where color is pink. Null values are shown if the key doesnt exist.
//SELECT id, name, JSONB_OBJECT_KEYS(favs) FROM student; This will return all keys in favs column.

//KEY EXISTS in JSON column:
1. SELECT id, name, favs from student where favs ? 'color' = true; // Checks if 'color' key exists in favs column. Returns all records where color key exists.
   or we can write just: SELECT id, name, favs from student where favs ? 'color';
2. SELECT id, name, favs from student where favs ?&array['color','travel']; // Checks if 'color' and 'travel' keys exists in favs column. Returns all records where color and travel keys exists.
3. SELECT id, name, favs from student where favs ?|array['color','travel']; // Checks if 'color' or 'travel' keys exists in favs column. Returns all records where color or travel keys exists.

//UPDATE:
//UPDATE value of specific key in JSON column:
1. UPDATE student SET favs = JSONB_SET(favs, '{color}', '"yellow"') WHERE id = 1; //Updates the color to yellow for ID=1 record.
   //UPDATE value of specific key in JSON column or ADD a new key-value pair in JSON column if key doesnt exists:
1. update student set favs = favs || '{"color": "red"}' where id = 1; Updates/Adds a new key-value pair in favs column for ID=1 record.
   // Update entire json value for specific record:
1. update student set city='JAIPUR', state='RAJASTHAN', adult=true, age=24, id=2, name='Kush Sharma', favs='{"games": ["volleyball"], "color": "purple", "travel": ["Paris", "Kashmir", """Thailand"]}' where id=2;

//DELETE a key-value pair from JSON column:
UPDATE student SET favs = favs - 'color' WHERE id = 1; // Deletes the color key-value pair from favs column for ID=1 record.
