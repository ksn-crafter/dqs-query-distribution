package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.config.KafkaTestProducerConfig;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(KafkaTestProducerConfig.class)
@EmbeddedKafka(partitions = 1, topics = "incoming_queries_jpmc")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryReceivedConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, QueryReceived> kafkaTemplate;

    @Autowired
    private QueryDescriptionService service;

    @AfterAll
    void tearDown() {
        kafkaTemplate.destroy();
    }

    @Test
    void consumeQueryReceived() throws InterruptedException {
        UUID queryId = UUID.randomUUID();
        QueryReceived event = new QueryReceived(queryId.toString(), "jpmc", "HISTORICAL", 2001, 2002, LocalDateTime.of(2024, 5, 24, 10, 15, 30));
        kafkaTemplate.send("incoming_queries_jpmc", "jpmc", event);

        Thread.sleep(2000);

        QueryDescription queryDescription = service.getQueryDescriptionById(queryId);
        assertNotNull(queryDescription);
        assertEquals(event.tenantId(), queryDescription.tenantId());
        assertEquals(event.term(), queryDescription.term());
        assertEquals(event.yearStart(), queryDescription.yearStart());
        assertEquals(event.yearEnd(), queryDescription.yearEnd());
        assertEquals(event.creationTime(), queryDescription.creationTime());
        assertEquals(QueryStatus.Acknowledged, queryDescription.status());
    }
}
