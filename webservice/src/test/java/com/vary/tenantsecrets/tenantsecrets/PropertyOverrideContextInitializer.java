package com.vary.tenantsecrets.tenantsecrets;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.io.File;
import java.net.URISyntaxException;

public class PropertyOverrideContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		try {
			String testCipherFile = new File(getClass().getResource("/cipher.aes").toURI())
					.getAbsolutePath()
					.replace("\\", "\\\\");

			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
					configurableApplicationContext,
					"tenantsecrets.cipherFile=" + testCipherFile);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Could not load test cipher file");
		}
	}
}
