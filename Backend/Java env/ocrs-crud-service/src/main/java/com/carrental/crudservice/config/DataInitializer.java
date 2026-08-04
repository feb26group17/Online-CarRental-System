package com.carrental.crudservice.config;

import com.carrental.crudservice.entity.Brand;
import com.carrental.crudservice.entity.Model;
import com.carrental.crudservice.repository.BrandRepository;
import com.carrental.crudservice.repository.ModelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;

    public DataInitializer(BrandRepository brandRepository, ModelRepository modelRepository) {
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (brandRepository.count() == 0) {
            System.out.println("Seeding default brands and models into database...");

            Brand toyota = brandRepository.save(Brand.builder().bname("Toyota").build());
            Brand honda = brandRepository.save(Brand.builder().bname("Honda").build());
            Brand hyundai = brandRepository.save(Brand.builder().bname("Hyundai").build());
            Brand tata = brandRepository.save(Brand.builder().bname("Tata").build());
            Brand mahindra = brandRepository.save(Brand.builder().bname("Mahindra").build());
            Brand suzuki = brandRepository.save(Brand.builder().bname("Maruti Suzuki").build());
            Brand bmw = brandRepository.save(Brand.builder().bname("BMW").build());
            Brand audi = brandRepository.save(Brand.builder().bname("Audi").build());

            // Toyota models
            modelRepository.save(Model.builder().brandId(toyota.getBrandId()).modelName("Fortuner").seatingCapacity(7).build());
            modelRepository.save(Model.builder().brandId(toyota.getBrandId()).modelName("Innova Crysta").seatingCapacity(7).build());
            modelRepository.save(Model.builder().brandId(toyota.getBrandId()).modelName("Camry").seatingCapacity(5).build());

            // Honda models
            modelRepository.save(Model.builder().brandId(honda.getBrandId()).modelName("City").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(honda.getBrandId()).modelName("Civic").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(honda.getBrandId()).modelName("Amaze").seatingCapacity(5).build());

            // Hyundai models
            modelRepository.save(Model.builder().brandId(hyundai.getBrandId()).modelName("Creta").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(hyundai.getBrandId()).modelName("Verna").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(hyundai.getBrandId()).modelName("i20").seatingCapacity(5).build());

            // Tata models
            modelRepository.save(Model.builder().brandId(tata.getBrandId()).modelName("Nexon").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(tata.getBrandId()).modelName("Harrier").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(tata.getBrandId()).modelName("Safari").seatingCapacity(7).build());

            // Mahindra models
            modelRepository.save(Model.builder().brandId(mahindra.getBrandId()).modelName("Thar").seatingCapacity(4).build());
            modelRepository.save(Model.builder().brandId(mahindra.getBrandId()).modelName("XUV700").seatingCapacity(7).build());
            modelRepository.save(Model.builder().brandId(mahindra.getBrandId()).modelName("Scorpio-N").seatingCapacity(7).build());

            // Maruti Suzuki models
            modelRepository.save(Model.builder().brandId(suzuki.getBrandId()).modelName("Swift").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(suzuki.getBrandId()).modelName("Baleno").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(suzuki.getBrandId()).modelName("Brezza").seatingCapacity(5).build());

            // BMW models
            modelRepository.save(Model.builder().brandId(bmw.getBrandId()).modelName("X5").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(bmw.getBrandId()).modelName("3 Series").seatingCapacity(5).build());

            // Audi models
            modelRepository.save(Model.builder().brandId(audi.getBrandId()).modelName("Q5").seatingCapacity(5).build());
            modelRepository.save(Model.builder().brandId(audi.getBrandId()).modelName("A6").seatingCapacity(5).build());

            System.out.println("Brands and models seeded successfully!");
        }
    }
}
