package gr.aueb.cf.schoolapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a course in the school application.
 */
@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", length = 45, nullable = false)
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @JsonIgnore
    private Teacher teacher;

    @ManyToMany(mappedBy = "courses")
    @Getter(AccessLevel.PROTECTED)
    private Set<Student> students = new HashSet<>();

    public Set<Student> getAllStudents() {
        return Collections.unmodifiableSet(students);
    }

    public void addStudent(Student student) {
        if (student == null || students.contains(student)) return;
        students.add(student);
        student.addCourse(this);
    }


    public void removeStudent(Student student) {
        if (student == null || !students.contains(student)) return;
        students.remove(student);
        student.removeCourse(this);
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null || this.getTeacher() == teacher) return;
        this.setTeacher(teacher);
        teacher.getCourses().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        teacher.getCourses().remove(this);
        this.setTeacher(null);
    }
}
