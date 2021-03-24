# optimiser project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `optimiser-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/optimiser-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/optimiser-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JAX-RS

<p>A Hello World RESTEasy resource</p>

Guide: https://quarkus.io/guides/rest-json

# RESTEasy JSON serialisation using Jackson

<p>This example demonstrate RESTEasy JSON serialisation by letting you list, add and remove quark types from a list.</p>
<p><b>Quarked!</b></p>

Guide: https://quarkus.io/guides/rest-json

# Spring Web

<p>A Hello World Spring Web Controller</p>

Guide: https://quarkus.io/guides/spring-web

# Default application port: 8080
quarkus.http.port to change it

http://localhost:8080/

# Quarkus info

 - https://quarkus.io/vision/continuum
 
 # config de ssh et git
 - dans le fichier C:\Users\xxx\.ssh ou le ~/.ssh

Pour gérer plusieurs ssh keys pour github il faut y mettre un fichier nommé config et qui contient:

```
Host github_fred
  Hostname github.com
  IdentityFile ~/.ssh/github_rsa
  #IdentitiesOnly yes
  User git
  
Host github_mch
  Hostname github.com
  IdentityFile ~/ssh/github_mch_planner
  #IdentitiesOnly yes
  User git  
```

 - il y a aussi les fichiers gitconfig a paramétrer, ils sont sur 3 niveaux:
Local: .git/config in the current repository
Global: .gitconfig in the user's home directory
System: gitconfig in the system configuration directory (e.g. /etc on Linux)
   n'a pas l'air de compter vraiment pour cet aspect multi user
