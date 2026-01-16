package org.sopt.pawkey.backendapi.domain.walkPreparation.infra.persistence.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.ArrayList;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
	private static final String SPLIT_CHAR = "\\|";
	private static final String JOIN_CHAR = "|";

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		if (attribute == null || attribute.isEmpty())
			return null;
		return attribute.stream()
			.filter(Objects::nonNull)
			.collect(Collectors.joining(JOIN_CHAR));
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isBlank()) {
			return new java.util.ArrayList<>();
		}
		return Arrays.stream(dbData.split(SPLIT_CHAR))
			.map(String::trim)
			.collect(Collectors.toCollection(ArrayList::new));
	}
}
