# 预构建模式：先在本机执行 mvn clean package -DskipTests，再 docker compose up -d --build

FROM eclipse-temurin:21-jdk AS provider

RUN groupadd -r app && useradd -r -g app -m -d /home/app app && \
    mkdir -p /home/app/.dubbo && chown -R app:app /home/app
WORKDIR /app
COPY easy-pay-provider/target/easy-pay-provider.jar app.jar
RUN chown app:app app.jar
USER app

EXPOSE 20880
ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk AS task

RUN groupadd -r app && useradd -r -g app -m -d /home/app app && \
    mkdir -p /home/app/.dubbo && chown -R app:app /home/app
WORKDIR /app
COPY easy-pay-task/target/easy-pay-task.jar app.jar
RUN chown app:app app.jar
USER app

ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
