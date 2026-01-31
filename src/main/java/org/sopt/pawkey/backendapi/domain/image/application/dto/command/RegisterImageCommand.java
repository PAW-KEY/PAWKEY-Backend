package org.sopt.pawkey.backendapi.domain.image.application.dto.command;

import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;

public record RegisterImageCommand(
	String imageUrl,
	String contentType,
	int width,
	int height,
	ImageDomain domain
) {}