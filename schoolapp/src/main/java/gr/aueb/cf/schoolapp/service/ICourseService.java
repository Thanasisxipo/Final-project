package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.CourseInsertDTO;
import gr.aueb.cf.schoolapp.dto.CourseUpdateDTO;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ICourseService {
    Course insertCourse(CourseInsertDTO dto) throws Exception;
    Course updateCourse(CourseUpdateDTO dto) throws EntityNotFoundException;
    void deleteCourse(Long id) throws EntityNotFoundException;
    Course getCourseById(Long id) throws EntityNotFoundException;
    List<Course> getAllCourses() throws EntityNotFoundException;
}
