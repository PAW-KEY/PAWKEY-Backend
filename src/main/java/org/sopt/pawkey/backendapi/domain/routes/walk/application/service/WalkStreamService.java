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
		long startedAt = System.currentTimeMillis();
		String routeId = redisRepository.createSession(command.userId(), command.deviceInfo(),startedAt);

		//3. ACTIVE 세션(TTL)
		redisRepository.bindActiveSession( 
			command.userId(),routeId, Duration.ofHours(5).toSeconds()
		);

		return new StartWalkResult(routeId, startedAt);
	}

	public void appendPoint(AppendWalkPointCommand command){
		String routeId = command.routeId();

		//(추가)
		//0. 세션 로드 + 소유권 검증
		WalkSession session = redisRepository.loadSession(routeId);
		if (!session.getUserId().equals(command.userId())) {
			throw new WalkBusinessException(WalkErrorCode.INVALID_SESSION_OWNER);
		}


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

		WalkSession session = redisRepository.loadSession(routeId);

		if (!session.getUserId().equals(command.userId())) {
			throw new WalkBusinessException(WalkErrorCode.INVALID_SESSION_OWNER);
		}

		redisRepository.endSession(routeId);

		redisRepository.clearActiveSession(command.userId());

		return session;
	}

}
