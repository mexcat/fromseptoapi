# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV JAVA_OPTS=""
ENV APP_LOCAL_CONFIG=/app/config/local.json
COPY --from=build /workspace/target/json-to-api-0.1.0.jar /app/app.jar
EXPOSE 3000
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
