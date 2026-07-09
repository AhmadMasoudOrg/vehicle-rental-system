package najah.stu.domain;

public class Vehicle {
    private int id;
    private String brand;
    private String model;
    private VehicleStatus status;

    public Vehicle(int id, String brand, String model, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.status = available ? VehicleStatus.AVAILABLE : VehicleStatus.RENTED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }

    
    
    
    
    
    
    public void setAvailable(boolean available) {
        this.status = available ? VehicleStatus.AVAILABLE : VehicleStatus.RENTED;
    }

  
    
    
    
    
    public void markAsRented() {
        this.status = VehicleStatus.RENTED;
    }

    public void markAsAvailable() {
        this.status = VehicleStatus.AVAILABLE;
    }
}