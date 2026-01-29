package org.sopt.pawkey.backendapi.domain.auth.infra.persistence.converter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Component
@Converter
public class AppleRefreshTokenEncryptor
	implements AttributeConverter<String, String> {

	private static final String ALGORITHM = "AES";

	@Value("${apple.refresh-token.secret}")
	private String secretKey; // Base64 문자열

	private SecretKeySpec getKey() {
		byte[] decodedKey = Base64.getDecoder().decode(secretKey);
		return new SecretKeySpec(decodedKey, ALGORITHM);
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, getKey());

			byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encrypted);

		} catch (Exception e) {
			throw new IllegalStateException("Apple Refresh Token 암호화 실패", e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, getKey());

			byte[] decoded = Base64.getDecoder().decode(dbData);
			return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new IllegalStateException("Apple Refresh Token 복호화 실패", e);
		}
	}
}