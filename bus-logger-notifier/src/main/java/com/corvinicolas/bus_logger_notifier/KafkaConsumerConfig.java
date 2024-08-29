package com.corvinicolas.bus_logger_notifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.SimpleKafkaHeaderMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.HeaderMapper;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
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

    @Bean
    public RecordFilterStrategy<String, Object> recordFilterStrategy(){
        return consumerRecord -> {
            long secondsToDiscard = 300;
            long epochMillis = new BigInteger(consumerRecord.headers().lastHeader("produced_timestamp").value()).longValue();
            Instant producedAt = Instant.ofEpochMilli(epochMillis);
            LOGGER.info("Produced:{}", producedAt);
            if(Instant.now().isAfter(producedAt.plusSeconds(secondsToDiscard))){
                LOGGER.info("Discarding event {} since it was produced {} seconds ago", consumerRecord, secondsToDiscard);
                return true;
            }
            return false;
        };
    }
}
