package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostCardResponseDto;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
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
	private final PostRepository postRepository;

	public List<PostCardResponseDto> getMyPosts(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<PostEntity> posts = postRepository.findAllByUser(user)
			.stream()
			.sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed()) // 최신순 정렬
			.toList();

		return posts.stream()
			.map(post -> {
				String repImageUrl = post.getPostImageEntityList().stream()
					.findFirst()
					.map(img -> img.getImage().getImageUrl())
					.orElse(null);

				PetEntity pet = post.getUser().getPet();
				PostCardResponseDto.WriterDto writer = new PostCardResponseDto.WriterDto(
					post.getUser().getUserId(),
					pet != null ? pet.getName() : null,
					(pet != null && pet.getProfileImage() != null) ? pet.getProfileImage().getImageUrl() : null
				);

				List<String> tags = post.getPostSelectedCategoryOptionEntityList().stream()
					.map(opt -> opt.getCategoryOption().getOptionText())
					.toList();

				return new PostCardResponseDto(
					post.getPostId(),
					post.getCreatedAt(),
					false, // isLike = false (내 게시글)
					post.getTitle(),
					repImageUrl,
					writer,
					tags
				);
			})
			.toList();
	}

}