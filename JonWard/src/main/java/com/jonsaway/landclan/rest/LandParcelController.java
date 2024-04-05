package com.jonsaway.landclan.rest;

import com.jonsaway.landclan.jpa.LandParcel;
import com.jonsaway.landclan.service.LandParcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.NoSuchElementException;

@RestController
public class LandParcelController {
    @Autowired
    LandParcelServiceImpl service;

    // retrieve all land parcels
    // http://localhost:8080/landParcel
    // or
    // curl "http://localhost:8080/landParcel"
    @GetMapping("/landParcel")
    public Iterable<LandParcel> retrieve() {
        return service.retrieveAll();
    }

    // Retrieve a land parcel by object id
    // http://localhost:8080/landParcel/1
    // or
    // curl "http://localhost:8080/landParcel/1"
    @GetMapping("/landParcel/{id}")
    public LandParcel retrieve(@PathVariable("id") long objectId) {
        try {
            return service.retrieve(objectId);
        }
        catch (NoSuchElementException e) {
            // No land parcel exists with the specified object id
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new land parcel
    // curl -X POST "http://localhost:8080/landParcel/6" -F "name=\"Fred\"" -F"status=SAVED" -F "area=42" -F "constraints=false"
    // Object id must not already exist in database
    // All values are required
    // Returns 400 (bad request) if any of the values is missing or invalid
    // Returns the created object
    @PostMapping("/landParcel/{id}")
    public LandParcel create(@PathVariable("id") long objectId,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "status") ParcelStatus status,
                             @RequestParam(value = "area") double area,
                             @RequestParam(value = "constraints") boolean constraints) {
        try {
            return service.create(objectId, name, status, area, constraints);
        }
        catch (IllegalArgumentException e) {
            // A land parcel already exists with the specified object id
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // update an existing land parcel
    // curl -X PUT "http://localhost:8080/landParcel/6" -F "name=\"Fred Laboratory\"" -F "area=1.23"
    // Only updates the specified values
    // Returns the updated object
    @PutMapping("/landParcel/{id}")
    public LandParcel update(@PathVariable("id") long objectId,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "status", required = false, defaultValue = "NO_CHANGE") ParcelStatus status,
                             @RequestParam(value = "area", required = false) Double area,
                             @RequestParam(value = "constraints", required = false) Boolean constraints) {
        try {
            // Update the non-null values
            // (for this ParcelStatus.NO_CHANGE is treated as null)
            return service.update(objectId, name, status, area, constraints);
        }
        catch (NoSuchElementException e) {
            // No land parcel exists with the specified object id
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // delete a land parcel by object id
    // curl -X DELETE "http://localhost:8080/landParcel/6"
    @DeleteMapping("/landParcel/{id}")
    public void delete(@PathVariable("id") long objectId) {
        try {
            service.delete(objectId);
        }
        catch (NoSuchElementException e) {
            // No land parcel exists with the specified object id
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}