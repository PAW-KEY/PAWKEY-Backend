package org.sopt.pawkey.backendapi.domain.user.api.dto;

public record AuthorDto(
	Long authorId,
	Long petId,
	String petName,
	String petProfileImage
) {
}