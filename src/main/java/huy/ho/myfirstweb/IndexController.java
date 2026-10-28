package huy.ho.myfirstweb;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class IndexController {

    private final VehicleDataService vehicleDataService;

    public IndexController(VehicleDataService vehicleDataService) {
        this.vehicleDataService = vehicleDataService;
    }

	@GetMapping("/")
	public String index(Model model) {
        VehicleManager vehicleManager = new VehicleManager();
        vehicleDataService.load(vehicleManager);
        List<Vehicle> vehicles = new ArrayList<>(vehicleManager.getVehicles().values());
        List<RentalContract> contracts = RentalManagerController.getContractList();

        long availableVehicles = vehicles.stream().filter(Vehicle::isAvailable).count();
        long rentedVehicles = vehicles.size() - availableVehicles;
        long carCount = vehicles.stream().filter(Car.class::isInstance).count();
        long truckCount = vehicles.size() - carCount;
        long rentingCount = contracts.stream()
                .filter(contract -> "Đang thuê".equalsIgnoreCase(contract.getStatus()))
                .count();
        long returnedCount = contracts.size() - rentingCount;
        double totalRevenue = contracts.stream()
                .mapToDouble(RentalContract::getTotalPrice)
                .sum();

        List<String> monthLabels = new ArrayList<>();
        List<Double> monthlyRevenue = new ArrayList<>();
        int reportYear = LocalDate.now().getYear();
        for (Month month : Month.values()) {
            monthLabels.add(month.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("vi")));
            double revenue = contracts.stream()
                    .filter(contract -> contract.getRentalDate() != null
                            && contract.getRentalDate().getYear() == reportYear
                            && contract.getRentalDate().getMonth() == month)
                    .mapToDouble(RentalContract::getTotalPrice)
                    .sum();
            monthlyRevenue.add(revenue);
        }

        model.addAttribute("totalVehicles", vehicles.size());
        model.addAttribute("availableVehicles", availableVehicles);
        model.addAttribute("rentedVehicles", rentedVehicles);
        model.addAttribute("carCount", carCount);
        model.addAttribute("truckCount", truckCount);
        model.addAttribute("totalContracts", contracts.size());
        model.addAttribute("rentingCount", rentingCount);
        model.addAttribute("returnedCount", returnedCount);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("reportYear", reportYear);
        model.addAttribute("monthLabels", monthLabels);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
		return "index";
	}
}