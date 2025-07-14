package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.service.PetService;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
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

	public UserRegisterResponseDto execute(UserRegisterCommand command, MultipartFile petProfileImage){
		ImageEntity imageEntity = null;
		try{
		UserEntity user = userService.saveUser(command.userCommand());
		imageEntity = imageService.storePetProfileImage(petProfileImage);

		PetEntity pet = petService.savePet(command.petCommand(),user,imageEntity);



		return UserRegisterResponseDto.from(user, pet);
		}catch (Exception e){
			if (imageEntity != null) {
				imageService.deleteImage(imageEntity); // 저장소 및 DB 동기화 처리
			}
			throw e; // 트랜잭션 롤백 유도
		}



}
}
