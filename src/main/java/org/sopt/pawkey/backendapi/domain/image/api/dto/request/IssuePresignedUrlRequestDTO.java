package org.sopt.pawkey.backendapi.domain.image.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record IssuePresignedUrlRequestDTO(
	@NotBlank String domain,      // route, profile , walk...
	@NotBlank String contentType
) {}
