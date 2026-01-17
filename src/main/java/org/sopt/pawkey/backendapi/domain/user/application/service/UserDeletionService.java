package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeletionService {
	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;
	private final AppleRefreshTokenRepository appleRefreshTokenRepository;

	@Transactional
	public void deleteUser(Long userId) {
		appleRefreshTokenRepository.deleteById(userId);
		socialAccountRepository.deleteByUser_UserId(userId);
		userRepository.deleteById(userId);
	}
}
