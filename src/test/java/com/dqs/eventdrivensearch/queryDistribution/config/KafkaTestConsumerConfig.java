package com.dqs.eventdrivensearch.queryDistribution.config;


import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class KafkaTestConsumerConfig {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Bean
    public ConsumerFactory<String, QueryReceived> testConsumerFactory() {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.dqs.eventdrivensearch.queryDistribution.event");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, QueryReceived> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, QueryReceived> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(testConsumerFactory());
        return factory;
    }
}
