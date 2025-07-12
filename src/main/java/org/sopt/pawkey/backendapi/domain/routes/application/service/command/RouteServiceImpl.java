package org.sopt.pawkey.backendapi.domain.routes.application.service.command;

import org.sopt.pawkey.backendapi.domain.common.ImageStorage;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.infra.persistence.entity.RouteEntity;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

	private final RouteRepository routeRepository;
	private final ImageStorage imageStorage;

	@Override
	public Void saveRoute(UserEntity user, RouteRegisterCommand command, ImageEntity trackingImage) {
		RouteEntity route = RouteEntity.createRoute(user, command, trackingImage);

		routeRepository.save(route);
		return null;
	}
}
