package org.blb.service.user;

import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.exeption.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.user.UserNewDTO;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.security.dto.AuthResponse;
import org.blb.security.service.JwtTokenProvider;
import org.blb.service.mail.MailCreateUtil;
import org.blb.service.mail.UserMailSender;
import org.blb.service.util.UserConvert;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final UserConvert userConvert;
    private final UserFindService userFindService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MailCreateUtil mailCreateUtil;
    private final UserMailSender userMailSender;

    @Transactional
    public void addUser(UserNewDTO user) {
        userFindService.findUserForAuth(user.getEmail());
        Role role = roleRepository.findByRole("USER");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String codeValue = UUID.randomUUID().toString();
        User newUser = userConvert.fromDTOtoUser(user, role, State.NOT_CONFIRMED);
        newUser.setCode(codeValue);
        repository.save(newUser);
        sendEmail(newUser, codeValue);
    }


    public AuthResponse authentication(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication.getName());
        return new AuthResponse(jwt, authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList().get(0));
    }
    public ResponseEntity<StandardResponseDto> confirm(String id, String code){
        Long userId= decodeUserId(id);
        User user=userFindService.findUserById(userId);
        if(code.equals(user.getCode())){
            user.setCode("");
            user.setState(State.CONFIRMED);
            repository.save(user);
            return ResponseEntity.ok(new StandardResponseDto("User confirmed successfully"));
        }
            return ResponseEntity.badRequest().body(new StandardResponseDto("Invalid confirmation code"));
    }


    private void sendEmail(User user, String code) {
        String link = "?data=" + (codeUserId(Long.toString(user.getId()))) + "&code=" + code;
        String html = mailCreateUtil.createConfirmationMail(user.getName(), link);
        userMailSender.send(user.getEmail(), "Registration", html);
    }


    private Long decodeUserId(String code){
        byte[] byteArray=code.getBytes();
        StringBuilder buffer =new StringBuilder();
        for(int i=0;i<byteArray.length;i++){
            if (byteArray[i] < 97 || byteArray[i] > 106){
                throw new RestException(HttpStatus.NOT_FOUND, "User not found");
            }
            if (i > 1 && i < byteArray.length - 3) {
                buffer.append((char) (byteArray[i] - 49));
            }
        }
        return Long.parseLong(buffer.toString());
    }
    private String codeUserId(String id){
        String code=randomString(5);
        StringBuilder result= new StringBuilder();
        for (int i=0;i<code.length();i++){
            result.append(code.charAt(i));
            if(i==1) {
                result.append(numberToCodeString(id));
            }
        }
        return result.toString();
    }
    private String numberToCodeString(String number){
        byte[] byteArray=number.getBytes();
        StringBuilder buffer =new StringBuilder();;
        for(int i=0;i<byteArray.length;i++){
           buffer.append((char)(byteArray[i]+49));
        }
        return buffer.toString();
    }

    private String randomString(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 106; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}
