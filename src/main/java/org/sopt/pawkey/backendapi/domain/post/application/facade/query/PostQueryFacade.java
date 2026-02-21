package org.sopt.pawkey.backendapi.domain.post.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.image.domain.model.ImageType;
import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostDetailResponseDto;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostPagingResponseDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostDetailResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.WalkImageResult;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostQueryService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryFacade {
	private final PostQueryService postQueryService;
	private final PostService postService;
	private final PresignedImageService presignedImageService;

	public PostPagingResponseDto getFilterPostList(FilterPostsRequestDto requestDto, String sortBy, String cursor,
		int size, Long userId) {
		List<GetPostCardResult> results = postQueryService.getFilteredPosts(requestDto, sortBy, cursor, size, userId);

		boolean hasNext = results.size() > size;
		List<GetPostCardResult> pagedResults = hasNext ? results.subList(0, size) : results;

		List<PostCardResponseDto> posts = pagedResults.stream().map(PostCardResponseDto::from).toList();

		// 커서 생성
		String nextCursor = null;
		if (!posts.isEmpty() && hasNext) {
			GetPostCardResult last = pagedResults.get(pagedResults.size() - 1); // 마지막 게시글 정보
			nextCursor = "popular".equals(sortBy)
				? last.likeCount() + "_" + last.postId() // 인기순
				: String.valueOf(last.postId()); // 최신순
		}

		// 리스트, 다음 커서 위치, 다음 페이지 유무
		return new PostPagingResponseDto(posts, nextCursor, hasNext);
	}

	public PostDetailResponseDto getPostDetail(Long postId, Long userId) {

		PostEntity post = postService.findByIdWithAllDetails(postId);

		boolean isMine = post.getUser().getUserId().equals(userId);

		// route image presigned
		String routeImageUrl = post.getRoute().getTrackingImage() != null
				? presignedImageService.createPresignedGetUrl(
				post.getRoute().getTrackingImage().getImageUrl()
		)
				: null;

		// walk images presigned
		List<WalkImageResult> walkImages =
				post.getPostImageEntityList().stream()
						.filter(img -> img.getImageType() == ImageType.WALK_POST)
						.map(img -> new WalkImageResult(
								img.getImage().getImageId(),
								presignedImageService.createPresignedGetUrl(
										img.getImage().getImageUrl()
								)
						))
						.toList();

		GetPostDetailResult result =
				postQueryService.getPostDetailResult(post, isMine, routeImageUrl, walkImages);

		return PostDetailResponseDto.from(result);
	}

}
