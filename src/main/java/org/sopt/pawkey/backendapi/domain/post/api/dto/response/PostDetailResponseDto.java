package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostDetailResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.RouteDisplayResult;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

import java.util.List;

public record PostDetailResponseDto(
        Long postId,
        String title,
        String description,
        boolean isPublic,
        boolean isMine,

        AuthorDto authorInfo,

        RouteDisplayDto routeDisplay,

        List<String> categoryTagTexts,

        List<WalkImageDto> walkImages
) {

    public static PostDetailResponseDto from(GetPostDetailResult result) {
        return new PostDetailResponseDto(
                result.postId(),
                result.title(),
                result.description(),
                result.isPublic(),
                result.isMine(),
                result.authorInfo(),
                RouteDisplayDto.from(result.routeDisplay()),
                result.categoryTagTexts(),
                result.walkImages().stream()
                        .map(w -> new WalkImageDto(w.imageId(), w.imageUrl()))
                        .toList()
        );
    }

    public record RouteDisplayDto(
            Long routeId,
            String locationText,
            String dateTimeText,
            List<String> metaTagTexts,
            String routeImageUrl
    ) {
        public static RouteDisplayDto from(RouteDisplayResult r) {
            return new RouteDisplayDto(
                    r.routeId(),
                    r.locationText(),
                    r.dateTimeText(),
                    r.metaTagTexts(),
                    r.routeImageUrl()
            );
        }
    }

    public record WalkImageDto(
            Long imageId,
            String imageUrl
    ) {}
}