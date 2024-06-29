package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.RegisterStudentDTO;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.*;
import gr.aueb.cf.schoolapp.repositories.CityRepository;
import gr.aueb.cf.schoolapp.repositories.CourseRepository;
import gr.aueb.cf.schoolapp.repositories.StudentRepository;
import gr.aueb.cf.schoolapp.repositories.UserRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.StudentAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Student Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService{
    private final CityRepository cityRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Inserts a new student into database.
     *
     * @param dto The DTO containing student data to insert.
     * @return The newly inserted student.
     * @throws Exception If there is an error during insertion.
     */
    @Transactional
    @Override
    public Student insertStudent(StudentInsertDTO dto) throws Exception {
        Student student = null;
        try {
            City city = cityRepository.findById(dto.getCity().getId()).orElseThrow(() -> new EntityNotFoundException(City.class, dto.getCity().getId()));
            User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotFoundException(User.class, dto.getUser().getId()));
            Set<Course> courses = new HashSet<>();
            for (Course course : dto.getCourses()) {
                Long courseId = course.getId();
                Course courseToAdd = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));
                if (courseToAdd == null) {
                    throw new EntityNotFoundException(Course.class, courseId);
                }
                courses.add(courseToAdd);
            }
            student = Mapper.mapToStudent(dto);
            student.addCity(city);
            student.addUser(user);
            user.setStudent(student);
            for (Course course : courses) {
                student.addCourse(course);
            }
            studentRepository.save(student);
            if (student.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Insert success for student with id " + student.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return student;
    }

    /**
     * Updates an existing student in database.
     *
     * @param dto The DTO containing updated student data.
     * @return The updated student entity.
     * @throws EntityNotFoundException If the student with the given ID does not exist.
     */
    @Transactional
    @Override
    public Student updateStudent(StudentUpdateDTO dto) throws EntityNotFoundException {
        Student student = null;
        Student updatedStudent = null;
        try {
            student = studentRepository.findStudentById(dto.getId());
            if (student == null ) {
                throw new EntityNotFoundException(Student.class, dto.getId());
            }
            updatedStudent = Mapper.mapToStudent(dto);
            City city = cityRepository.findById(dto.getCity().getId()).orElseThrow(() -> new EntityNotFoundException(City.class, dto.getCity().getId()));
            User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotFoundException(User.class, dto.getUser().getId()));
            Set<Course> courses = new HashSet<>();
            for (Course course : dto.getCourses()) {
                Long courseId = course.getId();
                Course courseToAdd = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));
                if (courseToAdd == null) {
                    throw new EntityNotFoundException(Course.class, courseId);
                }
                courses.add(courseToAdd);
            }
            updatedStudent.addCity(city);
            updatedStudent.addUser(user);
            user.setStudent(updatedStudent);
            for (Course course : courses) {
                updatedStudent.addCourse(course);
            }
            studentRepository.save(updatedStudent);
            log.info("Student with id " + updatedStudent.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedStudent;
    }

    /**
     * Deletes a student from database.
     *
     * @param id The ID of the student to delete.
     * @throws EntityNotFoundException If the student with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteStudent(Long id) throws EntityNotFoundException {
        Student student = null;
        try {
            student = studentRepository.findStudentById(id);
            if (student == null ) {
                throw new EntityNotFoundException(Student.class, id);
            }
            studentRepository.deleteById(id);
            log.info("Student with id " + student.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a list of students whose lastname starts with the specified string.
     *
     * @param lastname The starting substring of lastnames to search for.
     * @return A list of students matching the criteria.
     * @throws EntityNotFoundException If no students match the criteria.
     */
    @Override
    public List<Student> getStudentByLastname(String lastname) throws EntityNotFoundException {
        List<Student> students = new ArrayList<>();
        try {
            students = studentRepository.findByLastnameStartingWith(lastname);
            if (students.isEmpty()) {
                throw new EntityNotFoundException(Student.class, 0L);
            }
            log.info("Students with lastname starting with " + lastname + " were found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return students;
    }

    /**
     * Retrieves a student by ID.
     *
     * @param id The ID of the student to retrieve.
     * @return The student entity.
     * @throws EntityNotFoundException If the student with the given ID does not exist.
     */
    @Transactional
    @Override
    public Student getStudentById(Long id) throws EntityNotFoundException {
        Student student = null;
        try {
            student = studentRepository.findStudentById(id);
            if (student == null ) {
                throw new EntityNotFoundException(Student.class, id);
            }
            log.info("Student with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return student;
    }

    /**
     * Registers a new student in database.
     *
     * @param dto The DTO containing student registration data.
     * @return The newly registered student.
     * @throws StudentAlreadyExistsException If a student with the same username already exists.
     */
    @Transactional
    @Override
    public Student registerStudent(RegisterStudentDTO dto) throws StudentAlreadyExistsException {
        try {
            Optional<User> returnedUser = userRepository.findByUsername(dto.getUsername());
            if (returnedUser.isPresent()) {
                throw new StudentAlreadyExistsException(dto.getUsername());
            }
            User user = User.NEW_STUDENT(dto.getUsername(), dto.getPassword());
            Student student = Mapper.extractStudentFromRegisterStudentDTO(dto);
            student.addUser(user);
            studentRepository.save(student);

            log.info("Student with id:" + student.getId() + " inserted");
            return student;
        } catch (StudentAlreadyExistsException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a list of students by their gender.
     *
     * @param gender The gender of students to retrieve.
     * @return A list of students matching the criteria.
     * @throws EntityNotFoundException If no students match the criteria.
     */
    @Override
    public List<Student> getStudentByGender(Gender gender) throws EntityNotFoundException {
        List<Student> students = new ArrayList<>();
        try {
            students = studentRepository.findStudentByGender(gender);
            if (students.isEmpty()) {
                throw new EntityNotFoundException(Student.class, 0L);
            }
            log.info("Students with gender " + gender + " were found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return students;
    }

    /**
     * Retrieves all students registered in database.
     *
     * @return A list of all students.
     * @throws EntityNotFoundException If no students are registered in the system.
     */
    @Override
    public List<Student> getAllStudents() throws EntityNotFoundException {
        List<Student> students = new ArrayList<>();
        try {
            students = studentRepository.findAll();
            if (students.isEmpty()) {
                throw new EntityNotFoundException(Student.class, 0L);
            }
            log.info("Retrieved all students.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return students;
    }

    /**
     * Adds a course to a student's list of courses.
     *
     * @param studentId The ID of the student.
     * @param courseId  The ID of the course to add.
     * @throws EntityNotFoundException If either the student or the course with the given IDs does not exist.
     */
    @Override
    @Transactional
    public void addCourseToStudent(Long studentId, Long courseId) throws EntityNotFoundException {
        try {
            Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException(Student.class, studentId));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));

            student.addCourse(course);
            course.addStudent(student);
            studentRepository.save(student);
            courseRepository.save(course);
            log.info("Added course with id " + courseId + " to student with id " + studentId);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Removes a course from a student's list of courses.
     *
     * @param studentId The ID of the student.
     * @param courseId  The ID of the course to remove.
     * @throws EntityNotFoundException If either the student or the course with the given IDs does not exist.
     */
    @Transactional
    @Override
    public void removeCourseFromStudent(Long studentId, Long courseId) throws EntityNotFoundException {
        try {
            Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException(Student.class, studentId));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));

            student.removeCourse(course);
            studentRepository.save(student);

            log.info("Removed course with id " + courseId + " from student with id " + studentId);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all courses from a student.
     *
     * @param id  The ID of the student.
     * @return    A list of all student's  courses.
     * @throws EntityNotFoundException  If either the student with the given ID does not exist.
     */
    @Override
    public List<Course> getStudentCourses(Long id) throws EntityNotFoundException {
        try {
            List<Course> courses = new ArrayList<>();
            Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Student.class, id));
            courses = student.getAllCourses().stream().toList();
            log.info("Retrieved all courses from student with id " + id);
            return courses;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
