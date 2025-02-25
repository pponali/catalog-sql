package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.*;
import com.scaler.repository.*;
import com.scaler.service.CategoryFeatureTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import static com.scaler.constants.FeatureConstants.*;


@Service
public class DemoService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private StoreCatalogRepository storeCatalogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryFeatureTemplateRepository categoryFeatureTemplateRepository;
    @Autowired
    private ProductFeatureRepository productFeatureRepository;
    @Autowired
    private ProductFeatureValueRepository productFeatureValueRepository;


    public void setup() throws JsonProcessingException {
        Merchant tatacliqmerchant = Merchant.builder()
                .code("TCLQ-001")
                .name("Tata CLiQ")
                .description("Tata CLiQ - Tata Group's E-commerce Platform")
                .status("ACTIVE")
                .contactEmail("support@tatacliq.com")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        merchantRepository.save(tatacliqmerchant);

        Catalog cligCatalog = Catalog.builder()
                .code("CLIG-001")
                .name("CLIG")
                .description("CLIG Catalog")
                .status("ACTIVE")
                .type("CLIG")
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        catalogRepository.save(cligCatalog);

        merchantRepository.save(tatacliqmerchant);

        Store tatacliqonliestore = Store.builder()
                .name("CLIG")
                .domain("tataclig.com")
                .merchant(tatacliqmerchant)
                .locale("en")
                .currency("INR")
                .description("CLIG Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        storeRepository.save(tatacliqonliestore);


        StoreCatalog storeCatalog = StoreCatalog.builder()
                .store(tatacliqonliestore)
                .catalog(cligCatalog)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        storeCatalogRepository.save(storeCatalog);


        Category category = Category.builder()
                .code("CLIG-001")
                .name("CLIG")
                .merchant(tatacliqmerchant)
                .description("Lipstick")
                .createdBy("system")
                .lastModifiedBy("system")
                .catalog(cligCatalog)
                .build();
        categoryRepository.save(category);


        Product cliqProduct = Product.builder()
                .code("CLIG-001")
                .name("CLIG")
                .description("Lipstick")
                .status("ACTIVE")
                .productType(ProductType.SIMPLE)
                .createdBy("system")
                .lastModifiedBy("system")
                .catalog(cligCatalog)
                .merchant(tatacliqmerchant)
                .build();
        productRepository.save(cliqProduct);

        CategoryFeatureTemplate laptopProcessor = FeatureTemplateBuilder.createLaptopProcessorTemplate(category);
        CategoryFeatureTemplate laptopRam = FeatureTemplateBuilder.createLaptopRamTemplate(category);
        CategoryFeatureTemplate laptopStorage = FeatureTemplateBuilder.createLaptopStorageTemplate(category);


        categoryFeatureTemplateRepository.save(laptopProcessor);
        categoryFeatureTemplateRepository.save(laptopRam);
        categoryFeatureTemplateRepository.save(laptopStorage);


        ProductFeature processor = ProductFeature.builder()
                .code(LAPTOP_PROCESSOR)
                .name("Processor")
                .description("Laptop Processor Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern("")
                .minValue("")
                .maxValue("")
                .allowedValues(PROCESSOR_VALUES)
                .defaultValue("")
                .featureType(ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(objectMapper.readTree("{"
                        + "\"displayOrder\": 1,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Processor model that powers the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        ProductFeature ram = ProductFeature.builder()
                .code(LAPTOP_RAM)
                .name("RAM")
                .description("Laptop Memory (RAM) Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(RAM_PATTERN)
                .minValue("4")
                .maxValue("128")
                .allowedValues(RAM_VALUES)
                .defaultValue(DEFAULT_RAM)
                .featureType(ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(objectMapper.readTree("{"
                        + "\"displayOrder\": 2,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Amount of RAM installed in the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        ProductFeature storage = ProductFeature.builder()
                .code(LAPTOP_STORAGE)
                .name("Storage")
                .description("Laptop Storage Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(STORAGE_PATTERN)
                .minValue("128")
                .maxValue("4096")
                .allowedValues(STORAGE_VALUES)
                .defaultValue(DEFAULT_STORAGE)
                .featureType(ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(objectMapper.readTree("{"
                        + "\"displayOrder\": 3,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Storage capacity of the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        productFeatureRepository.save(processor);
        productFeatureRepository.save(ram);
        productFeatureRepository.save(storage);



        // Processor Feature Value
        ProductFeatureValue processorValue = ProductFeatureValue.builder()
                .product(cliqProduct)  // Reference to the MacBook Pro product
                .feature(processor)  // Reference to the processor feature
                .templateId(laptopProcessor.getId())  // Reference to the template ID
                .type("SPECIFICATION")
                .unit("")
                .unitOfMeasure("")
                .attributeValues(objectMapper.readTree("{" +
                        "\"value\": \"Apple M2 Pro\"," +
                        "\"displayValue\": \"Apple M2 Pro\"," +
                        "\"additionalInfo\": {" +
                        "\"cores\": \"12-core\"," +
                        "\"architecture\": \"ARM\"," +
                        "\"generation\": \"2nd Gen\"" +
                        "}" +
                        "}"))
                .metadata(objectMapper.readTree("{" +
                        "\"displayOrder\": 1," +
                        "\"group\": \"Technical Specifications\"," +
                        "\"importance\": \"HIGH\"" +
                        "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

// RAM Feature Value
        ProductFeatureValue ramValue = ProductFeatureValue.builder()
                .product(cliqProduct)  // Reference to the MacBook Pro product
                .feature(ram)  // Reference to the RAM feature
                .templateId(laptopRam.getId())  // Reference to the template ID
                .type("SPECIFICATION")
                .unit("GB")
                .unitOfMeasure("GB")
                .attributeValues(objectMapper.readTree("{" +
                        "\"value\": \"16\"," +
                        "\"displayValue\": \"16 GB\"," +
                        "\"additionalInfo\": {" +
                        "\"type\": \"Unified Memory\"," +
                        "\"speed\": \"6400MHz\"," +
                        "\"technology\": \"LPDDR5\"" +
                        "}" +
                        "}"))
                .metadata(objectMapper.readTree("{" +
                        "\"displayOrder\": 2," +
                        "\"group\": \"Technical Specifications\"," +
                        "\"importance\": \"HIGH\"" +
                        "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

// Storage Feature Value
        ProductFeatureValue storageValue = ProductFeatureValue.builder()
                .product(cliqProduct)  // Reference to the MacBook Pro product
                .feature(storage)  // Reference to the storage feature
                .templateId(laptopStorage.getId())  // Reference to the template ID
                .type("SPECIFICATION")
                .unit("GB")
                .unitOfMeasure("GB")
                .attributeValues(objectMapper.readTree("{" +
                        "\"value\": \"512\"," +
                        "\"displayValue\": \"512 GB\"," +
                        "\"additionalInfo\": {" +
                        "\"type\": \"SSD\"," +
                        "\"technology\": \"NVMe\"," +
                        "\"readSpeed\": \"3500 MB/s\"," +
                        "\"writeSpeed\": \"3000 MB/s\"" +
                        "}" +
                        "}"))
                .metadata(objectMapper.readTree("{" +
                        "\"displayOrder\": 3," +
                        "\"group\": \"Technical Specifications\"," +
                        "\"importance\": \"HIGH\"" +
                        "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        productFeatureValueRepository.save(processorValue);
        productFeatureValueRepository.save(ramValue);
        productFeatureValueRepository.save(storageValue);





    }
}
