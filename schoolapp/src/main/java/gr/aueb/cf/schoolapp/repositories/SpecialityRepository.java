package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    Speciality findSpecialityById(Long id);
}
