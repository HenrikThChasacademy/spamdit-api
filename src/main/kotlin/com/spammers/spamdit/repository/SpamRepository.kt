package com.spammers.spamdit.repository;

import com.spammers.spamdit.model.Spam
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface SpamRepository : ReactiveMongoRepository<Spam, String>