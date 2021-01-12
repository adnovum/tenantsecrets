package com.vary.tenantsecrets.tenantsecrets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secrets")
public class SecretsRestController {


	private final SecretsService secretsService;

	@Autowired
	public SecretsRestController(SecretsService secretsService) {
		this.secretsService = secretsService;
	}

	@PostMapping("/{tenantId}")
	public ResponseEntity<String> encrypt(@PathVariable("tenantId") String tenantId,
										 @RequestBody String plainText) {
		try {
			String secret = secretsService.encrypt(plainText, tenantId);
			return ResponseEntity.status(HttpStatus.CREATED).body(secret);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating secret.");
		}
	}

}
