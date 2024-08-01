package org.blb.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.models.user.Role;


import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data with list of user roles and user statuses")

public class UsersRolesDTO {
    List<Role> roles;
}
