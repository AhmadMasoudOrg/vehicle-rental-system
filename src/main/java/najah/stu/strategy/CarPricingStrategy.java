package najah.stu.strategy;

public class CarPricingStrategy implements PricingStrategy {

    @Override
    public double calculateCost(long rentalDays,double dailyRate) {

        return rentalDays * dailyRate;
    }
}