package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.RegisterTeacherDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.model.Course;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.TeacherAlreadyExistsException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO dto) throws Exception;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException;
    void deleteTeacher(Long id) throws EntityNotFoundException;
    List<Teacher> getTeacherByLastname(String lastname) throws EntityNotFoundException;
    Teacher getTeacherById(Long id) throws EntityNotFoundException;
    List<Teacher> getAllTeachers() throws EntityNotFoundException;
    Teacher registerTeacher(RegisterTeacherDTO dto) throws TeacherAlreadyExistsException;

    void addCourseToTeacher(Long teacherId, Long courseId) throws EntityNotFoundException;
    void removeCourseFromTeacher(Long teacherId, Long courseId) throws EntityNotFoundException;
    List<Course> getTeacherCourses(Long id) throws EntityNotFoundException;

}
