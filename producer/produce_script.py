from typing import Any, Callable
import argparse
from kafka import KafkaProducer
import orjson # x10 faster than json
from datetime import datetime
from time import sleep

TOPIC_NAME = "test-topic"

PRODUCER = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
)
def orjson_dumps(k: Any, default: Callable[[Any], Any] | None = None, option: int | None = None):
    return orjson.dumps(k, default=default, option=option).decode("utf-8")
def send_message(user_id, data):
    partition_key = str(user_id).encode("utf-8")
    PRODUCER.send(TOPIC_NAME, key=partition_key, value=orjson_dumps(data).encode("utf-8"))
    PRODUCER.flush(timeout=3)


def produce(interval, cnt):
    for i in range(cnt):
        to_send_data = {"at": datetime.now().isoformat(), "msg": "send_brandon"}
        send_message(user_id=i, data=to_send_data)
        sleep(interval)


# Function to serialize the message to JSON

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--count",  # Name of the argument
        required=True,  # Make it mandatory
    )
    parser.add_argument(
        "--interval",  # Name of the argument
        required=True,  # Make it mandatory
    )

    args = parser.parse_args()
    produce_cnt = int(args.count)
    interval = float(args.interval)
    produce(interval, produce_cnt)