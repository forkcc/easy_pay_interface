# 多模块共用：通过 ARG CMD 指定构建 cmd/merchant | agent | payment | manager | bot | job
ARG CMD=merchant
FROM golang:1.24-alpine AS builder
WORKDIR /src
ARG CMD
RUN apk add --no-cache git ca-certificates
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go mod tidy && CGO_ENABLED=0 go build -o /app-${CMD} ./cmd/${CMD}

FROM alpine:3.19
ARG CMD=merchant
ENV SVC=${CMD}
RUN apk add --no-cache ca-certificates
COPY --from=builder /app-${CMD} /app-${CMD}
ENTRYPOINT ["/bin/sh", "-c", "exec /app-${SVC}"]
