package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.response.CategoryResult;
import org.sopt.pawkey.backendapi.domain.category.application.dto.response.SelectResult;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryRepository;
import org.sopt.pawkey.backendapi.domain.category.domain.repository.SelectRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SelectQueryServiceImpl implements SelectQueryService {
	private final SelectRepository selectRepository;

	@Override
	public List<SelectResult> getAllSelects() {
		return selectRepository.findAllSelectWithOptions()
			.stream()
			.map(SelectResult::fromEntity)
			.toList();
	}
}

