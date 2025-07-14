package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.Comparator;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.domain.repository.PostRepository;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.MyPostResponseDto;
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

	public List<MyPostResponseDto> getMyPosts(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		List<PostEntity> posts = postRepository.findAllByUser(user)
			.stream()
			.sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed()) // 최신순 정렬
			.toList();

		return posts.stream()
			.map(post -> {
				// 대표 이미지
				String repImageUrl = post.getPostImages().stream()
					.findFirst()
					.map(img -> img.getImage().getImageUrl())
					.orElse(null);

				// 작성자 정보
				PetEntity pet = post.getUser().getPet();
				MyPostResponseDto.WriterDto writer = new MyPostResponseDto.WriterDto(
					post.getUser().getUserId(),
					pet != null ? pet.getName() : null,
					(pet != null && pet.getProfileImage() != null) ? pet.getProfileImage().getImageUrl() : null
				);

				// 설명 태그
				List<String> tags = post.getPostCategoryOptionTop3EntityList().stream()
					.map(opt -> opt.getCategoryOption().getOptionText())
					.toList();

				return MyPostResponseDto.of(post, repImageUrl, writer, tags);
			})
			.toList();
	}

}