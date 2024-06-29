package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.CityInsertDTO;
import gr.aueb.cf.schoolapp.dto.CityUpdateDTO;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ICityService {
    City insertCity(CityInsertDTO dto) throws Exception;
    City updateCity(CityUpdateDTO dto) throws EntityNotFoundException;
    void deleteCity(Long id) throws EntityNotFoundException;
    City getCityById(Long id) throws EntityNotFoundException;
    List<City> getAllCities() throws EntityNotFoundException;
}
