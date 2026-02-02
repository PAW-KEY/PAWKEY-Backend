package org.sopt.pawkey.backendapi.domain.image.api.dto.request;

public record ImageRegisterRequestDto( String imageUrl,
									   String contentType,
									   int width,
									   int height,
									   String domain) {
}
