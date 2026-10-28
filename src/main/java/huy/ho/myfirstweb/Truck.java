package huy.ho.myfirstweb;

public class Truck extends Vehicle {
    public double payload;
    public Truck(String licensePlate, String brand, int manufactureYear, double baseDailyPrice, double payload) {
        super(licensePlate, brand, manufactureYear, baseDailyPrice);
        this.payload = payload;
    }

    public Truck(String licensePlate, String brand, int manufactureYear,
                 double baseDailyPrice, double payload, String condition) {
        super(licensePlate, brand, manufactureYear, baseDailyPrice, condition);
        this.payload = payload;
    }
//    public double getPayload() {
//        return payload;
//    }
    public void setPayload(double payload) {
        this.payload = payload;
    }
    public double calculateRentCost(long days) {
        double dailyPrice = getBaseDailyPrice() + payload * 5;
        return dailyPrice * days;
    }
    public void showInfo() {
        System.out.println("THONG TIN XE TAI");
        System.out.println("Bien so xe: " + getLicensePlate());
        System.out.println("Hang xe: " + getBrand());
        System.out.println("Nam san xuat: " + getManufactureYear());
        System.out.println("Gia thue/ngay: " + getBaseDailyPrice());
        System.out.println("Tinh trang xe: " + getCondition());
        System.out.println("Tai trong: " + payload + " tan");
        System.out.println("Trang thai: " + (isAvailable() ? "San sang" : "Dang duoc thue"));
    }
    public String toFileString() {
        return "TRUCK," + getLicensePlate() + "," + getBrand() + "," + getManufactureYear() + "," + getBaseDailyPrice() + "," + payload + "," + isAvailable() + "," + getCondition();
    }
}
