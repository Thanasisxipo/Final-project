package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.repositories.CourseRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Course Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService{
    private final CourseRepository courseRepository;

    /**
     * Inserts a new course into the database.
     *
     * @param dto The DTO containing the data to insert.
     * @return The newly inserted course.
     * @throws Exception If there is an error during insertion.
     */
    @Transactional
    @Override
    public Course insertCourse(CourseInsertDTO dto) throws Exception {
        Course course = null;
        try {
            course = courseRepository.save(Mapper.mapToCourse(dto));
            if (course.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Insert success for course with id " + course.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return course;    }

    /**
     * Updates an existing course in the database.
     *
     * @param dto The DTO containing the updated data.
     * @return The updated course entity.
     * @throws EntityNotFoundException If the course with the given ID does not exist.
     */
    @Transactional
    @Override
    public Course updateCourse(CourseUpdateDTO dto) throws EntityNotFoundException {
        Course course = null;
        Course updatedCourse = null;
        try {
            course = courseRepository.findCourseById(dto.getId());
            if (course == null ) {
                throw new EntityNotFoundException(Course.class, dto.getId());
            }
            updatedCourse = courseRepository.save(Mapper.mapToCourse(dto));
            log.info("Course with id " + updatedCourse.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedCourse;
    }

    /**
     * Deletes a course from the database.
     *
     * @param id The ID of the course to delete.
     * @throws EntityNotFoundException If the course with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteCourse(Long id) throws EntityNotFoundException {
        Course course = null;
        try {
            course = courseRepository.findCourseById(id);
            if (course == null ) {
                throw new EntityNotFoundException(Course.class, id);
            }
            courseRepository.deleteById(id);
            log.info("Course with id " + course.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a course by its ID from the database.
     *
     * @param id The ID of the course to retrieve.
     * @return The course entity.
     * @throws EntityNotFoundException If the course with the given ID does not exist.
     */
    @Override
    public Course getCourseById(Long id) throws EntityNotFoundException {
        Course course = null;
        try {
            course = courseRepository.findCourseById(id);
            if (course == null ) {
                throw new EntityNotFoundException(Course.class, id);
            }
            log.info("Course with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return course;
    }

    /**
     * Retrieves all courses from the database.
     *
     * @return A list of all courses.
     * @throws EntityNotFoundException If no courses are found in the database.
     */
    @Override
    public List<Course> getAllCourses() throws EntityNotFoundException {
        List<Course> courses = new ArrayList<>();
        try {
            courses = courseRepository.findAll();
            if (courses.isEmpty()) {
                throw new EntityNotFoundException(Course.class, 0L);
            }
            log.info("Retrieved all courses.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return courses;
    }
}
