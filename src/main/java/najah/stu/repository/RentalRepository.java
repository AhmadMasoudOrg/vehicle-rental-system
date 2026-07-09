package najah.stu.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
public class RentalRepository {

    private final List<Rental> rentals;
    private final AtomicInteger idGenerator;

    public RentalRepository() {
        this.rentals = new ArrayList<>();
        this.idGenerator = new AtomicInteger(1);
    }

    public Rental save(Rental rental) {
        if (rental.getId() == 0) {
            rental.setId(idGenerator.getAndIncrement());
        }
        rentals.add(rental);
        return rental;
        
        
        
    }

    public List<Rental> getAllRentals() {
        return rentals;
        
        
        
    }

    
    public Optional<Rental> findById(int id) {
        return rentals.stream()
                .filter(rental -> rental.getId() == id)
                .findFirst();
    }
    public Optional<Rental> findActiveRentalByVehicleId(int vehicleId) {
        return rentals.stream()
                .filter(rental -> rental.getVehicleId() == vehicleId)
                .filter(Rental::isActive)
                .findFirst();
    }

    
    public boolean existsActiveRentalForVehicle(int vehicleId) {
        return findActiveRentalByVehicleId(vehicleId).isPresent();
    }

    
    public List<Rental> findByStatus(RentalStatus status) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getStatus() == status) {
                result.add(rental);
                
                
                
                
            }
        }
        return result;
    }
}