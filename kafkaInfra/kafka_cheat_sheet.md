# Kafka Cheat sheet 
Following commands are executed in kafka server, where kafka scripts such as kafka-topics.sh are located.

1. View the topics 
```bash
$> kafka-topics.sh --list --bootstrap-server localhost:9092  
```

2. Create topic
```bash
$> kafka-topics.sh  --create --bootstrap-server localhost:9092 \
  --replication-factor 3 \ 
  --partitions 4 \
  --topic test-topic
```

3. Monitor incoming message to broker 
```bash
$> kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic 
```

