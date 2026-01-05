package org.sopt.pawkey.backendapi.domain.user.application.dto.request;

import java.time.LocalDate;

public record UpdateUserInfoCommand(
	String name,
	LocalDate birth,
	String gender

) {
}