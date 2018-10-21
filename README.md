# vk-gallery
A Spring 5 + REST API + Angular 6 Web application

A Web-Application that provides a convenient way of viewing pictures from communities of a popular social network **VK.com**

## Core Technologies

### Backend:
* Java 8
* Spring 5
* Spring Boot 2
* Spring Data JPA
* Spring Security OAuth
* Gradle
* H2 Embedded Database

### Frontend:
* TypeScript
* Angular 2
* Angular CLI
* Bootstrap

# How to build and run
## Run in Development mode
1. Go to the project root direcotry
2. From the command line run: `./gradlew api:bootRun`
3. Go to client subdirectory
4. From the command line run: `npm install`
5. From the command line run: `npm run start`

The backend will be running at 9000 port. The frontend - on the 4200. The browser will automatically open to browse http://localhost:4200

Application utilizes Swagger UI to generate nice API documantation. Go to http://localhost:9000/swagger-ui.html to open it
