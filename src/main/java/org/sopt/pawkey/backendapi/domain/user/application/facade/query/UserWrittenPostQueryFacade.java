package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.user.api.dto.response.MyPostResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserWrittenPostQueryService;
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
	private final UserWrittenPostQueryService writtenPostQueryService;

	public List<MyPostResponseDto> getMyPosts(Long userId) {

		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));

		return writtenPostQueryService.findWrittenPostsByUser(user).stream()
			.map(MyPostResponseDto::from)
			.toList();
	}
}