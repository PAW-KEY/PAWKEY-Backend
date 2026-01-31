package org.sopt.pawkey.backendapi.domain.image.domain;

public enum ImageDomain { //S3 경로의 최상위 prefix로 사용({domain}/{uuid}.{ext})
	WALK("walk"),
	ROUTE("route"),
	PROFILE("profile");

	private final String path;

	ImageDomain(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
