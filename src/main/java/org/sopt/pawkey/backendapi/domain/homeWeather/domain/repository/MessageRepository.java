package org.sopt.pawkey.backendapi.domain.homeWeather.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.MessageEntity;

public interface MessageRepository {
	Optional<MessageEntity> findMessageByWeather(int temp, int prob);
}
