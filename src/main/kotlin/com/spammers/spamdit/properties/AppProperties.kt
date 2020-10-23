package com.spammers.spamdit.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {

    @Value("\${spring.data.mongodb.host}")
    lateinit var host: String

    @Value("\${spring.data.mongodb.port}")
    lateinit var port: String

    @Value("\${spring.data.mongodb.database}")
    lateinit var database: String
}