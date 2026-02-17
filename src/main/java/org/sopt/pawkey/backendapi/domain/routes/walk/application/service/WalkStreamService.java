package org.sopt.pawkey.backendapi.domain.routes.walk.application.service;

import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.EndWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkPoint;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.repository.WalkSessionRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkBusinessException;
import org.sopt.pawkey.backendapi.domain.routes.walk.exception.WalkErrorCode;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkStreamService {

	private final WalkSessionRepository redisRepository;

	public StartWalkResult start(StartWalkCommand command) {
		//1.중복 산책 방지
		if (redisRepository.existsActiveSession(command.userId())) {
			throw new WalkBusinessException(WalkErrorCode.ALREADY_IN_WALK);
		}
		//2. 세션 생성
		//스트리밍 중 임시식별자이므로 -> String
		long startedAt = System.currentTimeMillis();
		String routeId = redisRepository.createSession(command.userId(), command.deviceInfo(),startedAt);


		//3. ACTIVE 세션 바인딩(TTL)
		redisRepository.bindActiveSession( 
			command.userId(),routeId, Duration.ofHours(5).toSeconds()
		);

		return new StartWalkResult(routeId, startedAt);
	}

	public void appendPoint(AppendWalkPointCommand command){
		String routeId = command.routeId();


		//1. 중복 산책 방지
		boolean duplicated = redisRepository.isDuplicated(routeId, command.timestamp());
		if (duplicated) {
			return;
		}

		//2. 좌표 append
		WalkPoint point = new WalkPoint(command.lat(), command.lng(), command.timestamp());
		redisRepository.appendPoint(routeId, point);

	}

	public WalkSession end(EndWalkCommand command){
		String routeId = command.routeId();
		// 1. 세션 종료 처리
		redisRepository.endSession(routeId);

		// 2. Redis 좌표 → 도메인 모델로 복원
		WalkSession session = redisRepository.loadSession(routeId);

		//3. ACTIVE 세션 해지
		redisRepository.clearActiveSession(session.getUserId());

		return session;

	}


}
