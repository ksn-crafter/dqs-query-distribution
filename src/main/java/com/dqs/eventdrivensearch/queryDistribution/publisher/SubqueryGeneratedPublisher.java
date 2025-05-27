package com.dqs.eventdrivensearch.queryDistribution.publisher;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubqueryGeneratedPublisher {

    private final KafkaTemplate<String, SubQueryGenerated> kafkaTemplate;

    private static final String topicPrefix = "subqueries_";

    public SubqueryGeneratedPublisher(KafkaTemplate<String, SubQueryGenerated> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(SubQueryGenerated event) {
        kafkaTemplate.send(topicNameFor(event.tenantId()), event);
    }

    static String topicNameFor(String tenant) {
        return topicPrefix + tenant.toLowerCase();
    }
}