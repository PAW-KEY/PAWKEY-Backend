package org.sopt.pawkey.backendapi.domain.user.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final SpringDataUserRepository springDataUserRepository;

	@Override
	public UserEntity save(final UserEntity user) {
		return springDataUserRepository.save(user);
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return springDataUserRepository.findById(id);
	}

	@Override
	public void deleteById(Long userId) {
		springDataUserRepository.deleteById(userId);
	}

	@Override
	public boolean existsByName(String name) {
		return springDataUserRepository.existsByName(name);
	}
}
