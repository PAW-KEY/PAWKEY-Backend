package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.region.domain.RegionRepository;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.CreateUserCommand;
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
	private final RegionRepository regionRepository;

	/**
	 * Retrieves a user entity by the specified user ID.
	 *
	 * @param userId the unique identifier of the user
	 * @return the UserEntity corresponding to the given user ID
	 * @throws UserBusinessException if no user is found with the specified ID
	 */
	@Override
	public UserEntity getByUserId(Long userId) {
		return userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param id the unique identifier of the user to retrieve
	 * @return the UserEntity corresponding to the given ID
	 * @throws UserBusinessException if no user with the specified ID is found
	 */
	public UserEntity findById(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
	}

	/**
	 * Creates and saves a new user based on the provided command data.
	 *
	 * Retrieves the region specified in the command; if the region does not exist, throws a {@link RegionBusinessException}.
	 * Builds a new {@link UserEntity} using the command's information and the retrieved region, then persists it.
	 *
	 * @param command the command containing user creation data
	 * @return the saved {@link UserEntity}
	 * @throws RegionBusinessException if the specified region is not found
	 */
	@Override
	public UserEntity saveUser(CreateUserCommand command) {
		RegionEntity region = regionRepository.getById(command.regionId())
			.orElseThrow(()-> new RegionBusinessException(RegionErrorCode.REGION_NOT_FOUND));

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
}

