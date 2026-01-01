package org.sopt.pawkey.backendapi.domain.routes.walktest;

import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkPointRequest;
import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkStartRequest;
import org.sopt.pawkey.backendapi.domain.routes.walktest.dto.WalkStartResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkStreamService {

	private final WalkStreamRedisRepository redisRepository;

	public WalkStartResponse startSession(WalkStartRequest req) {
		String routeId = redisRepository.initSession(req.userId(), req.deviceInfo());
		long issuedAt = System.currentTimeMillis();

		log.info("[WalkStart] userId={}, routeId={}, issuedAt={}",
			req.userId(), routeId, issuedAt);

		return new WalkStartResponse(routeId, issuedAt);
	}

	public void appendPoint(WalkPointRequest req) {
		boolean duplicated = redisRepository.isDuplicated(req.routeId(), req.timestamp());

		if (duplicated) {
			log.info("[Sync:skip] routeId={}, ts={} (duplicate)", req.routeId(), req.timestamp());
			return;
		}

		redisRepository.saveLatestPoint(req.routeId(), req);

		log.info("[Sync:append] routeId={}, lat={}, lng={}, ts={}",
			req.routeId(), req.lat(), req.lng(), req.timestamp());
	}

	public void endSession(String routeId) {
		// 이후 Polygon 계산을 위한 예약 작업 혹은 Flush 준비
		redisRepository.markSessionEnded(routeId);
		log.info("[WalkEnd] routeId={} session ended", routeId);
	}


}
