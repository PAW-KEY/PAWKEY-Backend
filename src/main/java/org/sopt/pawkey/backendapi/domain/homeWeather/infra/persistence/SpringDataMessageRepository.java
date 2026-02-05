package org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence;

import java.util.Optional;

import org.sopt.pawkey.backendapi.domain.homeWeather.infra.persistence.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataMessageRepository extends JpaRepository<MessageEntity, Long> {

	@Query("SELECT m FROM MessageEntity m " +
		"JOIN FETCH m.tempRange t " +
		"JOIN FETCH m.rainyRange r " +
		"WHERE :temp >= t.minTemp AND :temp <= t.maxTemp " +
		"AND :prob >= r.minProb AND :prob <= r.maxProb")
	Optional<MessageEntity> findMessageByWeather(
		@Param("temp") int temp,
		@Param("prob") int prob
	);
}