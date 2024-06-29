package gr.aueb.cf.schoolapp.rest;

import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Gender;
import gr.aueb.cf.schoolapp.model.Student;
import gr.aueb.cf.schoolapp.service.IStudentService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.validator.StudentInsertValidator;
import gr.aueb.cf.schoolapp.validator.StudentUpdateValidator;
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
 * REST Controller for managing Student entities.
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {
    private final IStudentService studentService;
    private final StudentInsertValidator insertValidator;
    private final StudentUpdateValidator updateValidator;

    /**
     * Add a new student.
     *
     * @param dto   the student insert DTO containing the details of the student to insert.
     * @param bindingResult the binding result for validation errors.
     * @return  a ResponseEntity with the created student or an error status.
     */
    @Operation(summary = "Add a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/")
    public ResponseEntity<StudentReadOnlyDTO> addStudent(@Valid @RequestBody StudentInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.insertStudent(dto);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
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
     *  Updates an existing student.
     *
     * @param id    the ID of the student to update.
     * @param dto   the student update DTO containing the updated details of the student.
     * @param bindingResult the binding result for validation errors.
     * @return  a ResponseEntity with the updated student or an error status.
     */
    @Operation(summary = "Update a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<StudentReadOnlyDTO> updateStudent(@PathVariable("id") Long id, @Valid @RequestBody StudentUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.updateStudent(dto);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an existing student by its ID.
     *
     * @param id    the ID of the student to delete.
     * @return      a ResponseEntity with the deleted student or an error status.
     */
    @Operation(summary = "Delete a student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentReadOnlyDTO> deleteStudent(@PathVariable("id") Long id) {
        try {
            Student student = studentService.getStudentById(id);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
            studentService.deleteStudent(id);
            return ResponseEntity.ok(readOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Retrieves a list o students by their lastname starting with the specified string.
     *
     * @param lastname  the starting substring of lastnames to search for.
     * @return      a ResponseEntity with a list of the students or an error status.
     */
    @Operation(summary = "Get students by their lastname starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @GetMapping("/by-lastname")
    public ResponseEntity<List<StudentReadOnlyDTO>> getStudentByLastname(@RequestParam("lastname") String lastname) {
        List<Student> students;
        try {
            students = studentService.getStudentByLastname(lastname);
            List<StudentReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Student student : students) {
                readOnlyDTOS.add(Mapper.mapStudentToReadOnly(student));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a student by its ID.
     *
     * @param id    the ID of the student to retrieve.
     * @return      a ResponseEntity with the retrieved student or an error status.
     */
    @Operation(summary = "Get a student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<StudentReadOnlyDTO> getStudent(@PathVariable("id") Long id) {
        try {
            Student student = studentService.getStudentById(id);
            StudentReadOnlyDTO dto = Mapper.mapStudentToReadOnly(student);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves a list o students by their gender.
     *
     * @param gender    the gender of the students to retrieve.
     * @return          a ResponseEntity with a list of the students or an error status.
     */
    @Operation(summary = "Get students by their gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid gender supplied",
                    content = @Content)})
    @GetMapping("/by-gender")
    public ResponseEntity<List<StudentReadOnlyDTO>> getStudentByGender(@RequestParam("gender") Gender gender) {
        List<Student> students;
        try {
            students = studentService.getStudentByGender(gender);
            List<StudentReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Student student : students) {
                readOnlyDTOS.add(Mapper.mapStudentToReadOnly(student));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves all students.
     *
     * @return a ResponseEntity with a list of all students or an error status.
     */
    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<StudentReadOnlyDTO>> getAllStudents() {
        List<Student> students;
        try {
            students = studentService.getAllStudents();
            List<StudentReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (Student student : students) {
                readOnlyDTOS.add(Mapper.mapStudentToReadOnly(student));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *  Adds a specified course to a specified student.
     *
     * @param studentId the ID of the student to whom we add a course.
     * @param courseId  the ID of the course to add.
     * @return          a ResponseEntity with the student added the course or an error status.
     */
    @Operation(summary = "Add a course to a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course added to student",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Student or Course not found",
                    content = @Content) })
    @PutMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentReadOnlyDTO> addCourseToStudent(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId) {
        try {
            studentService.addCourseToStudent(studentId, courseId);
            Student student = studentService.getStudentById(studentId);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes a specified course from a specified student.
     *
     * @param studentId the ID of the student from whom we remove a course.
     * @param courseId  the ID of the course to remove.
     * @return          a ResponseEntity with the student removed a course from or an error status.
     */
    @Operation(summary = "Remove a course from a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course removed from student",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Student or Course not found",
                    content = @Content) })
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentReadOnlyDTO> removeCourseFromStudent(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId) {
        try {
            studentService.removeCourseFromStudent(studentId, courseId);
            Student student = studentService.getStudentById(studentId);
            StudentReadOnlyDTO readOnlyDTO = Mapper.mapStudentToReadOnly(student);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Retrieves all courses from a student.
     *
     * @param id    The ID of the student.
     * @return      a ResponseEntity with a list of all courses of a specified student or an error status.
     */
    @Operation(summary = "Get all courses of a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses found for student",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseReadOnlyDTO>> getStudentCourses(@PathVariable("id") Long id) {
        try {
            List<Course> courses = studentService.getStudentCourses(id);
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
