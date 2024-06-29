package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.authentication.util.JwtUtil;
import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.IUserService;
import gr.aueb.cf.schoolapp.validator.RegisterStudentValidator;
import gr.aueb.cf.schoolapp.validator.TeacherRegisterValidator;
import gr.aueb.cf.schoolapp.validator.UserInsertValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * REST Controller for user, teacher, and student registration.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegisterRestController {
    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private final TeacherRegisterValidator teacherRegisterValidator;
    private final RegisterStudentValidator studentValidator;
    private final UserInsertValidator insertValidator;
    private final ITeacherService teacherService;
    private final IStudentService studentService;

    /**
     * Registers a new user.
     *
     * @param dto            the user insert DTO containing user details.
     * @param bindingResult  the binding result for validation.
     * @return a ResponseEntity containing the JWT token and user details, or an error status.
     */
    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/register/")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.insertUser(dto);
            UserReadOnlyDTO readOnlyDTO = Mapper.mapUserToReadOnly(user);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readOnlyDTO.getId())
                    .toUri();
            String jwt = jwtUtil.generateToken(dto.getUsername(), userService.getByUsername(dto.getUsername()).getRole());
            return ResponseEntity.created(location)
                    .header("Authorization", "Bearer " + jwt)
                    .body(Map.of("access_token", jwt, "user", readOnlyDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Registers a new teacher.
     *
     * @param dto            the teacher register DTO containing teacher details.
     * @param bindingResult  the binding result for validation.
     * @return a ResponseEntity containing the JWT token and teacher details, or an error status.
     */
    @Operation(summary = "Register a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/register-teacher")
    public ResponseEntity<?> registerTeacher(@Valid @RequestBody RegisterTeacherDTO dto, BindingResult bindingResult) {
        teacherRegisterValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.registerTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readOnlyDTO.getId())
                    .toUri();
            String jwt = jwtUtil.generateToken(dto.getUsername(), userService.getByUsername(dto.getUsername()).getRole());
            return ResponseEntity.created(location)
                    .header("Authorization", "Bearer " + jwt)
                    .body(Map.of("access_token", jwt, "user", readOnlyDTO));


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Registers a new student.
     *
     * @param dto            the student register DTO containing student details.
     * @param bindingResult  the binding result for validation.
     * @return a ResponseEntity containing the JWT token and student details, or an error status.
     */
    @Operation(summary = "Register a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/register-student")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody RegisterStudentDTO dto, BindingResult bindingResult) {
        studentValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.registerStudent(dto);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readOnlyDTO.getId())
                    .toUri();
            String jwt = jwtUtil.generateToken(dto.getUsername(), userService.getByUsername(dto.getUsername()).getRole());
            return ResponseEntity.created(location)
                    .header("Authorization", "Bearer " + jwt)
                    .body(Map.of("access_token", jwt, "user", readOnlyDTO));

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
