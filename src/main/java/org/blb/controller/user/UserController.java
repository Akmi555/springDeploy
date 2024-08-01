package org.blb.controller.user;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.user.UserResponseDTO;
import org.blb.DTO.user.UserRoleDTO;
import org.blb.DTO.user.UserStateDTO;
import org.blb.DTO.user.UsersRolesDTO;
import org.blb.controller.api.user.UserAPI;
import org.blb.repository.user.UserRepository;
import org.blb.service.user.UserFindService;
import org.blb.service.user.UserUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RequiredArgsConstructor
@RestController
public class UserController implements UserAPI {
    private final UserFindService userFindService;
    private final UserRepository userRepository;
    private final UserUpdateService userUpdateService;

    @Override
    public ResponseEntity<List<UserResponseDTO>> getUsers(Integer page) {
        return ResponseEntity.ok(userFindService.findAllUsers(page));
    }

    @Override
    public ResponseEntity<UsersRolesDTO> getUsersRole() {
        return ResponseEntity.ok(userFindService.getUsersStateData());
    }

    @Override
    public ResponseEntity<StandardResponseDto> updateUsersState(UserStateDTO state) {
       userUpdateService.changeState(state);
        return ResponseEntity.ok(new StandardResponseDto("User update successfully"));
    }

    @Override
    public ResponseEntity<StandardResponseDto> changeUserRole(UserRoleDTO dto) {
        userUpdateService.changeRole(dto);
        return ResponseEntity.ok(new StandardResponseDto("User update successfully"));
    }




}
