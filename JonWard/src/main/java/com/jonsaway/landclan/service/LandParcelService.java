package com.jonsaway.landclan.service;

import com.jonsaway.landclan.jpa.LandParcel;
import com.jonsaway.landclan.rest.ParcelStatus;
import org.springframework.stereotype.Service;

public interface LandParcelService {
    // Retrieve all land parcels
    public Iterable<LandParcel> retrieveAll();

    // Retrieve a land parcel by object id
    // Throws NoSuchElement exception if parcel doesn't exist
    public LandParcel retrieve(long objectId);

    // Create a new land parcel
    // Returns the created object
    // Throws IllegalArgumentException exception if parcel already exists
    public LandParcel create(long objectId,
                             String name,
                             ParcelStatus status,
                             double area,
                             boolean constraints) ;

    // Update an existing land parcel
    // Only modifies non-null values
    // (object id is the key, so not modifiable)
    // Returns the updated object
    // Throws NoSuchElement exception if parcel doesn't exist
    public LandParcel update(long objectId,
                             String name,
                             ParcelStatus status,
                             Double area,
                             Boolean constraints);

    // Delete a land parcel by object id
    // Throws IllegalArgumentException exception if parcel doesn't exist
    public void delete(long objectId);

}
