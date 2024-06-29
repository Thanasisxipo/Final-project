package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.CityInsertDTO;
import gr.aueb.cf.schoolapp.dto.CityUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.repositories.CityRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * City Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService{
    private final CityRepository cityRepository;

    /**
     * Inserts a new city into the database.
     *
     * @param dto The DTO containing the data to insert.
     * @return The newly inserted city.
     * @throws Exception If there is an error during insertion.
     */
    @Transactional
    @Override
    public City insertCity(CityInsertDTO dto) throws Exception {
        City city = null;
        try {
            city = cityRepository.save(Mapper.mapToCity(dto));
            if (city.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Insert success for city with id " + city.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return city;
    }

    /**
     * Updates an existing city in the database.
     *
     * @param dto The DTO containing the updated data.
     * @return The updated city entity.
     * @throws EntityNotFoundException If the city with the given ID does not exist.
     */
    @Transactional
    @Override
    public City updateCity(CityUpdateDTO dto) throws EntityNotFoundException {
        City city = null;
        City updatedCity = null;
        try {
            city = cityRepository.findCityById(dto.getId());
            if (city == null ) {
                throw new EntityNotFoundException(City.class, dto.getId());
            }
            updatedCity = cityRepository.save(Mapper.mapToCity(dto));
            log.info("City with id " + updatedCity.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedCity;
    }

    /**
     * Deletes a city from the database.
     *
     * @param id The ID of the city to delete.
     * @throws EntityNotFoundException If the city with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteCity(Long id) throws EntityNotFoundException {
        City city = null;
        try {
            city = cityRepository.findCityById(id);
            if (city == null ) {
                throw new EntityNotFoundException(City.class, id);
            }
            cityRepository.deleteById(id);
            log.info("City with id " + city.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a city by its ID from the database.
     *
     * @param id The ID of the city to retrieve.
     * @return The city entity.
     * @throws EntityNotFoundException If the city with the given ID does not exist.
     */
    @Transactional
    @Override
    public City getCityById(Long id) throws EntityNotFoundException {
        City city = null;
        try {
            city = cityRepository.findCityById(id);
            if (city == null ) {
                throw new EntityNotFoundException(City.class, id);
            }
            log.info("City with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return city;
    }

    /**
     * Retrieves all cities from the database.
     *
     * @return A list of all cities.
     * @throws EntityNotFoundException If no cities are found in the database.
     */
    @Override
    public List<City> getAllCities() throws EntityNotFoundException {
        List<City> cities = new ArrayList<>();
        try {
            cities = cityRepository.findAll();
            if (cities.isEmpty()) {
                throw new EntityNotFoundException(City.class, 0L);
            }
            log.info("Retrieved all cities.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return cities;
    }
}
