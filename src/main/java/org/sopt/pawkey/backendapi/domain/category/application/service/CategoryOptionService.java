package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.domain.repository.CategoryOptionRepository;
import org.sopt.pawkey.backendapi.domain.category.infra.persistence.entity.CategoryOptionEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryOptionService {
	private final CategoryOptionRepository categoryOptionRepository;

	public List<CategoryOptionEntity> getAllWhereInIds(List<Long> selectedOptionIds) {
		return categoryOptionRepository.findAllWhereInIds(selectedOptionIds);
	}
}
