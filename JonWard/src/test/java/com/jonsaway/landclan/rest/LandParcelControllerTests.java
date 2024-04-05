package com.jonsaway.landclan.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LandParcelControllerTests {
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void init() throws Exception {
        // Ensure that the database is empty before each test.
        // NB: this is safe for our in-memory tests, but wouldn't want to do this near real data...
        // NB: for simplicity, just delete the objects we may have added during these tests
        // (also gets rid of objects created by the 'demo' function in the Application

        // Some of these deletes may fail, if we haven't created them yet. Don't really care...
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/123"));
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/246"));
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/369"));
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/4812"));
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/51020"));

        // DB should be empty at this point
        mvc.perform(MockMvcRequestBuilders.get("/landParcel").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

    // Test that we can retrieve all objects from the repository
    @Test
    public void testRetrieveAll() throws Exception {
        // At this point, the repository should be empty
        mvc.perform(MockMvcRequestBuilders.get("/landParcel").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

        // add some entries to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                    .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/246")
                        .param("name","Bob Office")
                        .param("status", "APPROVED")
                        .param("area", "27.0")
                        .param("constraints", "false"))
                   .andExpect(status().isOk());

        // Check that we can retrieve the entries
        mvc.perform(MockMvcRequestBuilders.get("/landParcel").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"objectId\":123,\"name\":\"Alice House\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true},{\"objectId\":246,\"name\":\"Bob Office\",\"status\":\"APPROVED\",\"area\":27.0,\"constraints\":false}]")));
    }

    // Test that we can retrieve a given object that exists in the repository
    @Test
    public void testRetrieveExistingParcel() throws Exception {
        // At this point, the repository should be empty
        // add some entries to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/246")
                        .param("name","Bob Office")
                        .param("status", "APPROVED")
                        .param("area", "27.0")
                        .param("constraints", "false"))
                .andExpect(status().isOk());

        // Check that we can retrieve an entry
        mvc.perform(MockMvcRequestBuilders.get("/landParcel/246").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":246,\"name\":\"Bob Office\",\"status\":\"APPROVED\",\"area\":27.0,\"constraints\":false}")));
    }

    // Test that an attempt to retrieve a non-existant object from the repository will fail
    @Test
    public void testRetrieveNonExistantParcel() throws Exception {
        // At this point, the repository should be empty
        // add some entries to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/246")
                        .param("name","Bob Office")
                        .param("status", "APPROVED")
                        .param("area", "27.0")
                        .param("constraints", "false"))
                .andExpect(status().isOk());

        // Check that retrieve fails for non-existant object
        mvc.perform(MockMvcRequestBuilders.get("/landParcel/12").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("")));
    }

    // Test we can create a new object in the repository
    @Test
    public void testCreateNew() throws Exception {
        // At this point, the repository should be empty
        // Add an entry to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Alice House\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true}")));
    }

    // Test that an attempt to create object reusing an id that exists in the repository will fail
    @Test
    public void testCreateAlreadyExists() throws Exception {
        // At this point, the repository should be empty
        // Add an entry to the repository - this should succeed
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Alice House\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true}")));

        // Try to add another entry to the repository with the same ID - this should fail
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","NOT Alice")
                        .param("status", "APPROVED")
                        .param("area", "423.0")
                        .param("constraints", "false"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("")));
    }

    // Test we can update an object that exists in the repository
    @Test
    public void testUpdateExisting() throws Exception {
        // At this point, the repository should be empty
        // Add an entry to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Alice House\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true}")));

        // Modify the existing entry
        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("name","Renamed"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Renamed\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true}")));

        // We can modify each value in turn...
        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("status", "UNDER_CONSIDERATION"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Renamed\",\"status\":\"UNDER_CONSIDERATION\",\"area\":42.0,\"constraints\":true}")));

        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("area", "423.0"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Renamed\",\"status\":\"UNDER_CONSIDERATION\",\"area\":423.0,\"constraints\":true}")));

        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("constraints", "false"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Renamed\",\"status\":\"UNDER_CONSIDERATION\",\"area\":423.0,\"constraints\":false}")));

        // ... or modify multiple values simultaneously
        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("name","Renamed Again")
                        .param("status", "APPROVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Renamed Again\",\"status\":\"APPROVED\",\"area\":42.0,\"constraints\":true}")));
    }

    // Test that attempt to update an object that does not exist in the repository will fail
    @Test
    public void testUpdateNew() throws Exception {
        // At this point, the repository should be empty
        // Try to update a non-existant entry - this should fail
        mvc.perform(MockMvcRequestBuilders.put("/landParcel/123")
                        .param("name","Renamed Again")
                        .param("status", "APPROVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("")));
    }


    // Test we can delete an object that exists in the repository
    @Test
    public void testDeleteExisting() throws Exception {
        // At this point, the repository should be empty
        // Add an entry to the repository
        mvc.perform(MockMvcRequestBuilders.post("/landParcel/123")
                        .param("name","Alice House")
                        .param("status", "SAVED")
                        .param("area", "42.0")
                        .param("constraints", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"objectId\":123,\"name\":\"Alice House\",\"status\":\"SAVED\",\"area\":42.0,\"constraints\":true}")));

        // Delete the entry - this should succeed
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/123"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("")));
    }

    // Test that attempt to delete an object that does not exist in the repository will fail
    @Test
    public void testDeleteNonExistant() throws Exception {
        // At this point, the repository should be empty
        // Try to delete an entry - this should fail
        mvc.perform(MockMvcRequestBuilders.delete("/landParcel/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("")));
    }

}