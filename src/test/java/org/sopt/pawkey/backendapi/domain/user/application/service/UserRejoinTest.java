package org.sopt.pawkey.backendapi.domain.user.application.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;
import org.sopt.pawkey.backendapi.domain.user.api.dto.result.UserCreationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRejoinTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDeletionService userDeletionService;

	@Test
	void 탈퇴_후_재가입하면_신규유저로_처리된다() {
		// given
		String platform = Provider.APPLE.name();
		String platformUserId = "apple-test-uid"+ UUID.randomUUID();
		String email = "test@apple.com";

		// 최초 가입
		UserCreationResult firstJoin =
			userService.findOrCreateUserBySocialId(
				Provider.APPLE,
				platformUserId,
				email
			);

		assertTrue(firstJoin.isNewUser());

		Long userId = firstJoin.userId();

		// when-> 탈퇴
		userDeletionService.deleteUser(userId);

		// then-> 동일 소셜 계정으로 재가입
		UserCreationResult rejoin =
			userService.findOrCreateUserBySocialId(
				Provider.APPLE,
				platformUserId,
				email
			);

		assertTrue(rejoin.isNewUser());
		assertNotEquals(userId, rejoin.userId());
	}
}
