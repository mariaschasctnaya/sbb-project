#!/bin/bash
while ! exec 6<>/dev/tcp/train/8080; do
    echo "Trying to connect to train..."
    sleep 10
done

while ! exec 6<>/dev/tcp/kafka/9092; do
    echo "Trying to connect to kafka..."
    sleep 10
done

/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0