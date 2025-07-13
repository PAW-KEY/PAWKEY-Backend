package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserPetQueryService;
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
public class UserPetQueryFacade {

	private final UserQueryRepository userQueryRepository;
	private final UserPetQueryService userPetQueryService;

	public List<PetProfileResponseDto> getUserPets(Long userId) {
		UserEntity user = userQueryRepository.getUserByUserId(userId)
			.orElseThrow(() -> new UserBusinessException(UserErrorCode.USER_NOT_FOUND));
		return userPetQueryService.getPetProfiles(user);
	}
}
