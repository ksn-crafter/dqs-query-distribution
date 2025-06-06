package com.dqs.eventdrivensearch.queryDistribution.publisher.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EvenRoundRobinPartitioner implements Partitioner {

    private final AtomicInteger counter = new AtomicInteger(0);
    private static final int TOTAL_PARTITIONS = 125;

    @Override
    public void configure(Map<String, ?> configs) {
        // No-op
    }

    @Override
    public int partition(String topic,
                         Object key,
                         byte[] keyBytes,
                         Object value,
                         byte[] valueBytes,
                         Cluster cluster) {

        int partitionCount = cluster.partitionCountForTopic(topic);
        if (partitionCount != TOTAL_PARTITIONS) {
            throw new IllegalArgumentException("Expected 125 partitions, but got " + partitionCount);
        }

        int partition = counter.getAndIncrement() % TOTAL_PARTITIONS;
        System.out.println("partition is " + partition);

        return partition;
    }

    @Override
    public void close() {
        // No-op
    }
}