FROM openjdk:17-oracle

WORKDIR /app

COPY target/hotel-data-merge-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]