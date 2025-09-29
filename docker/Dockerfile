#Es necesario aplicar un mvn package antes de ejecutar el docker build

FROM registry.access.redhat.com/ubi8/openjdk-21:1.20

ENV LANGUAGE='en_US:en'
 
# Copiar las dependencias y el código de la aplicación
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

#COPY opentelemetry/opentelemetry-javaagent.jar /opentelemetry/opentelemetry-javaagent.jar

EXPOSE 8082
USER 185
#ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

 
# Eliminar CMD y usar ENTRYPOINT para iniciar la aplicación correctamente
#ENTRYPOINT ["java","-javaagent:/otel/opentelemetry-javaagent.jar", "-jar", "/deployments/quarkus-run.jar"]
#ENTRYPOINT ["java", "-javaagent:/opentelemetry/opentelemetry-javaagent.jar","-jar", "/deployments/quarkus-run.jar"]
ENTRYPOINT ["java","-jar", "/deployments/quarkus-run.jar"]