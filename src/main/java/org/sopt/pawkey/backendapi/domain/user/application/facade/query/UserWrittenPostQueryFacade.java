package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.ListResponseWrapper;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.ReviewCardResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserWrittenPostQueryService;
import org.sopt.pawkey.backendapi.domain.user.domain.repository.UserQueryRepository;
import org.sopt.pawkey.backendapi.domain.user.exception.UserBusinessException;
import org.sopt.pawkey.backendapi.domain.user.exception.UserErrorCode;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserWrittenPostQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserWrittenPostQueryService userWrittenPostQueryService;

	public List<PostCardResponseDto> getMyPosts(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<Long> likedPostIds = userWrittenPostQueryService.getLikedPostIds(userId);
		List<GetPostCardResult> results = userWrittenPostQueryService.findMyPostResults(user, likedPostIds);

		return results.stream()
			.map(PostCardResponseDto::from)
			.toList();
	}

	public ListResponseWrapper<ReviewCardResponseDto> getMyReviews(Long userId) {
		userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<ReviewCardResponseDto> reviews = userWrittenPostQueryService.getMyWrittenReviews(userId);

		return ListResponseWrapper.from(reviews);
	}
}