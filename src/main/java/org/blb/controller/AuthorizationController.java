package org.blb.controller;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.user.PassRecoveryDTO;
import org.blb.DTO.user.UserNewDTO;
import org.blb.controller.api.user.AuthApi;
import org.blb.security.dto.AuthRequest;
import org.blb.security.dto.AuthResponse;
import org.blb.service.user.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizationController implements AuthApi {
    private final UserAuthService userAuthService;

    @Override
    public ResponseEntity<AuthResponse> auth(AuthRequest request) {
        return new ResponseEntity<>(userAuthService.authentication(request.getEmail(), request.getPassword())
                , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StandardResponseDto> registration(UserNewDTO user) {
        userAuthService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardResponseDto("Confirmation sanded to your email"));
    }

    @Override
    public ResponseEntity<StandardResponseDto> confirmation(String data, String code) {
       return userAuthService.confirm(data, code);
    }

    @Override
    public ResponseEntity<StandardResponseDto> recovery(AuthRequest request) {
        userAuthService.recoverPassword(request);
        return new ResponseEntity<>(new StandardResponseDto("Instruction sent to your Email"), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<StandardResponseDto> recoveryConfirmation(PassRecoveryDTO request) {
        return userAuthService.recoverConfirm(request.getData(), request.getCode());
    }


}
