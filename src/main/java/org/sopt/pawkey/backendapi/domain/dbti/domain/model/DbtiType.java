package org.sopt.pawkey.backendapi.domain.dbti.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DbtiType {
	EPR, EPF, ESR, ESF, IPR, IPF, ISR, ISF;

	public static DbtiType determine(int ei, int ps, int rf) {
		String code = (ei >= 2 ? "I" : "E") +
			(ps >= 2 ? "S" : "P") +
			(rf >= 2 ? "R" : "F");
		return DbtiType.valueOf(code);
	}
}