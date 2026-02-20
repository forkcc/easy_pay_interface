# 预构建模式：先在本机执行 mvn clean package -DskipTests，再 docker compose up -d --build
# 不依赖网络拉取 maven 镜像

FROM eclipse-temurin:21-jdk AS provider

RUN groupadd -r app && useradd -r -g app app
WORKDIR /app
COPY easy-pay-provider/target/easy-pay-provider.jar app.jar
RUN chown -R app:app /app
USER app

EXPOSE 20880
ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

FROM eclipse-temurin:21-jdk AS task

RUN groupadd -r app && useradd -r -g app app
WORKDIR /app
COPY easy-pay-task/target/easy-pay-task.jar app.jar
RUN chown -R app:app /app
USER app

ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
