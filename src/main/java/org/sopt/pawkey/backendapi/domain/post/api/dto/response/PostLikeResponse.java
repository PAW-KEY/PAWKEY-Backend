package org.sopt.pawkey.backendapi.domain.post.api.dto.response;

public record PostLikeResponse(
	String status
) {
	public static final String LIKE_SUCCESS = "LIKE_SUCCESS";
	public static final String CANCEL_SUCCESS = "CANCEL_SUCCESS";

	public static PostLikeResponse of(String status) {
		return new PostLikeResponse(status);
	}
}