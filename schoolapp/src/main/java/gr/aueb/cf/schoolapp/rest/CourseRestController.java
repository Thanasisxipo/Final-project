package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.CourseReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.service.ICourseService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.validator.CourseInsertValidator;
import gr.aueb.cf.schoolapp.validator.CourseUpdateValidator;
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
 * REST Controller for managing Course entities.
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseRestController {
    private final ICourseService courseService;
    private final CourseInsertValidator insertValidator;
    private final CourseUpdateValidator updateValidator;

    /**
     * Adds a new course.
     *
     * @param dto the course insert DTO containing the details of the course to insert.
     * @param bindingResult the binding result for validation errors.
     * @return a ResponseEntity with the created course or an error status.
     */
    @Operation(summary = "Add a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/")
    public ResponseEntity<CourseReadOnlyDTO> addCourse(@Valid @RequestBody CourseInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Course course = courseService.insertCourse(dto);
            CourseReadOnlyDTO readOnlyDTO = Mapper.mapToReadOnlyDTO(course);
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
     * Updates an existing course.
     *
     * @param id the ID of the course to update.
     * @param dto the course update DTO containing the updated details of the course.
     * @param bindingResult the binding result for validation errors.
     * @return a ResponseEntity with the updated course or an error status.
     */
    @Operation(summary = "Update a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<CourseReadOnlyDTO> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Course course = courseService.updateCourse(dto);
            CourseReadOnlyDTO readOnlyDTO = Mapper.mapToReadOnlyDTO(course);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an existing course by its ID.
     *
     * @param id the ID of the course to delete.
     * @return a ResponseEntity with the deleted course or an error status.
     */
    @Operation(summary = "Delete a course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<CourseReadOnlyDTO> deleteCourse(@PathVariable("id") Long id) {
        try {
            Course course = courseService.getCourseById(id);
            CourseReadOnlyDTO readOnlyDTO = Mapper.mapToReadOnlyDTO(course);
            courseService.deleteCourse(id);
            return ResponseEntity.ok(readOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param id the ID of the course to retrieve.
     * @return a ResponseEntity with the retrieved course or an error status.
     */
    @Operation(summary = "Get a course by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Course not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CourseReadOnlyDTO> getCourse(@PathVariable("id") Long id) {
        try {
            Course course = courseService.getCourseById(id);
            CourseReadOnlyDTO dto = Mapper.mapToReadOnlyDTO(course);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all courses.
     *
     * @return a ResponseEntity with a list of all courses or an error status.
     */
    @Operation(summary = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<CourseReadOnlyDTO>> getAllCourses() {
        List<Course> courses;
        try {
            courses = courseService.getAllCourses();
            List<CourseReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Course course : courses) {
                readOnlyDTOS.add(Mapper.mapToReadOnlyDTO(course));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
