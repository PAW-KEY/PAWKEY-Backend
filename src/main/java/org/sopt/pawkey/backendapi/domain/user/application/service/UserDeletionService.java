package org.sopt.pawkey.backendapi.domain.user.application.service;

import jakarta.persistence.EntityManager;
import org.sopt.pawkey.backendapi.domain.auth.domain.repository.AppleRefreshTokenRepository;
import org.sopt.pawkey.backendapi.domain.dbti.domain.repository.DbtiResultRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostSelectedCategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.SocialAccountRepository;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeletionService {
	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;
	private final AppleRefreshTokenRepository appleRefreshTokenRepository;

	private final DbtiResultRepository dbtiResultRepository;
	private final PostSelectedCategoryOptionRepository postSelectedCategoryOptionRepository;

	private final PostRepository postRepository;
	private final RouteRepository routeRepository;

	private final EntityManager entityManager;

	@Transactional
	public void deleteUser(Long userId) {

		// 1 option
		postSelectedCategoryOptionRepository.deleteByUserId(userId);
		entityManager.flush();

		// 2. post_like (자식 먼저)
		postRepository.deletePostLikesByUserId(userId);
		entityManager.flush();

		// 3. post_image 먼저 (FK 자식)
		postRepository.deletePostImagesByRouteUserId(userId);
		postRepository.deletePostImagesByUserId(userId);
		entityManager.flush();

		// 4. posts
		postRepository.deleteByRouteUserId(userId);
		postRepository.deleteByUserId(userId);
		entityManager.flush();

		// 5. routes
		routeRepository.deleteByUserId(userId);
		entityManager.flush();

		// 6. 기타
		dbtiResultRepository.deleteByUserId(userId);
		appleRefreshTokenRepository.deleteById(userId);
		socialAccountRepository.deleteByUser_UserId(userId);

		// 7. user
		userRepository.deleteById(userId);
	}
}
