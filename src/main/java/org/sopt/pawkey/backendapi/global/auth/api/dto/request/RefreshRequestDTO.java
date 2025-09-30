package org.sopt.pawkey.backendapi.global.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO (@NotBlank String refreshToken) {}
