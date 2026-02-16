package org.sopt.pawkey.backendapi.domain.routes.walk.application.service;

import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.EndWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.walk.infra.persistence.WalkStreamRedisRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkStreamService {

	private final WalkStreamRedisRepository redisRepository;
	private final RouteRepository routeRepository;
	public StartWalkResult start(StartWalkCommand command) {
		//1.중복 산책 방지
		if (redisRepository.existsActiveSession(command.userId())) {
			throw new WalkBusinessException(WalkErrorCode.ALREADY_IN_WALK);
		}
		//2. 세션 생성
		//스트리밍 중 임시식별자이므로 -> String

		String routeId = redisRepository.initSession(command.userId(), command.deviceInfo());


		//3. ACTIVE 세션 바인딩(TTL)
		redisRepository.bindActiveSession( 
			command.userId(),routeId, Duration.ofHours(5)
		);
		long issuedAt = System.currentTimeMillis();
		return new StartWalkResult(routeId, issuedAt);
	}

	public void appendPoint(AppendWalkPointCommand command){
		//1. 세션 유효성 검증

		//2. 중복 산책 방지

		//3. 좌표 append

	}

	public WalkSession end(EndWalkCommand command){
		// 1. 세션 종료 처리

		// 2. Redis 좌표 → 도메인 모델로 복원


	}


}
