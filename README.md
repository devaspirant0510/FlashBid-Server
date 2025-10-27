# FlashBid-Server 
# Redis 설정
### redis 서버 접속
```shell
redis-cli
```
### keyspace 설정
```shell
config set notify-keyspace-events Ex
```
# Kafka 설정 (Redpanda)
```shell
docker run -d --name redpanda -p 9092:9092 redpandadata/redpanda
```