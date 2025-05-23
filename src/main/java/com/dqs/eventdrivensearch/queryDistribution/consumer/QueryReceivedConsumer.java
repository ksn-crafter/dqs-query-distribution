package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QueryReceivedConsumer {

    @Autowired
    private QueryDescriptionService service;

    @KafkaListener(topicPattern="incoming_queries_.*", groupId = "${spring.application.name}")
    public void receive(QueryReceived event) {
        QueryDescription queryDescription = new QueryDescription(event);
        service.createQueryDescription(queryDescription);
    }
}
