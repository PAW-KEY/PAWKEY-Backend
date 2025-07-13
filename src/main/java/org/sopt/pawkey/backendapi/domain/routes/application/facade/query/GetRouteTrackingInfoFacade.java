package org.sopt.pawkey.backendapi.domain.routes.application.facade.query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.sopt.pawkey.backendapi.domain.pet.infra.persistence.entity.PetEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.GetRouteTrackingInfoCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.result.GetRouteTrackingInfoResult;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class GetRouteTrackingInfoFacade {

	private final UserService userService;
	private final RouteService routeService;

	private static String getFormattedDate(LocalDateTime date) {
		return date
			.format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E) | a hh:mm")
				.withLocale(Locale.KOREAN));
	}

	public GetRouteTrackingInfoResult execute(Long userId, GetRouteTrackingInfoCommand getRouteTrackingInfoCommand) {
		UserEntity user = userService.getByUserId(userId);

		RouteEntity route = routeService.getRouteById(getRouteTrackingInfoCommand.routeId());

		route.validateOwnership(user);
		String dateDescription = getFormattedDate(route.getCreatedAt());
		String locationDescription = route.getRegion().getFullRegionName();

		PetEntity pet = user.getPetOrThrow();

		return new GetRouteTrackingInfoResult(
			GetRouteTrackingInfoResult.RouteDto.builder()
				.id(route.getRouteId())
				.locationDescription(locationDescription)
				.dateDescription(dateDescription)
				.build(),
			pet.getName()
		);
	}
}
