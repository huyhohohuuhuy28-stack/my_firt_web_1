package huy.ho.myfirstweb;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VehicleDataService {
    private final Path file = Path.of("vehicles.txt");

    public void load(VehicleManager manager) {
        if (!Files.exists(file)) return;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",", -1);
                if (data.length < 7) continue;

                Vehicle vehicle;
                if ("CAR".equals(data[0])) {
                    vehicle = new Car(data[1], data[2], Integer.parseInt(data[3]),
                            Double.parseDouble(data[4]), Integer.parseInt(data[5]),
                            data.length > 7 && !data[7].isBlank() ? data[7] : "Tốt");
                } else if ("TRUCK".equals(data[0])) {
                    vehicle = new Truck(data[1], data[2], Integer.parseInt(data[3]),
                            Double.parseDouble(data[4]), Double.parseDouble(data[5]),
                            data.length > 7 && !data[7].isBlank() ? data[7] : "Tốt");
                } else {
                    continue;
                }

                vehicle.setAvailable(Boolean.parseBoolean(data[6]));
                manager.addVehicle(vehicle);
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Không thể đọc vehicles.txt", e);
        }
    }

    public void save(VehicleManager manager) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Vehicle vehicle : manager.getVehicles().values()) {
                writer.write(vehicle.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Không thể lưu vehicles.txt", e);
        }
    }
}