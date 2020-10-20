package com.spammers.spamdit.router

import com.spammers.spamdit.handler.SpamHandler
import com.spammers.spamdit.model.Spam
import com.spammers.spamdit.repository.SpamRepository
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@WebFluxTest(
        excludeAutoConfiguration = [ReactiveUserDetailsServiceAutoConfiguration::class, ReactiveSecurityAutoConfiguration::class]
)
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [SpamHandler::class, SpamRouters::class])
class SpamRoutersTest {

    @Autowired
    private lateinit var client: WebTestClient

    @MockBean
    private lateinit var webClient: WebClient

    @MockBean
    private lateinit var spamRepository: SpamRepository


    lateinit var date: LocalDateTime
    lateinit var date2: LocalDateTime
    lateinit var date3: LocalDateTime
    @Before
    fun init() {
           date = LocalDateTime.of(2020, 9, 5, 12, 22, 43, 333)
           date2 = LocalDateTime.of(2020, 9, 7, 12, 12, 12, 333)
           date3 = LocalDateTime.of(2020, 9, 12, 12, 53, 32, 333)

    }


    @FlowPreview
    @Test
    fun `get all spam`() {
        val spam1 = Spam("1", "super_user", "best topic", "the ultimate text", date)
        val spam2 = Spam("2", "super_user", "another best topic", "the second ultimate text", date2)
        val spam3 = Spam("3", "super_user", "last best topic", "the worst text", date3)

        val spamFlux = Flux.just(spam1, spam2, spam3)
        given(spamRepository.findAll()).willReturn(spamFlux)
        client.get()
                .uri("/spam")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Spam>()
                .contains(spam1, spam2, spam3)

    }

    @FlowPreview
    @Test
    fun `get spam by id`() {
        val spam = Spam("1", "super_user", "best topic", "the ultimate text", date)
        val spamMono = Mono.just(spam)

        given(spamRepository.findById("1")).willReturn(spamMono)
        client.get()
                .uri("/spam/1")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<Spam>()
                .isEqualTo(spam)
    }

}