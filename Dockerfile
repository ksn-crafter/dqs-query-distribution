FROM gcr.io/distroless/java17-debian11:nonroot-arm64
WORKDIR /app
COPY target/queryDistribution-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]