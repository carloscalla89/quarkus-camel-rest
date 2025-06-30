#Es necesario aplicar un mvn package antes de ejecutar el docker build

FROM registry.access.redhat.com/ubi8/openjdk-21:1.20



ENV LANGUAGE='en_US:en'
 
# Copiar las dependencias y el código de la aplicación
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

# Descarga el agente de OpenTelemetry
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.7.0/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar

EXPOSE 8080
USER 185
#ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

 
# Eliminar CMD y usar ENTRYPOINT para iniciar la aplicación correctamente
ENTRYPOINT ["java","-javaagent:/otel/opentelemetry-javaagent.jar", "-jar", "/deployments/quarkus-run.jar"]