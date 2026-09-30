package com.travelit.config;

import com.travelit.destination.entity.Destination;
import com.travelit.destination.repository.DestinationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDestinations(DestinationRepository destinationRepository) {
        return args -> {
            // Only seed if database is empty
            if (destinationRepository.count() == 0) {
                System.out.println("[DataSeeder] Seeding destinations...");
                
                List<Destination> destinations = List.of(
                    new Destination("Taj Mahal", "India", "Uttar Pradesh", 
                        "Iconic white marble mausoleum, one of the Seven Wonders of the World"),
                    new Destination("India Gate", "India", "Delhi", 
                        "Historic war memorial and iconic landmark in New Delhi"),
                    new Destination("Goa Beaches", "India", "Goa", 
                        "Beautiful beaches, vibrant nightlife, and Portuguese heritage"),
                    new Destination("Jaipur", "India", "Rajasthan", 
                        "The Pink City with magnificent forts and palaces"),
                    new Destination("Kerala Backwaters", "India", "Kerala", 
                        "Serene network of lagoons and lakes with lush greenery"),
                    new Destination("Varanasi", "India", "Uttar Pradesh", 
                        "Ancient spiritual city on the banks of Ganges"),
                    new Destination("Agra Fort", "India", "Uttar Pradesh", 
                        "UNESCO World Heritage Site and historic Mughal fort"),
                    new Destination("Jaisalmer", "India", "Rajasthan", 
                        "Golden City with stunning desert landscapes and fort"),
                    new Destination("Mumbai", "India", "Maharashtra", 
                        "Bustling metropolis, Bollywood, Gateway of India"),
                    new Destination("Udaipur", "India", "Rajasthan", 
                        "City of Lakes with romantic palaces and heritage"),
                    new Destination("Hampi", "India", "Karnataka", 
                        "Ancient ruins and UNESCO World Heritage Site"),
                    new Destination("Rishikesh", "India", "Uttarakhand", 
                        "Yoga capital and adventure sports hub on Ganges"),
                    new Destination("Leh Ladakh", "India", "Ladakh", 
                        "High-altitude desert with breathtaking landscapes"),
                    new Destination("Mysore Palace", "India", "Karnataka", 
                        "Magnificent royal palace with Indo-Saracenic architecture"),
                    new Destination("Andaman Islands", "India", "Andaman and Nicobar", 
                        "Tropical paradise with pristine beaches and coral reefs")
                );
                
                destinationRepository.saveAll(destinations);
                System.out.println("[DataSeeder] Seeded " + destinations.size() + " destinations");
            } else {
                System.out.println("[DataSeeder] Destinations already exist (" + 
                    destinationRepository.count() + " found)");
            }
        };
    }
}
