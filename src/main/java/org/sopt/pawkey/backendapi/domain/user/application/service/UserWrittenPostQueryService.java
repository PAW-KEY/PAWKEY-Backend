package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface UserWrittenPostQueryService {
	List<PostEntity> findWrittenPostsByUser(UserEntity user);
}
