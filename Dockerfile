# 多模块共用：通过 ARG CMD 指定构建 cmd/merchant | agent | payment | bot | job
ARG CMD=merchant
FROM golang:1.21-alpine AS builder
WORKDIR /src
ARG CMD
RUN apk add --no-cache git ca-certificates
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 go build -o /app ./cmd/${CMD}

FROM alpine:3.19
RUN apk add --no-cache ca-certificates
COPY --from=builder /app /app
ENTRYPOINT ["/app"]
