package org.blb.controller.contact;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.contactDTO.ContactDTO;
import org.blb.controller.api.contact.ContactApi;
import org.blb.service.contact.ContactMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController implements ContactApi {
    private final ContactMailService contactMailService;

    @Override
    @PostMapping
    public ResponseEntity<StandardResponseDto> addContact(@Valid @RequestBody ContactDTO dto) {
        try {
            contactMailService.sendMail(dto);
            return ResponseEntity.ok(new StandardResponseDto("Contact form was sent successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponseDto("Failed to send email"));
        }
    }
}
