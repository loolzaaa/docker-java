# Docker Java example project

An example of a Java project based on Spring Boot with the simplest rest api.
The project run from docker container together with the Postgres database 
from another container. Both containers are connected with bridge docker network.

-------------------

## Build and Run via classic Docker
### Build Docker image:
```shell
> cd /path/to/this/project
> docker build -t docker-java .
```

### Create bridge network for container communication:
```shell
> docker network create notes_net
```

### Create named volume for persist Postgres data:
```shell
> docker volume create notes_data
```

### Run Postgres container in PowerShell:
```shell
> docker run --rm -d -v notes_data:/var/lib/postgresql/data `
    -v /path/to/secret:/etc/secrets/secret:ro `
    --network notes_net `
    --name notes_postgres `
    -e POSTGRES_USER_FILE=/etc/secrets/secret `
    -e POSTGRES_PASSWORD_FILE=/etc/secrets/secret `
    -e POSTGRES_DB_FILE=/etc/secrets/secret `
    postgres:14.2-alpine
```
**Note:** replace \` to / in linux shell.\
**Note:** replace `/path/to/secret` to absolute path of secret file.

### Run Notes container in PowerShell:
```shell
> docker run --rm -d `
    --name notes_server `
    --network notes_net `
    -e POSTGRES_URL=jdbc:postgresql://notes_postgres/notes `
    -e spring.profiles.active=postgres `
    -p 8080:8080 docker-java
```

## ... or via Docker Compose
### Production mode
```shell
> cd /path/to/this/project
> docker-compose up
```
### Development mode with debug feature
```shell
> cd /path/to/this/project
> docker-compose -f docker-compose.dev.yml up
```
**Note:** debugger can access to port 8000

-----------------------

## Usage
#### Get all notes from database
```shell
curl -X GET -H "Content-Type: text/html" http://localhost:8080/note/all
```

#### Get note by UUID from database
```shell
curl -X GET -H "Content-Type: text/html" http://localhost:8080/note/{uuid}
```
**Note:** replace {uuid} to concrete UUID

#### Create new note
```shell
curl -X PUT -H "Content-Type: text/html" http://localhost:8080/note -d "Some text of new note"
```

#### Change note text
```shell
curl -X PATCH -H "Content-Type: text/html" http://localhost:8080/note/{uuid} -d "New text of note"
```
**Note:** replace {uuid} to concrete UUID

#### Delete note by UUID
```shell
curl -X DELETE -H "Content-Type: text/html" http://localhost:8080/note/{uuid}
```
**Note:** replace {uuid} to concrete UUID

---------------------------
### Access to database via psql
1. Run Postgres container how it described above
2. Run in terminal `docker exec -it notes_postgres psql -h notes_postgres -U notes`
3. Enter password from secret file
4. Type `SELECT * FROM notes;`
