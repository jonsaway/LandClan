package com.jonsaway.landclan.jpa;

import com.jonsaway.landclan.rest.ParcelStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LandParcel {

    @Id
    private long objectId;
    private String name;
    private ParcelStatus status;
    private double area;
    private boolean constraints;

    protected LandParcel() {}

    public LandParcel(long objectId, String name, ParcelStatus status, double area, boolean constraints) {
        this.objectId = objectId;
        this.name = name;
        this.status = status;
        this.area = area;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return String.format(
                "LandParcel[objectId=%d, name='%s', status=%s, area=%f, constraints=%s]",
                objectId, name, status, area, constraints);
    }

    public Long getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }

    public ParcelStatus getStatus() {
        return status;
    }
    public void setStatus(ParcelStatus status) {
        this.status=status;
    }

    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area=area;
    }

    public boolean getConstraints() {
        return constraints;
    }
    public void setConstraints(boolean constraints) {
        this.constraints=constraints;
    }
}