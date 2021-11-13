FROM maven:3.8.1-jdk-11-slim
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src /app/src
RUN mvn clean package -DskipTests
CMD  "mvn" "test" && "java" "-jar" "./target/register-0.0.1-SNAPSHOT.jar"
