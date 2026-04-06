FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /workspace

COPY . .

RUN mvn -f /workspace/pom.xml -pl local-seed -am clean install -DskipTests \
 && mvn -f /workspace/pom.xml -pl local-seed dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=/workspace/local-seed/target/lib

FROM eclipse-temurin:24-jre
WORKDIR /app

COPY --from=build /workspace/local-seed/target/local-seed.jar /app/local-seed.jar
COPY --from=build /workspace/local-seed/target/lib /app/lib

ENTRYPOINT ["java", "-cp", "/app/local-seed.jar:/app/lib/*", "com.breece.local.seed.SeedApp"]