package com.vary.tenantsecrets.crypto;

import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class KeyProvider {

	private KeyProvider() {
		// Static helper class.
	}

	public static byte[] fromFile(File file) throws IOException {
		try (InputStream is = new FileInputStream(file)) {
			return fromStream(is);
		}
	}

	public static byte[] fromStream(InputStream s) throws IOException {
		String hex = CharStreams.toString(new InputStreamReader(s, StandardCharsets.UTF_8));
		return BaseEncoding.base16().decode(hex.toUpperCase());
	}
}
