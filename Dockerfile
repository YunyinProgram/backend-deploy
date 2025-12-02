FROM mcr.microsoft.com/openjdk/jdk:25-ubuntu
COPY target/mindora-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
