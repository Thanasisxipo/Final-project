package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.SpecialityInsertDTO;
import gr.aueb.cf.schoolapp.dto.SpecialityUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Speciality;
import gr.aueb.cf.schoolapp.repositories.SpecialityRepository;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Speciality Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialityServiceImpl implements ISpecialityService{
    private final SpecialityRepository specialityRepository;

    /**
     * Inserts a new speciality into the database.
     *
     * @param dto the speciality insert DTO containing the data to insert.
     * @return the inserted speciality.
     * @throws Exception if an error occurs during the insertion process.
     */
    @Transactional
    @Override
    public Speciality insertSpeciality(SpecialityInsertDTO dto) throws Exception {
        Speciality speciality = null;
        try {
            speciality = specialityRepository.save(Mapper.mapToSpeciality(dto));
            if (speciality.getId() == null) {
                throw new Exception("Insert error");
            }
            log.info("Insert success for speciality with id " + speciality.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return speciality;
    }

    /**
     * Updates an existing speciality in the database.
     *
     * @param dto The DTO containing the updated data.
     * @return The updated speciality entity.
     * @throws EntityNotFoundException If the speciality with the given ID does not exist.
     */
    @Transactional
    @Override
    public Speciality updateSpeciality(SpecialityUpdateDTO dto) throws EntityNotFoundException {
        Speciality speciality = null;
        Speciality updatedSpeciality = null;
        try {
            speciality = specialityRepository.findSpecialityById(dto.getId());
            if (speciality == null ) {
                throw new EntityNotFoundException(Speciality.class, dto.getId());
            }
            updatedSpeciality = specialityRepository.save(Mapper.mapToSpeciality(dto));
            log.info("Speciality with id " + updatedSpeciality.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return updatedSpeciality;
    }

    /**
     * Deletes a speciality from the database.
     *
     * @param id The ID of the speciality to delete.
     * @throws EntityNotFoundException If the speciality with the given ID does not exist.
     */
    @Transactional
    @Override
    public void deleteSpeciality(Long id) throws EntityNotFoundException {
        Speciality speciality = null;
        try {
            speciality = specialityRepository.findSpecialityById(id);
            if (speciality == null ) {
                throw new EntityNotFoundException(Speciality.class, id);
            }
            specialityRepository.deleteById(id);
            log.info("Speciality with id " + speciality.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a speciality by its ID from the database.
     *
     * @param id The ID of the speciality to retrieve.
     * @return The speciality entity.
     * @throws EntityNotFoundException If the speciality with the given ID does not exist.
     */
    @Transactional
    @Override
    public Speciality getSpecialityById(Long id) throws EntityNotFoundException {
        Speciality speciality = null;
        try {
            speciality = specialityRepository.findSpecialityById(id);
            if (speciality == null ) {
                throw new EntityNotFoundException(Speciality.class, id);
            }
            log.info("Speciality with id  " + id + " was found");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return speciality;
    }

    /**
     * Retrieves all specialities from the database.
     *
     * @return A list of all specialities.
     * @throws EntityNotFoundException If no specialities are found in the database.
     */
    @Override
    public List<Speciality> getAllSpecialities() throws EntityNotFoundException {
        List<Speciality> specialities = new ArrayList<>();
        try {
            specialities = specialityRepository.findAll();
            if (specialities.isEmpty()) {
                throw new EntityNotFoundException(Speciality.class, 0L);
            }
            log.info("Retrieved all specialities.");
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        }
        return specialities;
    }
}
