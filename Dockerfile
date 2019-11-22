FROM navikt/java:13-appdynamics
COPY build/libs/api.jar /app/app.jar
ENV PORT=8080
EXPOSE $PORT
