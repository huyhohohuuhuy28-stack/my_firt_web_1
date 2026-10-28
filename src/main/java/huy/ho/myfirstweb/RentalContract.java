package huy.ho.myfirstweb;

import java.time.LocalDate;

public class RentalContract {
    private String contractId;
    private String customerName;
    private String licensePlate;
    private LocalDate rentalDate;
    private LocalDate expectedReturnDate;
    private String status; // "Đang thuê" hoặc "Đã trả"
    private double totalPrice;

    public RentalContract() {}

    public RentalContract(String contractId, String customerName, String licensePlate, 
                          LocalDate rentalDate, LocalDate expectedReturnDate, 
                          String status, double totalPrice) {
        this.contractId = contractId;
        this.customerName = customerName;
        this.licensePlate = licensePlate;
        this.rentalDate = rentalDate;
        this.expectedReturnDate = expectedReturnDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public LocalDate getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }

    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
