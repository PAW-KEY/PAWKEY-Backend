package org.sopt.pawkey.backendapi.domain.image.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;

public record IssuePresignedUrlRequestDTO(
	@NotBlank String domain,      // route, profile , walk...
	@NotBlank String contentType
) {
	public ImageDomain getDomainEnum(){
		return ImageDomain.from(this.domain);

	}
}
