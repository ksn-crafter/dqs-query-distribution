package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.event.SubQueryGenerated;
import com.dqs.eventdrivensearch.queryDistribution.publisher.SubQueryGeneratedPublisher;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 125, topics = {"incoming_sub_queries_jpmc", "incoming_queries_jpmc"})
public class SubQueryGeneratedPublisherIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    @Autowired
    private SubQueryGeneratedPublisher publisher;
    private Consumer<String, SubQueryGenerated> consumer;


    @BeforeAll
    void setupConsumer() {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, SubQueryGenerated> consumerFactory = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(SubQueryGenerated.class, false));

        consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "incoming_sub_queries_jpmc");
    }

    @AfterAll
    void tearDown() {
        consumer.close();
    }

    @Test
    void publishQueryReceived() {
        List<String> partitionIds = Arrays.asList("p-1", "p-2", "p-3", "p-4");
        SubQueryGenerated event = new SubQueryGenerated(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "jpmc", partitionIds, 4);

        publisher.publish(event);

        ConsumerRecord<String, SubQueryGenerated> record = KafkaTestUtils.getSingleRecord(consumer, "incoming_sub_queries_jpmc");

        SubQueryGenerated subQueryGenerated = record.value();
        assertEquals(event.subQueryId(), subQueryGenerated.subQueryId());
        assertEquals(event.tenant(), subQueryGenerated.tenant());
        assertEquals(event.queryId(), subQueryGenerated.queryId());
        assertEquals(event.filePaths(), subQueryGenerated.filePaths());
        assertEquals(event.totalSubQueries(), subQueryGenerated.totalSubQueries());
    }
}
