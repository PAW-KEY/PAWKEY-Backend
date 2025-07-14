package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserQueryRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserQueryRepository userQueryRepository;
	private final UserRepository userRepository;

	public UserEntity findById(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}

	@Override
	public void updateUserRegion(UserEntity user, RegionEntity region) {
		user.updateRegion(region);
	}
}

