package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

import java.util.List;

public record PostPagingResponseDto(
        List<PostCardResponseDto> posts,
        String nextCursor,
        boolean hasNext
) {}