# ============================
# 第一阶段：Maven 构建
# 基于已有的 eclipse-temurin:21-jdk，手动安装 Maven
# ============================
FROM eclipse-temurin:21-jdk AS builder

ARG MAVEN_VERSION=3.9.9
RUN apt-get update && apt-get install -y --no-install-recommends curl && \
    curl -fsSL https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    | tar xz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn && \
    apt-get purge -y curl && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*

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
FROM eclipse-temurin:21-jdk AS provider

RUN groupadd -r app && useradd -r -g app app
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
FROM eclipse-temurin:21-jdk AS task

RUN groupadd -r app && useradd -r -g app app
WORKDIR /app

COPY --from=builder /build/easy-pay-task/target/easy-pay-task.jar app.jar
RUN chown -R app:app /app
USER app

ENTRYPOINT ["java", \
  "-XX:+UseZGC", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
