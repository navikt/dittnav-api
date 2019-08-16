FROM navikt/java:12
COPY build/libs/proxy.jar /app/app.jar
EXPOSE 8090
