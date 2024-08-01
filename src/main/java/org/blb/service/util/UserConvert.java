package org.blb.service.util;


import org.blb.DTO.user.UserNewDTO;
import org.blb.DTO.user.UserWithIdDTO;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserConvert {
    public User fromDTOtoUser(UserNewDTO user, Role role, State state) {
        return   new User(user.getName(),
                user.getPassword(),
                user.getEmail(),
                 role,state,"");
    }
    public User fromIdDTOtoUser(UserWithIdDTO user, Role role, State state) {
        return   new User(user.getId(), user.getName(),
                user.getPassword(),
                user.getEmail(), role, state,"");
    }
}
