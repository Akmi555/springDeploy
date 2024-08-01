package org.blb.service.user;

import lombok.AllArgsConstructor;
import org.blb.DTO.user.UserRoleDTO;
import org.blb.DTO.user.UserStateDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.util.UserConvert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserUpdateService {
   private final UserRepository repository;
   private final RoleRepository roleRepository;
    UserConvert userConvert;
    UserFindService userFindService;

//    public void EditUser(UserWithIdDTO user) {
//        User oldUser = userFindService.findUserById(user.getId());
//        User newUser = userConvert.fromIdDTOtoUser(user, oldUser.getRole());
//        repository.save(newUser);
//    }
//
    public void changeState(UserStateDTO state) {
        User user = userFindService.findUserById(state.getId());
        if (state.getConfirmed()) {
            user.setState(State.CONFIRMED);
        } else {
            user.setState(State.BANNED);
        }
        repository.save(user);
    }
    public void changeRole(UserRoleDTO dto) {
        User user = userFindService.findUserById(dto.getUserId());
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(()->new NotFoundException("Role with id: "+dto.getRoleId()+" not found"));
        user.setRole(role);
        repository.save(user);
    }


}
