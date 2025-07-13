package org.sopt.pawkey.backendapi.domain.post.application.service;

import org.sopt.pawkey.backendapi.domain.post.application.dto.command.PostCreateCommand;
import org.sopt.pawkey.backendapi.domain.post.infra.persistence.entity.PostEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PostService {
	PostEntity findById(Long postId);

	void createPost(UserEntity writer, PostCreateCommand command);

}
