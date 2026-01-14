package org.sopt.pawkey.backendapi.domain.dbti.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbtiType {
	EPR, EPF, ESR, ESF, IPR, IPF, ISR, ISF;

	public static String determine(int ei, int ps, int rf) {
		return (ei >= 2 ? "E" : "I") +
			(ps >= 2 ? "P" : "S") +
			(rf >= 2 ? "R" : "F");
	}
}