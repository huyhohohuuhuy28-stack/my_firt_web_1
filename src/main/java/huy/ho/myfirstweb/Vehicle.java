package huy.ho.myfirstweb;

public abstract class Vehicle implements Icalculate {
    public String licensePlate;
    public String brand;
    public int manufactureYear;
    public double baseDailyPrice;
    public boolean isAvailable;
    public String condition;

    public Vehicle(String licensePlate, String brand, int manufactureYear, double baseDailyPrice){
        this(licensePlate, brand, manufactureYear, baseDailyPrice, "Tốt");
    }

    public Vehicle(String licensePlate, String brand, int manufactureYear,
                   double baseDailyPrice, String condition){
        this.licensePlate=licensePlate;
        this.brand=brand;
        this.manufactureYear=manufactureYear;
        this.baseDailyPrice=baseDailyPrice;
        this.isAvailable=true;
        this.condition=condition;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand=brand;
    }
    public int getManufactureYear() {
        return manufactureYear;
    }
    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear=manufactureYear;
    }
    public double getBaseDailyPrice() {
        return baseDailyPrice;
    }
    public void setBaseDailyPrice(double baseDailyPrice) {
        this.baseDailyPrice=baseDailyPrice;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
//    public abstract double calculateRentCost(long days);
    public abstract void showInfo();
    public abstract String toFileString();
}


