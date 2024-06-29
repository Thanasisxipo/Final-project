package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.*;
import gr.aueb.cf.schoolapp.repositories.StudentRepository;
import gr.aueb.cf.schoolapp.repositories.TeacherRepository;
import gr.aueb.cf.schoolapp.repositories.UserRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.UserAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Inserts a new user into database.
     *
     * @param dto The DTO containing user data to insert.
     * @return The newly inserted user.
     * @throws UserAlreadyExistsException If a user with the same username already exists.
     * @throws Exception                  If there is an error during insertion.
     */
    @Transactional
    @Override
    public User insertUser(UserInsertDTO dto) throws UserAlreadyExistsException, Exception {
        try {
            Optional<User> userOptional = userRepository.findByUsername(dto.getUsername());
            if (userOptional.isPresent()) {
                throw new UserAlreadyExistsException(dto.getUsername());
            }
            User user = Mapper.mapToUser(dto);
            user.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode the password
            user = userRepository.save(user);
            if (user.getId() == null) {
                throw new Exception("Error in inserting");
            }
            log.info("Insert success for user with id " + user.getId());
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Updates an existing user in database.
     *
     * @param dto The DTO containing updated user data.
     * @return The updated user entity.
     * @throws EntityNotFoundException If the user with the given ID does not exist.
     */
    @Transactional
    @Override
    public User updateUser(UserUpdateDTO dto) throws EntityNotFoundException {
        try {
            User user = userRepository.findUserById(dto.getId());
            if (user == null) {
                throw new EntityNotFoundException(User.class, dto.getId());
            }
            if (isAdmin(dto.getRole())) {
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode the password
                user.setRole(dto.getRole());
                User updatedUser = userRepository.save(user);
                log.info("User with id " + updatedUser.getId() + " was updated");
                return updatedUser;
            } else if (isStudent(dto.getRole())) {
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setRole(dto.getRole());

                Student student = studentRepository.findById(dto.getStudent().getId())
                        .orElseThrow(() -> new EntityNotFoundException(Student.class, dto.getStudent().getId()));
                user.setStudent(student);

                User updatedUser = userRepository.save(user);
                log.info("User with id " + updatedUser.getId() + " was updated");
                return updatedUser;
            } else {
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setRole(dto.getRole());

                Teacher teacher = teacherRepository.findById(dto.getTeacher().getId())
                        .orElseThrow(() -> new EntityNotFoundException(Teacher.class, dto.getTeacher().getId()));
                user.setTeacher(teacher);

                User updatedUser = userRepository.save(user);
                log.info("User with id " + updatedUser.getId() + " was updated");
                return updatedUser;
            }
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a user from database.
     *
     * @param id The ID of the user to delete.
     * @throws EntityNotFoundException If the user with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        User user = null;
        try {
            user = userRepository.findUserById(id);
            if (user == null ) {
                throw new EntityNotFoundException(User.class, id);
            }
            userRepository.deleteById(id);
            log.info("User with id " + user.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a user whose username is the specified string.
     *
     * @param username The starting substring of usernames to search for.
     * @return A user matching the criteria.
     * @throws EntityNotFoundException If no user match the criteria.
     */
    @Transactional
    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        User user = null;
        try {
            user = userRepository.findUserByUsername(username);
            if (user == null) {
                throw new EntityNotFoundException(User.class, 0L);
            }
            log.info("User with " + username + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return user;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user entity.
     * @throws EntityNotFoundException If the user with the given ID does not exist.
     */
    @Transactional
    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        User user = null;
        try {
            user = userRepository.findUserById(id);
            if (user == null ) {
                throw new EntityNotFoundException(User.class, id);
            }
            log.info("User with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return user;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for.
     * @return The user entity.
     * @throws UsernameNotFoundException If no user with the given username is found.
     */
    @Transactional
    @Override
    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
    }

    /**
     * Retrieves all users registered in the system.
     *
     * @return A list of all users.
     * @throws EntityNotFoundException If no users are registered in the system.
     */
    @Override
    public List<User> getAllUsers() throws EntityNotFoundException {
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
            if (users.isEmpty()) {
                throw new EntityNotFoundException(User.class, 0L);
            }
            log.info("Retrieved all users.");
        } catch (EntityNotFoundException e) {
            log.error("There are no users registered.");
            throw e;
        }
        return users;
    }

    /**
     * Checks if the provided raw password matches the encoded password of the user.
     *
     * @param user         The user whose password to check.
     * @param rawPassword  The raw (unencoded) password to verify.
     * @return true if the raw password matches the encoded password, false otherwise.
     * @throws WrongPasswordException If the provided password does not match the user's encoded password.
     */
    @Override
    public boolean checkPassword(User user, String rawPassword) throws WrongPasswordException {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * Checks if the user is a student.
     *
     * @param role  The role user has.
     * @return      true if user is a student.
     */
    @Override
    public boolean isStudent(Role role) {
        return role.name().equals("STUDENT");
    }

    /**
     * Checks if the user is a admin.
     *
     * @param role  The role user has.
     * @return      true if user is admin.
     */
    @Override
    public boolean isAdmin(Role role) {
        return role.name().equals("ADMIN");
    }

    /**
     * Checks if the user is a teacher.
     *
     * @param role  The role user has.
     * @return      true if user is a teacher.
     */
    @Override
    public boolean isTeacher(Role role) {
        return role.name().equals("TEACHER");
    }
}
