# quarkus-camel

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

```mvn command
mvn quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

```mvn command
mvn package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

``` maven command
mvn package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```maven command
mvn package -Dnative -Dquarkus.native.container-build=true
```

```in application.properties
# Construcción nativa dentro de contenedor
quarkus.native.container-build=true

# Runtime de contenedor (elige lo que tengas)
quarkus.native.container-runtime=docker

# Imagen builder Mandrel (elige versión y Java 17/21 acorde a tu proyecto)
quarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21
```

You can then execute your native executable with: `./target/quarkus-camel-1.0.0-SNAPSHOT-runner`


```construcción de imágenes nativas:

-- contrucción con imagen nativa minima UBI/RHEL
mvn -Pnative package
docker build -f .\docker\Dockerfile.native -t carlos89/quarkus-native .
docker run --rm -d -p 8081:8081 --name quarkus-native carlos89/quarkus-native:latest

-- contrucción con imagen nativa micro UBI/RHEL
mvn -Pnative package
docker build -f .\docker\Dockerfile.native-micro -t carlos89/quarkus-native-micro .
docker run --rm -d -p 8083:8081 --name quarkus-native-micro carlos89/quarkus-native-micro:latest

-- contrucción con imagen nativa micro lite UBI/RHEL
mvn -Pnative package
docker build -f .\docker\Dockerfile.native-micro-lite -t carlos89/quarkus-native-micro-lite .
docker run --rm -d -p 8084:8081 --name quarkus-native-micro-lite carlos89/quarkus-native-micro-lite:latest

-- construcción de imagen con jvm
docker build -f .\docker\Dockerfile -t carlos89/quarkus-jvm .
docker run --rm -d -p 8082:8081 --name quarkus-jvm carlos89/quarkus-jvm:latest

## Propiedades para opentelemetry
quarkus.otel.traces.exporter (by default cdi)
quarkus.otel.metrics.exporter (by default cdi)
quarkus.otel.logs.exporter (by default cdi)
```

comando apra ejecutar k6 con docker: docker run --rm -i -v "C:\Users\gcall\Documents\carlos\workspace\quarkus-camel-rest\scripts":/scripts -w /scripts --add-host=host.docker.internal:host-gateway grafana/k6:latest run --vus 20 --duration 1m --out json=resultado.json script.js
