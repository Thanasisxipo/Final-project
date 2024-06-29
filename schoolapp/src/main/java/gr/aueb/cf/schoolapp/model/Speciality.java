package gr.aueb.cf.schoolapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a speciality in the school application.
 */
@Entity
@Table(name = "specialities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String speciality;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "speciality")
    @Getter(AccessLevel.PROTECTED)
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();

    public Set<Teacher> getAllTeachers() {
        return Collections.unmodifiableSet(teachers);
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.setSpeciality(this);
    }
}
