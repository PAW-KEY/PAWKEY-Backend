package org.sopt.pawkey.backendapi.domain.review.api.dto.response;


import org.sopt.pawkey.backendapi.domain.review.application.dto.result.ReviewHeaderResult;

public record ReviewHeaderResponseDto(
        String postTitle,
        ReviewerProfile reviewerProfile
) {
    public record ReviewerProfile(
            String profileName,
            String profileImageUrl
    ) {}

    public static ReviewHeaderResponseDto from(ReviewHeaderResult result) {
        return new ReviewHeaderResponseDto(
                result.postTitle(),
                new ReviewerProfile(
                        result.profileName(),
                        result.profileImageUrl()
                )
        );
    }
}