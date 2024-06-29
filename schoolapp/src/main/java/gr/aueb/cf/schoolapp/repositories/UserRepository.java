package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.Role;
import gr.aueb.cf.schoolapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameStartingWith(String username);
    User findUserById(Long id);
    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);
    Optional<User> findByRole(Role role);
    Long countByRole(Role role);
}
