package com.vary.tenantsecrets.tenantsecrets;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
        initializers = PropertyOverrideContextInitializer.class,
        classes = TenantsecretsApplication.class)
class TenantsecretsApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldEncrypt() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/secrets/api/my_group", "my secret", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).startsWith("AES:");
    }

    @Test
    void shouldLoadUi() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/secrets", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("<form");
    }

}
