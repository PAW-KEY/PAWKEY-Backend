package org.sopt.pawkey.backendapi.global.enums;

public enum Gender {
	M("남성"),
	F("여성");

	private final String description;

	Gender(String description) {
		this.description = description;
	}
}