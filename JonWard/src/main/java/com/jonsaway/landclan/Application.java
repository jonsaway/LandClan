package com.jonsaway.landclan;

import com.jonsaway.landclan.jpa.LandParcel;
import com.jonsaway.landclan.jpa.LandParcelRepository;
import com.jonsaway.landclan.rest.ParcelStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // NB: Uncomment 'demo()' to prepopulate the DB with some example land parcels
    /*
    @Bean
    public CommandLineRunner demo(LandParcelRepository repository) {
        return (args) -> {
            // Populate with a few example land parcels
            repository.save(new LandParcel(123, "Alice House", ParcelStatus.SAVED, 42, true));
            repository.save(new LandParcel(246, "Bob Office", ParcelStatus.APPROVED, 27, false));
            repository.save(new LandParcel(369, "Charlie School", ParcelStatus.SHORT_LISTED, 103, true));
            repository.save(new LandParcel(4812, "David Field", ParcelStatus.UNDER_CONSIDERATION, 2.5, false));
            repository.save(new LandParcel(51020, "Eve Hospital", ParcelStatus.SAVED, 198, true));
        };
    }
    */
}