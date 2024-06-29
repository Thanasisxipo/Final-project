package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.UserInsertDTO;
import gr.aueb.cf.schoolapp.dto.UserUpdateDTO;
import gr.aueb.cf.schoolapp.model.Role;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.exceptions.UserAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.WrongPasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService {
    User insertUser(UserInsertDTO dto) throws UserAlreadyExistsException, Exception;
    User updateUser(UserUpdateDTO dto) throws EntityNotFoundException;
    void deleteUser(Long id) throws EntityNotFoundException;
    User getUserByUsername(String username) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    User getByUsername(String username) throws UsernameNotFoundException;
    List<User> getAllUsers() throws EntityNotFoundException;
    boolean checkPassword(User user, String rawPassword) throws WrongPasswordException;
    boolean isStudent(Role role);
    boolean isAdmin(Role role);
    boolean isTeacher(Role role);
}
