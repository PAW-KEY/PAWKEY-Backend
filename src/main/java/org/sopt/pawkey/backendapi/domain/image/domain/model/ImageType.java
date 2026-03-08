package org.sopt.pawkey.backendapi.domain.image.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {

	PET_PROFILE(1L),
	ROUTE(null),
	WALK_POST(null),
	ETC(null);

	private final Long defaultId;
}