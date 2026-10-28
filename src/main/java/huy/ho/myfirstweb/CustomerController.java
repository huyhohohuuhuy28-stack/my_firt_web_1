// package huy.ho.myfirstweb;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.ArrayList;
// import java.util.List;

// @Controller
// @RequestMapping("/customers")
// public class CustomerController {

//     // Danh sách lưu tạm trong bộ nhớ
//     private static final List<Customer> customerList = new ArrayList<>();

//     static {
//         customerList.add(new Customer("KH01", "Nguyễn Văn A", "0901234567", "nguyenvana@gmail.com"));
//         customerList.add(new Customer("KH02", "Trần Thị B", "0987654321", "tranthib@gmail.com"));
//     }

    
//     @GetMapping("")
//     public String listCustomers(Model model) {
//         // Tự động gán mã ID nếu người dùng chưa có ID
//         for (int i = 0; i < customerList.size(); i++) {
//             Customer c = customerList.get(i);
//             if (c.getId() == null || c.getId().trim().isEmpty()) {
//                 c.setId(String.format("KH%02d", i + 1));
//             }
//         }
//         model.addAttribute("customers", customerList);
//         return "customers";
//     }

//     // 2. Mở Form Thêm mới
//     @GetMapping("/add")
//     public String showAddForm(Model model) {
//         model.addAttribute("customer", new Customer());
//         model.addAttribute("pageTitle", "Thêm Khách Hàng Mới");
//         return "CustomerForm";
//     }

//     // 3. Mở Form Sửa
//     @GetMapping("/edit/{id}")
//     public String showEditForm(@PathVariable("id") String id, Model model) {
//         Customer existing = customerList.stream()
//                 .filter(c -> c.getId().equalsIgnoreCase(id))
//                 .findFirst()
//                 .orElse(null);

//         if (existing != null) {
//             model.addAttribute("customer", existing);
//             model.addAttribute("pageTitle", "Cập Nhật Khách Hàng");
//             return "CustomerForm";
//         }
//         return "redirect:/customers";
//     }

//     // 4. Xử lý Lưu (Cả Thêm mới và Cập nhật)
//     @PostMapping("/save")
//     public String saveCustomer(@ModelAttribute("customer") Customer customer) {
//         Customer existing = customerList.stream()
//                 .filter(c -> c.getId().equalsIgnoreCase(customer.getId()))
//                 .findFirst()
//                 .orElse(null);

//         if (existing != null) {
//             // Cập nhật thông tin khách hàng cũ
//             existing.setName(customer.getName());
//             existing.setPhone(customer.getPhone());
//             existing.setEmail(customer.getEmail());
//         } else {
//             // Thêm khách hàng mới
//             customerList.add(customer);
//         }
//         return "redirect:/customers";
//     }

//     @GetMapping("/delete")
//     public String deleteCustomerByParam(@RequestParam(name = "id", required = false) String id) {
//         if (id != null && !id.trim().isEmpty()) {
//             customerList.removeIf(c -> c.getId() != null && c.getId().equalsIgnoreCase(id.trim()));
//         }
//         return "redirect:/customers";
//     }

//     // Xử lý Xóa nhận tham số path /delete/KH...
//     @GetMapping("/delete/{id}")
//     public String deleteCustomerByPath(@PathVariable("id") String id) {
//         if (id != null && !id.trim().isEmpty()) {
//             customerList.removeIf(c -> c.getId() != null && c.getId().equalsIgnoreCase(id.trim()));
//         }
//         return "redirect:/customers";
//     }
// }




package huy.ho.myfirstweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private static final List<Customer> customerList = new ArrayList<>();

    static {
        customerList.add(new Customer("KH01", "Nguyễn Văn A", "0901234567", "nguyenvana@gmail.com"));
        customerList.add(new Customer("KH02", "Trần Thị B", "0987654321", "tranthib@gmail.com"));
    }

    public static List<Customer> getCustomerList() {
        return customerList;
    }

    @GetMapping("")
    public String listCustomers(Model model) {
        for (int i = 0; i < customerList.size(); i++) {
            Customer c = customerList.get(i);
            if (c.getId() == null || c.getId().trim().isEmpty()) {
                c.setId(String.format("KH%02d", i + 1));
            }
        }
        model.addAttribute("customers", customerList);
        return "customers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("pageTitle", "Thêm Khách Hàng Mới");
        return "CustomerForm";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Customer existing = customerList.stream()
                .filter(c -> c.getId() != null && c.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            model.addAttribute("customer", existing);
            model.addAttribute("pageTitle", "Cập Nhật Khách Hàng");
            return "CustomerForm";
        }
        return "redirect:/customers";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customer") Customer customer) {
        if (customer.getId() == null || customer.getId().trim().isEmpty()) {
            customer.setId(String.format("KH%02d", customerList.size() + 1));
            customerList.add(customer);
        } else {
            Customer existing = customerList.stream()
                    .filter(c -> c.getId() != null && c.getId().equalsIgnoreCase(customer.getId()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setName(customer.getName());
                existing.setPhone(customer.getPhone());
                existing.setEmail(customer.getEmail());
            } else {
                customerList.add(customer);
            }
        }
        return "redirect:/customers";
    }

    @GetMapping("/delete")
    public String deleteCustomerByParam(@RequestParam(name = "id", required = false) String id) {
        if (id != null && !id.trim().isEmpty()) {
            customerList.removeIf(c -> c.getId() != null && c.getId().equalsIgnoreCase(id.trim()));
        }
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomerByPath(@PathVariable("id") String id) {
        if (id != null && !id.trim().isEmpty()) {
            customerList.removeIf(c -> c.getId() != null && c.getId().equalsIgnoreCase(id.trim()));
        }
        return "redirect:/customers";
    }
}