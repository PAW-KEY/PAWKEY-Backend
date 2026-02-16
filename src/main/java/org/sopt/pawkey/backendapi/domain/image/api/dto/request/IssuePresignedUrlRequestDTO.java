package org.sopt.pawkey.backendapi.domain.image.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;

public record IssuePresignedUrlRequestDTO(
	@NotBlank String domain,      // route, profile , walk...
	@NotBlank String contentType
) {

	@JsonIgnore
	@Schema(hidden = true)
	public ImageDomain getDomainEnum(){
		return ImageDomain.from(this.domain);

	}
}
