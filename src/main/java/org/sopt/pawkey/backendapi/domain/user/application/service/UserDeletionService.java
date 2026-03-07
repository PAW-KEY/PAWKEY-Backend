package org.sopt.pawkey.backendapi.domain.user.application.service;

import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
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

	private final PostRepository postRepository;
	private final RouteRepository routeRepository;

	@Transactional
	public void deleteUser(Long userId) {

		postRepository.deleteByUserId(userId);
		routeRepository.deleteByUserId(userId); //post와 route는 cascade설정시, 대량 Lazy Loading이 발생할 수 있어서, service단에서 처리(나머지 pet,review,postlike연관관계는 userEntity에서 casecade로 처리합니다)


		appleRefreshTokenRepository.deleteById(userId);
		socialAccountRepository.deleteByUser_UserId(userId);
		userRepository.deleteById(userId);
	}
}
