package org.sopt.pawkey.backendapi.domain.routes.application.dto.result;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.region.infra.persistence.entity.RegionEntity;
import org.sopt.pawkey.backendapi.domain.routes.exception.RouteBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.sopt.pawkey.backendapi.global.exception.GlobalErrorCode;

import lombok.Builder;

public record GetRouteTrackingInfoResult(
	RouteDto routeDto,
	String petName
) {
	@Builder
	public record RouteDto(
		Long id,
		String locationDescription,
		String dateDescription
	) {

	}

}
