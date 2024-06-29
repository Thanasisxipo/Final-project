package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.Gender;
import gr.aueb.cf.schoolapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByLastnameStartingWith(String lastname);
    Student findStudentById(Long id);
    List<Student> findStudentByGender(Gender gender);
}
