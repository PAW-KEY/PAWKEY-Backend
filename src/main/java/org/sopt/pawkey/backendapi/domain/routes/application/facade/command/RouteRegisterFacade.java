package org.sopt.pawkey.backendapi.domain.routes.application.facade.command;

import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.routes.application.service.command.RouteService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class RouteRegisterFacade {
	private final UserService userService;
	private final RouteService routeService;
	private final ImageService imageService;

	public void execute(Long userId, RouteRegisterCommand command, MultipartFile trackingImage) {
		UserEntity user = userService.getByUserId(userId);
		ImageEntity imageEntity = imageService.storeRouteImage(trackingImage);

		try {
			routeService.saveRoute(user, command, imageEntity);
		} catch (Exception e) {
			imageService.deleteImage(imageEntity);
			throw e;
		}
	}
}
