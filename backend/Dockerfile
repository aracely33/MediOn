FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

WORKDIR /app

COPY ./target/medtech-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]

