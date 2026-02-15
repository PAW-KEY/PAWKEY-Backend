package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.auth.domain.Provider;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.result.UserCreationResult;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.OnboardingInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UpdateUserInfoCommand;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.SocialAccountEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;

	@Transactional(readOnly = true)
	public UserEntity findById(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}

	@Transactional
	public UserEntity completeOnboarding(Long userId, OnboardingInfoCommand command, RegionEntity region) {

		if (userRepository.existsByName(command.name())) {
			throw new UserBusinessException(UserErrorCode.USER_DUPLICATE_NICKNAME);
		}

		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		try {
			user.updateProfile(command.name(), command.gender(), command.birth());
			user.updateRegion(region);
			userRepository.saveAndFlush(user); // save() 대신 saveAndFlush()를 사용하여 즉시 DB 제약조건 검사
		} catch (DataIntegrityViolationException e) {
			throw new UserBusinessException(UserErrorCode.USER_DUPLICATE_NICKNAME);
		}

		return user;
	}

	public void updateUserRegion(UserEntity user, RegionEntity region) {
		user.updateRegion(region);
	}

	@Transactional
	public UserCreationResult findOrCreateUserBySocialId(Provider provider, String platformUserId, String primaryEmail) {
		// 1. SocialAccount가 이미 존재하는지 확인 (로그인 시도)

		String platform = provider.name();
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
				.name(platform + " User")
				.gender(null)
				.birth(null)
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

	@Transactional
	public void updateUserInfo(Long userId, UpdateUserInfoCommand command) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		if (!command.name().equals(user.getName())) {
			if (userRepository.existsByName(command.name())) {
				throw new UserBusinessException(UserErrorCode.USER_DUPLICATE_NICKNAME);
			}
		}

		user.updateProfile(
			command.name(),
			command.gender(),
			command.birth()
		);
	}
}


