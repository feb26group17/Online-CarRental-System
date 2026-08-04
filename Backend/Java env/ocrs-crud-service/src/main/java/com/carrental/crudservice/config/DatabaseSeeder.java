package com.carrental.crudservice.config;

import com.carrental.crudservice.entity.Brand;
import com.carrental.crudservice.entity.Model;
import com.carrental.crudservice.entity.Vehicle;
import com.carrental.crudservice.entity.enums.FuelType;
import com.carrental.crudservice.entity.enums.VehicleStatus;
import com.carrental.crudservice.repository.BrandRepository;
import com.carrental.crudservice.repository.ModelRepository;
import com.carrental.crudservice.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final VehicleRepository vehicleRepository;

    public DatabaseSeeder(BrandRepository brandRepository,
                          ModelRepository modelRepository,
                          VehicleRepository vehicleRepository) {
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedBrandsAndModels();
        seedVehicles();
    }

    private void seedBrandsAndModels() {
        if (brandRepository.count() > 0) {
            return;
        }

        // 1. Brands
        Brand toyota = brandRepository.save(Brand.builder().bname("Toyota").build());
        Brand hyundai = brandRepository.save(Brand.builder().bname("Hyundai").build());
        Brand honda = brandRepository.save(Brand.builder().bname("Honda").build());
        Brand tata = brandRepository.save(Brand.builder().bname("Tata Motors").build());
        Brand mahindra = brandRepository.save(Brand.builder().bname("Mahindra").build());
        Brand maruti = brandRepository.save(Brand.builder().bname("Maruti Suzuki").build());
        Brand bmw = brandRepository.save(Brand.builder().bname("BMW").build());
        Brand mercedes = brandRepository.save(Brand.builder().bname("Mercedes-Benz").build());

        // 2. Models
        List<Model> models = Arrays.asList(
                // Toyota
                Model.builder().brandId(toyota.getBrandId()).modelName("Fortuner").seatingCapacity(7).build(),
                Model.builder().brandId(toyota.getBrandId()).modelName("Innova Crysta").seatingCapacity(7).build(),
                Model.builder().brandId(toyota.getBrandId()).modelName("Urban Cruiser").seatingCapacity(5).build(),
                Model.builder().brandId(toyota.getBrandId()).modelName("Camry").seatingCapacity(5).build(),

                // Hyundai
                Model.builder().brandId(hyundai.getBrandId()).modelName("Creta").seatingCapacity(5).build(),
                Model.builder().brandId(hyundai.getBrandId()).modelName("Verna").seatingCapacity(5).build(),
                Model.builder().brandId(hyundai.getBrandId()).modelName("Tucson").seatingCapacity(5).build(),
                Model.builder().brandId(hyundai.getBrandId()).modelName("i20").seatingCapacity(5).build(),

                // Honda
                Model.builder().brandId(honda.getBrandId()).modelName("City").seatingCapacity(5).build(),
                Model.builder().brandId(honda.getBrandId()).modelName("Civic").seatingCapacity(5).build(),
                Model.builder().brandId(honda.getBrandId()).modelName("Amaze").seatingCapacity(5).build(),

                // Tata Motors
                Model.builder().brandId(tata.getBrandId()).modelName("Harrier").seatingCapacity(5).build(),
                Model.builder().brandId(tata.getBrandId()).modelName("Safari").seatingCapacity(7).build(),
                Model.builder().brandId(tata.getBrandId()).modelName("Nexon").seatingCapacity(5).build(),
                Model.builder().brandId(tata.getBrandId()).modelName("Altroz").seatingCapacity(5).build(),

                // Mahindra
                Model.builder().brandId(mahindra.getBrandId()).modelName("Thar").seatingCapacity(4).build(),
                Model.builder().brandId(mahindra.getBrandId()).modelName("XUV700").seatingCapacity(7).build(),
                Model.builder().brandId(mahindra.getBrandId()).modelName("Scorpio-N").seatingCapacity(7).build(),
                Model.builder().brandId(mahindra.getBrandId()).modelName("Bolero").seatingCapacity(7).build(),

                // Maruti Suzuki
                Model.builder().brandId(maruti.getBrandId()).modelName("Swift").seatingCapacity(5).build(),
                Model.builder().brandId(maruti.getBrandId()).modelName("Baleno").seatingCapacity(5).build(),
                Model.builder().brandId(maruti.getBrandId()).modelName("Ertiga").seatingCapacity(7).build(),
                Model.builder().brandId(maruti.getBrandId()).modelName("Brezza").seatingCapacity(5).build(),

                // BMW
                Model.builder().brandId(bmw.getBrandId()).modelName("X5").seatingCapacity(5).build(),
                Model.builder().brandId(bmw.getBrandId()).modelName("3 Series").seatingCapacity(5).build(),

                // Mercedes-Benz
                Model.builder().brandId(mercedes.getBrandId()).modelName("C-Class").seatingCapacity(5).build(),
                Model.builder().brandId(mercedes.getBrandId()).modelName("GLE").seatingCapacity(5).build()
        );

        modelRepository.saveAll(models);
        System.out.println("✅ Seeded Car Brands & Models successfully!");
    }

    private void seedVehicles() {
        if (vehicleRepository.count() > 0) {
            return;
        }

        List<Model> allModels = modelRepository.findAll();
        if (allModels.isEmpty()) {
            return;
        }

        // Find Fortuner, Creta, City, Thar, XUV700, Swift, X5, C-Class
        Model fortuner = findModelByName(allModels, "Fortuner");
        Model creta = findModelByName(allModels, "Creta");
        Model city = findModelByName(allModels, "City");
        Model thar = findModelByName(allModels, "Thar");
        Model xuv700 = findModelByName(allModels, "XUV700");
        Model swift = findModelByName(allModels, "Swift");
        Model x5 = findModelByName(allModels, "X5");
        Model cClass = findModelByName(allModels, "C-Class");

        List<Vehicle> sampleVehicles = Arrays.asList(
                Vehicle.builder()
                        .userId(1)
                        .modelId(fortuner.getModelId())
                        .registrationNumber("MH-01-AX-1001")
                        .fuelType(FuelType.Diesel)
                        .rentPerDay(new BigDecimal("4500.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(creta.getModelId())
                        .registrationNumber("MH-03-CZ-3003")
                        .fuelType(FuelType.Petrol)
                        .rentPerDay(new BigDecimal("2800.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(city.getModelId())
                        .registrationNumber("KA-01-MA-4004")
                        .fuelType(FuelType.Petrol)
                        .rentPerDay(new BigDecimal("2500.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(thar.getModelId())
                        .registrationNumber("MH-14-RS-6006")
                        .fuelType(FuelType.Diesel)
                        .rentPerDay(new BigDecimal("3200.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(xuv700.getModelId())
                        .registrationNumber("KA-05-TU-7007")
                        .fuelType(FuelType.Diesel)
                        .rentPerDay(new BigDecimal("4200.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(swift.getModelId())
                        .registrationNumber("DL-01-AB-8008")
                        .fuelType(FuelType.CNG)
                        .rentPerDay(new BigDecimal("1800.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(x5.getModelId())
                        .registrationNumber("MH-02-VW-9009")
                        .fuelType(FuelType.Petrol)
                        .rentPerDay(new BigDecimal("8500.00"))
                        .status(VehicleStatus.Available)
                        .build(),
                Vehicle.builder()
                        .userId(1)
                        .modelId(cClass.getModelId())
                        .registrationNumber("MH-01-XY-9999")
                        .fuelType(FuelType.Battery)
                        .rentPerDay(new BigDecimal("9500.00"))
                        .status(VehicleStatus.Available)
                        .build()
        );

        vehicleRepository.saveAll(sampleVehicles);
        System.out.println("✅ Seeded sample fleet vehicles successfully!");
    }

    private Model findModelByName(List<Model> models, String name) {
        return models.stream()
                .filter(m -> m.getModelName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(models.get(0));
    }
}
