package com.adnovum.tenantsecrets.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.lang.invoke.MethodHandles;

@Controller
@RequestMapping("/secrets")
public class SecretsUiController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String SECRETS_FORM = "secretsForm";

	private final SecretsService secretsService;

	private final String docLink;

	@Autowired
	public SecretsUiController(SecretsService secretsService, @Value("${tenantsecrets.docLink}") String docLink) {
		this.secretsService = secretsService;
		this.docLink = docLink;
	}

	@GetMapping
	public String getSecretsForm(Model model) {
		initializeModel(model, new EncryptRequest());
		return SECRETS_FORM;
	}

	@PostMapping
	public String postSecretsForm(@ModelAttribute("request") @Valid EncryptRequest request, BindingResult bindingResult,
						  Model model) {
		initializeModel(model, request);
		if (bindingResult.hasErrors()) {
			return SECRETS_FORM;
		}

		try {
			String secret = secretsService.encrypt(request.getPlaintext(), request.getTenantId());
			model.addAttribute("secret", secret);
			model.addAttribute("exampleUse",
					String.format("{{SECRET:[%s][%s]}}", request.getTenantId(), secret));
		} catch (Exception e) {
			LOG.error("Error encrypting secret in form request", e);
			model.addAttribute("encryptError", "Unexpected error when encrypting.");
		}

		return SECRETS_FORM;
	}

	private void initializeModel(Model model, EncryptRequest request) {
		model.addAttribute("request", request);
		model.addAttribute("docLink", docLink);
	}

	public static class EncryptRequest {
		@NotEmpty
		private String tenantId;

		@NotEmpty
		private String plaintext;

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

		public String getPlaintext() {
			return plaintext;
		}

		public void setPlaintext(String plaintext) {
			this.plaintext = plaintext;
		}
	}
}
