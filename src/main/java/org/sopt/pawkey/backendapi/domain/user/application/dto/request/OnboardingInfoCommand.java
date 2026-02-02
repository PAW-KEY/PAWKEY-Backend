package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.global.enums.Gender;

public record OnboardingInfoCommand(
	String name,
	LocalDate birth,
	Gender gender,
	Long regionId
) {

	public static OnboardingInfoCommand of(String name, LocalDate birth, Gender gender, Long regionId) {
		return new OnboardingInfoCommand(name, birth, gender, regionId);
	}
}