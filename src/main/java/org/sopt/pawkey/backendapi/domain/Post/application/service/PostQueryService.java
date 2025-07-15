package org.sopt.pawkey.backendapi.domain.post.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.api.dto.response.PostResponseDto;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;

public interface PostQueryService {

	PostResponseDto getPostDetail(PostEntity post, boolean isLiked, String routeMapImage, List<String> walkingImages);

}
