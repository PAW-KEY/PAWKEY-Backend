package org.sopt.pawkey.backendapi.domain.user.application.facade.query;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserPetQueryService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPetQueryFacade {

	private final UserPetQueryService userPetQueryService;
	private final UserService userService;

	public List<PetProfileResponseDto> getUserPets(Long userId) {

		UserEntity user = userService.getByUserId(userId);

		return userPetQueryService.getPetProfiles(user);
	}
}
