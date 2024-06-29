package gr.aueb.cf.schoolapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a city in the school application.
 */
@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String city;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "city")
    @Getter(AccessLevel.PROTECTED)
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    public Set<Student> getAllStudents() {
        return Collections.unmodifiableSet(students);
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.setCity(this);
    }
}
