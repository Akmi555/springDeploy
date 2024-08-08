package org.blb.controller.team;

import lombok.AllArgsConstructor;
import org.blb.DTO.team.TeamResponse;
import org.blb.controller.api.team.TeamApi;
import org.blb.service.team.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TeamController implements TeamApi {
    private final TeamService teamService;

    @Override
    public ResponseEntity<TeamResponse> getTeam() {
        return ResponseEntity.ok(teamService.getTeam());
    }
}
