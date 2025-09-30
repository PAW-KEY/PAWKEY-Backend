package org.sopt.pawkey.backendapi.global.auth.application.verifier;

import java.util.Map;

public interface  VerifierService {
	Map<String, String> verifyToken(String platform, String idToken);
}
