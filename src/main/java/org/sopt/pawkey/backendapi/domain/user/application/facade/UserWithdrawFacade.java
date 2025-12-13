package org.sopt.pawkey.backendapi.domain.user.application.facade;

import org.sopt.pawkey.backendapi.domain.user.application.service.UserService;
import org.sopt.pawkey.backendapi.global.auth.application.service.TokenService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWithdrawFacade {


	//사용자 관련 데이터 삭제
	private final UserService userService;
	private final TokenService tokenService;


	//인증 정보 삭제





}
