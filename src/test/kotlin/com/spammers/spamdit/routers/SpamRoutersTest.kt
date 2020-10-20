package com.spammers.spamdit.routers

import org.junit.Before
import org.junit.Test
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

class SpamRoutersTest {

    lateinit var client: WebTestClient
    lateinit var date: LocalDateTime
    @Before
    fun init() {
        this.client = WebTestClient.bindToRouterFunction(SpamRouters().getSpam()).build()
        date = LocalDateTime.of(2020, 9, 5, 12, 22, 43, 333)

    }

    @Test
    fun whenRequestToGetSpam_thenStatusShouldBeOk() {
        client.get()
                .uri("/spam")
                .exchange()
                .expectStatus().isOk
    }

    @Test
    fun whenRequestToGetSpam_thenBodyShouldContainSpam() {
        client.get()
                .uri("/spam")
                .exchange()
                .expectBody()
                .json("[{\"id\":\"1\",\"user\":\"spammer_20k\",\"topic\":\"Look at this topic\",\"text\":\"Text is even better\",\"date\":[2020,9,5,12,22,43,333]}]")
    }
}