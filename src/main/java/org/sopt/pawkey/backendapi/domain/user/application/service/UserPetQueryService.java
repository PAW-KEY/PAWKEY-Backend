package org.sopt.pawkey.backendapi.domain.user.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface UserPetQueryService {
	List<PetProfileResponseDto> getPetProfiles(UserEntity user);
}
