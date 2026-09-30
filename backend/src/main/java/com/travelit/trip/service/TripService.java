package com.travelit.trip.service;
import com.travelit.auth.entity.User;
import com.travelit.trip.dto.CreateTripRequest;
import com.travelit.trip.dto.TripResponse;
import com.travelit.trip.dto.UpdateTripRequest;
import com.travelit.trip.entity.Trip;
import com.travelit.trip.repository.TripRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TripService {
    private final TripRepository tripRepository;
    public TripService(TripRepository tripRepository) { this.tripRepository = tripRepository; }

    public TripResponse createTrip(CreateTripRequest request, Authentication auth) {
        User user = requireUser(auth);
        if (request.getEndDate().isBefore(request.getStartDate())) throw new IllegalArgumentException("End date cannot be before start date");
        Trip trip = new Trip(user.getId(), request.getTitle().trim(), clean(request.getDestination()), request.getStartDate(), request.getEndDate(), clean(request.getDescription()));
        return toResponse(tripRepository.save(trip));
    }

    public List<TripResponse> getMyTrips(Authentication auth) {
        User user = requireUser(auth);
        return tripRepository.findByUserIdOrderByStartDateDesc(user.getId()).stream().map(this::toResponse).toList();
    }

    public TripResponse getTrip(Long id, Authentication auth) {
        return toResponse(findOwned(id, requireUser(auth).getId()));
    }

    public TripResponse updateTrip(Long id, UpdateTripRequest request, Authentication auth) {
        Trip trip = findOwned(id, requireUser(auth).getId());
        if (request.getEndDate().isBefore(request.getStartDate())) throw new IllegalArgumentException("End date cannot be before start date");
        trip.setTitle(request.getTitle().trim()); trip.setDestination(clean(request.getDestination()));
        trip.setStartDate(request.getStartDate()); trip.setEndDate(request.getEndDate()); trip.setDescription(clean(request.getDescription()));
        return toResponse(tripRepository.save(trip));
    }

    public void deleteTrip(Long id, Authentication auth) { tripRepository.delete(findOwned(id, requireUser(auth).getId())); }

    public void deactivateTrip(Long id, Authentication auth) {
        Trip trip = findOwned(id, requireUser(auth).getId());
        trip.setActive(false); tripRepository.save(trip);
    }

    public List<TripResponse> getAllTrips() { return tripRepository.findAll().stream().map(this::toResponse).toList(); }
    public TripResponse getTrip(Long id) { return toResponse(tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found"))); }
    public List<TripResponse> getActiveTrips() { return tripRepository.findByActiveTrue().stream().map(this::toResponse).toList(); }
    public List<TripResponse> getTripsByDestination(String dest) { return tripRepository.findByDestinationIgnoreCase(dest.trim()).stream().map(this::toResponse).toList(); }
    public TripResponse updateTrip(Long id, UpdateTripRequest req) {
        if (req.getEndDate().isBefore(req.getStartDate())) throw new IllegalArgumentException("End date cannot be before start date");
        Trip t = tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
        t.setTitle(req.getTitle().trim()); t.setDestination(clean(req.getDestination())); t.setStartDate(req.getStartDate()); t.setEndDate(req.getEndDate()); t.setDescription(clean(req.getDescription()));
        return toResponse(tripRepository.save(t));
    }
    public void deleteTrip(Long id) { tripRepository.delete(tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found"))); }
    public void deactivateTrip(Long id) { Trip t = tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found")); t.setActive(false); tripRepository.save(t); }

    private Trip findOwned(Long id, Long uid) { return tripRepository.findByIdAndUserId(id, uid).orElseThrow(() -> new IllegalArgumentException("Trip not found")); }
    private User requireUser(Authentication auth) { if (auth == null || !(auth.getPrincipal() instanceof User u)) throw new IllegalArgumentException("Auth required"); return u; }
    private String clean(String v) { if (v == null) return null; String c = v.trim(); return c.isEmpty() ? null : c; }
    private TripResponse toResponse(Trip t) { return new TripResponse(t.getId(), t.getTitle(), t.getDestination(), t.getStartDate(), t.getEndDate(), t.getDescription(), t.isActive()); }
}
