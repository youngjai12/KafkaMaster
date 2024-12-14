package com.dds.brandon.KafkaMaster.configuration

import com.dds.brandon.KafkaMaster.util.YamlPropertyLoaderFactory
import com.dds.brandon.KafkaMaster.worker.core.Worker
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:topics.yml", factory = YamlPropertyLoaderFactory::class)
@ConfigurationProperties(prefix="topic-information")
class TopicInformation(
    private val context: ApplicationContext
) {
    final fun convertFullTopicName() {
        topics.forEach {
            it.id = it.name
            topicMap[it.id] = it
        }
    }

    init {
        convertFullTopicName()
    }

    lateinit var topics: Array<Topic>

    val topicMap = HashMap<String, Topic>()

    fun getTopicName(id: String): String {
        return topicMap[id]?.name ?: ""
    }

    fun worker(fullTopicName: String): List<Worker> {
        return topics.asSequence()
            .filter { it.name == fullTopicName }
            .map { it.worker }
            .flatMap { it.asIterable() }
            .filter { context.containsBean(it) }
            .map { context.getBean(it) as Worker }.toList()
    }

}

data class Topic(
    var id: String,
    var name: String,
    var location: String,
    var worker: Array<String>,
    var excludePhase: Array<String> = arrayOf(),
    var overwriteTopicName: HashMap<String, String> = HashMap(),
    var retryable: Boolean = false,
    var dlt: Boolean = false
)
