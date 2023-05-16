package com.vary.tenantsecrets.tenantsecrets;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secrets/api")
public class SecretsRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        }
        catch (Exception e) {
            LOG.error("Error encrypting secret in REST API request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating secret.");
        }
    }

}
