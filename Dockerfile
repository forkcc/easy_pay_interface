# ============================
# 第一阶段：Maven 构建
# ============================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY easy-pay-api/pom.xml easy-pay-api/
COPY easy-pay-provider/pom.xml easy-pay-provider/
COPY easy-pay-task/pom.xml easy-pay-task/
RUN mvn dependency:go-offline -B -q 2>/dev/null || true

COPY easy-pay-api/ easy-pay-api/
COPY easy-pay-provider/ easy-pay-provider/
COPY easy-pay-task/ easy-pay-task/
RUN mvn clean package -DskipTests -B -q

# ============================
# 第二阶段：Provider 运行环境
# ============================
FROM eclipse-temurin:21-jre-alpine AS provider
LABEL maintainer="easypay" description="Easy Pay Dubbo Provider"

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=builder /build/easy-pay-provider/target/easy-pay-provider.jar app.jar
RUN chown -R app:app /app
USER app

EXPOSE 20880
ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

# ============================
# 第三阶段：Task 运行环境
# ============================
FROM eclipse-temurin:21-jre-alpine AS task
LABEL maintainer="easypay" description="Easy Pay Scheduled Task Runner"

RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=builder /build/easy-pay-task/target/easy-pay-task.jar app.jar
RUN chown -R app:app /app
USER app

ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
