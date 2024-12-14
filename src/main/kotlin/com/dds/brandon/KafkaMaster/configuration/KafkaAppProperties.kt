package com.dds.brandon.KafkaMaster.configuration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.StickyAssignor
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.stereotype.Component
import java.net.InetAddress

@EnableKafka
@Configuration
class KafkaAppProperties(
    private val localKafkaProperties: LocalKafkaProperties,
    private val localKafkaConsumerProperties: LocalKafkaConsumerProperties,
    private val localKafkaProducerProperties: LocalKafkaProducerProperties
) {

    fun generateClientId(kafkaName: String): String {
        return "${InetAddress.getLocalHost().hostName}-$kafkaName"
    }

    fun defaultConsumerProperties(maxPollInterval: Int=120000, maxPollRecords: Int = 500):  HashMap<String, Any> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = localKafkaProperties.bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = localKafkaConsumerProperties.groupId
        props[ConsumerConfig.CLIENT_ID_CONFIG] = generateClientId("local")
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG] = listOf(StickyAssignor::class.java)
        props[ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG] = 60000
        props[ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG] = 60000
        props[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = maxPollInterval
        props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = maxPollRecords
        return props
    }
}

open class KafkaProperties {
    open lateinit var bootstrapServers: String
}

open class KafkaConsumerProperties {
    open lateinit var groupId: String
}

open class KafkaProducerProperties {
    open lateinit var acks: String
}

@Component
@ConfigurationProperties(prefix = "kafka.local")
class LocalKafkaProperties : KafkaProperties()

@Component
@ConfigurationProperties(prefix = "kafka.local.consumer")
class LocalKafkaConsumerProperties : KafkaConsumerProperties()

@Component
@ConfigurationProperties(prefix = "kafka.local.producer")
class LocalKafkaProducerProperties : KafkaProducerProperties()
