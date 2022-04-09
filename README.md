# Plutus

This [Quarkus](https://quarkus.io/) project (will hopefully) track(s) your expenses and accounts for
you, with minimal effort from yourself.

For now, here are some basic usage instructions. You can run Plutus in dev mode (which enables live 
coding) using `./mvnw compile quarkus:dev` or `quarkus dev` if you installed the Quarkus CLI. You
can build a native executable as follows:

```shell script
$ sdk env install  # only necessary initially
$ sdk env
$ quarkus build --native  # or `./mvnw package -Pnative` without the Quarkus CLI
$ ./target/plutus-0.1.0-runner
```

Alternatively, you can build an executable JAR:

```shell script
$ quarkus build -Dquarkus.package.type=uber-jar  # or ./mvnw package -D...
```
