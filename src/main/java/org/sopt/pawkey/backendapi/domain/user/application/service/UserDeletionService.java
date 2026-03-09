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

		//연관 테이블의 child테이블 우선 삭제
		dbtiResultRepository.deleteByUserId(userId);
		postSelectedCategoryOptionRepository.deleteByUserId(userId);


		//연관 테이블 삭제
		postRepository.deleteByUserId(userId);

		entityManager.flush();

		routeRepository.deleteByUserId(userId); //post와 route는 cascade설정시, 대량 Lazy Loading이 발생할 수 있어서, service단에서 처리(나머지 pet,review,postlike연관관계는 userEntity에서 casecade로 처리합니다)
		entityManager.flush();

		appleRefreshTokenRepository.deleteById(userId);
		socialAccountRepository.deleteByUser_UserId(userId);

		userRepository.deleteById(userId);
	}
}
