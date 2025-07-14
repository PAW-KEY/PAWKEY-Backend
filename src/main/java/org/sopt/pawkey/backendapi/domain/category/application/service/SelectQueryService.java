package org.sopt.pawkey.backendapi.domain.category.application.service;

import java.util.List;

import org.sopt.pawkey.backendapi.domain.category.application.dto.response.SelectResult;

public interface SelectQueryService {
	List<SelectResult> getAllSelects();
}