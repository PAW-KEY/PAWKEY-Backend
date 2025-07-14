package org.sopt.pawkey.backendapi.domain.pet.application.service;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.pet.application.dto.request.CreatePetCommand;
import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface PetService {
	/**
 * Saves a new pet entity associated with the specified user and profile image.
 *
 * @param command      the command containing pet creation details
 * @param user         the user to whom the pet will be linked
 * @param profileImage the image to be set as the pet's profile picture
 * @return the persisted PetEntity representing the newly created pet
 */
PetEntity savePet(CreatePetCommand command, UserEntity user, ImageEntity profileImage);
}


