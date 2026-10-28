package huy.ho.myfirstweb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VehicleManagerController {

    private static final List<String> VEHICLE_CONDITIONS =
            List.of("Mới", "Tốt", "Bình thường", "Cần bảo dưỡng");
    private final VehicleManager vehicleManager = new VehicleManager();
    private final VehicleDataService vehicleDataService;

    public VehicleManagerController(VehicleDataService vehicleDataService) {
        this.vehicleDataService = vehicleDataService;
        this.vehicleDataService.load(vehicleManager);
    }

    @GetMapping("/vehicles")
    public String listVehicleManager(Model model) {
        System.out.println("Total vehicles: " + vehicleManager.getVehicles());
        List<Vehicle> vehicles = new ArrayList<>(vehicleManager.getVehicles().values());
        System.out.println("Total vehicles: " + vehicles.size());
        model.addAttribute("vehicles", vehicles);

        model.addAttribute("totalVehicles", vehicleManager.getVehicles().size());
        model.addAttribute("availableVehicles",
                vehicleManager.getVehicles().values().stream()
                        .filter(Vehicle::isAvailable).count());
        model.addAttribute("rentedVehicles",
                vehicleManager.getVehicles().values().stream()
                        .filter(v -> !v.isAvailable()).count());
        model.addAttribute("brands", vehicleManager.getSupportedBrands());
        return "VehicleManager";
    }

    @GetMapping("/vehicles/add")
    public String showAddForm(Model model) {
        addVehicleFormOptions(model);
        return "VehicleForm";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(
            @RequestParam String type,
            @RequestParam String licensePlate,
            @RequestParam String brand,
            @RequestParam int manufactureYear,
            @RequestParam double baseDailyPrice,
            @RequestParam String condition,
            @RequestParam(required = false) Integer numberOfSeats,
            @RequestParam(required = false) Double payload,
            Model model) {

        licensePlate = licensePlate.trim().toUpperCase();
        String normalizedBrand = vehicleManager.normalizeBrand(brand);

        if (vehicleManager.findVehicle(licensePlate) != null) {
            model.addAttribute("error", "Biển số xe đã tồn tại.");
            addVehicleFormOptions(model);
            return "VehicleForm";
        }

        if (normalizedBrand == null) {
            model.addAttribute("error", "Hãng xe không hợp lệ.");
            addVehicleFormOptions(model);
            return "VehicleForm";
        }

        if (!VEHICLE_CONDITIONS.contains(condition)) {
            model.addAttribute("error", "Tình trạng xe không hợp lệ.");
            addVehicleFormOptions(model);
            return "VehicleForm";
        }

        Vehicle vehicle;
        if ("CAR".equalsIgnoreCase(type)) {
            if (numberOfSeats == null || numberOfSeats <= 0) {
                model.addAttribute("error", "Số chỗ ngồi phải lớn hơn 0.");
                addVehicleFormOptions(model);
                return "VehicleForm";
            }
            vehicle = new Car(licensePlate, normalizedBrand, manufactureYear,
                    baseDailyPrice, numberOfSeats, condition);
        } else if ("TRUCK".equalsIgnoreCase(type)) {
            if (payload == null || payload <= 0) {
                model.addAttribute("error", "Tải trọng phải lớn hơn 0.");
                addVehicleFormOptions(model);
                return "VehicleForm";
            }
            vehicle = new Truck(licensePlate, normalizedBrand, manufactureYear,
                    baseDailyPrice, payload, condition);
        } else {
            model.addAttribute("error", "Loại xe không hợp lệ.");
            addVehicleFormOptions(model);
            return "VehicleForm";
        }

        vehicleManager.addVehicle(vehicle);
        vehicleDataService.save(vehicleManager);
        //return "redirect:/VehicleManager";
        return "redirect:/vehicles";
    }

    @GetMapping("/vehicles/edit/{licensePlate}")
    public String showEditForm(@PathVariable String licensePlate, Model model) {
        Vehicle vehicle = vehicleManager.findVehicle(licensePlate.toUpperCase());
        if (vehicle == null) {
            return "redirect:/vehicles";
        }
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("brands", vehicleManager.getSupportedBrands());
        model.addAttribute("conditions", VEHICLE_CONDITIONS);
        model.addAttribute("isCar", vehicle instanceof Car);
        return "VehicleEdit";
    }

    @PostMapping("/vehicles/edit")
    public String editVehicle(
            @RequestParam String licensePlate,
            @RequestParam String brand,
            @RequestParam int manufactureYear,
            @RequestParam double baseDailyPrice,
            @RequestParam String condition,
            @RequestParam(required = false) Integer numberOfSeats,
            @RequestParam(required = false) Double payload,
            Model model) {

        Vehicle vehicle = vehicleManager.findVehicle(licensePlate.toUpperCase());
        if (vehicle == null || !vehicle.isAvailable()) {
            return "redirect:/vehicles";
        }

        String normalizedBrand = vehicleManager.normalizeBrand(brand);
        if (normalizedBrand == null || manufactureYear < 1900 ||
                baseDailyPrice <= 0 || !VEHICLE_CONDITIONS.contains(condition)) {
            model.addAttribute("error", "Dữ liệu nhập không hợp lệ.");
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("brands", vehicleManager.getSupportedBrands());
            model.addAttribute("conditions", VEHICLE_CONDITIONS);
            model.addAttribute("isCar", vehicle instanceof Car);
            return "VehicleEdit";
        }

        vehicle.setBrand(normalizedBrand);
        vehicle.setManufactureYear(manufactureYear);
        vehicle.setBaseDailyPrice(baseDailyPrice);
        vehicle.setCondition(condition);

        if (vehicle instanceof Car car && numberOfSeats != null && numberOfSeats > 0) {
            car.setNumberOfSeats(numberOfSeats);
        } else if (vehicle instanceof Truck truck && payload != null && payload > 0) {
            truck.setPayload(payload);
        }

        vehicleDataService.save(vehicleManager);
        return "redirect:/vehicles";
    }

    private void addVehicleFormOptions(Model model) {
        model.addAttribute("brands", vehicleManager.getSupportedBrands());
        model.addAttribute("conditions", VEHICLE_CONDITIONS);
    }

    @PostMapping("/vehicles/delete")
    public String deleteVehicle(@RequestParam String licensePlate) {
        vehicleManager.deleteVehicle(licensePlate.toUpperCase());
        vehicleDataService.save(vehicleManager);
        return "redirect:/vehicles";
    }
}