package org.sopt.pawkey.backendapi.domain.weather.domain.repository;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.MessageEntity;

public interface MessageRepository {
	Optional<MessageEntity> findMessageByWeather(int temp, int prob);
}
