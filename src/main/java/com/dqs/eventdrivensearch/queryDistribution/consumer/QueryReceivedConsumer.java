package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.publisher.SubqueryGeneratedPublisher;
import com.dqs.eventdrivensearch.queryDistribution.service.IndexPartitionService;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QueryReceivedConsumer {

    @Autowired
    private QueryDescriptionService queryDescriptionService;

    @Autowired
    private IndexPartitionService indexPartitionService;

    @Autowired
    private SubqueryGeneratedPublisher subqueryPublisher;

    @KafkaListener(topicPattern="incoming_queries_.*", groupId = "${spring.application.name}")
    public void receive(QueryReceived event) {
        QueryDescription queryDescription = new QueryDescription(event);
        queryDescriptionService.createQueryDescription(queryDescription);

        generateSubQuery(event.queryId(), new QueryFilter(event.tenantId(), event.yearStart(), event.yearEnd()));
    }

    public void generateSubQuery(String queryId, QueryFilter filter) {
        List<List<String>> partitions = indexPartitionService.findIndexPartitions(filter);

        for(List<String> partition : partitions) {
            String subqueryId = UUID.randomUUID().toString();
            SubQueryGenerated subQueryGenerated = new SubQueryGenerated(subqueryId, queryId, filter.tenantId(), partition, partition.size());
            subqueryPublisher.publish(subQueryGenerated);
        }
    }
}
