package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.authentication.util.JwtUtil;
import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.IUserService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for user authentication and login.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginRestController {
    private final IUserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user and returns a JWT token if successful.
     *
     * @param dto the user login DTO containing the username and password.
     * @return a ResponseEntity containing the JWT token and user details, or an error status.
     */
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/login/")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO dto) {
        try {
            User user = userService.getByUsername(dto.getUsername());
            if (user.getUsername() == null){
                throw new EntityNotFoundException(User.class, user.getId());
            }
            if (userService.checkPassword(user, dto.getPassword())) {
                String jwt = jwtUtil.generateToken(dto.getUsername(), userService.getByUsername(dto.getUsername()).getRole());
                System.out.println("Generated JWT: " + jwt);
                UserReadOnlyDTO readOnlyDTO = Mapper.mapUserToReadOnly(user);
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + jwt)
                        .body(Map.of("access_token", jwt, "user", readOnlyDTO));
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
