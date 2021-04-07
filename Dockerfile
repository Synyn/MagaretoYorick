FROM openjdk

COPY ./target/MagaretoYorick-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/source-code/MagaretoYorick.jar

ENTRYPOINT java -jar /opt/source-code/MagaretoYorick.jar