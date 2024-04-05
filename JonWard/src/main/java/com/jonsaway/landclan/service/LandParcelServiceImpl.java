package com.jonsaway.landclan.service;

import com.jonsaway.landclan.jpa.LandParcel;
import com.jonsaway.landclan.jpa.LandParcelRepository;
import com.jonsaway.landclan.rest.ParcelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LandParcelServiceImpl implements LandParcelService {
    @Autowired
    LandParcelRepository repository;

    // Retrieve all land parcels
    public Iterable<LandParcel> retrieveAll()
    {
        return repository.findAll();
    }

    // Retrieve a land parcel by object id
    // Throws NoSuchElement exception if parcel doesn't exist
    public LandParcel retrieve(long objectId) {
        // Retrieve the parcel with this id
        LandParcel landParcel = repository.findByObjectId(objectId);

        if(landParcel == null)
        {
            // Parcel doesn't exist: complain
            throw new NoSuchElementException(String.format("No Land Parcel exists with objectId=%d", objectId));
        }

        // Return the object to the caller
        return landParcel;
    }

    // Create a new land parcel
    // Returns the created object
    // Throws IllegalArgumentException exception if parcel already exists
    public LandParcel create(long objectId,
                             String name,
                             ParcelStatus status,
                             double area,
                             boolean constraints) {
        // Check if a parcel already exists with this id
        if(repository.existsById(objectId))
        {
            throw new IllegalArgumentException(String.format("Land Parcel already exists with objectId=%d - ignoring create", objectId));
        }

        // Create the object
        LandParcel landParcel = new LandParcel(objectId, name, status, area, constraints);

        // Store the new object and return it to the caller
        return repository.save(landParcel);
    }

    // Update an existing land parcel
    // Only modifies non-null values
    // (object id is the key, so not modifiable)
    // Returns the updated object
    // Throws NoSuchElement exception if parcel doesn't exist
    public LandParcel update(long objectId,
                             String name,
                             ParcelStatus status,
                             Double area,
                             Boolean constraints) {
        // Retrieve the existing parcel
        LandParcel landParcel = repository.findByObjectId(objectId);
        if(landParcel == null)
        {
            // Parcel doesn't exist: complain
            throw new NoSuchElementException(String.format("No Land Parcel exists with objectId=%d - ignoring update", objectId));
        }

        // Only perform modifications to non-null attributes
        if(name!=null) {
            landParcel.setName(name);
        }

        if(status!=ParcelStatus.NO_CHANGE) {
            landParcel.setStatus(status);
        }

        if(area!=null) {
            landParcel.setArea(area);
        }

        if(constraints!=null) {
            landParcel.setConstraints(constraints);
        }

        // Store the modified object and return it to the caller
        return repository.save(landParcel);
    }

    // Delete a land parcel by object id
    // Throws IllegalArgumentException exception if parcel doesn't exist
    public void delete(long objectId) {
        // Check that the parcel exists
        boolean exists = repository.existsById(objectId);
        if(!exists)
        {
            // Parcel doesn't exist: complain
            throw new NoSuchElementException(String.format("No Land Parcel exists with objectId=%d - ignoring delete", objectId));
        }

        // Delete the object
        repository.deleteById(objectId);
    }
}
