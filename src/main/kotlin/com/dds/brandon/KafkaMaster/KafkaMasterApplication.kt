package com.dds.brandon.KafkaMaster

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaMasterApplication

fun main(args: Array<String>) {
	runApplication<KafkaMasterApplication>(*args)
}
