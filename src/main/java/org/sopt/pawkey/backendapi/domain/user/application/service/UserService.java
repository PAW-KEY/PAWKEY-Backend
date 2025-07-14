package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.user.application.dto.request.CreateUserCommand;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface UserService {
	/**
 * Retrieves the user entity associated with the specified user ID.
 *
 * @param userId the unique identifier of the user
 * @return the UserEntity corresponding to the given user ID
 */
UserEntity getByUserId(Long userId);

	/**
 * Retrieves the user entity associated with the specified user ID.
 *
 * @param userId the unique identifier of the user to retrieve
 * @return the UserEntity corresponding to the given user ID
 */
UserEntity findById(Long userId);

	/**
 * Saves a new user based on the provided command and returns the created user entity.
 *
 * @param command the command object containing user creation details
 * @return the saved UserEntity
 */
UserEntity saveUser(CreateUserCommand command);

}
