package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;

public record PetProfileResponseDto(
	Long petId,
	String imageUrl,
	String name,
	LocalDate birth,
	long age,
	String gender,
	boolean isNeutered,
	String breed,
	String dbti
) {
	public static PetProfileResponseDto from(PetEntity pet) {
		return new PetProfileResponseDto(
			pet.getPetId(),
			pet.getProfileImage() != null ? pet.getProfileImage().getImageUrl() : null,
			pet.getName(),
			pet.getBirth(),
			calculateAgeInMonths(pet.getBirth()),
			convertGender(pet.getGender()),
			pet.isNeutered(),
			pet.getBreed() != null ? pet.getBreed().getName() : null,
			pet.getDbtiResult() != null ? pet.getDbtiResult().getDbtiType().name() : "DBTI 검사 미완료"
		);
	}

	private static long calculateAgeInMonths(LocalDate birth) {
		if (birth == null)
			return 0;
		return ChronoUnit.MONTHS.between(birth, LocalDate.now());
	}

	private static String convertGender(String gender) {
		if ("M".equals(gender)) {
			return "남아";
		} else if ("F".equals(gender)) {
			return "여아";
		}
		throw new IllegalArgumentException("올바르지 않은 성별 값입니다: " + gender);
	}
}