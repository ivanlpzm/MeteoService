package com.smoke.meteoservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ActiveProfiles("test")
class TemperatureIntegrationTest {

    private static MongoDBContainer mongoDBContainer;
    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setUp() {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6.0"));
        mongoDBContainer.start();

        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

        configureFor("localhost", 8089);
        stubFor(get(urlEqualTo("/v1/forecast?latitude=35,689500&longitude=139,691700&current_weather=true"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"current_weather\": { \"temperature\": 23.5, \"date\": \"2024-10-07T14:13:36\" } }")));

    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.kafka.enabled", () -> "false");
        registry.add("openmeteo.api.url", () -> "http://localhost:8089/v1/forecast");
    }

    @Karate.Test
    Karate testTemperatureIntegration() {
        return Karate.run("classpath:karate/temperature-api.feature");
    }
}
