FROM navikt/java:12
COPY build/libs/api.jar /app/app.jar
EXPOSE 8090
