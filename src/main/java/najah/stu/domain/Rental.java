package najah.stu.domain;

import java.time.LocalDate;












public class Rental {
    private int id;
    private int vehicleId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
    
    
    
    public Rental(int id, int vehicleId, String customerName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = RentalStatus.ACTIVE;
    }

    
    
    
    
    public int getId() {
        return id;
    }

    
    
    
    
    public void setId(int id) {
        this.id = id;
    }

    
    
    
    
    public int getVehicleId() {
        return vehicleId;
    }

    
    
    
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    
    
    
    
    public String getCustomerName() {
          return customerName;
    }

    
    
    
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    
    
    
    
    public LocalDate getStartDate() {
           return startDate;
    }

    
    
    
    
    public void setStartDate(LocalDate startDate) {
           this.startDate = startDate;
    }

    
    
    
    
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
           this.endDate = endDate;
    }

    public RentalStatus getStatus() {
         return status;
    }

    
    public void setStatus(RentalStatus status) {
             this.status = status;
    }

    
    public boolean isActive() {
          return status == RentalStatus.ACTIVE;
    }

    
    public long getDurationInDays() {
            return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }
}