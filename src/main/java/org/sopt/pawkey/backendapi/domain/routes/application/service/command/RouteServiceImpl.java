package org.sopt.pawkey.backendapi.domain.routes.application.service.command;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

	private final RouteRepository routeRepository;

	@Override
	public RouteEntity saveRoute(UserEntity user, RouteRegisterCommand command, ImageEntity trackingImage) {
		RouteEntity route = RouteEntity.createRoute(user, command, trackingImage);

		return routeRepository.save(route);
	}
}
