package com.spammers.spamdit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude= [MongoReactiveAutoConfiguration::class,
	MongoReactiveDataAutoConfiguration::class, MongoAutoConfiguration::class, MongoDataAutoConfiguration::class])
class SpamditApplication

fun main(args: Array<String>) {
	runApplication<SpamditApplication>(*args)
}
