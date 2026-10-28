package huy.ho.myfirstweb;

import java.util.HashMap;
import java.util.Map;

public class VehicleManager {

    public Map<String, Vehicle> vehicles;

    public String[] supportedBrands = { "Toyota", "Honda", "Ford", "Hyundai", "BMW", "Mercedes" };

    public VehicleManager() {
        this.vehicles = new HashMap<>();
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (vehicles.containsKey(vehicle.getLicensePlate())) {
            System.out.println("Xe voi bien so này da ton tai.");
            return false;
        }
        vehicles.put(vehicle.getLicensePlate(), vehicle);
        System.out.println("Them xe thanh cong.");
        return true;
    }

    public Vehicle findVehicle(String licensePlate) {
        return vehicles.get(licensePlate);
    }

    public boolean deleteVehicle(String licensePlate) {
        if (vehicles.containsKey(licensePlate)) {
            Vehicle vehicle = vehicles.get(licensePlate);
            if (!vehicle.isAvailable()) {
                System.out.println("Khong the xoa xe dang duoc thue.");
                return false;
            }
            vehicles.remove(licensePlate);
            System.out.println("Xoa xe thanh cong.");
            return true;
        }
        System.out.println("Khong tim thay xe.");
        return false;
    }

    public void displayAll() {
        if (vehicles.isEmpty()) {
            System.out.println("Danh sach xe dang trong.");
            return;
        }
        System.out.println("DANH SACH XE:");
        for (Vehicle vehicle : vehicles.values()) {
            vehicle.showInfo();
        }
    }

    public void displayAvailableVehicles() {
        boolean hasAvailable = false;
        for (Vehicle vehicle : vehicles.values()) {
            if (vehicle.isAvailable()) {
                vehicle.showInfo();
                hasAvailable = true;
            }
        }
        if (!hasAvailable) {
            System.out.println("Khong co xe nao dang san sang cho thue.");
        }
    }

    public String[] getSupportedBrands() {
        return supportedBrands;
    }

    public String normalizeBrand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        for (String brand : supportedBrands) {
            if (brand.equalsIgnoreCase(input.trim())) {
                return brand;
            }
        }
        return null;
    }

    public Map<String, Vehicle> getVehicles() {
        return vehicles;
    }
}