package org.sopt.pawkey.backendapi.domain.auth.infra.persistence.converter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AppleRefreshTokenEncryptor
	implements AttributeConverter<String, String> {

	private static final String ALGORITHM = "AES";

	// ⚠️ application.yml에서 주입받는 걸 권장
	private static final String SECRET_KEY = "pawkey-apple-token!"; // 16byte

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec key =
				new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

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
			SecretKeySpec key =
				new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] decoded = Base64.getDecoder().decode(dbData);
			return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);

		} catch (Exception e) {
			throw new IllegalStateException("Apple Refresh Token 복호화 실패", e);
		}
	}
}