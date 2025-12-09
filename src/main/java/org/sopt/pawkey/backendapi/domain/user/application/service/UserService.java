package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.result.UserCreationResult;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.CreateUserCommand;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;

	public UserEntity findById(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}

	public UserEntity saveUser(CreateUserCommand command, RegionEntity region) {

		if (userRepository.existsByLoginId(command.loginId())) {
			throw new UserBusinessException(UserErrorCode.USER_DUPLICATE_LOGIN_ID);
		}

		UserEntity user = UserEntity.builder()
			.loginId(command.loginId())
			.password(command.password())
			.name(command.name())
			.gender(command.gender())
			.age(command.age())
			.region(region)
			.build();

		return userRepository.save(user);
	}

	public void updateUserRegion(UserEntity user, RegionEntity region) {
		user.updateRegion(region);
	}

	@Transactional
	public UserCreationResult findOrCreateUserBySocialId(String platform, String platformUserId, String primaryEmail) {
		// 1. SocialAccount가 이미 존재하는지 확인 (로그인 시도)
		Optional<SocialAccountEntity> existingAccount = socialAccountRepository.findByPlatformAndPlatformUserId(
			platform, platformUserId);

		if (existingAccount.isPresent()) {
			// 2. 존재하면, 연결된 User ID 반환
			return UserCreationResult.builder()
				.userId(existingAccount.get().getUser().getUserId())
				.isNewUser(false)
				.build();
		} else {
			// 3. 존재하지 않으면, 신규 가입 트랜잭션 수행

			// 3-1. 새로운 User 엔티티 생성 (최소 정보만으로 생성)
			UserEntity newUser = userRepository.save(UserEntity.builder()
				.loginId(platform.toLowerCase() + "_" + platformUserId)
				.password("social-login-default-password") // 소셜 사용자는 비밀번호가 없지만, NOT NULL일 경우 빈 문자열 또는 임시값 지정
				.name(platform + " User")
				.gender("MALE")
				.age(1)
				.region(null)
				.build());

			// 3-2. SocialAccount 엔티티 생성 및 User와 연결
			SocialAccountEntity socialAccount = SocialAccountEntity.create(
				newUser,
				platform,
				platformUserId,
				primaryEmail
			);
			socialAccountRepository.save(socialAccount);

			// 3-3. 신규 User ID 반환
			return UserCreationResult.builder()
				.userId(newUser.getUserId())
				.isNewUser(true)
				.build();
		}
	}

}


