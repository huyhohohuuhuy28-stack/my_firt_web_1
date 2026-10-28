// package huy.ho.myfirstweb;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

// @Controller
// public class RentalManagerController {

//     private static final List<RentalContract> contractList = new ArrayList<>();

//     static {
//         contractList.add(new RentalContract("HD001", "Nguyễn Văn A", "51A-123.45", 
//                 LocalDate.of(2026, 9, 30), LocalDate.of(2026, 10, 2), "Đang thuê", 1500000));
//         contractList.add(new RentalContract("HD002", "Trần Thị B", "51A-123.46", 
//                 LocalDate.of(2026, 9, 28), LocalDate.of(2026, 9, 30), "Đã trả", 1200000));
//     }

//     // 1. Màn hình danh sách hợp đồng
//     @GetMapping({"/RentalManager", "/contracts"})
//     public String listRentalManager(Model model) {
//         long totalContracts = contractList.size();
//         long rentingCount = contractList.stream().filter(c -> "Đang thuê".equalsIgnoreCase(c.getStatus())).count();
//         long returnedCount = contractList.stream().filter(c -> "Đã trả".equalsIgnoreCase(c.getStatus())).count();

//         model.addAttribute("contracts", contractList);
//         model.addAttribute("totalContracts", totalContracts);
//         model.addAttribute("rentingCount", rentingCount);
//         model.addAttribute("returnedCount", returnedCount);
//         return "RentalManager";
//     }

//     // 2. Mở Form tạo hợp đồng mới
//     @GetMapping("/RentalManager/add")
//     public String showAddContractForm(Model model) {
//         model.addAttribute("contract", new RentalContract());
//         model.addAttribute("pageTitle", "Tạo Hợp Đồng Thuê Xe");
//         return "ContractForm";
//     }

//     // 3. Xử lý lưu hợp đồng
//     @PostMapping("/RentalManager/add")
//     public String saveContract(
//             @RequestParam String customerName,
//             @RequestParam String licensePlate,
//             @RequestParam String rentalDate,
//             @RequestParam String expectedReturnDate,
//             @RequestParam double totalPrice,
//             Model model) {

//         String newId = String.format("HD%03d", contractList.size() + 1);
//         RentalContract contract = new RentalContract(
//                 newId,
//                 customerName,
//                 licensePlate,
//                 LocalDate.parse(rentalDate),
//                 LocalDate.parse(expectedReturnDate),
//                 "Đang thuê",
//                 totalPrice
//         );

//         contractList.add(contract);
//         model.addAttribute("success", "Tạo hợp đồng thành công!");
//         model.addAttribute("contract", new RentalContract());
//         model.addAttribute("pageTitle", "Tạo Hợp Đồng Thuê Xe");
//         return "ContractForm";
//     }

//     // 4. Xóa hợp đồng trực tiếp
//     @GetMapping("/RentalManager/delete")
//     public String deleteContract(@RequestParam(name = "id", required = false) String id) {
//         if (id != null && !id.trim().isEmpty()) {
//             contractList.removeIf(c -> c.getContractId().equalsIgnoreCase(id.trim()));
//         }
//         return "redirect:/RentalManager";
//     }
// }


package huy.ho.myfirstweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalManagerController {

    private static final List<RentalContract> contractList = new ArrayList<>();
    private final VehicleManager vehicleManager = new VehicleManager();
    private final VehicleDataService vehicleDataService;

    static List<RentalContract> getContractList() {
        return List.copyOf(contractList);
    }

    public RentalManagerController(VehicleDataService vehicleDataService) {
        this.vehicleDataService = vehicleDataService;
        this.vehicleDataService.load(vehicleManager);
    }

    @GetMapping({"/RentalManager", "/contracts"})
    public String listRentalManager(Model model) {
        long totalContracts = contractList.size();
        long rentingCount = contractList.stream().filter(c -> "Đang thuê".equalsIgnoreCase(c.getStatus())).count();
        long returnedCount = contractList.stream().filter(c -> "Đã trả".equalsIgnoreCase(c.getStatus())).count();

        model.addAttribute("contracts", contractList);
        model.addAttribute("totalContracts", totalContracts);
        model.addAttribute("rentingCount", rentingCount);
        model.addAttribute("returnedCount", returnedCount);
        return "RentalManager";
    }

    // Mở Form tạo hợp đồng - Lấy danh sách Khách hàng và Xe sẵn sàng
    @GetMapping("/RentalManager/add")
    public String showAddContractForm(Model model) {
        // Lấy danh sách xe còn trống (isAvailable == true)
        List<Vehicle> availableVehicles = vehicleManager.getVehicles().values().stream()
                .filter(Vehicle::isAvailable)
                .toList();

        model.addAttribute("customers", CustomerController.getCustomerList());
        model.addAttribute("vehicles", availableVehicles);
        model.addAttribute("contract", new RentalContract());
        model.addAttribute("pageTitle", "Tạo Hợp Đồng Thuê Xe");
        return "ContractForm";
    }

    // Xử lý tạo hợp đồng & Đổi trạng thái xe thành "Đang thuê"
    @PostMapping("/RentalManager/add")
    public String saveContract(
            @RequestParam String customerName,
            @RequestParam String licensePlate,
            @RequestParam String rentalDate,
            @RequestParam String expectedReturnDate,
            Model model) {

        Vehicle vehicle = vehicleManager.findVehicle(licensePlate);
        if (vehicle == null || !vehicle.isAvailable()) {
            model.addAttribute("error", "Xe không hợp lệ hoặc đang được thuê!");
            return showAddContractForm(model);
        }

        LocalDate start = LocalDate.parse(rentalDate);
        LocalDate end = LocalDate.parse(expectedReturnDate);
        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) days = 1;

        // Tự động tính tổng tiền theo giá xe/ngày
        double totalPrice = vehicle.calculateRentCost(days);

        String newId = String.format("HD%03d", contractList.size() + 1);
        RentalContract contract = new RentalContract(
                newId, customerName, licensePlate, start, end, "Đang thuê", totalPrice
        );

        contractList.add(contract);

        // Chuyển trạng thái xe thành Đang thuê (isAvailable = false)
        vehicle.setAvailable(false);
        vehicleDataService.save(vehicleManager);

        return "redirect:/RentalManager";
    }

    // Trả xe - Chuyển trạng thái xe về "Sẵn sàng"
    @GetMapping("/RentalManager/return")
    public String returnVehicle(@RequestParam String id) {
        RentalContract contract = contractList.stream()
                .filter(c -> c.getContractId().equalsIgnoreCase(id))
                .findFirst().orElse(null);

        if (contract != null && "Đang thuê".equalsIgnoreCase(contract.getStatus())) {
            contract.setStatus("Đã trả");
            Vehicle vehicle = vehicleManager.findVehicle(contract.getLicensePlate());
            if (vehicle != null) {
                vehicle.setAvailable(true);
                vehicleDataService.save(vehicleManager);
            }
        }
        return "redirect:/RentalManager";
    }

    // Xóa hợp đồng & Phục hồi trạng thái xe
    @GetMapping("/RentalManager/delete")
    public String deleteContract(@RequestParam(name = "id", required = false) String id) {
        if (id != null && !id.trim().isEmpty()) {
            RentalContract contract = contractList.stream()
                    .filter(c -> c.getContractId().equalsIgnoreCase(id.trim()))
                    .findFirst().orElse(null);

            if (contract != null) {
                if ("Đang thuê".equalsIgnoreCase(contract.getStatus())) {
                    Vehicle vehicle = vehicleManager.findVehicle(contract.getLicensePlate());
                    if (vehicle != null) {
                        vehicle.setAvailable(true);
                        vehicleDataService.save(vehicleManager);
                    }
                }
                contractList.remove(contract);
            }
        }
        return "redirect:/RentalManager";
    }
}