package najah.stu.domain;

import najah.stu.strategy.CarPricingStrategy;

public class Car extends Vehicle {

    public Car(int id,String brand,String model,boolean available,double dailyRate) {

        super(
                id,
                brand,
                model,
                available,
                dailyRate,
                new CarPricingStrategy()
        );
    }

    @Override
    public VehicleType getType() {

        return VehicleType.CAR;
    }
}