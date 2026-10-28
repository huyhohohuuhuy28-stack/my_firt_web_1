package huy.ho.myfirstweb;

public class Car extends Vehicle {
    public int numberOfSeats;

    public Car(String licensePlate, String brand, int manufactureYear, double baseDailyPrice, int numberOfSeats) {
        super(licensePlate, brand, manufactureYear, baseDailyPrice);
        this.numberOfSeats = numberOfSeats;
    }

    public Car(String licensePlate, String brand, int manufactureYear,
               double baseDailyPrice, int numberOfSeats, String condition) {
        super(licensePlate, brand, manufactureYear, baseDailyPrice, condition);
        this.numberOfSeats = numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public double calculateRentCost(long days) {
        double dailyPrice = getBaseDailyPrice();
        if (numberOfSeats >= 7) {
            dailyPrice = dailyPrice * 1.10;
        }
        return dailyPrice * days;
    }

    public void showInfo() {
        System.out.println("THONG TIN XE O TO");
        System.out.println("Bien so xe: " + getLicensePlate());
        System.out.println("Hang xe: " + getBrand());
        System.out.println("Nam san xuat: " + getManufactureYear());
        System.out.println("Gia thue/ngay: " + getBaseDailyPrice());
        System.out.println("Tinh trang xe: " + getCondition());
        System.out.println("So cho ngoi: " + numberOfSeats);
        System.out.println("Trang thai: " + (isAvailable() ? "San sang" : "Dang duoc thue"));
    }

    public String toFileString() {
        return "CAR," + getLicensePlate() + "," + getBrand() + "," + getManufactureYear() + "," + getBaseDailyPrice() + "," + numberOfSeats + "," + isAvailable() + "," + getCondition();
    }
}