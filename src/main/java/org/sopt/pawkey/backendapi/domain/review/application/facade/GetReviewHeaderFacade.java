package org.sopt.pawkey.backendapi.domain.review.application.facade;
import org.sopt.pawkey.backendapi.domain.image.application.service.PresignedImageService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.review.application.dto.result.ReviewHeaderResult;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReviewHeaderFacade {

    private final PostService postService;
    private final UserService userService;
    private final PresignedImageService presignedImageService;

    public ReviewHeaderResult execute(Long postId, Long userId) {
        PostEntity post = postService.findById(postId);
        UserEntity user = userService.findById(userId);
        PetEntity pet = user.getPetOrThrow();

        String profileImageUrl = pet.getProfileImage() != null
                ? presignedImageService.createPresignedGetUrl(pet.getProfileImage().getImageUrl())
                : null;

        return new ReviewHeaderResult(
                post.getTitle(),
                pet.getName(),
                profileImageUrl
        );
    }
}