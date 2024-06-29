package gr.aueb.cf.schoolapp.rest;


import gr.aueb.cf.schoolapp.dto.CityInsertDTO;
import gr.aueb.cf.schoolapp.dto.CityReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.CityUpdateDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.City;
import gr.aueb.cf.schoolapp.service.ICityService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.validator.CityInsertValidator;
import gr.aueb.cf.schoolapp.validator.CityUpdateValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * REST Controller for managing City entities.
 */
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityRestController {
    private final ICityService cityService;
    private final CityInsertValidator insertValidator;
    private final CityUpdateValidator updateValidator;

    /**
     * Adds a new city.
     *
     * @param dto the city insert DTO containing the details of the city to insert.
     * @param bindingResult the binding result for validation errors.
     * @return a ResponseEntity with the created city or an error status.
     */
    @Operation(summary = "Add a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "City created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CityReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable",
                    content = @Content)})
    @PostMapping("/")
    public ResponseEntity<CityReadOnlyDTO> addCity(@Valid @RequestBody CityInsertDTO dto, BindingResult bindingResult) {
        insertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            City city = cityService.insertCity(dto);
            CityReadOnlyDTO readOnlyDTO = Mapper.mapCityToReadOnly(city);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(readOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(readOnlyDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Updates an existing city.
     *
     * @param id the ID of the city to update.
     * @param dto the city update DTO containing the updated details of the city.
     * @param bindingResult the binding result for validation errors.
     * @return a ResponseEntity with the updated city or an error status.
     */
    @Operation(summary = "Update a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CityReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<CityReadOnlyDTO> updateCity(@PathVariable("id") Long id, @Valid @RequestBody CityUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        updateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            City city = cityService.updateCity(dto);
            CityReadOnlyDTO readOnlyDTO = Mapper.mapCityToReadOnly(city);
            return new ResponseEntity<>(readOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an existing city by its ID.
     *
     * @param id the ID of the city to delete.
     * @return a ResponseEntity with the deleted city or an error status.
     */
    @Operation(summary = "Delete a city by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CityReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<CityReadOnlyDTO> deleteCity(@PathVariable("id") Long id) {
        try {
            City city = cityService.getCityById(id);
            CityReadOnlyDTO readOnlyDTO = Mapper.mapCityToReadOnly(city);
            cityService.deleteCity(id);
            return ResponseEntity.ok(readOnlyDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves a city by its ID.
     *
     * @param id the ID of the city to retrieve.
     * @return a ResponseEntity with the retrieved city or an error status.
     */
    @Operation(summary = "Get a city by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CityReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<CityReadOnlyDTO> getCity(@PathVariable("id") Long id) {
        try {
            City city = cityService.getCityById(id);
            CityReadOnlyDTO dto = Mapper.mapCityToReadOnly(city);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves all cities.
     *
     * @return a ResponseEntity with a list of all cities or an error status.
     */
    @Operation(summary = "Get all cities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CityReadOnlyDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @GetMapping("/")
    public ResponseEntity<List<CityReadOnlyDTO>> getAllCities() {
        List<City> cities;
        try {
            cities = cityService.getAllCities();
            List<CityReadOnlyDTO> readOnlyDTOS = new ArrayList<>();
            for (City city : cities) {
                readOnlyDTOS.add(Mapper.mapCityToReadOnly(city));
            }
            return new ResponseEntity<>(readOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
