FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY ./spring-boot-url-shortener/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]