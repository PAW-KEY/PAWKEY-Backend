package org.sopt.pawkey.backendapi.domain.homeWeather.domain.service;

import org.sopt.pawkey.backendapi.domain.homeWeather.domain.model.WeatherMessage;
import org.sopt.pawkey.backendapi.domain.homeWeather.domain.repository.MessageRepository;
import org.sopt.pawkey.backendapi.domain.homeWeather.exception.WeatherBusinessException;
import org.sopt.pawkey.backendapi.domain.homeWeather.exception.WeatherErrorCode;
import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.MessageEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherCommentaryGenerator {

	private final MessageRepository messageRepository;

	public WeatherMessage generate(int temp, int rainProb) {
		MessageEntity messageEntity = messageRepository.findMessageByWeather(temp, rainProb)
			.orElseThrow(() -> new WeatherBusinessException(WeatherErrorCode.WEATHER_MESSAGE_NOT_FOUND));

		return new WeatherMessage(
			messageEntity.getMainMessage(),
			messageEntity.getSubMessage()
		);
	}
}