package org.sopt.pawkey.backendapi.domain.post.application.facade.command;

import org.sopt.pawkey.backendapi.domain.common.ImageStorage;
import org.sopt.pawkey.backendapi.domain.image.application.service.command.ImageService;
import org.sopt.pawkey.backendapi.domain.post.application.service.PostService;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {
	private final UserService userService;
	private final PostService postService;
	private final ImageStorage imageStorage;
	private final RouteService routeService;
	private final ImageService imageService;

}
