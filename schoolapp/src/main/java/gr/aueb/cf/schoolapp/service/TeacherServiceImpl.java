package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.RegisterTeacherDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.*;
import gr.aueb.cf.schoolapp.repositories.CourseRepository;
import gr.aueb.cf.schoolapp.repositories.SpecialityRepository;
import gr.aueb.cf.schoolapp.repositories.TeacherRepository;
import gr.aueb.cf.schoolapp.repositories.UserRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.StudentAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Teacher service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements ITeacherService{
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;
    private final CourseRepository courseRepository;

    /**
     * Inserts a new teacher into database.
     *
     * @param dto The DTO containing teacher data to insert.
     * @return The newly inserted teacher.
     * @throws Exception If there is an error during insertion.
     */
    @Transactional
    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws Exception {
        Teacher teacher = null;
        try {
            Speciality speciality = specialityRepository.findById(dto.getSpeciality().getId()).orElseThrow(() -> new EntityNotFoundException(Speciality.class, dto.getSpeciality().getId()));
            User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotFoundException(User.class, dto.getUser().getId()));
            teacher = Mapper.mapToTeacher(dto);
            teacher.addSpeciality(speciality);
            teacher.addUser(user);
            user.setTeacher(teacher);
            teacherRepository.save(teacher);
            if (teacher.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Insert success for teacher with id " + teacher.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return teacher;
    }

    /**
     * Updates an existing teacher in database.
     *
     * @param dto The DTO containing updated teacher data.
     * @return The updated teacher entity.
     * @throws EntityNotFoundException If the teacher with the given ID does not exist.
     */
    @Transactional
    @Override
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException {
        Teacher teacher = null;
        Teacher updatedTeacher = null;
        try {
            teacher = teacherRepository.findTeacherById(dto.getId());
            if (teacher == null ) {
                throw new EntityNotFoundException(Teacher.class, dto.getId());
            }
            updatedTeacher = Mapper.mapToTeacher(dto);
            Speciality speciality = specialityRepository.findById(dto.getSpeciality().getId()).orElseThrow(() -> new EntityNotFoundException(Speciality.class, dto.getSpeciality().getId()));
            User user = userRepository.findById(dto.getUser().getId()).orElseThrow(() -> new EntityNotFoundException(User.class, dto.getUser().getId()));
            updatedTeacher.addSpeciality(speciality);
            updatedTeacher.addUser(user);
            user.setTeacher(updatedTeacher);
            teacherRepository.save(updatedTeacher);
            log.info("Teacher with id " + updatedTeacher.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedTeacher;
    }

    /**
     * Deletes a teacher from database.
     *
     * @param id The ID of the teacher to delete.
     * @throws EntityNotFoundException If the teacher with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteTeacher(Long id) throws EntityNotFoundException {
        Teacher teacher = null;
        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null ) {
                throw new EntityNotFoundException(Teacher.class, id);
            }
            teacherRepository.deleteById(id);
            log.info("Teacher with id " + teacher.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a list of teachers whose lastname starts with the specified string.
     *
     * @param lastname The starting substring of lastnames to search for.
     * @return A list of teachers matching the criteria.
     * @throws EntityNotFoundException If no teachers match the criteria.
     */
    @Transactional
    @Override
    public List<Teacher> getTeacherByLastname(String lastname) throws EntityNotFoundException {
        List<Teacher> teachers = new ArrayList<>();
        try {
            teachers = teacherRepository.findByLastnameStartingWith(lastname);
            if (teachers.isEmpty()) {
                throw new EntityNotFoundException(Teacher.class, 0L);
            }
            log.info("Teachers with lastname starting with " + lastname + " were found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teachers;
    }

    /**
     * Retrieves a teacher by ID.
     *
     * @param id The ID of the teacher to retrieve.
     * @return The teacher entity.
     * @throws EntityNotFoundException If the teacher with the given ID does not exist.
     */
    @Transactional
    @Override
    public Teacher getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacher = null;
        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null ) {
                throw new EntityNotFoundException(Teacher.class, id);
            }
            log.info("Teacher with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teacher;
    }

    /**
     * Registers a new teacher in database.
     *
     * @param dto The DTO containing teacher registration data.
     * @return The newly registered teacher.
     * @throws TeacherAlreadyExistsException If a teacher with the same username already exists.
     */
    @Transactional
    @Override
    public Teacher registerTeacher(RegisterTeacherDTO dto) throws TeacherAlreadyExistsException {
        try {
            Optional<User> returnedUser = userRepository.findByUsername(dto.getUsername());
            if (returnedUser.isPresent()) {
                throw new TeacherAlreadyExistsException(dto.getUsername());
            }

            User user = User.NEW_TEACHER(dto.getUsername(), dto.getPassword());

            Teacher teacher = Mapper.extractTeacherFromRegisterTeacherDTO(dto);
            teacher.addUser(user);
            teacherRepository.save(teacher);

            log.info("Teacher with id:" + teacher.getId() + " inserted");
            return teacher;
        } catch (TeacherAlreadyExistsException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all teachers registered in database.
     *
     * @return A list of all teachers.
     * @throws EntityNotFoundException If no teachers are registered in the system.
     */
    @Override
    public List<Teacher> getAllTeachers() throws EntityNotFoundException {
        List<Teacher> teachers = new ArrayList<>();
        try {
            teachers = teacherRepository.findAll();
            if (teachers.isEmpty()) {
                throw new EntityNotFoundException(Teacher.class, 0L);
            }
            log.info("Retrieved all teachers.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return teachers;
    }

    /**
     * Adds a course to a teacher's list of courses.
     *
     * @param teacherId The ID of the teacher.
     * @param courseId  The ID of the course to add.
     * @throws EntityNotFoundException If either the teacher or the course with the given IDs does not exist.
     */
    @Transactional
    @Override
    public void addCourseToTeacher(Long teacherId, Long courseId) throws EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new EntityNotFoundException(Teacher.class, teacherId));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));

            teacher.addCourse(course);
            course.addTeacher(teacher);
            teacherRepository.save(teacher);
            courseRepository.save(course);
            log.info("Added course with id " + courseId + " to teacher with id " + teacherId);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Removes a course from a teacher's list of courses.
     *
     * @param teacherId The ID of the teacher.
     * @param courseId  The ID of the course to remove.
     * @throws EntityNotFoundException If either the teacher or the course with the given IDs does not exist.
     */
    @Transactional
    @Override
    public void removeCourseFromTeacher(Long teacherId, Long courseId) throws EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new EntityNotFoundException(Teacher.class, teacherId));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(Course.class, courseId));

            teacher.removeCourse(course);
            teacherRepository.save(teacher);

            log.info("Removed course with id " + courseId + " from teacher with id " + teacherId);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves all courses from a teacher.
     *
     * @param id  The ID of the teacher.
     * @return    A list of all teacher's  courses.
     * @throws EntityNotFoundException  If either the teacher with the given ID does not exist.
     */
    @Override
    public List<Course> getTeacherCourses(Long id) throws EntityNotFoundException {
        try {
            List<Course> courses = new ArrayList<>();
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Teacher.class, id));
            courses = teacher.getAllCourses().stream().toList();
            log.info("Retrieved all courses from teacher with id " + id);
            return courses;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
