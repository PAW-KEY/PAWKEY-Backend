package org.sopt.pawkey.backendapi.domain.image.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.sopt.pawkey.backendapi.domain.image.domain.ImageDomain;

public record ImageRegisterRequestDto(String imageUrl,
									  String contentType,
									  int width,
									  int height,
									  String domain) {


	@JsonIgnore
	public ImageDomain getDomainEnum() {
		return ImageDomain.from(this.domain);
	}
}
