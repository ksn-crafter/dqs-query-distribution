package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.publisher.SubQueryGeneratedPublisher;
import com.dqs.eventdrivensearch.queryDistribution.service.DQSTaskService;
import com.dqs.eventdrivensearch.queryDistribution.service.IndexPartitionService;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QueryReceivedConsumer {

    @Value("${incoming-sub-queries-partitions}")
    private int numberOfPartitions;

    @Autowired
    private QueryDescriptionService queryDescriptionService;

    @Autowired
    private IndexPartitionService indexPartitionService;

    @Autowired
    private SubQueryGeneratedPublisher subQueryPublisher;

    @Autowired
    private DQSTaskService dqsTaskService;

    @Value("${use_dqs_task:false}")
    private boolean useDqsTask;

    public void receive(QueryReceived event) {
        System.out.println(event);
        QueryDescription queryDescription = new QueryDescription(event);
        queryDescriptionService.createQueryDescription(queryDescription);

        if (useDqsTask) {
            dqsTaskService.generateDQSTask(queryDescription);
        } else {
            generateSubQuery(event.queryId(), new QueryFilter(event.tenantId(), event.beginYear(), event.endYear()));
        }
    }

    public void generateSubQuery(String queryId, QueryFilter filter) {
        List<List<String>> indexPartitions = indexPartitionService.findIndexPartitions(filter);

        System.out.printf("partition size " + indexPartitions.size());

        for (int i = 0; i < indexPartitions.size(); i++) {
            SubQueryGenerated subQueryGenerated = new SubQueryGenerated(UUID.randomUUID().toString(), queryId, filter.tenant(), indexPartitions.get(i), indexPartitions.size());
            subQueryPublisher.publish(subQueryGenerated, i % numberOfPartitions);
        }
    }
}
