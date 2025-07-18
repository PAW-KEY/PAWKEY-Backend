package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostQueryRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostQueryService {

	private final PostQueryRepository postQueryRepository;

	public PostResponseDto getPostDetail(PostEntity post, boolean isLiked, String routeMapImage,
		List<String> walkingImages) {

		//작성자
		AuthorDto authorDto = new AuthorDto(
			post.getUser().getUserId(),
			post.getPet().getPetId(),
			post.getPet().getName(),
			post.getPet().getProfileImage() != null ? post.getPet().getProfileImage().getImageUrl() : null
		);

		List<String> categoryTags = post.getPostSelectedCategoryOptionEntityList().stream()
			.map(sel -> sel.getCategoryOption().getOptionSummary())
			.toList();

		PostResponseDto.CategoryTagsDto categoryTagsDto = new PostResponseDto.CategoryTagsDto(categoryTags);

		String regionName = post.getRoute().getRegion() != null
			? post.getRoute().getRegion().getFullRegionName()
			: "";

		return PostResponseDto.of(
			post.getPostId(),
			post.getRoute().getRouteId(),
			post.getTitle(),
			post.getDescription(),
			isLiked,
			authorDto,
			categoryTagsDto,
			regionName,
			post.getCreatedAt(),
			routeMapImage,
			walkingImages
		);
	}

	public List<GetPostCardResult> getFilteredPosts(FilterPostsRequestDto requestDto, Long userId) {
		return postQueryRepository.findByFilter(requestDto, userId);
	}
}
