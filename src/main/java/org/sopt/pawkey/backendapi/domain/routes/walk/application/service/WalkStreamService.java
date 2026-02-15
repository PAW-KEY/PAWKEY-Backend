package org.sopt.pawkey.backendapi.domain.routes.walk.application.service;

import org.sopt.pawkey.backendapi.domain.routes.domain.repository.RouteRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.infra.persistence.WalkStreamRedisRepository;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkStreamService {

	private final WalkStreamRedisRepository redisRepository;
	private final RouteRepository routeRepository;
	public StartWalkResult start(StartWalkCommand command) {
		String routeId = redisRepository.initSession(command.userId(), command.deviceInfo());
		long issuedAt = System.currentTimeMillis();
		return new StartWalkResult(routeId, issuedAt);
	}

	public void appendPoint(AppendWalkPointCommand command) {
		if (redisRepository.isDuplicated(command.routeId(), command.timestamp())) return;
		redisRepository.appendPoint(command);
	}

	@Transactional
	public void end(String routeId) {
		List<WalkPoint> points = redisRepository.getAllPoints(routeId);
		routeRepository.saveRoute(routeId, points);  // 🔥 DB 영속
		redisRepository.markSessionEnded(routeId);
	}


}
