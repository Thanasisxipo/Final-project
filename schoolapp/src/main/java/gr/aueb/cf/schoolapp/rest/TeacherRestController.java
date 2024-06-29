package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.validator.TeacherInsertValidator;
import gr.aueb.cf.schoolapp.validator.TeacherUpdateValidator;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * REST Controller for managing Teacher entities.
 */
@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherRestController {
    private final ITeacherService teacherService;
    private final TeacherInsertValidator insertValidator;
    private final TeacherUpdateValidator updateValidator;

    /**
     * Add a new teacher.
     *
     * @param dto   the teacher insert DTO containing the details of the teacher to insert.
     * @param bindingResult the binding result for validation errors.
     * @return  a ResponseEntity with the created teacher or an error status.
     */
    @Operation(summary = "Add a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Teacher created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/")
    public ResponseEntity<TeacherReadOnlyDTO> addTeacher(@Valid @RequestBody TeacherInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.insertTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(readOnlyDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     *  Updates an existing teacher.
     *
     * @param id    the ID of the teacher to update.
     * @param dto   the teacher update DTO containing the teacher details of the student.
     * @param bindingResult the binding result for validation errors.
     * @return  a ResponseEntity with the updated teacher or an error status.
     */
    @Operation(summary = "Update a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> updateTeacher(@PathVariable("id") Long id, @Valid @RequestBody TeacherUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an existing teacher by its ID.
     *
     * @param id    the ID of the teacher to delete.
     * @return      a ResponseEntity with the deleted teacher or an error status.
     */
    @Operation(summary = "Delete a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> deleteTeacher(@PathVariable("id") Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok(readOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Retrieves a list o teachers by their lastname starting with the specified string.
     *
     * @param lastname  the starting substring of lastnames to search for.
     * @return      a ResponseEntity with a list of the teachers or an error status.
     */
    @Operation(summary = "Get teachers by their lastname starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @GetMapping("/by-lastname")
    public ResponseEntity<List<TeacherReadOnlyDTO>> getTeacherByLastname(@RequestParam("lastname") String lastname) {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getTeacherByLastname(lastname);
            List<TeacherReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Teacher teacher : teachers) {
                readOnlyDTOS.add(Mapper.mapTeacherToReadOnly(teacher));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a teacher by its ID.
     *
     * @param id    the ID of the teacher to retrieve.
     * @return      a ResponseEntity with the retrieved teacher or an error status.
     */
    @Operation(summary = "Get a Teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<TeacherReadOnlyDTO> getTeacher(@PathVariable("id") Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            TeacherReadOnlyDTO dto = Mapper.mapTeacherToReadOnly(teacher);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all teachers.
     *
     * @return a ResponseEntity with a list of all teachers or an error status.
     */
    @Operation(summary = "Get all teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teachers Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<TeacherReadOnlyDTO>> getAllTeachers() {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getAllTeachers();
            List<TeacherReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Teacher teacher : teachers) {
                readOnlyDTOS.add(Mapper.mapTeacherToReadOnly(teacher));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  Adds a specified course to a specified teacher.
     *
     * @param teacherId the ID of the teacher to whom we add a course.
     * @param courseId  the ID of the course to add.
     * @return          a ResponseEntity with the teacher added the course or an error status.
     */
    @Operation(summary = "Add a course to a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course added to teacher",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher or Course not found",
                    content = @Content) })
    @PutMapping("/{teacherId}/courses/{courseId}")
    public ResponseEntity<TeacherReadOnlyDTO> addCourseToTeacher(@PathVariable("teacherId") Long teacherId, @PathVariable("courseId") Long courseId) {
        try {
            teacherService.addCourseToTeacher(teacherId, courseId);
            Teacher teacher = teacherService.getTeacherById(teacherId);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes a specified course from a specified teacher.
     *
     * @param teacherId the ID of the teacher from whom we remove a course.
     * @param courseId  the ID of the course to remove.
     * @return          a ResponseEntity with the teacher removed a course from or an error status.
     */
    @Operation(summary = "Remove a course from a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course removed from teacher",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher or Course not found",
                    content = @Content) })
    @DeleteMapping("/{teacherId}/courses/{courseId}")
    public ResponseEntity<TeacherReadOnlyDTO> removeCourseFromTeacher(@PathVariable("teacherId") Long teacherId, @PathVariable("courseId") Long courseId) {
        try {
            teacherService.removeCourseFromTeacher(teacherId, courseId);
            Teacher teacher = teacherService.getTeacherById(teacherId);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapTeacherToReadOnly(teacher);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Retrieves all courses from a teacher.
     *
     * @param id    The ID of the teacher.
     * @return      a ResponseEntity with a list of all courses of a specified teacher or an error status.
     */
    @Operation(summary = "Get all courses of a teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found for teacher",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content)})
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseReadOnlyDTO>> getTeacherCourses(@PathVariable("id") Long id) {
        try {
            List<Course> courses = teacherService.getTeacherCourses(id);
            List<CourseReadOnlyDTO> readOnlyDTOs = new ArrayList<>();
            for (Course course : courses) {
                readOnlyDTOs.add(Mapper.mapToReadOnlyDTO(course));
            }
            return new ResponseEntity<>(readOnlyDTOs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
