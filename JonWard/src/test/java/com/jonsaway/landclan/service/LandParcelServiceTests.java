package com.jonsaway.landclan.service;

import com.jonsaway.landclan.jpa.LandParcel;
import com.jonsaway.landclan.jpa.LandParcelRepository;
import com.jonsaway.landclan.rest.ParcelStatus;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class LandParcelServiceTests {
    // Some test data, common to all tests
    private static final LandParcel PARCEL_123 = new LandParcel(123, "Alice House", ParcelStatus.SAVED, 42, true);
    private static final LandParcel PARCEL_246 = new LandParcel(246, "Bob Office", ParcelStatus.APPROVED, 27, false);
    private static final LandParcel PARCEL_369 = new LandParcel(369, "Charlie School", ParcelStatus.SHORT_LISTED, 103, true);
    private static final List<LandParcel> THREE_PARCELS = Arrays.asList(PARCEL_123, PARCEL_246, PARCEL_369);

    // Test that we can retrieve all objects from the repository
    @Test
    public void testRetrieveAll() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use a mock repository containing known data
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.findAll()).thenReturn(THREE_PARCELS);

        service.repository = mockRepo;

        // Service should return all the objects in the repository
        Iterable<LandParcel> iter = service.retrieveAll();
        int count = 0;
        for(LandParcel elem: iter)
        {
            assertThat(elem.getObjectId()).isEqualTo(THREE_PARCELS.get(count).getObjectId());
            count++;
        }

        assertThat(count == 3);
    }

    // Test that we can retrieve a given object that exists in the repository
    @Test
    public void testRetrieveExistingParcel() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use a mock repository containing known data
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.findByObjectId(246)).thenReturn(PARCEL_246);

        service.repository = mockRepo;

        // Service should return the requested object from the repository
        LandParcel parcel = service.retrieve(246);
        assertThat(parcel).isEqualTo(PARCEL_246);
    }

    // Test that an attempt to retrieve a non-existant object from the repository will fail
    @Test
    public void testRetrieveNonExistantParcel() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use an empty mock repository
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);

        service.repository = mockRepo;

        // Check that retrieve fails for non-existant object
        assertThatThrownBy(() -> {
            LandParcel parcel = service.retrieve(12);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("objectId=12");

    }

    // Test we can create a new object in the repository
    @Test
    public void testCreateNew() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use an empty mock repository
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);

        // repo will return the new parcel
        LandParcel newParcel = new LandParcel(12, "new", ParcelStatus.APPROVED, 21, true);
        when(mockRepo.save(any())).thenReturn(newParcel); // NB: not very robust

        service.repository = mockRepo;

        // Service should return the newly created object
        LandParcel parcel = service.create(12, "new", ParcelStatus.APPROVED, 21, false);
        assertThat(parcel).isEqualTo(newParcel);
    }

    // Test that an attempt to create object reusing an id that exists in the repository will fail
    @Test
    public void testCreateAlreadyExists() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use a mock repository containing the objectId we want to create
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.existsById(123L)).thenReturn(true);

        service.repository = mockRepo;

        // Check that create fails for already-existing object
        assertThatThrownBy(() -> {
            LandParcel parcel = service.create(123, "new", ParcelStatus.SAVED, 21.0, true);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("objectId=123");
    }

    // Test we can update an object that exists in the repository
    @Test
    public void testUpdateExisting() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use a mock repository containing the objectId we want to create
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.findByObjectId(123)).thenReturn(PARCEL_123);
        when(mockRepo.existsById(123L)).thenReturn(true);

        // repo will return the modified parcel
        LandParcel modifiedParcel = new LandParcel(123, "new", ParcelStatus.APPROVED, 21, true);
        when(mockRepo.save(any())).thenReturn(modifiedParcel); // NB: not very robust

        service.repository = mockRepo;

        // modify all the values on the parcel
        LandParcel parcel = service.update(123, "new", ParcelStatus.APPROVED, 21.0, false);
        assertThat(parcel).isEqualTo(modifiedParcel);
    }

    // Test that attempt to update an object that does not exist in the repository will fail
    @Test
    public void testUpdateNew() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use an empty mock repository
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);

        service.repository = mockRepo;

        // Check that update fails for non-existant object
        assertThatThrownBy(() -> {
            LandParcel parcel = service.update(12, "new", ParcelStatus.APPROVED, 21.0, false);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("objectId=12");
    }


    // Test we can delete an object that exists in the repository
    @Test
    public void testDeleteExisting() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use a mock repository containing the objectId we want to delete
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.existsById(123L)).thenReturn(true);

        service.repository = mockRepo;

        // delete the parcel
        service.delete(123);
    }

    // Test that attempt to delete an object that does not exist in the repository will fail
    @Test
    public void testDeleteNonExistant() throws Exception {
        LandParcelServiceImpl service = new LandParcelServiceImpl();

        // Use an empty mock repository
        LandParcelRepository mockRepo = mock(LandParcelRepository.class);
        when(mockRepo.existsById(anyLong())).thenReturn(false);

        service.repository = mockRepo;

        // Check that delete fails for non-existant object
        assertThatThrownBy(() -> {
            service.delete(12);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("objectId=12");
    }

}
