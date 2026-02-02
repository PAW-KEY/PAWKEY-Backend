package org.sopt.pawkey.backendapi.domain.image.application.dto.command;

public record IssuePresignedUrlCommand (String domain,
										String contentType){
}
