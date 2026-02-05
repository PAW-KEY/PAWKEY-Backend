package org.sopt.pawkey.backendapi.domain.pet.application.facade;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.dbti.application.service.DbtiQueryService;
import org.sopt.pawkey.backendapi.domain.dbti.infra.persistence.entity.DbtiEntity;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.BreedListResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.api.dto.response.PetProfileResponseDto;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryFacade {

	private final PetQueryService petQueryService;
	private final DbtiQueryService dbtiQueryService;

	public BreedListResponseDto getBreedList() {
		List<BreedListResponseDto.BreedDto> breeds = petQueryService.getAllBreeds();
		return BreedListResponseDto.from(breeds);
	}

	public PetProfileResponseDto getPetProfile(Long userId, Long petId) {
		PetEntity pet = petQueryService.getPetDetail(userId, petId);
		String formattedAge = petQueryService.getFormattedAge(pet.getBirth());

		String dbtiName = null;
		if (pet.getDbtiResult() != null) {
			DbtiEntity dbti = dbtiQueryService.getDbtiInfo(pet.getDbtiResult().getDbtiType());
			dbtiName = dbti.getName();
		}

		return PetProfileResponseDto.of(pet, formattedAge, dbtiName);
	}
}
