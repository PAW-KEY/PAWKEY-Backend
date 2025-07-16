package org.sopt.pawkey.backendapi.domain.post.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostListResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostQueryService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryFacade {
	private final PostQueryService postQueryService;
	private final PostService postService;

	public PostListResponseDto getFilterPostList(FilterPostsRequestDto requestDto, Long userId) {
		List<GetPostCardResult> results = postQueryService.getFilteredPosts(requestDto, userId);

		List<PostCardResponseDto> postResponseDtoList = results.stream()
			.map(PostCardResponseDto::from)
			.toList();

		return new PostListResponseDto(postResponseDtoList);
	}

	public PostResponseDto getPostDetail(Long postId, Long userId) {

		PostEntity post = postService.findById(postId);

		boolean isLiked = post.getPostLikeEntityList().stream()
			.anyMatch(like -> like.getUser().getUserId().equals(userId));

		String routeMapImageUrl =
			post.getRoute().getTrackingImage() != null ? post.getRoute().getTrackingImage().getImageUrl() : null;

		List<String> walkingImages = post.getPostImageEntityList().stream()
			.filter(img -> img.getImageType() == ImageType.WALK_POST)
			.map(img -> img.getImage().getImageUrl())
			.toList();

		return postQueryService.getPostDetail(post, isLiked, routeMapImageUrl, walkingImages);
	}

}
