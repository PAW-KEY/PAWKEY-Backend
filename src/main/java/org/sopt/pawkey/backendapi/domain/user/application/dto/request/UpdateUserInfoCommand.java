package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import java.time.LocalDate;

import org.sopt.pawkey.backendapi.global.enums.Gender;

public record UpdateUserInfoCommand(
	String name,
	LocalDate birth,
	Gender gender

) {
}