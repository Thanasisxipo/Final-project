package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.SpecialityInsertDTO;
import gr.aueb.cf.schoolapp.dto.SpecialityUpdateDTO;
import gr.aueb.cf.schoolapp.model.Speciality;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ISpecialityService {
    Speciality insertSpeciality(SpecialityInsertDTO dto) throws Exception;
    Speciality updateSpeciality(SpecialityUpdateDTO dto) throws EntityNotFoundException;
    void deleteSpeciality(Long id) throws EntityNotFoundException;
    Speciality getSpecialityById(Long id) throws EntityNotFoundException;
    List<Speciality> getAllSpecialities() throws EntityNotFoundException;
}
