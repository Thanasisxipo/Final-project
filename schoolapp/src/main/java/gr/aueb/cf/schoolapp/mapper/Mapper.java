package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Mapper class providing static methods to map between DTOs and entity models.
 * This utility class handles the conversion of data transfer objects to model entities and vice versa.
 */
public class Mapper {
    private Mapper(){}

    public static Teacher mapToTeacher(TeacherInsertDTO dto) {
        return new Teacher(null, dto.getFirstname(), dto.getLastname(), dto.getSsn(), dto.getEmail(), dto.getSpeciality(), dto.getUser(), dto.getCourses());
    }

    public static Teacher mapToTeacher(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getSsn(), dto.getEmail(), dto.getSpeciality(), dto.getUser(), dto.getCourses());
    }

    public static TeacherReadOnlyDTO mapTeacherToReadOnly(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), teacher.getSsn(), teacher.getEmail(), teacher.getSpeciality(),teacher.getUser(),teacher.getAllCourses());
    }

    public static Student mapToStudent(StudentInsertDTO dto) {
        return new Student(null, dto.getFirstname(), dto.getLastname(), dto.getGender(), dto.getEmail(), dto.getCity(), dto.getUser(), dto.getCourses());
    }

    public static Student mapToStudent(StudentUpdateDTO dto) {
        return new Student(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getGender(), dto.getEmail(), dto.getCity(), dto.getUser(), dto.getCourses());
    }

    public static StudentReadOnlyDTO mapStudentToReadOnly(Student student) {
        return new StudentReadOnlyDTO(student.getId(), student.getFirstname(), student.getLastname(), student.getGender(), student.getEmail(), student.getCity(), student.getUser(), student.getAllCourses());
    }

    public static User mapToUser(UserInsertDTO dto) {
        return new User(null, dto.getUsername(), dto.getPassword(), dto.getRole(), dto.getTeacher(), dto.getStudent() );
    }

    public static User mapToUser(UserUpdateDTO dto) {
        return new User(dto.getId(), dto.getUsername(), dto.getPassword(), dto.getRole(), dto.getTeacher(), dto.getStudent() );
    }

    public static UserReadOnlyDTO mapUserToReadOnly(User user) {
        return new UserReadOnlyDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.getTeacher(), user.getStudent());
    }

    public static Speciality mapToSpeciality(SpecialityInsertDTO dto) {
        return new Speciality(null, dto.getSpeciality(), dto.getTeachers());
    }

    public static Speciality mapToSpeciality(SpecialityUpdateDTO dto) {
        return new Speciality(dto.getId(), dto.getSpeciality(), dto.getTeachers());
    }

    public static SpecialityReadOnlyDTO mapSpecialityToReadOnly(Speciality speciality) {
        return new SpecialityReadOnlyDTO(speciality.getId(), speciality.getSpeciality());
    }

    public static City mapToCity(CityInsertDTO dto) {
        return new City(null, dto.getCity(), dto.getStudents());
    }

    public static City mapToCity(CityUpdateDTO dto) {
        return new City(dto.getId(), dto.getCity(), dto.getStudents());
    }

    public static CityReadOnlyDTO mapCityToReadOnly(City city) {
        return new CityReadOnlyDTO(city.getId(), city.getCity(), city.getAllStudents());
    }

    public static Course mapToCourse(CourseInsertDTO dto) {
        return new Course(null, dto.getCourseName(), dto.getTeacher(), dto.getStudents());
    }

    public static Course mapToCourse(CourseUpdateDTO dto) {
        return new Course(dto.getId(), dto.getCourseName(), dto.getTeacher(), dto.getStudents());
    }

    public static CourseReadOnlyDTO mapToReadOnlyDTO(Course course) {
        return new CourseReadOnlyDTO(course.getId(), course.getCourseName(), course.getTeacher(), course.getAllStudents());
    }

    public static User mapToUser(UserLoginDTO dto) {
        return new User(null, dto.getUsername(), dto.getPassword(), null, null, null);
    }

    public static User mapToUser(UserRegisterDTO dto) {
        return new User(null, dto.getUsername(), dto.getPassword(), dto.getRole(), dto.getTeacher(), dto.getStudent());
    }

    public static Teacher extractTeacherFromRegisterTeacherDTO(RegisterTeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstname(dto.getFirstname());
        teacher.setLastname(dto.getLastname());
        teacher.setSsn(dto.getSsn());
        teacher.setEmail(dto.getEmail());
        return teacher;
    }

    public static User extractUserFromRegisterTeacherDto(RegisterTeacherDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodePassword(dto.getPassword()));
        user.setRole(Role.TEACHER);
        user.setIsActive(true);
        return user;
    }

    public static Student extractStudentFromRegisterStudentDTO(RegisterStudentDTO dto) {
        Student student = new Student();
        student.setFirstname(dto.getFirstname());
        student.setLastname(dto.getLastname());
        student.setGender(dto.getGender());
        student.setEmail(dto.getEmail());
        return student;
    }

    public static User extractUserFromRegisterStudentDto(RegisterStudentDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encodePassword(dto.getPassword()));
        user.setRole(Role.STUDENT);
        user.setIsActive(true);
        return user;
    }

    private static String encodePassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
