package gr.aueb.cf.schoolapp.repositories;

import gr.aueb.cf.schoolapp.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    City findCityById(Long id);
}
