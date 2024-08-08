package org.blb.DTO.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TeamResponse {
    private List<TeamResponseDTO> team;
}
