package com.spammers.spamdit

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude= arrayOf(MongoReactiveDataAutoConfiguration::class))
class SpamditApplication

fun main(args: Array<String>) {
	runApplication<SpamditApplication>(*args)
}
