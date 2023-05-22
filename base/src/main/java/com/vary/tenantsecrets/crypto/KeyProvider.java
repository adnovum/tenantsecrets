package com.vary.tenantsecrets.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

public class KeyProvider {

	private KeyProvider() {
		// Static helper class.
	}

	public static byte[] fromPath(Path path) throws IOException {
		try (InputStream is = Files.newInputStream(path)) {
			return fromStream(is);
		}
	}

	public static byte[] fromStream(InputStream s) throws IOException {
		String hex = CharStreams.toString(new InputStreamReader(s, StandardCharsets.UTF_8));
		return BaseEncoding.base16().decode(hex.toUpperCase());
	}
}
