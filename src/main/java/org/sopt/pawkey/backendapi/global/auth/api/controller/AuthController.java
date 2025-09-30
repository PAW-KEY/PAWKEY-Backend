package org.sopt.pawkey.backendapi.global.auth.api.controller;

import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final TokenService tokenService;
}
