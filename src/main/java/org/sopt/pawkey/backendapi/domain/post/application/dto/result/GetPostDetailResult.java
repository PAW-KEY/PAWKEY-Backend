package org.sopt.pawkey.backendapi.domain.post.application.dto.result;

import lombok.Builder;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.AuthorDto;

import java.util.List;

@Builder
public record GetPostDetailResult(
        Long postId,
        String title,
        String description,
        boolean isPublic,
        boolean isMine,
        boolean hasReviewed,

        AuthorDto authorInfo,
        RouteDisplayResult routeDisplay,

        List<String> categoryTagTexts,
        List<WalkImageResult> walkImages
) {}