[![Codefresh build status]( https://g.codefresh.io/api/badges/build?repoOwner=monitorjbl&repoName=user-service&branch=master&pipelineName=user-service&accountName=monitorjbl&type=cf-2)]( https://g.codefresh.io/repositories/monitorjbl/user-service/builds?filter=trigger:build;branch:master;service:59ab851dc3e3230001cecf79~user-service)

# User Service 

A sample app with a REST API and simple UI using:

* Maven
* Spring Boot
* Spring Data
* Spock
* Vue.js
* Docker

## Building and running  with Docker

```
git clone https://github.com/monitorjbl/user-service.git
cd user-service
docker build -t="user-service" .
docker run -it -p 8080:80 -e ENCRYPTION_KEY=asdfasdfasdfasdf user-service
```

## Development

To build this app locally, you will need Maven 3.5+ and Java8+.

```
git clone https://github.com/monitorjbl/user-service.git
cd user-service
mvn clean package
```

The backend is run separately from the UI, so you will need to start them up independently. Open your IDE and start the `io.monitorjbl.Main` class. The server will be available on port 8080. You can view the Swagger Docs at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

The UI can be started with the following CLI commands, run from the root of the project (you must have run `package` on the ui project at least once to fetch this binary):

```
./ui/node/npm run dev
```

The UI will be available at [http://localhost:3000/](http://localhost:3000/). Any changes made to the UI code will trigger an automatic reload.