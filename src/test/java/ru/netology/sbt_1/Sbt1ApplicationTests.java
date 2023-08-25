package ru.netology.sbt_1;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Sbt1ApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    static GenericContainer<?> devContainer;
    static GenericContainer<?> prodContainer;

    @BeforeAll
    public static void setUp() {
        devContainer = new GenericContainer<>("devapp")
                .withExposedPorts(8080)
                .withEnv("SPRING_PROFILES_ACTIVE", "dev")
                .withEnv("server.port", "8080")
                .withEnv("netology.profile.dev", "true");

        prodContainer = new GenericContainer<>("prodapp")
                .withExposedPorts(8081)
                .withEnv("SPRING_PROFILES_ACTIVE", "prod")
                .withEnv("server.port", "8081")
                .withEnv("netology.profile.dev", "false");

        devContainer.start();
        prodContainer.start();
    }

    @Test
    void testDevApp() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + devContainer.getMappedPort(8080) + "/profile",
                String.class
        );
        assertEquals("Current profile is dev", response.getBody());
    }

    @Test
    void testProdApp() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + prodContainer.getMappedPort(8081) + "/profile",
                String.class
        );
        assertEquals("Current profile is production", response.getBody());
    }
}