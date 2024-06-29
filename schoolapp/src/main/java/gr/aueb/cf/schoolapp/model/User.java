package gr.aueb.cf.schoolapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

/**
 * Represents a user in the school application.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 256, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private Teacher teacher;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private Student student;

    public User(Long id) {
        this.id = id;
        setIsActive(true);
    }

    public void setNewStudent(Student student) {
        if (student == null || this.getStudent() == student) return;
        this.setStudent(student);
        student.setUser(this);
    }

    public void setNewTeacher(Teacher teacher) {
        if (teacher == null || this.getTeacher() == teacher) return;
        this.setTeacher(teacher);
        teacher.setUser(this);
    }

    public static User NEW_TEACHER(String username, String password) {
        User user = new User();
        user.setRole(Role.TEACHER);
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setIsActive(true);
        return user;
    }

    public static User NEW_STUDENT(String username, String password) {
        User user = new User();
        user.setRole(Role.STUDENT);
        user.setUsername(username);
        user.setPassword(encodePassword(password));
        user.setIsActive(true);
        return user;
    }

    private static String encodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive() == null || this.getIsActive();
    }
}
