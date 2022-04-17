This directory is generated automatically using

```
java -agentlib:native-image-agent=config-output-dir=\
src/main/resources/META-INF/native-image \
  -jar target/plutus-0.1.0-runner.jar \
  src/test/resources/input.940
```

and contains configuration for GraalVM native builds.
