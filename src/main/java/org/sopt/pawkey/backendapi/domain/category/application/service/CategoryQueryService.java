package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.response.CategoryResult;

public interface CategoryQueryService {
	List<CategoryResult> getAllCategories();

	List<CategoryResult> getAllCategoriesSummary();
}
