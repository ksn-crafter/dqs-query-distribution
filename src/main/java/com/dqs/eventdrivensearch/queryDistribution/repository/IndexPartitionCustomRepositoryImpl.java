package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartitionCustomRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.utility.CustomAggregationOperation;
import jakarta.annotation.Resource;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IndexPartitionCustomRepositoryImpl implements IndexPartitionCustomRepository {

    @Value("${spring.data.mongodb.index-partitions.group-by}")
    private int batchSize;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<List<String>> findIndexPartitions(QueryFilter filter) {
        // TODO: 1 partition -> 1 index file -> 1 year of data (MAX)

        MatchOperation match = Aggregation.match(Criteria.where("tenantId").is(filter.tenantId())
                .and("yearStart").gte(filter.yearStart())
                .and("yearEnd").lte(filter.yearEnd()));

        // Add row numbers
        Document setWindowFieldsStage = new Document("$setWindowFields", new Document()
                .append("sortBy", new Document("_id", 1))
                .append("output", new Document("rowNumber", new Document("$documentNumber", new Document()))));

        // calculate batch number
        AddFieldsOperation addBatchNumber = Aggregation.addFields()
                .addField("batchNumber")
                .withValue(ConvertOperators.Convert.convertValue(
                        ArithmeticOperators.Floor.floorValueOf(
                                ArithmeticOperators.Divide.valueOf("rowNumber").divideBy(batchSize)
                        )
                ).to("int")).build();

        // group by batch number
        GroupOperation groupByBatch = Aggregation.group("batchNumber")
                .push("_id").as("partitionIds");

        // Final projection
        ProjectionOperation project = Aggregation.project()
                .and("partitionIds").as("partitionIds")
                .andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                new CustomAggregationOperation(setWindowFieldsStage),
                addBatchNumber,
                groupByBatch,
                project
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "indexPartition", Document.class);

        List<List<String>> partitions =  results.getMappedResults().stream()
                .map(doc -> (List<String>) doc.get("partitionIds"))
                .collect(Collectors.toList());

        return partitions;
    }

}

