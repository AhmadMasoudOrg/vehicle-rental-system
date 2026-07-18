package najah.stu.domain;

import najah.stu.strategy.TruckPricingStrategy;

public class Truck extends Vehicle {

    public Truck(int id,String brand,String model,boolean available,double dailyRate) {

        super(
                id,
                brand,
                model,
                available,
                dailyRate,
                new TruckPricingStrategy()
        );
    }

    @Override
    public VehicleType getType() {

        return VehicleType.TRUCK;
    }
}