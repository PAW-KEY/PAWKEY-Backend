package org.sopt.pawkey.backendapi.domain.routes.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.sopt.pawkey.backendapi.domain.coordinate.Coordinate;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RouteRegisterRequest(
	@NotEmpty(message = "루트 좌표 리스트는 비어 있을 수 없습니다.")
	@Valid
	List<@Valid Coordinate> coordinates,

	@Positive(message = "거리는 양수여야 합니다.")
	int distance,

	@Positive(message = "소요 시간은 양수여야 합니다.")
	int duration,

	@NotNull(message = "산책 시작 시간은 필수입니다.")
	LocalDateTime startedAt,

	@NotNull(message = "산책 종료 시간은 필수입니다.")
	LocalDateTime endedAt,

	@Positive(message = "걸음 수는 양수여야 합니다.")
	int stepCount,

	@NotNull(message = "산책 루트에는 대표 이미지가 반드시 필요합니다.")
	Long trackingImageId
) {
	public RouteRegisterCommand toCommand() {
		return RouteRegisterCommand.builder()
			.coordinates(coordinates())
			.distance(distance())
			.duration(duration())
			.startedAt(startedAt())
			.endedAt(endedAt())
			.stepCount(stepCount())
			.trackingImageId(trackingImageId)
			.build();
	}

}
