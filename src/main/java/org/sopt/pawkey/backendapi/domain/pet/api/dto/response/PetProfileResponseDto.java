package org.sopt.pawkey.backendapi.domain.pet.api.dto.response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.global.enums.Gender;

public record PetProfileResponseDto(
	Long petId,
	String imageUrl,
	String name,
	LocalDate birth,
	String age,
	String gender,
	boolean isNeutered,
	String breed,
	String dbtiName,
	String dbtiDescription
) {
	//imageUrl을 외부에서 주입받도록 시그니처 변경
	public static PetProfileResponseDto of(PetEntity pet, String imageUrl,String formattedAge, String dbtiDescription) {
		return new PetProfileResponseDto(
			pet.getPetId(),
			imageUrl,
			pet.getName(),
			pet.getBirth(),
			formattedAge,
			convertGender(pet.getGender()),
			pet.isNeutered(),
			pet.getBreed() != null ? pet.getBreed().getName() : null,
			pet.getDbtiResult() != null ? pet.getDbtiResult().getDbtiType().name() : null,
			dbtiDescription
		);
	}

	public static String formatAge(LocalDate birth) {
		if (birth == null)
			return "정보 없음";

		long months = ChronoUnit.MONTHS.between(birth, LocalDate.now());
		if (months < 0) {
			return "정보 없음";
		}
		if (months < 24) {
			return months + "개월";
		} else {
			return (months / 12) + "살";
		}
	}

	private static String convertGender(Gender gender) {
		if (gender == null)
			return "알 수 없음";

		return switch (gender) {
			case M -> "남아";
			case F -> "여아";
		};
	}
}