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

O si no tienes instalado GraalVM, puedes construir de manera nativa mediante contenedores:

```maven command
mvn -Pnative package \
  -Dquarkus.native.container-build=true \
  -Dquarkus.native.container-runtime=docker \
  -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21
```

Otra opción es  agregar los atributos container-build, container-runtime y builder-image en el archivo application.properties
```in application.properties
# Construcción nativa dentro de contenedor
quarkus.native.container-build=true

# Runtime de contenedor (elige lo que tengas)
quarkus.native.container-runtime=docker

# Imagen builder Mandrel (elige versión y Java 17/21 acorde a tu proyecto)
quarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21
```

Puedes ejecutar la imagen nativa de la siguiente forma: `./target/quarkus-camel-1.0.0-SNAPSHOT-runner`

## Construcción y ejecución de imágenes nativas (en docker desktop)
Construcción y ejecución de imágenes nativas UBI/RHEL

```construcción y ejecución de imágenes nativas UBI/RHEL:
# contrucción
mvn -Pnative package
docker build -f .\docker\Dockerfile.native -t carlos89/quarkus-native .

# ejecución
docker run --rm -d -p 8081:8081 --name quarkus-native carlos89/quarkus-native:latest
```



Construcción y ejecución de imágenes nativas micro UBI/RHEL
```Construcción y ejecución de imágenes nativas micro UBI/RHEL
# contrucción
mvn -Pnative package
docker build -f .\docker\Dockerfile.native-micro -t carlos89/quarkus-native-micro .

# ejecución
docker run --rm -d -p 8083:8081 --name quarkus-native-micro carlos89/quarkus-native-micro:latest
```



Construcción y ejecución de imágenes nativas micro lite UBI/RHEL
```Construcción y ejecución de imágenes nativas micro lite UBI/RHEL
# contrucción
mvn -Pnative package
docker build -f .\docker\Dockerfile.native-micro-lite -t carlos89/quarkus-native-micro-lite .

# ejecución
docker run --rm -d -p 8084:8081 --name quarkus-native-micro-lite carlos89/quarkus-native-micro-lite:latest
```



Construcción y ejecución de imagen jvm UBI/RHEL
```Construcción y ejecución de imagen jvm UBI/RHEL
# construcción con imagen jvm UBI/RHEL en docker desktop
docker build -f .\docker\Dockerfile -t carlos89/quarkus-jvm .

# ejecución de imagen jvm UBI/RHEL en docker desktop
docker run --rm -d -p 8082:8081 --name quarkus-jvm carlos89/quarkus-jvm:latest
```


# Propiedades para opentelemetry
quarkus.otel.traces.exporter (by default cdi)
quarkus.otel.metrics.exporter (by default cdi)
quarkus.otel.logs.exporter (by default cdi)


## Ejecución k6 con docker: 
```Ejecucar k6 con docker:
docker run --rm -i -v "C:\Users\gcall\Documents\carlos\workspace\quarkus-camel-rest\scripts":/scripts -w /scripts --add-host=host.docker.internal:host-gateway grafana/k6:latest run --vus 20 --duration 1m --out json=resultado.json script.js
