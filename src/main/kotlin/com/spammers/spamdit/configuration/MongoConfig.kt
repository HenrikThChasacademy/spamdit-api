package com.spammers.spamdit.configuration

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.spammers.spamdit.properties.AppProperties
import com.spammers.spamdit.repository.CommentRepository
import com.spammers.spamdit.repository.SpamRepository
import com.spammers.spamdit.repository.TopicRepository
import com.spammers.spamdit.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = [SpamRepository::class,
    CommentRepository::class, TopicRepository::class, UserRepository::class])
class MongoConfig : AbstractReactiveMongoConfiguration() {

    @Autowired
    lateinit var appProperties: AppProperties

    override fun reactiveMongoClient(): MongoClient = mongoClient()

    @Bean
    fun mongoClient(): MongoClient = MongoClients.create()

    override fun getDatabaseName(): String = appProperties.database

    @Bean
    fun reactiveMongoTemplate(): ReactiveMongoTemplate = ReactiveMongoTemplate(mongoClient(), databaseName)

}
