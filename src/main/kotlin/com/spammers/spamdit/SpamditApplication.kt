package com.spammers.spamdit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpamditApplication

fun main(args: Array<String>) {
	runApplication<SpamditApplication>(*args)
}
