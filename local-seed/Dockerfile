FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY local-seed/target/local-seed.jar local-seed.jar
ENTRYPOINT ["java", "-jar", "local-seed.jar"]
