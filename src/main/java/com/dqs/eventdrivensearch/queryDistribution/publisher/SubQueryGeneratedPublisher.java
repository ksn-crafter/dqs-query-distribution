package com.dqs.eventdrivensearch.queryDistribution.publisher;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubQueryGeneratedPublisher {

    @Autowired
    private KafkaTemplate<String, SubQueryGenerated> kafkaTemplate;

    private static final String topicPrefix = "incoming_sub_queries_";

    public void publish(SubQueryGenerated event, int partition) {
        System.out.println(event);
        kafkaTemplate.send(topicNameFor(event.tenant()), partition, event.subQueryId(),  event);
    }

    static String topicNameFor(String tenant) {
        return topicPrefix + tenant.toLowerCase();
    }
}