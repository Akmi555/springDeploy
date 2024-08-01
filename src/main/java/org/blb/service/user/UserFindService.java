package org.blb.service.user;


import org.blb.DTO.user.UserResponseDTO;
import org.blb.DTO.user.UsersRolesDTO;
import org.blb.exeption.AlreadyExistException;
import org.blb.exeption.NotFoundException;

import org.blb.models.user.User;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserFindService {
    private final RoleRepository roleRepository;
    UserRepository repository;

    public UserFindService(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    public List<UserResponseDTO> findAllUsers(Integer currentPage) {
        Pageable page = PageRequest.of(currentPage, 20);
        return repository.findAll(page).getContent().stream()
                .map(user-> new UserResponseDTO(user.getId(), user.getEmail(), user.getName(),
                        user.getRole().getRole(), user.getState().toString())).toList();
    }

    public User getUserFromContext(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findUserByEmail(email)
                .orElseThrow(()->new AccessDeniedException("Access denied"));
    }
    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(()
                        -> new NotFoundException( "User not found"));
    }
    public void findUserForAuth(String Email) {
        if(repository.findUserByEmail(Email).isPresent()){
            throw new AlreadyExistException( "User with Email : " + Email + " has already registered");
        }
    }

    public UsersRolesDTO getUsersStateData(){
        return new UsersRolesDTO(roleRepository.findAll());

    }
}
