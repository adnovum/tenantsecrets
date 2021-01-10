package com.vary.tenantsecrets.tenantsecrets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Controller
@RequestMapping("/secrets")
public class SecretsUiController {

	private static final String SECRETS_FORM = "secretsForm";

	private final SecretsService secretsService;

	@Autowired
	public SecretsUiController(SecretsService secretsService) {
		this.secretsService = secretsService;
	}

	@GetMapping
	public String getSecretsForm(Model model) {
		model.addAttribute("request", new EncryptRequest());
		return SECRETS_FORM;
	}

	@PostMapping
	public String postSecretsForm(@ModelAttribute("request") @Valid EncryptRequest request, BindingResult bindingResult,
						  Model model) {
		model.addAttribute("request", request);
		if (bindingResult.hasErrors()) {
			return SECRETS_FORM;
		}

		try {
			String secret = secretsService.encrypt(request.getPipelineGroup(), request.getPlaintext());
			model.addAttribute("secret", secret);
			model.addAttribute("exampleUse",
					String.format("{{SECRET:[%s][%s]}}", request.getPipelineGroup(), secret));
		} catch (Exception e) {
			model.addAttribute("encryptError", "Unexpected error when encrypting.");
		}

		return SECRETS_FORM;
	}

	public static class EncryptRequest {
		@NotEmpty
		private String pipelineGroup;

		@NotEmpty
		private String plaintext;

		public String getPipelineGroup() {
			return pipelineGroup;
		}

		public void setPipelineGroup(String pipelineGroup) {
			this.pipelineGroup = pipelineGroup;
		}

		public String getPlaintext() {
			return plaintext;
		}

		public void setPlaintext(String plaintext) {
			this.plaintext = plaintext;
		}
	}
}
