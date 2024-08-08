package org.blb.service.team;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.team.TeamResponse;
import org.blb.DTO.team.TeamResponseDTO;
import org.blb.repository.team.TeamRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    public TeamResponse getTeam(){
        return new TeamResponse(teamRepository.findAll().stream()
                .map(item -> new TeamResponseDTO(item.getName(), item.getRole(),
                        item.getDescription(),
                item.getPhotoUrl(), item.getUrl())).toList());
    }
}
