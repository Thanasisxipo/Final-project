package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByLastnameStartingWith(String lastname);
    Teacher findTeacherById(Long id);
}
