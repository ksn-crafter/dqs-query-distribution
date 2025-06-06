package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.publisher.SubQueryGeneratedPublisher;
import com.dqs.eventdrivensearch.queryDistribution.service.IndexPartitionService;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SubQueryGeneratedPublisher subQueryPublisher;

    public void receive(QueryReceived event) {
        System.out.println(event);
        QueryDescription queryDescription = new QueryDescription(event);
        queryDescriptionService.createQueryDescription(queryDescription);
        generateSubQuery(event.queryId(), new QueryFilter(event.tenantId(), event.beginYear(), event.endYear()));
    }

    public void generateSubQuery(String queryId, QueryFilter filter) {
        List<List<String>> partitions = indexPartitionService.findIndexPartitions(filter);

        for(List<String> partition : partitions) {
            SubQueryGenerated subQueryGenerated = new SubQueryGenerated(UUID.randomUUID().toString(), queryId, filter.tenant(), partition, partitions.size());
            subQueryPublisher.publish(subQueryGenerated);
        }
    }
}
