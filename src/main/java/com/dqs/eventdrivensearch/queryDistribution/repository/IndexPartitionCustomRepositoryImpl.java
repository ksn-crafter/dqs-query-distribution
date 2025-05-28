package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartitionCustomRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IndexPartitionCustomRepositoryImpl implements IndexPartitionCustomRepository {

    @Value("${spring.data.mongodb.index-partitions.group-by}")
    private int batchSize;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<List<String>> findIndexPartitions(QueryFilter filter) {
        // TODO: 1 partition -> 1 index file -> 1 year of data (MAX)

        MatchOperation match = Aggregation.match(Criteria.where("tenantId").is(filter.tenantId())
                .and("yearStart").gte(filter.yearStart())
                .and("yearEnd").lte(filter.yearEnd()));


        ProjectionOperation project = Aggregation.project("filepath");


        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "indexPartition", Document.class);


        List<String> allFilePaths = results.getMappedResults().stream()
                .map(doc -> doc.getString("filepath"))
                .collect(Collectors.toList());


        List<List<String>> filePaths = new ArrayList<>();
        for (int i = 0; i < allFilePaths.size(); i += batchSize) {
            filePaths.add(allFilePaths.subList(i, Math.min(i + batchSize, allFilePaths.size())));
        }

        return filePaths;
    }

}