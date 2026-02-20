package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.result.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.DurationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DurationQueryService {
	private final DurationRepository durationRepository;

	@Transactional(readOnly = true)
	public List<CategoryResult> getAllDurations() {
		return durationRepository.findAllDurationWithOptions().stream()
			.map(CategoryResult::fromDuration)
			.toList();
	}
}
