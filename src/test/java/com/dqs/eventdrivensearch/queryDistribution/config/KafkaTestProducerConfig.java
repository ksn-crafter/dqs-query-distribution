package com.dqs.eventdrivensearch.queryDistribution.config;

import java.util.HashMap;
import java.util.Map;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
public class KafkaTestProducerConfig {


    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Bean
    public ProducerFactory<String, QueryReceived> testProducerFactory() {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.dqs.eventdrivensearch.queryDistribution.event");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name = "testKafkaTemplate")
    public KafkaTemplate<String, QueryReceived> kafkaTemplate(ProducerFactory<String, QueryReceived> queryReceivedProducerFactory) {
        return new KafkaTemplate<>(queryReceivedProducerFactory);
    }
}
