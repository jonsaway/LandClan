package com.jonsaway.landclan.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LandParcelRepository extends CrudRepository<LandParcel, Long> {

    List<LandParcel> findByName(String name);

    LandParcel findByObjectId(long objectId);
}