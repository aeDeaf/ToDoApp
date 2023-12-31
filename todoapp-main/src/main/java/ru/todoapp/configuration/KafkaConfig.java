package ru.todoapp.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.todoapp.model.dto.PingRequestDTO;
import ru.todoapp.model.dto.RequestResultDTO;
import ru.todoapp.model.dto.RegisterRequestDTO;
import ru.todoapp.utils.KafkaTopics;
import ru.todoapp.utils.KafkaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration
 */
@Configuration
public class KafkaConfig {
    public static final String GROUP_ID = "todoapp-main";


    /**
     * Kafka URL
     */
    @Value("${todoapp.kafka.url}")
    private String kafkaUrl;

    /**
     * Kafka settings bean
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);

        return new KafkaAdmin(conf);
    }

    /**
     * Kafka topic creation for PingRequest
     *
     * @see ru.todoapp.model.dto.PingRequestDTO
     */
    @Bean
    public NewTopic pingTopic() {
        return new NewTopic(KafkaTopics.PING_TOPIC, 1, (short) 1);
    }

    /**
     * Kafka topic creation for sending request result
     *
     * @see ru.todoapp.model.dto.RequestResultDTO
     */
    @Bean
    public NewTopic requestResultTopic() {
        return new NewTopic(KafkaTopics.REQUEST_RESULT_TOPIC, 1, (short) 1);
    }

    /**
     * Kafka topic creation for registration
     *
     * @see RegisterRequestDTO
     */
    @Bean
    public NewTopic userRegistrationTopic() {
        return new NewTopic(KafkaTopics.REGISTRATION_TOPIC, 1, (short) 1);
    }

    /**
     * Consumer factory bean for sending PingRequestDTO
     */
    @Bean
    public ConsumerFactory<String, PingRequestDTO> pingRequestConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(PingRequestDTO.class, GROUP_ID, kafkaUrl);
    }

    /**
     * Listener factory bean for receiving PingRequestDTO
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PingRequestDTO> pingRequestContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PingRequestDTO>();
        factory.setConsumerFactory(pingRequestConsumerFactory());

        return factory;
    }

    /**
     * Producer factory bean for sending RequestResultDTO
     */
    @Bean
    public ProducerFactory<String, RequestResultDTO> requestResultDTOProducerFactory() {
        return KafkaUtils.getKafkaProducerFactory(kafkaUrl);
    }

    /**
     * KafkaTemplate bean for sending RequestResultDTO
     */
    @Bean
    public KafkaTemplate<String, RequestResultDTO> requestResultDTOKafkaTemplate() {
        return new KafkaTemplate<>(requestResultDTOProducerFactory());
    }

    /**
     * Consumer factory bean for receiving RegisterRequestDTO
     */
    @Bean
    public ConsumerFactory<String, RegisterRequestDTO> userRegistrationConsumerFactory() {
        return KafkaUtils.getKafkaConsumerFactory(RegisterRequestDTO.class, GROUP_ID, kafkaUrl);
    }

    /**
     * Listener factory bean for receiving RegisterRequestDTO
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegisterRequestDTO> userRegistrationRequestContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RegisterRequestDTO>();

        factory.setConsumerFactory(userRegistrationConsumerFactory());
        return factory;
    }
}
