FROM navikt/java:12
COPY build/libs/api.jar /app/app.jar
ENV PORT=8080
EXPOSE $PORT
