package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.response.SelectResult;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.SelectRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SelectQueryServiceImpl implements SelectQueryService {
	private final SelectRepository selectRepository;

	@Override
	public List<SelectResult> getAllSelects() {
		return selectRepository.findAllSelectWithOptionsOrderedById()
			.stream()
			.map(SelectResult::fromEntity)
			.toList();
	}
}

