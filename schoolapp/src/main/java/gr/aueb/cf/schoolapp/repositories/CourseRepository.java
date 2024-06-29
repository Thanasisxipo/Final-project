package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findCourseById(Long id);

}
