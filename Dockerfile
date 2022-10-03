FROM navikt/java:17
ENV APPD_ENABLED=true
COPY build/libs/api-all.jar /app/app.jar
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/oom-dump.hprof
ENV PORT=8080
ENV APPDYNAMICS_AGENT_BASE_DIR=/tmp/appdynamics
EXPOSE $PORT
