package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {

	/**
 * Retrieves a UserEntity by its user ID.
 *
 * @param userId the unique identifier of the user
 * @return the UserEntity associated with the given user ID, or null if not found
 */
UserEntity getByUserId(Long userId);
}
