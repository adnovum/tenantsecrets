package com.vary.tenantsecrets.tenantsecrets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
				"http://localhost:" + port + "/api/secrets/my_group", "my secret", String.class);

		assertEquals(201, response.getStatusCode().value());
		assertTrue(response.getBody().startsWith("AES:"));
	}

}
