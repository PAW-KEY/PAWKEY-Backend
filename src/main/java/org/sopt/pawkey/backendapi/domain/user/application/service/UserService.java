package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.CreateUserCommand;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

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
}


