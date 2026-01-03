package org.sopt.pawkey.backendapi.domain.pet.api.controller;

import static org.sopt.pawkey.backendapi.global.constants.AppConstants.*;

import org.sopt.pawkey.backendapi.domain.pet.application.service.PetQueryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/pets/traits")
public class PetController {

	private final PetQueryService petQueryService;

}
