package org.sopt.pawkey.backendapi.domain.image.application.dto.command;

public record RegisterImageCommand(
	String imageUrl,
	String contentType,
	int width,
	int height
) {}