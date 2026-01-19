package org.sopt.pawkey.backendapi.domain.tempImage.application.dto.command;

public record IssuePresignedUrlCommand (String domain,
										String contentType){
}
