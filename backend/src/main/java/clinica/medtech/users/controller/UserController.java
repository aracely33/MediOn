package clinica.medtech.users.controller;

import clinica.medtech.users.dtoResponse.UserResponseDto;
import clinica.medtech.users.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Admin Users", description = "Se encuentran los usuarios administradores")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    /*@GetMapping("/all_admin")
    public ResponseEntity<List<UserResponseDto>> getUserById() {
        return ResponseEntity.ok(userDetailsService.getUsersByRoleAdmin());
    }*/
}
