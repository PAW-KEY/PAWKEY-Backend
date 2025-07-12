package org.sopt.pawkey.backendapi.domain.routes.application.service.command;

import org.sopt.pawkey.backendapi.domain.image.infra.persistence.entity.ImageEntity;
import org.sopt.pawkey.backendapi.domain.routes.application.dto.command.RouteRegisterCommand;
import org.sopt.pawkey.backendapi.domain.user.infra.persistence.entity.UserEntity;

public interface RouteService {
	Void saveRoute(UserEntity user, RouteRegisterCommand command, ImageEntity trackingImage);
}
