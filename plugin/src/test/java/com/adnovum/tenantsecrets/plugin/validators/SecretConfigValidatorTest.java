package com.adnovum.tenantsecrets.plugin.validators;

import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.adnovum.tenantsecrets.plugin.validators.SecretConfigValidator.CIPHER_FILE_DOES_NOT_EXIST;
import static com.adnovum.tenantsecrets.plugin.validators.SecretConfigValidator.GIVEN_PATH_IS_NOT_A_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SecretConfigValidatorTest {

    @TempDir
    Path tempDir;

    @ParameterizedTest
    @MethodSource("invalidCipherFilePathExamples")
    void validateInvalidCipherFilePaths(String cipherFilePath, String expectedMessage) {
        // Given
        Map<String, String> requestBody = createRequestBody(tempDir.resolve(cipherFilePath));

        // When
        ValidationResult validationResult = new SecretConfigValidator().validate(requestBody);

        // Then
        assertSoftly(softly -> softly.assertThat(validationResult)
                .hasSize(1)
                .allMatch(validationError -> validationError.toString().contains(expectedMessage)));
    }

    static Stream<Arguments> invalidCipherFilePathExamples() {
        return Stream.of(
                Arguments.of("this/file/should/not/exist", CIPHER_FILE_DOES_NOT_EXIST),
                Arguments.of("", GIVEN_PATH_IS_NOT_A_FILE)
        );
    }

    @ParameterizedTest
    @MethodSource("cipherExamples")
    void validateCipherStrings(String cipher, boolean valid) throws IOException {
        // Given
        Path cipherFilePath = createCipherFile(cipher);
        Map<String, String> requestBody = createRequestBody(cipherFilePath);

        // When
        ValidationResult validationResult = new SecretConfigValidator().validate(requestBody);

        // Then
        assertThat(validationResult).hasSize(valid ? 0 : 1);
    }

    static Stream<Arguments> cipherExamples() {
        return Stream.of(
                Arguments.of("1", false),
                Arguments.of("01", true),
                Arguments.of("12", true),
                Arguments.of("123", false),
                Arguments.of("1234", true),
                Arguments.of("123x", false),
                Arguments.of("12/4", false),
                Arguments.of("12.4", false),
                Arguments.of("1234\n", false)
        );
    }

    private Path createCipherFile(String input) throws IOException {
        Path cipherFilePath = Files.createFile(tempDir.resolve("cipher.aes"));
        Files.write(cipherFilePath, input.getBytes());
        return cipherFilePath;
    }

    private static Map<String, String> createRequestBody(Path cipherFilePath) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("cipherFile", cipherFilePath.toAbsolutePath().toString());
        return requestBody;
    }

}
