package com.dqs.eventdrivensearch.queryDistribution.service;

import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.repository.DQSSplitMetadataRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.DQSSplitMetadata;
import com.dqs.eventdrivensearch.queryDistribution.repository.DQSTaskRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.DQSTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DQSTaskService {

    @Autowired
    private DQSSplitMetadataRepository splitMetadataRepository;

    @Autowired
    private DQSTaskRepository taskRepository;

    @Value("${worker_count_for_dqs_task}")
    private int numberOfWorkers;

    @Transactional
    public void generateDQSTask(QueryDescription queryDescription) {

        List<DQSSplitMetadata> splitMetadataList = splitMetadataRepository.findByTenantId(queryDescription.tenantId());
        if (!splitMetadataList.isEmpty()) {
            long workerId = 0;
            List<DQSTask> tasks = new java.util.ArrayList<>();

            for (DQSSplitMetadata splitMetadata : splitMetadataList) {

                int assignedWorkerId = (int) (workerId % numberOfWorkers);

                DQSTask task = new DQSTask(
                        null,
                        queryDescription.queryId(),
                        splitMetadata.id(),
                        "Pending",
                        assignedWorkerId,
                        System.currentTimeMillis(),
                        null,
                        queryDescription.term(),
                        splitMetadata.splitId(),
                        splitMetadata.footerOffsetsStart(),
                        splitMetadata.footerOffsetsEnd()
                );

                tasks.add(task);

                workerId++;

//                if (tasks.size() >= numberOfWorkers) {
//                    taskRepository.saveAll(tasks);
//                    tasks.clear();
//                    workerId = 0;
//                }
            }

            if (!tasks.isEmpty()) {
                taskRepository.saveAll(tasks);
                tasks.clear();
            }
        }
    }
}
