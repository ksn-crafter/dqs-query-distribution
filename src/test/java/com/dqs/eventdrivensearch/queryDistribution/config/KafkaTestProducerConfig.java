package com.dqs.eventdrivensearch.queryDistribution.config;

import java.util.HashMap;
import java.util.Map;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

//@TestConfiguration
public class KafkaTestProducerConfig {

//    @Bean
    public ProducerFactory<String, QueryReceived> testProducerFactory(EmbeddedKafkaBroker embeddedKafka) {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafka));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

//    @Bean
    public KafkaTemplate<String, QueryReceived> kafkaTemplate(ProducerFactory<String, QueryReceived> queryReceivedProducerFactory) {
        return new KafkaTemplate<>(queryReceivedProducerFactory);
    }
}
