FROM metsakuur/ufacemangosteen:1.0 AS builder
LABEL authors="DAVID ILYONG CHUN"

COPY src/main/resources/logback-spring.xml .
COPY src/main/resources/application.yml .
COPY build/libs/mangosteendemo-0.0.1-SNAPSHOT.jar ./app.jar

COPY models/depthface48x48.yml .
COPY models/fastvit_ma36_20241210.onnx .

FROM metsakuur/ufacemangosteen:1.0
RUN mkdir -p /models
COPY --from=builder app.jar /app.jar
COPY --from=builder logback-spring.xml /logback-spring.yml
COPY --from=builder application.yml /application.yml

COPY --from=builder depthface48x48.yml /models/depthface48x48.yml
COPY --from=builder fastvit_ma36_20241210.onnx /models/fastvit_ma36_20241210.onnx


EXPOSE 8082

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar", "--illegal-access=warn", "-Djava.library.path=/usr/lib", "-Djava.net.preferIPv4Stack=true"]