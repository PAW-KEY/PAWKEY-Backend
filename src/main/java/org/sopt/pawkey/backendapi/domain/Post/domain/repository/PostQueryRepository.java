package org.sopt.pawkey.backendapi.domain.post.domain.repository;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.request.FilterPostsRequestDto;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostCardResult;
import org.sopt.pawkey.backendapi.domain.post.application.dto.result.GetPostResult;

public interface PostQueryRepository {

	List<GetPostCardResult> findByFilter(FilterPostsRequestDto requestDto, Long userId);
}
