package com.dds.brandon.KafkaMaster.configuration


import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
class ConsumerConfig(
    private val kafkaAppProperties: KafkaAppProperties
) {

    @Bean
    fun localKafkaConsumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(
            kafkaAppProperties.defaultConsumerProperties(), StringDeserializer(), StringDeserializer()
        )
    }

    @Bean
    fun localKafkaListenerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.setConcurrency(3)
        factory.consumerFactory = localKafkaConsumerFactory()
        return factory
    }


}