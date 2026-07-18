package najah.stu.domain;

import najah.stu.strategy.SuvPricingStrategy;

public class SUV extends Vehicle {

    public SUV(int id,String brand,String model,boolean available,double dailyRate) {

        super(
                id,
                brand,
                model,
                available,
                dailyRate,
                new SuvPricingStrategy()
        );
    }

    @Override
    public VehicleType getType() {

        return VehicleType.SUV;
    }
}