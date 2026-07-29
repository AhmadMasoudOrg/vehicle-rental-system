package najah.stu.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import najah.stu.domain.Car;
import najah.stu.domain.SUV;
import najah.stu.domain.Truck;
import najah.stu.domain.Vehicle;

public class PricingStrategyTest {

    @Test
    public void carShouldCalculateNormalRentalCost() {

        Vehicle vehicle = new Car(
                1,
                "Toyota",
                "Corolla",
                true,
                50.0
        );

        double cost = vehicle.calculateRentalCost(5);

        assertEquals(
                250.0,
                cost,
                0.001
        );
    }

    @Test
    public void suvShouldAddTenPercentToRentalCost() {

        Vehicle vehicle = new SUV(
                2,
                "Toyota",
                "RAV4",
                true,
                50.0
        );

        double cost = vehicle.calculateRentalCost(5);

        assertEquals(
                275.0,
                cost,
                0.001
        );
    }

    @Test
    public void truckShouldAddTwentyPercentToRentalCost() {

        Vehicle vehicle = new Truck(
                3,
                "Volvo",
                "FMX",
                true,
                50.0
        );

        double cost = vehicle.calculateRentalCost(5);

        assertEquals(
                300.0,
                cost,
                0.001
        );
    }

    @Test
    public void carShouldReturnZeroWhenRentalDaysAreZero() {

        Vehicle vehicle = new Car(
                1,
                "Toyota",
                "Corolla",
                true,
                50.0
        );

        double cost = vehicle.calculateRentalCost(0);

        assertEquals(
                0.0,
                cost,
                0.001
        );
    }

    @Test
    public void pricingStrategyCanBeChanged() {

        Vehicle vehicle = new Car(
                1,
                "Toyota",
                "Corolla",
                true,
                50.0
        );

        vehicle.setPricingStrategy(
                new TruckPricingStrategy()
        );

        double cost = vehicle.calculateRentalCost(5);

        assertEquals(
                300.0,
                cost,
                0.001
        );
    }
}