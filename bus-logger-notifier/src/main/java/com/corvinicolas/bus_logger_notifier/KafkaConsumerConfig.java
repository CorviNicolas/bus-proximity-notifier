package com.corvinicolas.bus_logger_notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, BusProximityModel> busProximityModelConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        JsonDeserializer<BusProximityModel> jsonDeserializer = new JsonDeserializer<>(BusProximityModel.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
               jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BusProximityModel>
    busProximityModelListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BusProximityModel> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(busProximityModelConsumerFactory());
        return factory;
    }
}
