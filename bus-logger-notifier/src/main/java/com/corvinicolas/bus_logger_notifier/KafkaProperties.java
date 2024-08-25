package com.corvinicolas.bus_logger_notifier;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.kafka")
@Component
@Getter
@Setter
public class KafkaProperties {

    private String bootstrapServers;
    private Consumer consumer;


    @Getter
    @Setter
    public static class Consumer {
        private String groupId;
    }
}
