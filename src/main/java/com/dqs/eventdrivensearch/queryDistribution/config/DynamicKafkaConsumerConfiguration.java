package com.dqs.eventdrivensearch.queryDistribution.config;


import com.dqs.eventdrivensearch.queryDistribution.consumer.QueryReceivedConsumer;
import com.dqs.eventdrivensearch.queryDistribution.event.QueryReceived;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DynamicKafkaConsumerConfiguration {

    @Autowired
    private ConsumerFactory<String, QueryReceived> consumerFactory;

    @Autowired
    private QueryReceivedConsumer queryReceivedConsumer;

    private final List<KafkaMessageListenerContainer<String, QueryReceived>> containers = new ArrayList<>();

    public void registerConsumerForTopic(String tenantId) {
        String topic = "incoming_queries_" + tenantId;
        String groupId = "incoming_queries_group_" + tenantId;

        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setGroupId(groupId);
        containerProps.setMessageListener((MessageListener<String, QueryReceived>) record -> {
            QueryReceived event = record.value();
            queryReceivedConsumer.receive(event);
        });

        KafkaMessageListenerContainer<String, QueryReceived> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);

        container.start();
        containers.add(container);
    }

    @PreDestroy
    public void stopAllContainers() {
        containers.forEach(KafkaMessageListenerContainer::stop);
    }
}
