package org.sopt.pawkey.backendapi.domain.weather.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.weather.domain.repository.MessageRepository;
import org.sopt.pawkey.backendapi.domain.weather.infra.persistence.entity.MessageEntity;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
	private final SpringDataMessageRepository springDataMessageRepository;

	@Override
	public Optional<MessageEntity> findMessageByWeather(int temp, int prob) {
		return springDataMessageRepository.findMessageByWeather(temp, prob);
	}
}