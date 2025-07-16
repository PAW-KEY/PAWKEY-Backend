package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.region.application.service.RegionService;
import org.sopt.pawkey.backendapi.domain.region.domain.model.RegionType;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionBusinessException;
import org.sopt.pawkey.backendapi.domain.region.exception.RegionErrorCode;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.user.api.dto.response.UserRegisterResponseDto;
import org.sopt.pawkey.backendapi.domain.user.application.dto.request.UserRegisterCommand;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class UserRegisterFacade {

	private final UserService userService;
	private final ImageService imageService;
	private final PetService petService;
	private final RegionService regionService;

	public UserRegisterResponseDto execute(UserRegisterCommand command, MultipartFile petProfileImage) {
		ImageEntity imageEntity = null;
		try {
			RegionEntity region = regionService.getDongTypeRegionByIdOrThrow(command.userCommand().regionId());
			UserEntity user = userService.saveUser(command.userCommand(), region);

			imageEntity = imageService.storePetProfileImage(petProfileImage);
			PetEntity pet = petService.savePet(command.petCommand(), user, imageEntity);

			return UserRegisterResponseDto.from(user, pet);
		} catch (Exception e) {
			if (imageEntity != null) {
				imageService.deleteImage(imageEntity);
			}
			throw e;
		}

	}
}
