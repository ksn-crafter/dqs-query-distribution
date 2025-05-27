package com.dqs.eventdrivensearch.queryDistribution.consumer;

//import com.dqs.eventdrivensearch.queryDistribution.config.KafkaProducerConfiguration;
import com.dqs.eventdrivensearch.queryDistribution.config.KafkaTestProducerConfig;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryStatus;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import com.dqs.eventdrivensearch.queryDistribution.service.QueryDescriptionService;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = "incoming_queries_jpmc")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({KafkaTestProducerConfig.class})
public class QueryReceivedConsumerIntegrationTest {

    @Autowired
    @Qualifier("testKafkaTemplate")
    private KafkaTemplate<String, QueryReceived> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

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
        kafkaTemplate.send("incoming_queries_jpmc", event);


        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            QueryDescription queryDescription = service.getQueryDescriptionById(queryId);
            assertNotNull(queryDescription);
            assertEquals(event.tenantId(), queryDescription.tenantId());
            assertEquals(event.term(), queryDescription.term());
            assertEquals(event.yearStart(), queryDescription.yearStart());
            assertEquals(event.yearEnd(), queryDescription.yearEnd());
            assertEquals(event.creationTime(), queryDescription.creationTime());
            assertEquals(QueryStatus.Acknowledged, queryDescription.status());
        });


    }
}
