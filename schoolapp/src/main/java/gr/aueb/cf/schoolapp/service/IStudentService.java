package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.RegisterStudentDTO;
import gr.aueb.cf.schoolapp.dto.StudentInsertDTO;
import gr.aueb.cf.schoolapp.dto.StudentUpdateDTO;
import gr.aueb.cf.schoolapp.model.*;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.StudentAlreadyExistsException;

import java.util.List;

public interface IStudentService {
    Student insertStudent(StudentInsertDTO dto) throws Exception;
    Student updateStudent(StudentUpdateDTO dto) throws EntityNotFoundException;
    void deleteStudent(Long id) throws EntityNotFoundException;
    List<Student> getStudentByLastname(String lastname) throws EntityNotFoundException;
    List<Student> getStudentByGender(Gender gender) throws EntityNotFoundException;
    Student getStudentById(Long id) throws EntityNotFoundException;
    List<Student> getAllStudents() throws EntityNotFoundException;
    Student registerStudent(RegisterStudentDTO dto) throws StudentAlreadyExistsException;

    void addCourseToStudent(Long studentId, Long courseId) throws EntityNotFoundException;
    void removeCourseFromStudent(Long studentId, Long courseId) throws EntityNotFoundException;
    List<Course> getStudentCourses(Long id) throws EntityNotFoundException;
}
