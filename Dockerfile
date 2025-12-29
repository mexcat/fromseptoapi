# Build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV APP_LOCAL_CONFIG=/app/config/local.json
COPY config/local.json /app/config/local.json
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 3000
ENTRYPOINT ["sh","-c","mkdir -p /app/logs && java -jar /app/app.jar"]
