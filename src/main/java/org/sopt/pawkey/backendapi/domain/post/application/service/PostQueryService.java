package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostDetailResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.RouteDisplayResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.WalkImageResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostQueryService {

	private final PostQueryRepository postQueryRepository;

	public GetPostDetailResult getPostDetailResult(
			PostEntity post,
			boolean isMine,
			String routeImageUrl,
			List<WalkImageResult> walkImages
	) {
		// 작성자 정보
		AuthorDto authorInfo = new AuthorDto(
				post.getUser().getUserId(),
				post.getPet().getPetId(),
				post.getPet().getName(),
				post.getPet().getProfileImage() != null
						? post.getPet().getProfileImage().getImageUrl()
						: null
		);

		// 카테고리 태그 텍스트
		List<String> categoryTagTexts =
				post.getPostSelectedCategoryOptionEntityList().stream()
						.map(sel -> sel.getCategoryOption().getOptionValue())
						.toList();

		// Route 메타 정보 (from 사용)
		RouteDisplayResult routeDisplay =
				RouteDisplayResult.from(post.getRoute(), routeImageUrl);

		return new GetPostDetailResult(
				post.getPostId(),
				post.getTitle(),
				post.getDescription(),
				post.isPublic(),
				isMine,
				authorInfo,
				routeDisplay,
				categoryTagTexts,
				walkImages
		);
	}

	public List<GetPostCardResult> getFilteredPosts(
		FilterPostsRequestDto requestDto,
		String sortBy,
		String cursor,
		int size,
		Long userId
	) {
		return postQueryRepository.findByFilter(requestDto, sortBy, cursor, size, userId);
	}
}
