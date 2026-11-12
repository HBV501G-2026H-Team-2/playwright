FROM eclipse-temurin:25-jdk AS build

WORKDIR /build
COPY . .

RUN chmod +x mvnw && ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

COPY --from=build /build/target/playwright-0.1.0.jar /app/app.jar

RUN groupadd -g 10001 playwright \
    && useradd -u 10001 -g playwright -M playwright

USER playwright

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
