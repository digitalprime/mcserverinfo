FROM clojure:lein AS BUILD
COPY . /code
WORKDIR /code
RUN lein ring uberjar

FROM openjdk:11-jre-slim
EXPOSE 3000
COPY --from=BUILD /code/target/*-standalone.jar app.jar
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
