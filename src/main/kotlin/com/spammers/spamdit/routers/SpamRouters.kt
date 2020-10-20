package com.spammers.spamdit.routers

import com.spammers.spamdit.model.Spam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.time.LocalDateTime

@Configuration
class SpamRouters {

    @Bean
    fun getSpam() = router {
        GET("/spam") { _ -> ServerResponse.ok().body(fromValue(
                arrayOf(
                        Spam("1", "spammer_20k", "Look at this topic", "Text is even better",
                                LocalDateTime.of(2020, 9, 5, 12, 22, 43, 333)))
                )
            )
        }
    }
}