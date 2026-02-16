package org.sopt.pawkey.backendapi.domain.routes.walk.application.facade;


import lombok.RequiredArgsConstructor;
import org.sopt.pawkey.backendapi.domain.routes.application.service.RouteService;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.EndWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.service.WalkStreamService;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.AppendWalkPointCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.command.StartWalkCommand;
import org.sopt.pawkey.backendapi.domain.routes.walk.application.dto.result.StartWalkResult;
import org.sopt.pawkey.backendapi.domain.routes.walk.domain.model.WalkSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WalkStreamFacade {
    private final WalkStreamService walkStreamService;
    //private final RouteService routeService;

    public StartWalkResult start(StartWalkCommand command) {
        return walkStreamService.start(command);
    }

    public void appendPoint(AppendWalkPointCommand command){
        walkStreamService.appendPoint(command);
    }

    public void end(EndWalkCommand command){
        walkStreamService.end(command);
    }



}
