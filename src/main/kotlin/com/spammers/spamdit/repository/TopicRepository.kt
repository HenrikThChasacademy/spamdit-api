package com.spammers.spamdit.repository

import com.spammers.spamdit.model.Topic
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface TopicRepository: ReactiveMongoRepository<Topic, String>