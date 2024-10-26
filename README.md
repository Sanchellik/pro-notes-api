# Pro Notes API

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Project Structure](#project-structure)
4. [Environment Setup](#environment-setup)
5. [Configuration Profiles](#configuration-profiles)
6. [Database Setup](#database-setup)
7. [Running the Application](#running-the-application)
   1. [Running Locally](#running-locally)
   2. [Running Using Docker](#running-using-docker)
8. [Health Checks](#health-checks)
9. [Running Tests](#running-tests)
10. [Troubleshooting](#troubleshooting)
11. [License](#license)

## Project Overview
[//]: # (TODO...)



## Technologies Used
The project uses the following technologies:

- Java 21
- Spring Boot 3.3.4
- PostgreSQL
- Liquibase for database versioning
- Docker & Docker Compose
- Testcontainers for integration testing with PostgreSQL
- Maven as the build tool



## Project Structure
```
pro-notes-api/
│
├── .env                        # Environment variables for local and Docker setups
├── Dockerfile                  # Docker configuration file for building the app image
├── docker-compose.yaml         # Docker Compose file for running the app and PostgreSQL
├── src/
│   ├── main/
│   │   ├── java/ru/gozhan/pronotesapi/
│   │   └── resources/
│   │       └── db/changelog/                   # Liquibase changelogs for database schema
│   │       └── application.yaml                # Default configuration
│   │       └── application-local.yaml          # Local environment configuration
│   │       └── application-docker.yaml         # Docker environment configuration
│   └── test/                                   # Test sources
│
└── README.md                   # This readme file
```



## Environment Setup
Before you start, ensure that the following dependencies are installed:

**1. If run locally:**
- Java 21
- Maven
- PostgreSQL

**2. If run using Docker:**
- Docker & Docker Compose

### Environment Variables
You'll need a `.env` file for environment-specific configuration.
There are environment variables:
1. `HOST` - host of PostgreSQL database
2. `PORT` - app server port
3. `POSTGRES_DATABASE` - name of PostgreSQL database
4. `POSTGRES_PORT` - local port of PostgreSQL database
5. `POSTGRES_USERNAME` - name of PostgreSQL user
6. `POSTGRES_DATABASE` - password for PostgreSQL user
7. `POSTGRES_SCHEMA` - name of PostgreSQL scheme

Here's an example:

```dotenv
API_HOST=localhost
API_PORT=8080

POSTGRES_DB=pro_notes
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_SCHEMA=public
```

> You may use `dotenv` util for convenient use environment variables in project



## Configuration Profiles
The project uses different profiles for various environments:
- **Default** (`application.yaml`): Common settings for all environments.
- **Local** (`application-local.yaml`): Settings for running the app on your local machine.
- **Docker** (`application-docker.yaml`): Settings for running the app inside a Docker container.

You can switch between profiles by setting the `SPRING_PROFILES_ACTIVE` environment variable
or by using the `--spring.profiles.active` flag when running the application.



## Database Setup

### Locally
- Ensure PostgreSQL is installed and running.
- Use the credentials provided in the `.env` file to create a new database.

### Docker
- PostgreSQL will be automatically set up as part of the Docker Compose configuration.
The database schema will be managed by Liquibase.



## Running the Application

### Running Locally
To run the application locally:

1. Clone the repository:
```bash
git clone https://github.com/sanchellik/pro-notes-api.git
cd pro-notes-api
```

2. Install dependencies:
```bash
mvn clean install
```

3. Set up PostgreSQL:
- Create a PostgreSQL database using the credentials in your `.env` file.

4. Run the app:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The app will be accessible on `http://localhost:$PORT`.

### Running Using Docker
1. Build and start the application using Docker Compose (use -d if u don't want to block terminal thread):

- If you don't want to block the terminal thread (run in the background), use:
```bash
docker compose up --build -d
```

- To run the application and view logs in the current terminal session, use:
```bash
docker compose up --build
```

2. The application will be running inside a Docker container and will be accessible on `http://localhost:$PORT` (specified in your `.env` file).

3. Stopping the application:
```bash
docker compose down
```

To stop and clean up all containers, networks, and volumes:
```bash
docker compose down --volumes --remove-orphans
```



## Health Checks

### Application
The **Pro Notes API** application uses Spring Boot Actuator for health monitoring.
You can check the health status of the application by accessing the following endpoint:
```bash
curl "http://localhost:$PORT/actuator/health"
```

This will return the current health status of the application,
indicating whether it's ready to process requests.

Example correct response:
```json
{
  "status": "UP"
}
```

### Database
To check the availability of the PostgreSQL database, you can use the `pg_isready`
utility inside the database container (this command comes pre-installed with PostgreSQL).
Run the following command:
```bash
docker exec -it pro-notes-postgres pg_isready -U $POSTGRES_USERNAME
```

If the database is operational, the response will be
`/var/run/postgresql:5432 - accepting connections`.
If not, connection errors will be displayed.



## Running Tests
The project includes unit and integration tests.

1. **Run tests:**
```bash
mvn test
```

2. **Testcontainers** is used for integration tests, so PostgreSQL will run in a Docker container during testing.



## Troubleshooting

### Common Issues
- **PostgreSQL connection errors**: Ensure that PostgreSQL is running and the credentials match those in the `.env` file.
- **Docker port conflicts**: If the port is already in use, change the `PORT` in the `.env` file.

### Logs
- View Docker logs:
```bash
docker logs pro-notes-api
```

```bash
docker compose logs -f
```

- View application logs:
```bash
mvn spring-boot:run
```


## License
This project is licensed under the MIT License. See the [LICENSE](https://github.com/Sanchellik/pro-notes-api/blob/master/LICENSE) file for more details.
