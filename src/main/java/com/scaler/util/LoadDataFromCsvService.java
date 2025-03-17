package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.builder.*;
import com.scaler.entity.*;
import com.scaler.entity.enums.ChannelType;
import com.scaler.entity.enums.PlatformType;
import com.scaler.repository.*;
import com.scaler.service.ProductMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class LoadDataFromCsvService {

    // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final CatalogRepository catalogRepository;
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final ChannelRepository channelRepository;
    private final PlatformRepository platformRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryFeatureTemplateRepository categoryFeatureTemplateRepository;
    @SuppressWarnings("unused")
    private final ProductMappingService productMappingService;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductFeatureMappingRepository productFeatureMappingRepository;
    private final ProductFeatureValueRepository productFeatureValueRepository;
    private final ProductPlatformRepository productPlatformRepository;
    private final ProductFeatureValueMappingRepository productFeatureValueMappingRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final CategoryMappingRepository categoryMappingRepository;
    private final ResilientCsvDataLoader resilientCsvDataLoader;

    public LoadDataFromCsvService(MerchantRepository merchantRepository, CategoryRepository categoryRepository,
                                  CatalogRepository catalogRepository, SellerRepository sellerRepository,
                                  StoreRepository storeRepository, ChannelRepository channelRepository,
                                  PlatformRepository platformRepository, UnitOfMeasureRepository unitOfMeasureRepository,
                                  CategoryFeatureTemplateRepository categoryFeatureTemplateRepository,
                                   ProductMappingService productMappingService, ProductRepository productRepository,
                                  ProductCategoryRepository productCategoryRepository,
                                  ProductFeatureRepository productFeatureRepository,
                                  ProductFeatureMappingRepository productFeatureMappingRepository,
                                  ProductFeatureValueRepository productFeatureValueRepository,
                                  ProductPlatformRepository productPlatformRepository,
                                  SellerProductRepository sellerProductRepository,
                                  ProductFeatureValueMappingRepository productFeatureValueMappingRepository,
                                  ProductPriceRepository productPriceRepository,
                                  ProductInventoryRepository productInventoryRepository,
                                  CategoryMappingRepository categoryMappingRepository,
                                  ResilientCsvDataLoader resilientCsvDataLoader) {
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.catalogRepository = catalogRepository;
        this.sellerRepository = sellerRepository;
        this.storeRepository = storeRepository;
        this.channelRepository = channelRepository;
        this.platformRepository = platformRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryFeatureTemplateRepository = categoryFeatureTemplateRepository;
        this.productMappingService = productMappingService;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productFeatureRepository = productFeatureRepository;
        this.productFeatureMappingRepository = productFeatureMappingRepository;
        this.productFeatureValueRepository = productFeatureValueRepository;
        this.productPlatformRepository = productPlatformRepository;
        this.productFeatureValueMappingRepository = productFeatureValueMappingRepository;
        this.productPriceRepository = productPriceRepository;
        this.productInventoryRepository = productInventoryRepository;
        this.categoryMappingRepository = categoryMappingRepository;
        this.resilientCsvDataLoader = resilientCsvDataLoader;
    }

    /**
     * Populates data from CSV files in the resources/csv directory
     * This method implements a robust CSV-based product data loading system
     * Data is only loaded the first time this method is called
     */
    @Transactional
    public void populateDataFromCsvFiles() {
        log.info("Starting data population from CSV files...");

        try {
            // Check if data already exists in the database
            // Using product feature mappings as a reliable indicator that data has been loaded
            if (merchantRepository.count() > 0) {
                log.info("Data already exists in the database, skipping CSV data population");
                return;
            }

            // Step 1: Load merchants
            Map<String, Merchant> merchantMap = loadMerchants();

            // Step 2: Load catalogs
            Map<String, Catalog> catalogMap = loadCatalogs(merchantMap);

            // Step 3: Load categories
            Map<String, Category> categoryMap = loadCategories(catalogMap, merchantMap);

            // Step 4: Load unit of measures
            Map<String, UnitOfMeasure> uomMap = loadUnitOfMeasures();

            // Step 5: Load category feature templates
            Map<String, CategoryFeatureTemplate> templateMap = loadCategoryFeatureTemplates(categoryMap, uomMap);

            // Step 6: Load products
            Map<String, Product> productMap = loadProducts(categoryMap, merchantMap, catalogMap);

            // Step 7: Load product features
            Map<String, ProductFeature> featureMap = loadProductFeatures(templateMap, uomMap);

            // Step 8: Create product-feature mappings
            createProductFeatureMappings(productMap, featureMap, categoryMap);

            // Step 9: Load product feature values
            loadProductFeatureValues(productMap, featureMap, categoryMap, uomMap);

            // Step 10: Create sellers and store mappings
            Map<String, Seller> sellerMap = loadSellers(merchantMap);
            Map<String, Store> storeMap = loadStores(merchantMap);

            // Step 11: Create channels and platforms
            Map<String, Channel> channelMap = loadChannels(merchantMap, storeMap);
            Map<String, Platform> platformMap = loadPlatforms(channelMap);

            // Step 12: Create product-channel and product-platform mappings
            createProductChannelMappings(productMap, channelMap);
            createProductPlatformMappings(productMap, platformMap);

            // Step 13: Create prices and inventory
            createPricesAndInventory(productMap, merchantMap, channelMap, sellerMap);

            log.info("Successfully populated data from CSV files");
        } catch (Exception e) {
            log.error("Error populating data from CSV files", e);
            throw new RuntimeException("Failed to populate data from CSV files: " + e.getMessage(), e);
        }
    }

    /**
     * Loads merchants from CSV file
     * @return Map of merchant code to Merchant entity
     */
    public Map<String, Merchant> loadMerchants() {
        log.info("Loading merchants from CSV...");
        Map<String, Merchant> merchantMap = new HashMap<>();

        try {
            // First check if we already have merchants in the database
            if (merchantRepository.count() > 0) {
                log.info("Merchants already exist in database, using existing data");
                // Using method reference instead of lambda for better readability
                merchantRepository.findAll().forEach(merchant -> merchantMap.put(merchant.getCode(), merchant));
                return merchantMap;
            }

            // Load merchants from CSV file
            List<Map<String, String>> merchantData = resilientCsvDataLoader.loadCsvData("csv/merchants.csv");
            if (merchantData.isEmpty()) {
                log.info("No merchant data found in CSV, creating default merchants");
                // Create standard merchants if CSV data is not available
                createDefaultMerchants(merchantMap);
                return merchantMap;
            }

            log.info("Found {} merchants in CSV", merchantData.size());
            for (Map<String, String> data : merchantData) {
                String code = data.get("code");
                if (code == null || code.isEmpty()) {
                    log.warn("Skipping merchant with missing code");
                    continue;
                }

                // Check if merchant already exists
                Optional<Merchant> existingMerchant = merchantRepository.findByCode(code);
                if (existingMerchant.isPresent()) {
                    log.debug("Merchant with code {} already exists, skipping creation", code);
                    merchantMap.put(code, existingMerchant.get());
                    continue;
                }

                // Create new merchant
                Merchant merchant = createMerchantFromCsvData(data);
                merchant = merchantRepository.save(merchant);
                merchantMap.put(merchant.getCode(), merchant);
                log.info("Created merchant: {}", merchant.getName());
            }

            // Check and create Tata 1mg merchant
            String tata1mgCode = "T1MG-001";
            Optional<Merchant> tata1mgOpt = merchantRepository.findByCode(tata1mgCode);
            Merchant tata1mg;
            if (tata1mgOpt.isPresent()) {
                tata1mg = tata1mgOpt.get();
                log.info("Using existing Tata 1mg merchant");
            } else {
                tata1mg = merchantRepository.save(MerchantBuilder.createTata1mgMerchant());
                log.info("Created Tata 1mg merchant");
            }
            merchantMap.put(tata1mg.getCode(), tata1mg);

            // Check and create BigBasket merchant
            String bigBasketCode = "BBKT-001";
            Optional<Merchant> bigBasketOpt = merchantRepository.findByCode(bigBasketCode);
            Merchant bigBasket;
            if (bigBasketOpt.isPresent()) {
                bigBasket = bigBasketOpt.get();
                log.info("Using existing BigBasket merchant");
            } else {
                bigBasket = merchantRepository.save(MerchantBuilder.createBigBasketMerchant());
                log.info("Created BigBasket merchant");
            }
            merchantMap.put(bigBasket.getCode(), bigBasket);

            // Check and create Croma merchant
            String cromaCode = "CRMA-001";
            Optional<Merchant> cromaOpt = merchantRepository.findByCode(cromaCode);
            Merchant croma;
            if (cromaOpt.isPresent()) {
                croma = cromaOpt.get();
                log.info("Using existing Croma merchant");
            } else {
                croma = merchantRepository.save(MerchantBuilder.createCromaMerchant());
                log.info("Created Croma merchant");
            }
            merchantMap.put(croma.getCode(), croma);

            // Check and create Tanishq merchant
            String tanishqCode = "TNSH-001";
            Optional<Merchant> tanishqOpt = merchantRepository.findByCode(tanishqCode);
            Merchant tanishq;
            if (tanishqOpt.isPresent()) {
                tanishq = tanishqOpt.get();
                log.info("Using existing Tanishq merchant");
            } else {
                tanishq = merchantRepository.save(MerchantBuilder.createTanishqMerchant());
                log.info("Created Tanishq merchant");
            }
            merchantMap.put(tanishq.getCode(), tanishq);

            log.info("Successfully loaded {} merchants", merchantMap.size());
        } catch (Exception e) {
            log.error("Error loading merchants", e);
            throw new RuntimeException("Failed to load merchants: " + e.getMessage(), e);
        }

        return merchantMap;
    }

    /**
     * Creates a merchant from CSV data
     * @param data Map of CSV column name to value
     * @return Merchant entity
     */
    private Merchant createMerchantFromCsvData(Map<String, String> data) {
        String code = data.get("code");
        String name = data.get("name");
        String description = data.get("description");

        Merchant merchant = new Merchant();
        merchant.setCode(code);
        merchant.setName(name);
        merchant.setDescription(description);
        merchant.setStatus("ACTIVE");
        merchant.setCreatedBy("SYSTEM");
        merchant.setLastModifiedBy("SYSTEM");

        // Create metadata as JSON - commented out as Merchant entity doesn't have metadata field yet
        /*
        try {
            ObjectNode metadataNode = objectMapper.createObjectNode();

            // Add company information
            metadataNode.put("founded", data.getOrDefault("founded", ""));
            metadataNode.put("headquarters", data.getOrDefault("headquarters", ""));
            metadataNode.put("type", data.getOrDefault("type", ""));

            // Add contact information
            ObjectNode contactNode = metadataNode.putObject("contact");
            contactNode.put("email", data.getOrDefault("email", ""));
            contactNode.put("phone", data.getOrDefault("phone", ""));

            // Add address information
            ObjectNode addressNode = contactNode.putObject("address");
            addressNode.put("line1", data.getOrDefault("address_line1", ""));
            addressNode.put("line2", data.getOrDefault("address_line2", ""));
            addressNode.put("city", data.getOrDefault("city", ""));
            addressNode.put("state", data.getOrDefault("state", ""));
            addressNode.put("country", data.getOrDefault("country", ""));
            addressNode.put("pincode", data.getOrDefault("pincode", ""));

            // merchant.setMetadata(metadataNode);
        } catch (Exception e) {
            log.error("Error creating metadata for merchant: {}", code, e);
        }
        */

        // Set contact email from CSV data
        merchant.setContactEmail(data.getOrDefault("email", ""));

        return merchant;
    }

    /**
     * Creates default merchants when CSV data is not available
     * @param merchantMap Map to store created merchants
     */
    private void createDefaultMerchants(Map<String, Merchant> merchantMap) {
        // Create Tata CLiQ merchant
        String tataCliqCode = "TCLQ-001";
        Optional<Merchant> tataCliqOpt = merchantRepository.findByCode(tataCliqCode);
        Merchant tataCliq;
        if (tataCliqOpt.isPresent()) {
            tataCliq = tataCliqOpt.get();
            log.info("Using existing Tata CLiQ merchant");
        } else {
            tataCliq = merchantRepository.save(MerchantBuilder.createTataCliqMerchant());
            log.info("Created Tata CLiQ merchant");
        }
        merchantMap.put(tataCliq.getCode(), tataCliq);

        // Create Tata 1mg merchant
        String tata1mgCode = "T1MG-001";
        Optional<Merchant> tata1mgOpt = merchantRepository.findByCode(tata1mgCode);
        Merchant tata1mg;
        if (tata1mgOpt.isPresent()) {
            tata1mg = tata1mgOpt.get();
            log.info("Using existing Tata 1mg merchant");
        } else {
            tata1mg = merchantRepository.save(MerchantBuilder.createTata1mgMerchant());
            log.info("Created Tata 1mg merchant");
        }
        merchantMap.put(tata1mg.getCode(), tata1mg);

        // Create BigBasket merchant
        String bigBasketCode = "BBKT-001";
        Optional<Merchant> bigBasketOpt = merchantRepository.findByCode(bigBasketCode);
        Merchant bigBasket;
        if (bigBasketOpt.isPresent()) {
            bigBasket = bigBasketOpt.get();
            log.info("Using existing BigBasket merchant");
        } else {
            bigBasket = merchantRepository.save(MerchantBuilder.createBigBasketMerchant());
            log.info("Created BigBasket merchant");
        }
        merchantMap.put(bigBasket.getCode(), bigBasket);

        // Create Croma merchant
        String cromaCode = "CRMA-001";
        Optional<Merchant> cromaOpt = merchantRepository.findByCode(cromaCode);
        Merchant croma;
        if (cromaOpt.isPresent()) {
            croma = cromaOpt.get();
            log.info("Using existing Croma merchant");
        } else {
            croma = merchantRepository.save(MerchantBuilder.createCromaMerchant());
            log.info("Created Croma merchant");
        }
        merchantMap.put(croma.getCode(), croma);

        // Create Tanishq merchant
        String tanishqCode = "TNSH-001";
        Optional<Merchant> tanishqOpt = merchantRepository.findByCode(tanishqCode);
        Merchant tanishq;
        if (tanishqOpt.isPresent()) {
            tanishq = tanishqOpt.get();
            log.info("Using existing Tanishq merchant");
        } else {
            tanishq = merchantRepository.save(MerchantBuilder.createTanishqMerchant());
            log.info("Created Tanishq merchant");
        }
        merchantMap.put(tanishq.getCode(), tanishq);
    }

    /**
     * Loads catalogs from CSV file
     * @param merchantMap Map of merchant code to Merchant entity
     * @return Map of catalog code to Catalog entity
     */
    private Map<String, Catalog> loadCatalogs(Map<String, Merchant> merchantMap) {
        log.info("Loading catalogs from CSV...");
        Map<String, Catalog> catalogMap = new HashMap<>();

        try {
            // First check if we already have catalogs in the database
            if (catalogRepository.count() > 0) {
                log.info("Catalogs already exist in database, using existing data");
                // Using method reference instead of lambda for better readability
                catalogRepository.findAll().forEach(catalog -> catalogMap.put(catalog.getCode(), catalog));
                return catalogMap;
            }

            // Create standard catalogs if not found in CSV
            Merchant tataCliq = merchantMap.get("TCLQ-001");
            Merchant tata1mg = merchantMap.get("1MG-001");
            Merchant bigBasket = merchantMap.get("BBKT-001");
            Merchant croma = merchantMap.get("CROM-001");
            Merchant tanishq = merchantMap.get("TNSH-001");

            Catalog fashionCatalog = catalogRepository.save(CatalogBuilder.createFashionCatalog(tataCliq));
            Catalog groceryCatalog = catalogRepository.save(CatalogBuilder.createGroceryCatalog(bigBasket));
            Catalog pharmaCatalog = catalogRepository.save(CatalogBuilder.createPharmaCatalog(tata1mg));
            Catalog electronicsCatalog = catalogRepository.save(CatalogBuilder.createElectronicsCatalog(croma));
            Catalog jewelryCatalog = catalogRepository.save(CatalogBuilder.createJewelryCatalog(tanishq));

            catalogMap.put(fashionCatalog.getCode(), fashionCatalog);
            catalogMap.put(groceryCatalog.getCode(), groceryCatalog);
            catalogMap.put(pharmaCatalog.getCode(), pharmaCatalog);
            catalogMap.put(electronicsCatalog.getCode(), electronicsCatalog);
            catalogMap.put(jewelryCatalog.getCode(), jewelryCatalog);

            log.info("Successfully loaded {} catalogs", catalogMap.size());
        } catch (Exception e) {
            log.error("Error loading catalogs", e);
            throw new RuntimeException("Failed to load catalogs: " + e.getMessage(), e);
        }

        return catalogMap;
    }

    /**
     * Loads categories from CSV file
     * @param catalogMap Map of catalog code to Catalog entity
     * @param merchantMap Map of merchant code to Merchant entity
     * @return Map of category code to Category entity
     */
    private Map<String, Category> loadCategories(Map<String, Catalog> catalogMap, Map<String, Merchant> merchantMap) {
        log.info("Loading categories from CSV...");
        Map<String, Category> categoryMap = new HashMap<>();
        Map<String, Category> parentCategoryMap = new HashMap<>();

        try {
            // First check if we already have categories in the database
            if (categoryRepository.count() > 0) {
                log.info("Categories already exist in database, using existing data");
                categoryRepository.findAll().forEach(category ->
                        categoryMap.put(category.getCode(), category));
                return categoryMap;
            }

            // Load categories from CSV file
            Resource resource = new ClassPathResource("csv/categories.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 9) {
                            log.warn("Skipping invalid category line: {}", line);
                            continue;
                        }

                        // Parse CSV values (note: categoryId, level, and path are read but not used directly)
                        // They are kept for documentation and potential future use
                        String code = values[1];
                        String name = values[2];
                        String description = values[3];
                        String parentId = values[4];
                        boolean active = Boolean.parseBoolean(values[7]);
                        String metadata = values[8];

                        // Find the catalog for this category (default to fashion catalog)
                        Catalog catalog = catalogMap.values().iterator().next();
                        Merchant merchant = merchantMap.values().iterator().next();

                        // Create category
                        Category category = new Category();
                        category.setCode(code);
                        category.setName(name);
                        category.setDescription(description);
                        category.setCatalog(catalog);
                        category.setMerchant(merchant); // Using setMerchant instead of setBusiness
                        category.setStatus(active ? "ACTIVE" : "INACTIVE");
                        category.setMetadata(metadata);
                        category.setCreatedBy("CSV_IMPORT");
                        category.setCreatedDate(LocalDateTime.now());

                        // Save parent ID for later processing
                        if (StringUtils.hasText(parentId)) {
                            parentCategoryMap.put(code, category);
                        }

                        // Save category
                        category = categoryRepository.save(category);
                        categoryMap.put(code, category);
                    }
                }

                // Process parent-child relationships
                for (Map.Entry<String, Category> entry : parentCategoryMap.entrySet()) {
                    String childCode = entry.getKey();
                    Category child = categoryMap.get(childCode);

                    // Find parent in the CSV file
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                        // Skip header
                        String line = reader.readLine();

                        // Process each line to find the parent
                        while ((line = reader.readLine()) != null) {
                            String[] values = parseCSVLine(line);

                            if (values.length < 9) {
                                continue;
                            }

                            String categoryId = values[0];
                            String code = values[1];
                            String parentId = values[4];

                            if (code.equals(childCode) && StringUtils.hasText(parentId)) {
                                Category parent = categoryMap.get(parentId);
                                if (parent != null) {
                                    categoryMappingRepository.save(new CategoryMapping(parent, child));
                                    break;
                                }
                            }
                        }
                    }
                }

                log.info("Successfully loaded {} categories from CSV", categoryMap.size());
            } else {
                log.info("Categories CSV file not found, creating sample categories");

                // Create sample categories if CSV file not found
                Catalog fashionCatalog = catalogMap.values().stream()
                        .filter(c -> c.getCode().equals("FASH-001"))
                        .findFirst().orElse(catalogMap.values().iterator().next());

                Catalog electronicsCatalog = catalogMap.values().stream()
                        .filter(c -> c.getCode().equals("ELEC-001"))
                        .findFirst().orElse(catalogMap.values().iterator().next());

                Catalog jewelryCatalog = catalogMap.values().stream()
                        .filter(c -> c.getCode().equals("JEWL-001"))
                        .findFirst().orElse(catalogMap.values().iterator().next());

                Merchant tataCliq = merchantMap.values().stream()
                        .filter(m -> m.getCode().equals("TCLQ-001"))
                        .findFirst().orElse(merchantMap.values().iterator().next());

                Merchant croma = merchantMap.values().stream()
                        .filter(m -> m.getCode().equals("CROM-001"))
                        .findFirst().orElse(merchantMap.values().iterator().next());

                Merchant tanishq = merchantMap.values().stream()
                        .filter(m -> m.getCode().equals("TNSH-001"))
                        .findFirst().orElse(merchantMap.values().iterator().next());

                // Create categories
                Category laptopCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createLaptopCategory(electronicsCatalog, croma));
                Category smartphoneCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createSmartphoneCategory(electronicsCatalog, croma));
                Category necklaceCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createGoldNecklaceCategory(jewelryCatalog, tanishq));
                Category bangleCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createGoldBangleCategory(jewelryCatalog, tanishq));
                Category mensCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createMensCategory(fashionCatalog, tataCliq));
                Category womensCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createWomensCategory(fashionCatalog, tataCliq));

                categoryMap.put(laptopCategory.getCode(), laptopCategory);
                categoryMap.put(smartphoneCategory.getCode(), smartphoneCategory);
                categoryMap.put(necklaceCategory.getCode(), necklaceCategory);
                categoryMap.put(bangleCategory.getCode(), bangleCategory);
                categoryMap.put(mensCategory.getCode(), mensCategory);
                categoryMap.put(womensCategory.getCode(), womensCategory);

                log.info("Successfully created {} sample categories", categoryMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading categories", e);
            throw new RuntimeException("Failed to load categories: " + e.getMessage(), e);
        }

        return categoryMap;
    }

    /**
     * Loads unit of measures from CSV file
     * @return Map of unit of measure code to UnitOfMeasure entity
     */
    private Map<String, UnitOfMeasure> loadUnitOfMeasures() {
        log.info("Loading unit of measures from CSV...");
        Map<String, UnitOfMeasure> uomMap = new HashMap<>();

        try {
            // First check if we already have UOMs in the database
            if (unitOfMeasureRepository.count() > 0) {
                log.info("Unit of measures already exist in database, using existing data");
                unitOfMeasureRepository.findAll().forEach(uom ->
                        uomMap.put(uom.getCode(), uom));
                return uomMap;
            }

            // Load UOMs from CSV file
            Resource resource = new ClassPathResource("csv/unit_of_measure.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 5) {
                            log.warn("Skipping invalid UOM line: {}", line);
                            continue;
                        }

                        String code = values[0];
                        String name = values[1];
                        String symbol = values[2];
                        String description = values[3];
                        String type = values[4];

                        // Create UOM
                        UnitOfMeasure uom = UnitOfMeasure.builder()
                                .code(code)
                                .name(name)
                                .displaySymbol(symbol)
                                .description(description)
                                .type(type)
                                .createdBy("CSV_IMPORT")
                                .build();

                        // Save UOM
                        uom = unitOfMeasureRepository.save(uom);
                        uomMap.put(code, uom);
                    }
                }

                log.info("Successfully loaded {} unit of measures from CSV", uomMap.size());
            } else {
                log.info("Unit of measures CSV file not found, creating sample UOMs");

                // Create sample UOMs if CSV file not found
                UnitOfMeasure gbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createGBUnit());
                UnitOfMeasure tbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createTBUnit());
                @SuppressWarnings("unused")
                UnitOfMeasure kgUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createKGUnit());
                @SuppressWarnings("unused")
                UnitOfMeasure pcsUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createPiecesUnit());
                UnitOfMeasure gramUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createGramUnit());

                uomMap.put(gbUnit.getCode(), gbUnit);
                uomMap.put(tbUnit.getCode(), tbUnit);
                uomMap.put(kgUnit.getCode(), kgUnit);
                uomMap.put(pcsUnit.getCode(), pcsUnit);
                uomMap.put(gramUnit.getCode(), gramUnit);

                log.info("Successfully created {} sample unit of measures", uomMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading unit of measures", e);
            throw new RuntimeException("Failed to load unit of measures: " + e.getMessage(), e);
        }

        return uomMap;
    }

    /**
     * Loads category feature templates from CSV file
     * @param categoryMap Map of category code to Category entity
     * @param uomMap Map of unit of measure code to UnitOfMeasure entity
     * @return Map of template code to CategoryFeatureTemplate entity
     */
    private Map<String, CategoryFeatureTemplate> loadCategoryFeatureTemplates(
            Map<String, Category> categoryMap, Map<String, UnitOfMeasure> uomMap) {

        log.info("Loading category feature templates from CSV...");
        Map<String, CategoryFeatureTemplate> templateMap = new HashMap<>();

        try {
            // First check if we already have templates in the database
            if (categoryFeatureTemplateRepository.count() > 0) {
                log.info("Category feature templates already exist in database, using existing data");
                categoryFeatureTemplateRepository.findAll().forEach(template ->
                        templateMap.put(template.getCode(), template));
                return templateMap;
            }

            // Load templates from CSV file
            Resource resource = new ClassPathResource("csv/category_feature_templates.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 9) {
                            log.warn("Skipping invalid template line: {}", line);
                            continue;
                        }

                        String templateId = values[0];
                        String code = values[1];
                        String name = values[2];
                        String categoryCode = values[3];
                        boolean mandatory = Boolean.parseBoolean(values[4]);
                        String attributeType = values[5];
                        String minValue = values[6];
                        String maxValue = values[7];
                        String allowedValues = values[8];
                        String uomCode = values[9];

                        // Get category and UOM
                        Category category = categoryMap.get(categoryCode);
                        UnitOfMeasure uom = uomMap.get(uomCode);

                        if (category == null) {
                            log.warn("Skipping template with unknown category: {}", categoryCode);
                            continue;
                        }

                        // Create template
                        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
                        template.setCode(code);
                        template.setName(name);
                        template.setCategory(category);
                        template.setMandatory(mandatory);
                        template.setAttributeType(attributeType);
                        template.setMinValue(minValue);
                        template.setMaxValue(maxValue);
                        template.setAllowedValues(allowedValues);
                        template.setUnit(uom);
                        template.setCreatedBy("CSV_IMPORT");
                        template.setCreatedDate(LocalDateTime.now());

                        // Save template
                        template = categoryFeatureTemplateRepository.save(template);
                        templateMap.put(code, template);
                    }
                }

                log.info("Successfully loaded {} category feature templates from CSV", templateMap.size());
            } else {
                log.info("Category feature templates CSV file not found, creating sample templates");

                // Create sample templates if CSV file not found
                Category laptopCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("LAPTOP"))
                        .findFirst().orElse(null);

                Category smartphoneCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("SMARTPHONE"))
                        .findFirst().orElse(null);

                Category necklaceCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("GOLD_NECKLACE"))
                        .findFirst().orElse(null);

                Category bangleCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("GOLD_BANGLE"))
                        .findFirst().orElse(null);

                UnitOfMeasure gbUnit = uomMap.values().stream()
                        .filter(u -> u.getCode().equals("GB"))
                        .findFirst().orElse(null);

                UnitOfMeasure tbUnit = uomMap.values().stream()
                        .filter(u -> u.getCode().equals("TB"))
                        .findFirst().orElse(null);

                UnitOfMeasure gramUnit = uomMap.values().stream()
                        .filter(u -> u.getCode().equals("GRAM"))
                        .findFirst().orElse(null);

                // Create templates for laptops if category exists
                if (laptopCategory != null) {
                    CategoryFeatureTemplate processor = FeatureTemplateBuilder.createLaptopProcessorTemplate(laptopCategory);
                    CategoryFeatureTemplate ram = FeatureTemplateBuilder.createLaptopRamTemplate(laptopCategory, gbUnit);
                    CategoryFeatureTemplate storage = FeatureTemplateBuilder.createLaptopStorageTemplate(laptopCategory, tbUnit);

                    categoryFeatureTemplateRepository.save(processor);
                    categoryFeatureTemplateRepository.save(ram);
                    categoryFeatureTemplateRepository.save(storage);

                    templateMap.put(processor.getCode(), processor);
                    templateMap.put(ram.getCode(), ram);
                    templateMap.put(storage.getCode(), storage);
                }

                // Create templates for smartphones if category exists
                if (smartphoneCategory != null) {
                    CategoryFeatureTemplate processor = FeatureTemplateBuilder.createIPhoneProcessorTemplate(smartphoneCategory);
                    CategoryFeatureTemplate ram = FeatureTemplateBuilder.createIPhoneRamTemplate(smartphoneCategory, gbUnit);
                    CategoryFeatureTemplate storage = FeatureTemplateBuilder.createIPhoneStorageTemplate(smartphoneCategory, tbUnit);

                    categoryFeatureTemplateRepository.save(processor);
                    categoryFeatureTemplateRepository.save(ram);
                    categoryFeatureTemplateRepository.save(storage);

                    templateMap.put(processor.getCode(), processor);
                    templateMap.put(ram.getCode(), ram);
                    templateMap.put(storage.getCode(), storage);
                }

                // Create templates for jewelry if categories exist
                if (necklaceCategory != null) {
                    CategoryFeatureTemplate goldPurity = FeatureTemplateBuilder.createGoldPurityTemplate(necklaceCategory);
                    categoryFeatureTemplateRepository.save(goldPurity);
                    templateMap.put(goldPurity.getCode(), goldPurity);
                }

                if (bangleCategory != null && gramUnit != null) {
                    CategoryFeatureTemplate goldWeight = FeatureTemplateBuilder.createGoldWeightTemplate(bangleCategory, gramUnit);
                    categoryFeatureTemplateRepository.save(goldWeight);
                    templateMap.put(goldWeight.getCode(), goldWeight);
                }

                log.info("Successfully created {} sample category feature templates", templateMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading category feature templates", e);
            throw new RuntimeException("Failed to load category feature templates: " + e.getMessage(), e);
        }

        return templateMap;
    }

    /**
     * Parse a CSV line respecting quoted values
     * @param line The CSV line to parse
     * @return Array of values
     */
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentValue.toString().trim().replace("\"", ""));
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        result.add(currentValue.toString().trim().replace("\"", ""));
        return result.toArray(new String[0]);
    }

    /**
     * Loads products from CSV file
     * @param categoryMap Map of category code to Category entity
     * @param merchantMap Map of merchant code to Merchant entity
     * @param catalogMap Map of catalog code to Catalog entity
     * @return Map of product code to Product entity
     */
    private Map<String, Product> loadProducts(Map<String, Category> categoryMap,
                                              Map<String, Merchant> merchantMap,
                                              Map<String, Catalog> catalogMap) {
        log.info("Loading products from CSV...");
        Map<String, Product> productMap = new HashMap<>();

        try {
            // First check if we already have products in the database
            if (productRepository.count() > 0) {
                log.info("Products already exist in database, using existing data");
                productRepository.findAll().forEach(product ->
                        productMap.put(product.getCode(), product));
                return productMap;
            }

            // Load products from CSV file
            Resource resource = new ClassPathResource("csv/sample_products.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 12) {
                            log.warn("Skipping invalid product line: {}", line);
                            continue;
                        }

                        // Parse CSV values (note: productId, type, and imageUrl are read but not used directly)
                        // They are kept for documentation and potential future use
                        String code = values[1];
                        String name = values[2];
                        String description = values[3];
                        String status = values[5];
                        String price = values[6];
                        String categoryCode = values[7];
                        String merchantCode = values[8];
                        String catalogCode = values[9];
                        String metadata = values[10];

                        // Get references
                        Category category = categoryMap.get(categoryCode);
                        Merchant merchant = merchantMap.get(merchantCode);
                        Catalog catalog = catalogMap.get(catalogCode);

                        // Use defaults if references not found
                        if (category == null && !categoryMap.isEmpty()) {
                            category = categoryMap.values().iterator().next();
                        } else if (category == null) {
                            log.error("No categories available to assign to product");
                            continue;
                        }

                        if (merchant == null && !merchantMap.isEmpty()) {
                            merchant = merchantMap.values().iterator().next();
                        } else if (merchant == null) {
                            log.error("No merchants available to assign to product");
                            continue;
                        }

                        if (catalog == null && !catalogMap.isEmpty()) {
                            catalog = catalogMap.values().iterator().next();
                        } else if (catalog == null) {
                            log.error("No catalogs available to assign to product");
                            continue;
                        }

                        // Create product
                        Product product = new Product();
                        product.setCode(code);
                        product.setName(name);
                        product.setDescription(description);
                        product.setProductType(ProductType.SIMPLE);
                        product.setStatus(status);

                        // Set merchant and catalog
                        product.setMerchant(merchant);
                        product.setCatalog(catalog);

                        // Set price if available
                        if (StringUtils.hasText(price)) {
                            try {
                                product.setPrice(Double.parseDouble(price));
                            } catch (NumberFormatException e) {
                                log.warn("Invalid price format for product {}: {}", code, price);
                            }
                        }

                        // Convert metadata to proper JSON format
                        try {
                            // First check if it's already valid JSON
                            try {
                                JsonNode jsonNode = objectMapper.readTree(metadata);
                                // If no exception, it's already valid JSON
                                product.setMetadata(jsonNode);
                            } catch (JsonProcessingException e) {
                                // Not valid JSON, create a JSON object with the metadata as a property
                                ObjectNode metadataNode = objectMapper.createObjectNode();
                                metadataNode.put("code", metadata);
                                metadataNode.put("createdAt", System.currentTimeMillis());
                                metadataNode.put("source", "CSV_IMPORT");
                                product.setMetadata(metadataNode);
                            }
                        } catch (Exception e) {
                            log.warn("Error processing metadata for product {}: {}", code, e.getMessage());
                            // Create a default metadata object
                            com.fasterxml.jackson.databind.node.ObjectNode metadataNode = objectMapper.createObjectNode();
                            metadataNode.put("createdAt", System.currentTimeMillis());
                            metadataNode.put("source", "CSV_IMPORT");
                            product.setMetadata(metadataNode);
                        }
                        //product.setImageUrl(imageUrl);
                        product.setCreatedBy("CSV_IMPORT");
                        product.setCreatedDate(LocalDateTime.now());

                        // Save product
                        product = productRepository.save(product);
                        productMap.put(code, product);

                        // Create product-category mapping
                        ProductCategory productCategory = new ProductCategory();
                        productCategory.setProduct(product);
                        productCategory.setCategory(category);
                        productCategory.setMerchant(merchant); // Set the merchant to avoid null constraint violation
                        productCategory.setCreatedBy("CSV_IMPORT");
                        productCategory.setCreatedDate(LocalDateTime.now());
                        productCategoryRepository.save(productCategory);
                    }
                }

                log.info("Successfully loaded {} products from CSV", productMap.size());
            } else {
                log.info("Products CSV file not found, creating sample products");

                // Create sample products if CSV file not found
                // Find categories
                Category laptopCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("LAPTOP"))
                        .findFirst().orElse(null);

                Category smartphoneCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("SMARTPHONE"))
                        .findFirst().orElse(null);

                Category necklaceCategory = categoryMap.values().stream()
                        .filter(c -> c.getCode().equals("GOLD_NECKLACE"))
                        .findFirst().orElse(null);

                // Find merchants
                Merchant croma = merchantMap.values().stream()
                        .filter(m -> m.getCode().equals("CROM-001"))
                        .findFirst().orElse(merchantMap.values().iterator().next());

                Merchant tanishq = merchantMap.values().stream()
                        .filter(m -> m.getCode().equals("TNSH-001"))
                        .findFirst().orElse(merchantMap.values().iterator().next());

                // Find catalogs
                Catalog electronicsCatalog = catalogMap.values().stream()
                        .filter(c -> c.getCode().equals("ELEC-001"))
                        .findFirst().orElse(catalogMap.values().iterator().next());

                // Find jewelry catalog for potential use with jewelry products
                // This is currently not directly used but kept for future implementation
                // and code consistency

                // Create products
                if (laptopCategory != null) {
                    Product macbookPro = ProductBuilder.createMacbookPro(croma, laptopCategory, electronicsCatalog);
                    Product dellXps = ProductBuilder.createDellXps(croma, laptopCategory, electronicsCatalog);

                    productRepository.save(macbookPro);
                    productRepository.save(dellXps);

                    productMap.put(macbookPro.getCode(), macbookPro);
                    productMap.put(dellXps.getCode(), dellXps);
                }

                if (smartphoneCategory != null) {
                    Product iphone = ProductBuilder.createIPhone(smartphoneCategory, croma);
                    Product samsung = ProductBuilder.createSamsungPhone(croma, smartphoneCategory, electronicsCatalog);

                    productRepository.save(iphone);
                    productRepository.save(samsung);

                    productMap.put(iphone.getCode(), iphone);
                    productMap.put(samsung.getCode(), samsung);
                }

                if (necklaceCategory != null) {
                    Product goldNecklace = ProductBuilder.createGoldNecklace(necklaceCategory, tanishq);
                    productRepository.save(goldNecklace);
                    productMap.put(goldNecklace.getCode(), goldNecklace);
                }

                log.info("Successfully created {} sample products", productMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading products", e);
            throw new RuntimeException("Failed to load products: " + e.getMessage(), e);
        }

        return productMap;
    }

    /**
     * Loads product features from CSV file
     * @param templateMap Map of template code to CategoryFeatureTemplate entity
     * @param uomMap Map of unit of measure code to UnitOfMeasure entity
     * @return Map of feature code to ProductFeature entity
     */
    private Map<String, ProductFeature> loadProductFeatures(
            Map<String, CategoryFeatureTemplate> templateMap, Map<String, UnitOfMeasure> uomMap) {

        log.info("Loading product features from CSV...");
        Map<String, ProductFeature> featureMap = new HashMap<>();

        try {
            // First check if we already have features in the database
            if (productFeatureRepository.count() > 0) {
                log.info("Product features already exist in database, using existing data");
                productFeatureRepository.findAll().forEach(feature ->
                        featureMap.put(feature.getCode(), feature));
                return featureMap;
            }

            // Load features from CSV file
            Resource resource = new ClassPathResource("csv/product_features.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 10) {
                            log.warn("Skipping invalid feature line: {}", line);
                            continue;
                        }

                        // Parse according to the actual CSV structure
                        // feature_id,code,name,description,attribute_type,validation_pattern,min_value,max_value,allowed_values,default_value,feature_type,unit_of_measure_code,visible,editable,searchable,comparable,required,multi_valued,metadata,template_id
                        String featureId = values[0];
                        String code = values[1];
                        String name = values[2];
                        String description = values[3];
                        String attributeType = values[4];
                        String validationPattern = values[5];
                        String minValue = values[6];
                        String maxValue = values[7];
                        String allowedValues = values[8];
                        String defaultValue = values[9];
                        String featureType = values.length > 10 ? values[10] : "";
                        String uomCode = values.length > 11 ? values[11] : "";

                        // Get references
                        String templateId = values.length > 19 ? values[19] : "";
                        CategoryFeatureTemplate template = templateMap.get(templateId);
                        UnitOfMeasure uom = uomMap.get(uomCode);

                        // Create feature
                        ProductFeature feature = new ProductFeature();
                        feature.setCode(code);
                        feature.setName(name);
                        feature.setDescription(description);
                        feature.setTemplate(template);
                        feature.setUnitOfMeasure(uom);
                        feature.setAttributeType(attributeType);

                        // Set additional properties if available
                        if (minValue != null && !minValue.isEmpty()) {
                            feature.setMinValue(minValue);
                        }

                        if (maxValue != null && !maxValue.isEmpty()) {
                            feature.setMaxValue(maxValue);
                        }

                        if (allowedValues != null && !allowedValues.isEmpty()) {
                            feature.setAllowedValues(allowedValues);
                        }

                        feature.setCreatedBy("CSV_IMPORT");
                        feature.setCreatedDate(LocalDateTime.now());

                        // Save feature
                        feature = productFeatureRepository.save(feature);
                        featureMap.put(code, feature);
                    }
                }

                log.info("Successfully loaded {} product features from CSV", featureMap.size());
            } else {
                log.info("Product features CSV file not found, creating sample features");

                // Create sample features if CSV file not found
                // Find UOMs
                UnitOfMeasure gbUnit = uomMap.values().stream()
                        .filter(u -> u.getCode().equals("GB"))
                        .findFirst().orElse(null);

                UnitOfMeasure tbUnit = uomMap.values().stream()
                        .filter(u -> u.getCode().equals("TB"))
                        .findFirst().orElse(null);

                // Create sample features
                ProductFeature processorFeature = new ProductFeature();
                processorFeature.setCode("PROCESSOR");
                processorFeature.setName("Processor");
                processorFeature.setDescription("CPU model and specifications");
                processorFeature.setAttributeType("STRING");
                processorFeature.setCreatedBy("CSV_IMPORT");
                processorFeature.setCreatedDate(LocalDateTime.now());

                ProductFeature ramFeature = new ProductFeature();
                ramFeature.setCode("RAM");
                ramFeature.setName("RAM");
                ramFeature.setDescription("Memory capacity");
                ramFeature.setAttributeType("NUMERIC");
                ramFeature.setUnitOfMeasure(gbUnit);
                ramFeature.setCreatedBy("CSV_IMPORT");
                ramFeature.setCreatedDate(LocalDateTime.now());

                ProductFeature storageFeature = new ProductFeature();
                storageFeature.setCode("STORAGE");
                storageFeature.setName("Storage");
                storageFeature.setDescription("Storage capacity");
                storageFeature.setAttributeType("NUMERIC");
                storageFeature.setUnitOfMeasure(tbUnit);
                storageFeature.setCreatedBy("CSV_IMPORT");
                storageFeature.setCreatedDate(LocalDateTime.now());

                // Save features
                productFeatureRepository.save(processorFeature);
                productFeatureRepository.save(ramFeature);
                productFeatureRepository.save(storageFeature);

                featureMap.put(processorFeature.getCode(), processorFeature);
                featureMap.put(ramFeature.getCode(), ramFeature);
                featureMap.put(storageFeature.getCode(), storageFeature);

                log.info("Successfully created {} sample product features", featureMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading product features", e);
            throw new RuntimeException("Failed to load product features: " + e.getMessage(), e);
        }

        return featureMap;
    }

    /**
     * Creates product-feature mappings from CSV file
     * @param productMap Map of product code to Product entity
     * @param featureMap Map of feature code to ProductFeature entity
     * @param categoryMap Map of category code to Category entity
     */
    private void createProductFeatureMappings(Map<String, Product> productMap,
                                              Map<String, ProductFeature> featureMap,
                                              Map<String, Category> categoryMap) {
        log.info("Creating product-feature mappings...");

        try {
            // First check if we already have mappings in the database
            if (productFeatureMappingRepository.count() > 0) {
                log.info("Product-feature mappings already exist in database, skipping");
                return;
            }

            // Load mappings from CSV file
            Resource resource = new ClassPathResource("csv/product_feature_mappings.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 2) {
                            log.warn("Skipping invalid mapping line: {}", line);
                            continue;
                        }

                        String productCode = values[0];
                        String featureCode = values[1];

                        // Get references
                        Product product = productMap.get(productCode);
                        ProductFeature feature = featureMap.get(featureCode);

                        if (product == null || feature == null) {
                            log.warn("Skipping mapping with unknown product or feature: {} - {}", productCode, featureCode);
                            continue;
                        }

                        // Create mapping
                        ProductFeatureMapping mapping = new ProductFeatureMapping();
                        mapping.setProduct(product);
                        mapping.setFeature(feature);

                        // Set merchant from product
                        if (product.getMerchant() != null) {
                            mapping.setMerchant(product.getMerchant());
                        } else {
                            // If product doesn't have a merchant, try to get it from the product's category
                            Set<ProductCategory> productCategories = product.getProductCategories();
                            if (productCategories != null && !productCategories.isEmpty()) {
                                ProductCategory productCategory = productCategories.iterator().next();
                                Category category = productCategory.getCategory();
                                if (category != null && category.getMerchant() != null) {
                                    mapping.setMerchant(category.getMerchant());
                                } else {
                                    log.warn("Cannot create mapping for product {} and feature {} because no merchant found",
                                            productCode, featureCode);
                                    continue; // Skip this mapping
                                }
                            } else {
                                log.warn("Cannot create mapping for product {} and feature {} because no merchant found",
                                        productCode, featureCode);
                                continue; // Skip this mapping
                            }
                        }

                        // Set optional fields if available
                        if (values.length > 2 && !values[2].isEmpty()) {
                            mapping.setDisplayOrder(Integer.parseInt(values[2]));
                        } else {
                            mapping.setDisplayOrder(1);
                        }

                        if (values.length > 3 && !values[3].isEmpty()) {
                            mapping.setVisible(Boolean.parseBoolean(values[3]));
                        } else {
                            mapping.setVisible(true);
                        }

                        if (values.length > 4 && !values[4].isEmpty()) {
                            mapping.setEnabled(Boolean.parseBoolean(values[4]));
                        } else {
                            mapping.setEnabled(true);
                        }

                        // Set metadata as JSON
                        if (values.length > 5 && !values[5].isEmpty()) {
                            String metadata = values[5];
                            try {
                                // First check if it's already valid JSON
                                try {
                                    // Try to parse as JSON
                                    JsonNode metadataNode = objectMapper.readTree(metadata);
                                    mapping.setMetadata(metadataNode);
                                } catch (JsonProcessingException e) {
                                    // Not valid JSON, create a JSON object with the metadata as a property
                                    ObjectNode metadataNode = objectMapper.createObjectNode();
                                    metadataNode.put("value", metadata);
                                    metadataNode.put("mappedAt", System.currentTimeMillis());
                                    metadataNode.put("mappedBy", "CSV_IMPORT");
                                    mapping.setMetadata(metadataNode);
                                }
                            } catch (Exception e) {
                                log.warn("Error processing metadata for mapping {}-{}: {}", productCode, featureCode, e.getMessage());
                                // Create a default metadata object
                                ObjectNode metadataNode = objectMapper.createObjectNode();
                                metadataNode.put("mappedAt", System.currentTimeMillis());
                                metadataNode.put("mappedBy", "CSV_IMPORT");
                                metadataNode.put("notes", "Default metadata due to processing error");
                                mapping.setMetadata(metadataNode);
                            }
                        } else {
                            // Create default metadata
                            com.fasterxml.jackson.databind.node.ObjectNode metadataNode = objectMapper.createObjectNode();
                            metadataNode.put("mappedAt", System.currentTimeMillis());
                            metadataNode.put("mappedBy", "CSV_IMPORT");
                            mapping.setMetadata(metadataNode);
                        }

                        mapping.setCreatedBy("CSV_IMPORT");
                        mapping.setCreatedDate(LocalDateTime.now());

                        // Save mapping
                        productFeatureMappingRepository.save(mapping);
                    }
                }

                log.info("Successfully created product-feature mappings from CSV");
            } else {
                log.info("Product-feature mappings CSV file not found, creating sample mappings");

                // Create sample mappings if CSV file not found
                // For each product, find appropriate features based on category
                for (Product product : productMap.values()) {
                    // Get product categories to determine merchant
                    Set<ProductCategory> productCategories = product.getProductCategories();
                    if (productCategories == null || productCategories.isEmpty()) {
                        continue;
                    }

                    // Get first category's catalog's merchant
                    Category category = productCategories.iterator().next().getCategory();
                    if (category == null) {
                        continue;
                    }

                    // Find features for this category
                    List<ProductFeature> features = featureMap.values().stream()
                            .filter(f -> f.getTemplate() != null &&
                                    f.getTemplate().getCategory() != null &&
                                    f.getTemplate().getCategory().getCode().equals(category.getCode()))
                            .collect(Collectors.toList());

                    // If no category-specific features found, use generic features
                    if (features.isEmpty()) {
                        features = new ArrayList<>(featureMap.values());
                    }

                    // Create mappings
                    int displayOrder = 1;
                    for (ProductFeature feature : features) {
                        // Use the builder from ProductFeatureMappingBuilder
                        ProductFeatureMapping mapping = ProductFeatureMappingBuilder.createFeatureMapping(product, feature);
                        mapping.setDisplayOrder(displayOrder++);
                        mapping.setCreatedBy("CSV_IMPORT");
                        mapping.setCreatedDate(LocalDateTime.now());

                        // Set merchant from product or category
                        if (product.getMerchant() != null) {
                            mapping.setMerchant(product.getMerchant());
                        } else {
                            // If product doesn't have a merchant, get it from the category
                            if (category != null && category.getMerchant() != null) {
                                mapping.setMerchant(category.getMerchant());
                            } else {
                                log.warn("Cannot create mapping for product {} and feature {} because no merchant found",
                                        product.getCode(), feature.getCode());
                                continue; // Skip this mapping
                            }
                        }

                        productFeatureMappingRepository.save(mapping);
                    }
                }

                log.info("Successfully created sample product-feature mappings");
            }
        } catch (Exception e) {
            log.error("Error creating product-feature mappings", e);
            throw new RuntimeException("Failed to create product-feature mappings: " + e.getMessage(), e);
        }
    }

    /**
     * Loads product feature values from CSV file
     * @param productMap Map of product code to Product entity
     * @param featureMap Map of feature code to ProductFeature entity
     * @param categoryMap Map of category code to Category entity
     * @param uomMap Map of unit of measure code to UnitOfMeasure entity
     */
    private void loadProductFeatureValues(Map<String, Product> productMap,
                                          Map<String, ProductFeature> featureMap,
                                          Map<String, Category> categoryMap,
                                          Map<String, UnitOfMeasure> uomMap) {
        log.info("Loading product feature values from CSV...");

        try {
            // First check if we already have values in the database
            if (productFeatureValueRepository.count() > 0) {
                log.info("Product feature values already exist in database, skipping");
                return;
            }

            // Load values from CSV file
            Resource resource = new ClassPathResource("csv/product_feature_values.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 4) {
                            log.warn("Skipping invalid feature value line: {}", line);
                            continue;
                        }

                        String productCode = values[0];
                        String featureCode = values[1];
                        String valueType = values[2];
                        String value = values[3];

                        // Get references
                        Product product = productMap.get(productCode);
                        ProductFeature feature = featureMap.get(featureCode);

                        if (product == null || feature == null) {
                            log.warn("Skipping feature value with unknown product or feature: {} - {}", productCode, featureCode);
                            continue;
                        }

                        // Find or create mapping
                        ProductFeatureMapping mapping = productFeatureMappingRepository.findByProductAndFeature(product, feature);
                        if (mapping == null) {
                            mapping = new ProductFeatureMapping();
                            mapping.setProduct(product);
                            mapping.setFeature(feature);

                            // Set merchant from product
                            if (product.getMerchant() != null) {
                                mapping.setMerchant(product.getMerchant());
                            } else {
                                // If product doesn't have a merchant, try to get it from the product's category
                                Set<ProductCategory> productCategories = product.getProductCategories();
                                if (productCategories != null && !productCategories.isEmpty()) {
                                    ProductCategory productCategory = productCategories.iterator().next();
                                    Category category = productCategory.getCategory();
                                    if (category != null && category.getMerchant() != null) {
                                        mapping.setMerchant(category.getMerchant());
                                    } else {
                                        log.warn("Cannot create mapping for product {} and feature {} because no merchant found",
                                                productCode, featureCode);
                                        continue; // Skip this mapping
                                    }
                                } else {
                                    log.warn("Cannot create mapping for product {} and feature {} because no merchant found",
                                            productCode, featureCode);
                                    continue; // Skip this mapping
                                }
                            }

                            mapping.setCreatedBy("CSV_IMPORT");
                            mapping.setCreatedDate(LocalDateTime.now());
                            mapping = productFeatureMappingRepository.save(mapping);
                        }

                        // Create feature value using builder pattern
                        ProductFeatureValue featureValue = ProductFeatureValue.builder()
                                .feature(feature)  // Set the feature to avoid null constraint violation
                                .type(valueType)
                                .createdBy("CSV_IMPORT")
                                .build();

                        // Set created and modified dates
                        featureValue.setCreatedDate(LocalDateTime.now());
                        featureValue.setLastModifiedDate(LocalDateTime.now());

                        // Check if the value is already in JSON format
                        String jsonValue = value;
                        if (!value.trim().startsWith("{") && !value.trim().startsWith("[") &&
                                !value.trim().equals("null") && !value.trim().equals("true") && !value.trim().equals("false") &&
                                !value.trim().matches("^\\d+(\\.\\d+)?$")) {
                            // If not JSON, wrap it in quotes to make it a valid JSON string
                            jsonValue = "\"" + value.replace("\"", "\\\"") + "\"";
                        }

                        // Set attribute values
                        featureValue.setAttributeValues(objectMapper.readTree(jsonValue));

                        // Save feature value first - ensure it has a valid feature_id
                        featureValue = productFeatureValueRepository.save(featureValue);

                        // Create mapping between feature value and product-feature mapping using builder
                        // Get the primary category or find a valid category for the product
                        Category category = mapping.getProduct().getPrimaryCategory();
                        if (category == null && mapping.getProduct().getProductCategories() != null && !mapping.getProduct().getProductCategories().isEmpty()) {
                            // If no primary category, use the first available category
                            category = mapping.getProduct().getProductCategories().iterator().next().getCategory();
                        }

                        // If still no category, find one from the repository
                        if (category == null) {
                            List<ProductCategory> productCategories = productCategoryRepository.findByProductId(mapping.getProduct().getId());
                            if (!productCategories.isEmpty()) {
                                category = productCategories.get(0).getCategory();
                            } else {
                                // If no categories found, use a default category
                                List<Category> allCategories = categoryRepository.findAll();
                                if (!allCategories.isEmpty()) {
                                    category = allCategories.get(0);
                                    log.warn("No category found for product {}, using default category: {}",
                                            mapping.getProduct().getCode(), category.getCode());
                                } else {
                                    log.error("No categories available in the system, cannot create feature value mapping for product: {}",
                                            mapping.getProduct().getCode());
                                    continue; // Skip this mapping
                                }
                            }
                        }

                        ProductFeatureValueMapping valueMapping = ProductFeatureValueMapping.builder()
                                .featureMapping(mapping)
                                .featureValue(featureValue)
                                .product(mapping.getProduct())
                                .category(category) // Use the resolved category
                                .displayOrder(1)
                                .isPrimary(true)
                                .isActive(true)
                                .createdBy("CSV_IMPORT")
                                .build();

                        // Set created and modified dates
                        valueMapping.setCreatedDate(LocalDateTime.now());
                        valueMapping.setLastModifiedDate(LocalDateTime.now());

                        // Save the mapping
                        productFeatureValueMappingRepository.save(valueMapping);
                    }
                }

                log.info("Successfully loaded product feature values from CSV");
            } else {
                log.info("Product feature values CSV file not found, creating sample values");

                // Create sample values if CSV file not found
                // For each product-feature mapping, create a value
                List<ProductFeatureMapping> mappings = productFeatureMappingRepository.findAll();
                for (ProductFeatureMapping mapping : mappings) {
                    Product product = mapping.getProduct();
                    ProductFeature feature = mapping.getFeature();

                    if (product == null || feature == null) {
                        continue;
                    }

                    // Create a sample value based on feature type
                    ProductFeatureValue featureValue = new ProductFeatureValue();
                    //featureValue.setMapping(mapping);

                    if ("PROCESSOR".equals(feature.getCode())) {
                        featureValue.setType("STRING");
                        String processorValue;
                        if (product.getCode().contains("MACBOOK")) {
                            processorValue = "Apple M1";
                        } else if (product.getCode().contains("DELL")) {
                            processorValue = "Intel Core i7";
                        } else if (product.getCode().contains("IPHONE")) {
                            processorValue = "Apple A14 Bionic";
                        } else if (product.getCode().contains("SAMSUNG")) {
                            processorValue = "Snapdragon 888";
                        } else {
                            processorValue = "Generic Processor";
                        }
                        featureValue.setAttributeValue(objectMapper.readTree("\""+processorValue+"\""));
                    } else if ("RAM".equals(feature.getCode())) {
                        featureValue.setType("NUMERIC");
                        String ramValue;
                        if (product.getCode().contains("MACBOOK") || product.getCode().contains("DELL")) {
                            ramValue = "16";
                        } else if (product.getCode().contains("IPHONE") || product.getCode().contains("SAMSUNG")) {
                            ramValue = "8";
                        } else {
                            ramValue = "4";
                        }
                        featureValue.setAttributeValue(objectMapper.readTree(ramValue));
                    } else if ("STORAGE".equals(feature.getCode())) {
                        featureValue.setType("NUMERIC");
                        String storageValue;
                        if (product.getCode().contains("MACBOOK") || product.getCode().contains("DELL")) {
                            storageValue = "1";
                        } else if (product.getCode().contains("IPHONE") || product.getCode().contains("SAMSUNG")) {
                            storageValue = "256";
                        } else {
                            storageValue = "128";
                        }
                        featureValue.setAttributeValue(objectMapper.readTree(storageValue));
                    } else {
                        featureValue.setType("STRING");
                        featureValue.setAttributeValue(objectMapper.readTree("\""+"Sample Value"+"\""));
                    }

                    featureValue.setCreatedBy("CSV_IMPORT");
                    featureValue.setCreatedDate(LocalDateTime.now());

                    // Save feature value
                    productFeatureValueRepository.save(featureValue);
                }

                log.info("Successfully created sample product feature values");
            }
        } catch (Exception e) {
            log.error("Error loading product feature values", e);
            throw new RuntimeException("Failed to load product feature values: " + e.getMessage(), e);
        }
    }

    /**
     * Loads channels from CSV file
     * @param merchantMap Map of merchant code to Merchant entity
     * @return Map of channel code to Channel entity
     */
    private Map<String, Channel> loadChannels(Map<String, Merchant> merchantMap, Map<String, Store> storeMap) {
        log.info("Loading channels from CSV...");
        Map<String, Channel> channelMap = new HashMap<>();

        try {
            // First check if we already have channels in the database
            if (channelRepository.count() > 0) {
                log.info("Channels already exist in database, using existing data");
                channelRepository.findAll().forEach(channel ->
                        channelMap.put(channel.getCode(), channel));
                return channelMap;
            }

            // Load channels from CSV file
            Resource resource = new ClassPathResource("csv/channels.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 4) {
                            log.warn("Skipping invalid channel line: {}", line);
                            continue;
                        }

                        String code = values[0];
                        String name = values[1];
                        String description = values[2];
                        String merchantCode = values[3];
                        String channelType = values.length > 4 ? values[4] : "ECOMMERCE";
                        String status = values.length > 5 ? values[5] : "ACTIVE";
                        String storeCode = values.length > 6 ? values[6] : null;

                        // Get merchant reference
                        Merchant merchant = merchantMap.get(merchantCode);
                        if (merchant == null) {
                            log.warn("Skipping channel with unknown merchant: {}", merchantCode);
                            continue;
                        }

                        // Create channel
                        Channel channel = new Channel();
                        channel.setCode(code);
                        channel.setName(name);
                        channel.setDescription(description);
                        // Set channel type from enum
                        try {
                            channel.setType(ChannelType.valueOf(channelType));
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid channel type: {}, using default ECOMMERCE", channelType);
                            channel.setType(ChannelType.ECOMMERCE);
                        }
                        channel.setStatus(status);
                        channel.setCreatedBy("CSV_IMPORT");
                        channel.setCreatedDate(LocalDateTime.now());

                        // Assign a valid Store
                        if (storeCode != null && !storeCode.isEmpty()) {
                            Store store = storeMap.get(storeCode);
                            if (store == null) {
                                log.warn("Skipping channel with unknown store: {}", storeCode);
                                continue;
                            }
                            channel.setStore(store);
                        } else {
                            log.warn("Channel {} has no store code specified", code);
                            continue;
                        }

                        // Add metadata based on merchant type
                        ObjectNode metadata = objectMapper.createObjectNode();
                        if (merchantCode.contains("TATACLIQ")) {
                            metadata.put("url", "https://www.tatacliq.com");
                            metadata.put("appStoreUrl", "https://apps.apple.com/in/app/tata-cliq-online-shopping/id1101619385");
                            metadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.tul.tatacliq");
                        } else if (merchantCode.contains("CROMA")) {
                            metadata.put("url", "https://www.croma.com");
                            metadata.put("appStoreUrl", "https://apps.apple.com/in/app/croma-electronics/id1084171218");
                            metadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.croma.croma");
                        } else if (merchantCode.contains("TANISHQ")) {
                            metadata.put("url", "https://www.tanishq.co.in");
                            metadata.put("appStoreUrl", "https://apps.apple.com/in/app/tanishq/id1067877995");
                            metadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.titan.tanishq");
                        } else if (merchantCode.contains("BIGBASKET")) {
                            metadata.put("url", "https://www.bigbasket.com");
                            metadata.put("appStoreUrl", "https://apps.apple.com/in/app/bigbasket-online-grocery/id660683603");
                            metadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.bigbasket.mobileapp");
                        } else if (merchantCode.contains("1MG")) {
                            metadata.put("url", "https://www.1mg.com");
                            metadata.put("appStoreUrl", "https://apps.apple.com/in/app/tata-1mg-healthcare-app/id554578419");
                            metadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.aranoah.healthkart.plus");
                        } else {
                            metadata.put("url", "https://www.example.com");
                        }

                        try {
                            channel.setMetadata(objectMapper.writeValueAsString(metadata));
                        } catch (JsonProcessingException e) {
                            log.error("Error creating channel metadata", e);
                            channel.setMetadata("{}");
                        }

                        // Save channel
                        channel = channelRepository.save(channel);
                        channelMap.put(code, channel);
                    }
                }

                log.info("Successfully loaded {} channels from CSV", channelMap.size());
            } else {
                log.info("Channels CSV file not found, creating sample channels");

                // Create sample channels if CSV file not found
                for (Merchant merchant : merchantMap.values()) {
                    String merchantCode = merchant.getCode();

                    // Create online channel
                    Channel onlineChannel = new Channel();
                    onlineChannel.setCode(merchantCode + "_ONLINE");
                    onlineChannel.setName(merchant.getName() + " Online");
                    onlineChannel.setDescription("Online channel for " + merchant.getName());
                    onlineChannel.setType(ChannelType.ECOMMERCE);
                    onlineChannel.setStatus("ACTIVE");
                    onlineChannel.setCreatedBy("CSV_IMPORT");
                    onlineChannel.setCreatedDate(LocalDateTime.now());

                    // Assign a valid Store
                    String storeCode = merchantCode + "_STORE"; // Assuming storeCode is in the format merchantCode_STORE
                    Store store = storeMap.get(storeCode);
                    if (store == null) {
                        log.warn("Skipping channel with unknown store: {}", storeCode);
                        continue;
                    }
                    onlineChannel.setStore(store);

                    // Add metadata
                    com.fasterxml.jackson.databind.node.ObjectNode onlineMetadata = objectMapper.createObjectNode();
                    if (merchantCode.contains("TATACLIQ")) {
                        onlineMetadata.put("url", "https://www.tatacliq.com");
                        onlineMetadata.put("appStoreUrl", "https://apps.apple.com/in/app/tata-cliq-online-shopping/id1101619385");
                        onlineMetadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.tul.tatacliq");
                    } else if (merchantCode.contains("CROMA")) {
                        onlineMetadata.put("url", "https://www.croma.com");
                        onlineMetadata.put("appStoreUrl", "https://apps.apple.com/in/app/croma-electronics/id1084171218");
                        onlineMetadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.croma.croma");
                    } else if (merchantCode.contains("TANISHQ")) {
                        onlineMetadata.put("url", "https://www.tanishq.co.in");
                        onlineMetadata.put("appStoreUrl", "https://apps.apple.com/in/app/tanishq/id1067877995");
                        onlineMetadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.titan.tanishq");
                    } else if (merchantCode.contains("BIGBASKET")) {
                        onlineMetadata.put("url", "https://www.bigbasket.com");
                        onlineMetadata.put("appStoreUrl", "https://apps.apple.com/in/app/bigbasket-online-grocery/id660683603");
                        onlineMetadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.bigbasket.mobileapp");
                    } else if (merchantCode.contains("1MG")) {
                        onlineMetadata.put("url", "https://www.1mg.com");
                        onlineMetadata.put("appStoreUrl", "https://apps.apple.com/in/app/tata-1mg-healthcare-app/id554578419");
                        onlineMetadata.put("playStoreUrl", "https://play.google.com/store/apps/details?id=com.aranoah.healthkart.plus");
                    } else {
                        onlineMetadata.put("url", "https://www.example.com");
                    }

                    try {
                        onlineChannel.setMetadata(objectMapper.writeValueAsString(onlineMetadata));
                    } catch (JsonProcessingException e) {
                        log.error("Error creating channel metadata", e);
                        onlineChannel.setMetadata("{}");
                    }

                    // Save online channel
                    onlineChannel = channelRepository.save(onlineChannel);
                    channelMap.put(onlineChannel.getCode(), onlineChannel);

                    // Create in-store channel
                    Channel storeChannel = new Channel();
                    storeChannel.setCode(merchantCode + "_STORE");
                    storeChannel.setName(merchant.getName() + " In-Store");
                    storeChannel.setDescription("In-store channel for " + merchant.getName());
                    storeChannel.setType(ChannelType.PHYSICAL_STORE);
                    storeChannel.setStatus("ACTIVE");
                    storeChannel.setCreatedBy("CSV_IMPORT");
                    storeChannel.setCreatedDate(LocalDateTime.now());

                    // Assign a valid Store
                    storeChannel.setStore(store);

                    // Add metadata
                    ObjectNode storeMetadata = objectMapper.createObjectNode();
                    storeMetadata.put("storeCount", 100);
                    storeMetadata.put("locations", "Pan India");

                    try {
                        storeChannel.setMetadata(objectMapper.writeValueAsString(storeMetadata));
                    } catch (JsonProcessingException e) {
                        log.error("Error creating channel metadata", e);
                        storeChannel.setMetadata("{}");
                    }

                    // Save store channel
                    storeChannel = channelRepository.save(storeChannel);
                    channelMap.put(storeChannel.getCode(), storeChannel);
                }

                log.info("Successfully created {} sample channels", channelMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading channels", e);
            throw new RuntimeException("Failed to load channels: " + e.getMessage(), e);
        }

        return channelMap;
    }

    /**
     * Creates product-channel mappings
     * @param productMap Map of product code to Product entity
     * @param channelMap Map of channel code to Channel entity
     */
    private void createProductChannelMappings(Map<String, Product> productMap, Map<String, Channel> channelMap) {
        log.info("Creating product-channel mappings...");

        try {
            // ProductChannelMapping entity doesn't exist yet
            // Commenting out this code until the entity is created
            /*
            if (productChannelMappingRepository.count() > 0) {
                log.info("Product-channel mappings already exist in database, skipping");
                return;
            }
            */
            log.info("ProductChannelMapping entity not implemented yet, skipping");

            // Load mappings from CSV file
            Resource resource = new ClassPathResource("csv/product_channel_mappings.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 2) {
                            log.warn("Skipping invalid mapping line: {}", line);
                            continue;
                        }

                        String productCode = values[0];
                        String channelCode = values[1];

                        // Get references
                        Product product = productMap.get(productCode);
                        Channel channel = channelMap.get(channelCode);

                        if (product == null || channel == null) {
                            log.warn("Skipping mapping with unknown product or channel: {} - {}", productCode, channelCode);
                            continue;
                        }

                        // ProductChannelMapping entity doesn't exist yet
                        // Commenting out this code until the entity is created
                        /*
                        // Create mapping
                        ProductChannelMapping mapping = new ProductChannelMapping();
                        mapping.setProduct(product);
                        mapping.setChannel(channel);
                        mapping.setCreatedBy("CSV_IMPORT");
                        mapping.setCreatedDate(LocalDateTime.now());

                        // Save mapping
                        // ProductChannelMapping entity doesn't exist yet
                        // productChannelMappingRepository.save(mapping);
                        log.info("Would create mapping between product {} and channel {}",
                                product.getCode(), channel.getCode());
                        */

                        // Log the mapping that would be created
                        log.info("Would create mapping between product {} and channel {}",
                                product.getCode(), channel.getCode());
                    }
                }

                log.info("Successfully created product-channel mappings from CSV");
            } else {
                log.info("Product-channel mappings CSV file not found, creating sample mappings");

                // Create sample mappings if CSV file not found
                // For each product, map to all channels of the same merchant
                for (Product product : productMap.values()) {
                    // Get product categories to determine merchant
                    Set<ProductCategory> productCategories = product.getProductCategories();
                    if (productCategories == null || productCategories.isEmpty()) {
                        continue;
                    }

                    // Get first category's catalog's merchant
                    Category category = productCategories.iterator().next().getCategory();
                    if (category == null) {
                        continue;
                    }

                    // Find channels for this merchant
                    List<Channel> merchantChannels = channelMap.values().stream()
                            .filter(c -> c.getStore() != null && c.getStore().getMerchant() != null &&
                                    c.getStore().getMerchant().getCode().equals(category.getCatalog().getBusiness().getCode()))
                            .collect(Collectors.toList());

                    // Create mappings
                    for (Channel channel : merchantChannels) {
                        // ProductChannelMapping entity doesn't exist, so we'll just log the mapping
                        log.info("Would create mapping between product {} and channel {}",
                                product.getCode(), channel.getCode());

                        // This would be where we'd create and save the mapping if the entity existed
                    }
                }

                log.info("Successfully created sample product-channel mappings");
            }
        } catch (Exception e) {
            log.error("Error creating product-channel mappings", e);
            throw new RuntimeException("Failed to create product-channel mappings: " + e.getMessage(), e);
        }
    }

    /**
     * Loads platforms from CSV file
     * @param channelMap Map of channel code to Channel entity
     * @return Map of platform code to Platform entity
     */
    private Map<String, Platform> loadPlatforms(Map<String, Channel> channelMap) {
        log.info("Loading platforms from CSV...");
        Map<String, Platform> platformMap = new HashMap<>();

        try {
            // First check if we already have platforms in the database
            if (platformRepository.count() > 0) {
                log.info("Platforms already exist in database, using existing data");
                platformRepository.findAll().forEach(platform ->
                        platformMap.put(platform.getCode(), platform));
                return platformMap;
            }

            // Load platforms from CSV file
            Resource resource = new ClassPathResource("csv/platforms.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 5) {
                            log.warn("Skipping invalid platform line: {}", line);
                            continue;
                        }

                        String code = values[0];
                        String name = values[1];
                        String description = values[2];
                        String channelCode = values[3];
                        String platformType = values[4];

                        // Get channel reference
                        Channel channel = channelMap.get(channelCode);
                        if (channel == null) {
                            log.warn("Skipping platform with unknown channel: {}", channelCode);
                            continue;
                        }

                        // Create platform
                        Platform platform = new Platform();
                        platform.setCode(code);
                        platform.setName(name);
                        platform.setDescription(description);
                        platform.setChannel(channel);

                        // Set platform type from enum
                        try {
                            platform.setType(PlatformType.valueOf(platformType));
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid platform type: {}, using default DESKTOP_WEB", platformType);
                            platform.setType(PlatformType.DESKTOP_WEB);
                        }

                        platform.setCreatedBy("CSV_IMPORT");
                        platform.setCreatedDate(LocalDateTime.now());

                        // Add metadata based on platform type
                        ObjectNode metadata = objectMapper.createObjectNode();
                        if (platformType.contains("MOBILE_APP")) {
                            if (platformType.contains("ANDROID")) {
                                metadata.put("storeUrl", "https://play.google.com/store");
                                metadata.put("minSdkVersion", "21");
                                metadata.put("targetSdkVersion", "30");
                            } else if (platformType.contains("IOS")) {
                                metadata.put("storeUrl", "https://apps.apple.com");
                                metadata.put("minOsVersion", "13.0");
                            }
                            metadata.put("requiresLocation", true);
                            metadata.put("requiresCamera", true);
                            metadata.put("requiresNotifications", true);
                        } else if (platformType.contains("WEB")) {
                            metadata.put("requiresModernBrowser", true);
                            metadata.put("supportedBrowsers", "Chrome, Firefox, Safari, Edge");
                            if (platformType.contains("MOBILE")) {
                                metadata.put("responsiveDesign", true);
                                metadata.put("minScreenWidth", "320px");
                            } else {
                                metadata.put("minScreenWidth", "1024px");
                            }
                        }

                        // Set metadata directly as JsonNode
                        platform.setMetadata(metadata);

                        // Save platform
                        platform = platformRepository.save(platform);
                        platformMap.put(code, platform);
                    }
                }

                log.info("Successfully loaded {} platforms from CSV", platformMap.size());
            } else {
                log.info("Platforms CSV file not found, creating sample platforms");

                // Create sample platforms for each channel
                for (Channel channel : channelMap.values()) {
                    String channelCode = channel.getCode();

                    // Create web platform
                    Platform webPlatform = new Platform();
                    webPlatform.setCode(channelCode + "_WEB");
                    webPlatform.setName(channel.getName() + " Web");
                    webPlatform.setDescription("Web platform for " + channel.getName());
                    webPlatform.setChannel(channel);
                    webPlatform.setType(PlatformType.DESKTOP_WEB);
                    webPlatform.setCreatedBy("CSV_IMPORT");
                    webPlatform.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode webMetadata = objectMapper.createObjectNode();
                    webMetadata.put("requiresModernBrowser", true);
                    webMetadata.put("supportedBrowsers", "Chrome, Firefox, Safari, Edge");
                    webMetadata.put("minScreenWidth", "1024px");

                    // Set metadata directly as JsonNode
                    webPlatform.setMetadata(webMetadata);

                    // Save web platform
                    webPlatform = platformRepository.save(webPlatform);
                    platformMap.put(webPlatform.getCode(), webPlatform);

                    // Create mobile web platform
                    Platform mobileWebPlatform = new Platform();
                    mobileWebPlatform.setCode(channelCode + "_MOBILE_WEB");
                    mobileWebPlatform.setName(channel.getName() + " Mobile Web");
                    mobileWebPlatform.setDescription("Mobile web platform for " + channel.getName());
                    mobileWebPlatform.setChannel(channel);
                    mobileWebPlatform.setType(PlatformType.MOBILE_WEB);
                    mobileWebPlatform.setCreatedBy("CSV_IMPORT");
                    mobileWebPlatform.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode mobileWebMetadata = objectMapper.createObjectNode();
                    mobileWebMetadata.put("requiresModernBrowser", true);
                    mobileWebMetadata.put("supportedBrowsers", "Chrome, Firefox, Safari, Edge");
                    mobileWebMetadata.put("responsiveDesign", true);
                    mobileWebMetadata.put("minScreenWidth", "320px");

                    // Set metadata directly as JsonNode
                    mobileWebPlatform.setMetadata(mobileWebMetadata);

                    // Save mobile web platform
                    mobileWebPlatform = platformRepository.save(mobileWebPlatform);
                    platformMap.put(mobileWebPlatform.getCode(), mobileWebPlatform);

                    // Create Android app platform
                    Platform androidPlatform = new Platform();
                    androidPlatform.setCode(channelCode + "_ANDROID");
                    androidPlatform.setName(channel.getName() + " Android App");
                    androidPlatform.setDescription("Android app for " + channel.getName());
                    androidPlatform.setChannel(channel);
                    androidPlatform.setType(PlatformType.MOBILE_APP_ANDROID);
                    androidPlatform.setCreatedBy("CSV_IMPORT");
                    androidPlatform.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode androidMetadata = objectMapper.createObjectNode();
                    androidMetadata.put("storeUrl", "https://play.google.com/store");
                    androidMetadata.put("minSdkVersion", "21");
                    androidMetadata.put("targetSdkVersion", "30");
                    androidMetadata.put("requiresLocation", true);
                    androidMetadata.put("requiresCamera", true);
                    androidMetadata.put("requiresNotifications", true);

                    // Set metadata directly as JsonNode
                    androidPlatform.setMetadata(androidMetadata);

                    // Save Android platform
                    androidPlatform = platformRepository.save(androidPlatform);
                    platformMap.put(androidPlatform.getCode(), androidPlatform);

                    // Create iOS app platform
                    Platform iosPlatform = new Platform();
                    iosPlatform.setCode(channelCode + "_IOS");
                    iosPlatform.setName(channel.getName() + " iOS App");
                    iosPlatform.setDescription("iOS app for " + channel.getName());
                    iosPlatform.setChannel(channel);
                    iosPlatform.setType(PlatformType.MOBILE_APP_IOS);
                    iosPlatform.setCreatedBy("CSV_IMPORT");
                    iosPlatform.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode iosMetadata = objectMapper.createObjectNode();
                    iosMetadata.put("storeUrl", "https://apps.apple.com");
                    iosMetadata.put("minOsVersion", "13.0");
                    iosMetadata.put("requiresLocation", true);
                    iosMetadata.put("requiresCamera", true);
                    iosMetadata.put("requiresNotifications", true);

                    // Set metadata directly as JsonNode
                    iosPlatform.setMetadata(iosMetadata);

                    // Save iOS platform
                    iosPlatform = platformRepository.save(iosPlatform);
                    platformMap.put(iosPlatform.getCode(), iosPlatform);
                }

                log.info("Successfully created {} sample platforms", platformMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading platforms", e);
            throw new RuntimeException("Failed to load platforms: " + e.getMessage(), e);
        }

        return platformMap;
    }

    /**
     * Creates product-platform mappings from CSV file
     * @param productMap Map of product code to Product entity
     * @param platformMap Map of platform code to Platform entity
     */
    private void createProductPlatformMappings(Map<String, Product> productMap, Map<String, Platform> platformMap) {
        log.info("Creating product-platform mappings...");

        try {
            // First check if we already have mappings in the database
            if (productPlatformRepository.count() > 0) {
                log.info("Product-platform mappings already exist in database, skipping");
                return;
            }

            // Load mappings from CSV file
            Resource resource = new ClassPathResource("csv/product_platform_mappings.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 3) {
                            log.warn("Skipping invalid mapping line: {}", line);
                            continue;
                        }

                        String productCode = values[0];
                        String platformCode = values[1];
                        boolean isActive = values.length > 2 && Boolean.parseBoolean(values[2]);
                        Integer displayOrder = values.length > 3 ? Integer.parseInt(values[3]) : 0;
                        String status = values.length > 4 ? values[4] : "ACTIVE";

                        // Get references
                        Product product = productMap.get(productCode);
                        Platform platform = platformMap.get(platformCode);

                        if (product == null || platform == null) {
                            log.warn("Skipping mapping with unknown product or platform: {} - {}", productCode, platformCode);
                            continue;
                        }

                        // Create mapping
                        ProductPlatform mapping = new ProductPlatform();
                        mapping.setProduct(product);
                        mapping.setPlatform(platform);
                        mapping.setIsActive(isActive);
                        mapping.setDisplayOrder(displayOrder);
                        mapping.setStatus(status);
                        mapping.setCreatedBy("CSV_IMPORT");
                        mapping.setCreatedDate(LocalDateTime.now());

                        // Save mapping
                        productPlatformRepository.save(mapping);
                    }
                }

                log.info("Successfully created product-platform mappings from CSV");
            } else {
                log.info("Product-platform mappings CSV file not found, creating sample mappings");

                // Create sample mappings if CSV file not found
                // For each product, map to all platforms of the same merchant
                for (Product product : productMap.values()) {
                    // Get product categories to determine merchant
                    Set<ProductCategory> productCategories = product.getProductCategories();
                    if (productCategories == null || productCategories.isEmpty()) {
                        continue;
                    }

                    // Get first category's catalog's merchant
                    Category category = productCategories.iterator().next().getCategory();
                    if (category == null || category.getCatalog() == null) {
                        continue;
                    }

                    Catalog catalog = category.getCatalog();

                    // Find platforms for channels related to this merchant
                    List<Platform> merchantPlatforms = platformMap.values().stream()
                            .filter(p -> p.getChannel() != null)
                            .collect(Collectors.toList());

                    // Create mappings
                    for (Platform platform : merchantPlatforms) {
                        ProductPlatform mapping = new ProductPlatform();
                        mapping.setProduct(product);
                        mapping.setPlatform(platform);
                        mapping.setIsActive(true);
                        mapping.setDisplayOrder(1);
                        mapping.setStatus("ACTIVE");
                        mapping.setCreatedBy("CSV_IMPORT");
                        mapping.setCreatedDate(LocalDateTime.now());

                        productPlatformRepository.save(mapping);
                    }
                }

                log.info("Successfully created sample product-platform mappings");
            }
        } catch (Exception e) {
            log.error("Error creating product-platform mappings", e);
            throw new RuntimeException("Failed to create product-platform mappings: " + e.getMessage(), e);
        }
    }

    /**
     * Loads sellers from CSV file
     * @param merchantMap Map of merchant code to Merchant entity
     * @return Map of seller code to Seller entity
     */
    private Map<String, Seller> loadSellers(Map<String, Merchant> merchantMap) {
        log.info("Loading sellers from CSV...");
        Map<String, Seller> sellerMap = new HashMap<>();

        try {
            // First check if we already have sellers in the database
            if (sellerRepository.count() > 0) {
                log.info("Sellers already exist in database, using existing data");
                sellerRepository.findAll().forEach(seller ->
                        sellerMap.put(seller.getCode(), seller));
                return sellerMap;
            }

            // Load sellers from CSV file
            Resource resource = new ClassPathResource("csv/sellers.csv");
            if (resource.exists()) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                    // Skip header
                    String line = reader.readLine();

                    // Process each line
                    while ((line = reader.readLine()) != null) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 5) {
                            log.warn("Skipping invalid seller line: {}", line);
                            continue;
                        }

                        String code = values[0];
                        String name = values[1];
                        String description = values[2];
                        String type = values[3];
                        String status = values[4];
                        String merchantCode = values.length > 5 ? values[5] : null;

                        // Get merchant reference if provided
                        Merchant merchant = null;
                        if (merchantCode != null && !merchantCode.isEmpty()) {
                            merchant = merchantMap.get(merchantCode);
                            if (merchant == null) {
                                log.warn("Skipping seller with unknown merchant: {}", merchantCode);
                                continue;
                            }
                        }

                        // Create seller
                        Seller seller = new Seller();
                        seller.setCode(code);
                        seller.setName(name);
                        seller.setDescription(description);
                        seller.setType(type);
                        seller.setStatus(status);
                        seller.setCreatedBy("CSV_IMPORT");
                        seller.setCreatedDate(LocalDateTime.now());

                        // Add metadata based on seller type
                        ObjectNode metadata = objectMapper.createObjectNode();

                        // Common metadata fields
                        metadata.put("verified", true);
                        metadata.put("rating", 4.5);
                        metadata.put("reviewCount", 100);

                        // Type-specific metadata
                        if ("MARKETPLACE".equals(type)) {
                            metadata.put("commissionRate", 10.0);
                            metadata.put("returnPolicy", "30 days");
                            metadata.put("shippingTime", "2-3 business days");
                        } else if ("DIRECT".equals(type)) {
                            metadata.put("isOfficial", true);
                            metadata.put("warrantyProvider", true);
                            metadata.put("prioritySupport", true);
                        } else if ("PARTNER".equals(type)) {
                            metadata.put("partnershipLevel", "GOLD");
                            metadata.put("contractStartDate", LocalDate.now().minusMonths(6).toString());
                            metadata.put("contractEndDate", LocalDate.now().plusMonths(18).toString());
                        }

                        // Add contact information
                        ObjectNode contactInfo = objectMapper.createObjectNode();
                        contactInfo.put("email", "support@" + code.toLowerCase() + ".com");
                        contactInfo.put("phone", "+91-9876543210");

                        ObjectNode address = objectMapper.createObjectNode();
                        address.put("line1", "123 Business Park");
                        address.put("line2", "Sector 5");
                        address.put("city", "Mumbai");
                        address.put("state", "Maharashtra");
                        address.put("country", "India");
                        address.put("pincode", "400001");

                        contactInfo.set("address", address);
                        metadata.set("contactInfo", contactInfo);



                        // Set merchant if available
                        if (merchant != null) {
                            // Set seller's merchantId instead of merchant object
                            seller.setMerchantId(merchant.getId());
                        }

                        // Save seller
                        seller = sellerRepository.save(seller);
                        sellerMap.put(code, seller);
                    }
                }

                log.info("Successfully loaded {} sellers from CSV", sellerMap.size());
            } else {
                log.info("Sellers CSV file not found, creating sample sellers");

                // Create sample sellers for each merchant
                for (Merchant merchant : merchantMap.values()) {
                    String merchantCode = merchant.getCode();
                    String merchantName = merchant.getName();

                    // Create direct seller (owned by merchant)
                    Seller directSeller = new Seller();
                    directSeller.setCode(merchantCode + "_DIRECT");
                    directSeller.setName(merchantName + " Official");
                    directSeller.setDescription("Official seller for " + merchantName);
                    directSeller.setType("DIRECT");
                    directSeller.setStatus("ACTIVE");
                    // Set seller's merchantId instead of merchant object
                    directSeller.setMerchantId(merchant.getId());
                    directSeller.setCreatedBy("CSV_IMPORT");
                    directSeller.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode directMetadata = objectMapper.createObjectNode();
                    directMetadata.put("verified", true);
                    directMetadata.put("rating", 4.8);
                    directMetadata.put("reviewCount", 500);
                    directMetadata.put("isOfficial", true);
                    directMetadata.put("warrantyProvider", true);
                    directMetadata.put("prioritySupport", true);

                    // Add contact information
                    ObjectNode directContactInfo = objectMapper.createObjectNode();
                    directContactInfo.put("email", "support@" + merchantCode.toLowerCase() + ".com");
                    directContactInfo.put("phone", "+91-9876543210");

                    ObjectNode directAddress = objectMapper.createObjectNode();
                    directAddress.put("line1", "123 Business Park");
                    directAddress.put("line2", "Sector 5");
                    directAddress.put("city", "Mumbai");
                    directAddress.put("state", "Maharashtra");
                    directAddress.put("country", "India");
                    directAddress.put("pincode", "400001");

                    directContactInfo.set("address", directAddress);
                    directMetadata.set("contactInfo", directContactInfo);



                    // Save direct seller
                    directSeller = sellerRepository.save(directSeller);
                    sellerMap.put(directSeller.getCode(), directSeller);

                    // Create marketplace seller
                    Seller marketplaceSeller = new Seller();
                    marketplaceSeller.setCode(merchantCode + "_MARKETPLACE");
                    marketplaceSeller.setName(merchantName + " Marketplace");
                    marketplaceSeller.setDescription("Marketplace seller for " + merchantName);
                    marketplaceSeller.setType("MARKETPLACE");
                    marketplaceSeller.setStatus("ACTIVE");
                    // Set seller's merchantId instead of merchant object
                    marketplaceSeller.setMerchantId(merchant.getId());
                    marketplaceSeller.setCreatedBy("CSV_IMPORT");
                    marketplaceSeller.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode marketplaceMetadata = objectMapper.createObjectNode();
                    marketplaceMetadata.put("verified", true);
                    marketplaceMetadata.put("rating", 4.2);
                    marketplaceMetadata.put("reviewCount", 300);
                    marketplaceMetadata.put("commissionRate", 10.0);
                    marketplaceMetadata.put("returnPolicy", "30 days");
                    marketplaceMetadata.put("shippingTime", "2-3 business days");

                    // Add contact information
                    ObjectNode marketplaceContactInfo = objectMapper.createObjectNode();
                    marketplaceContactInfo.put("email", "marketplace@" + merchantCode.toLowerCase() + ".com");
                    marketplaceContactInfo.put("phone", "+91-9876543211");

                    ObjectNode marketplaceAddress = objectMapper.createObjectNode();
                    marketplaceAddress.put("line1", "456 Commerce Tower");
                    marketplaceAddress.put("line2", "Sector 10");
                    marketplaceAddress.put("city", "Mumbai");
                    marketplaceAddress.put("state", "Maharashtra");
                    marketplaceAddress.put("country", "India");
                    marketplaceAddress.put("pincode", "400002");

                    marketplaceContactInfo.set("address", marketplaceAddress);
                    marketplaceMetadata.set("contactInfo", marketplaceContactInfo);



                    // Save marketplace seller
                    marketplaceSeller = sellerRepository.save(marketplaceSeller);
                    sellerMap.put(marketplaceSeller.getCode(), marketplaceSeller);

                    // Create partner seller
                    Seller partnerSeller = new Seller();
                    partnerSeller.setCode(merchantCode + "_PARTNER");
                    partnerSeller.setName(merchantName + " Partner");
                    partnerSeller.setDescription("Partner seller for " + merchantName);
                    partnerSeller.setType("PARTNER");
                    partnerSeller.setStatus("ACTIVE");
                    partnerSeller.setMerchantId(merchant.getId());
                    partnerSeller.setCreatedBy("CSV_IMPORT");
                    partnerSeller.setCreatedDate(LocalDateTime.now());

                    // Add metadata
                    ObjectNode partnerMetadata = objectMapper.createObjectNode();
                    partnerMetadata.put("verified", true);
                    partnerMetadata.put("rating", 4.5);
                    partnerMetadata.put("reviewCount", 200);
                    partnerMetadata.put("partnershipLevel", "GOLD");
                    partnerMetadata.put("contractStartDate", LocalDate.now().minusMonths(6).toString());
                    partnerMetadata.put("contractEndDate", LocalDate.now().plusMonths(18).toString());

                    // Add contact information
                    ObjectNode partnerContactInfo = objectMapper.createObjectNode();
                    partnerContactInfo.put("email", "partner@" + merchantCode.toLowerCase() + ".com");
                    partnerContactInfo.put("phone", "+91-9876543212");

                    ObjectNode partnerAddress = objectMapper.createObjectNode();
                    partnerAddress.put("line1", "789 Partner Plaza");
                    partnerAddress.put("line2", "Sector 15");
                    partnerAddress.put("city", "Mumbai");
                    partnerAddress.put("state", "Maharashtra");
                    partnerAddress.put("country", "India");
                    partnerAddress.put("pincode", "400003");

                    partnerContactInfo.set("address", partnerAddress);
                    partnerMetadata.set("contactInfo", partnerContactInfo);


                    // Save partner seller
                    partnerSeller = sellerRepository.save(partnerSeller);
                    sellerMap.put(partnerSeller.getCode(), partnerSeller);
                }

                log.info("Successfully created {} sample sellers", sellerMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading sellers", e);
            throw new RuntimeException("Failed to load sellers: " + e.getMessage(), e);
        }

        return sellerMap;
    }

    /**
     * Loads stores from CSV file
     * @param merchantMap Map of merchant code to Merchant entity
     * @return Map of store code to Store entity
     */
    private Map<String, Store> loadStores(Map<String, Merchant> merchantMap) {
        Map<String, Store> storeMap = new HashMap<>();

        try {
            // Check if stores already exist
            List<Store> existingStores = storeRepository.findAll();
            if (!existingStores.isEmpty()) {
                log.info("Found {} existing stores, skipping store creation", existingStores.size());
                for (Store store : existingStores) {
                    // Use code instead of domain for Store mapping to match channel CSV
                    String storeCode = store.getCode();
                    storeMap.put(storeCode, store);
                    // Also add the domain as a key for backward compatibility
                    storeMap.put(store.getDomain(), store);
                }
                return storeMap;
            }

            // Load stores from CSV file
            Resource resource = new ClassPathResource("csv/stores.csv");
            if (resource.exists()) {
                log.info("Loading stores from CSV file");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    // Skip header
                    String line = reader.readLine();

                    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 7) {
                            log.warn("Invalid store data: {}", line);
                            continue;
                        }

                        String code = values[0];
                        String name = values[1];
                        String domain = values[2];
                        String locale = values[3];
                        String currency = values[4];
                        String description = values[5];
                        boolean active = Boolean.parseBoolean(values[6]);
                        String merchantCode = values[7];

                        // Get merchant
                        Merchant merchant = merchantMap.get(merchantCode);
                        if (merchant == null) {
                            log.warn("Unknown merchant code: {}, skipping store: {}", merchantCode, code);
                            continue;
                        }

                        // Create store
                        Store store = Store.builder()
                                // Store doesn't have a code method, using domain instead
                                .code(code)
                                .name(name)
                                .domain(domain)
                                .locale(locale)
                                .currency(currency)
                                .description(description)
                                .active(active)
                                .storeType(StoreType.ONLINE)
                                .status("ACTIVE")
                                .timezone("Asia/Kolkata")
                                .merchant(merchant)
                                .build();

                        // Set created_by and last_modified_by fields
                        store.setCreatedBy("system");
                        store.setLastModifiedBy("system");

                        // Create store metadata
                        ObjectNode metadata = objectMapper.createObjectNode();

                        // Store information
                        ObjectNode storeInfo = objectMapper.createObjectNode();
                        storeInfo.put("foundedYear", 2016);
                        storeInfo.put("type", "E-commerce");

                        // Store hours
                        ObjectNode storeHours = objectMapper.createObjectNode();
                        storeHours.put("monday", "09:00-21:00");
                        storeHours.put("tuesday", "09:00-21:00");
                        storeHours.put("wednesday", "09:00-21:00");
                        storeHours.put("thursday", "09:00-21:00");
                        storeHours.put("friday", "09:00-21:00");
                        storeHours.put("saturday", "10:00-22:00");
                        storeHours.put("sunday", "10:00-20:00");
                        storeInfo.set("hours", storeHours);

                        // Contact information
                        ObjectNode contactInfo = objectMapper.createObjectNode();
                        contactInfo.put("email", "support@" + domain);
                        contactInfo.put("phone", "+91-1800123456");

                        ObjectNode address = objectMapper.createObjectNode();
                        address.put("line1", "123 Commerce Street");
                        address.put("line2", "Tech Park");
                        address.put("city", "Mumbai");
                        address.put("state", "Maharashtra");
                        address.put("country", "India");
                        address.put("pincode", "400001");

                        contactInfo.set("address", address);

                        // Set metadata
                        metadata.set("storeInfo", storeInfo);
                        metadata.set("contactInfo", contactInfo);

                        // Convert ObjectNode to Map for Store metadata
                        Map<String, Object> metadataMap = objectMapper.convertValue(metadata, Map.class);
                        store.setMetadata(metadataMap);

                        // Save store
                        store = storeRepository.save(store);
                        // Store uses domain instead of code
                        storeMap.put(store.getCode(), store);
                    }

                    log.info("Successfully loaded {} stores from CSV", storeMap.size());
                }
            } else {
                log.info("Stores CSV file not found, creating sample stores");

                // Create sample stores for each merchant
                for (Merchant merchant : merchantMap.values()) {
                    String merchantCode = merchant.getCode();
                    String merchantName = merchant.getName();

                    // Create main store
                    String mainStoreCode = merchantCode + "_MAIN";
                    String mainStoreName = merchantName + " Main Store";
                    String mainStoreDomain = merchantCode.toLowerCase() + ".com";

                    Store mainStore = Store.builder()
                            // Store doesn't have code method, using domain for both code and domain
                            .domain(mainStoreCode) // Using domain for code
                            .name(mainStoreName)
                            .domain(mainStoreDomain)
                            .locale("en_IN")
                            .currency("INR")
                            .description("Main online store for " + merchantName)
                            .active(true)
                            .storeType(StoreType.ONLINE)
                            .status("ACTIVE")
                            .timezone("Asia/Kolkata")
                            .merchant(merchant)
                            .build();

                    // Set created_by and last_modified_by fields
                    mainStore.setCreatedBy("system");
                    mainStore.setLastModifiedBy("system");

                    // Create main store metadata
                    ObjectNode mainMetadata = objectMapper.createObjectNode();

                    // Store information
                    ObjectNode mainStoreInfo = objectMapper.createObjectNode();
                    mainStoreInfo.put("foundedYear", 2016);
                    mainStoreInfo.put("type", "E-commerce");

                    // Store hours
                    ObjectNode mainStoreHours = objectMapper.createObjectNode();
                    mainStoreHours.put("monday", "09:00-21:00");
                    mainStoreHours.put("tuesday", "09:00-21:00");
                    mainStoreHours.put("wednesday", "09:00-21:00");
                    mainStoreHours.put("thursday", "09:00-21:00");
                    mainStoreHours.put("friday", "09:00-21:00");
                    mainStoreHours.put("saturday", "10:00-22:00");
                    mainStoreHours.put("sunday", "10:00-20:00");
                    mainStoreInfo.set("hours", mainStoreHours);

                    // Contact information
                    ObjectNode mainContactInfo = objectMapper.createObjectNode();
                    mainContactInfo.put("email", "support@" + mainStoreDomain);
                    mainContactInfo.put("phone", "+91-1800123456");

                    ObjectNode mainAddress = objectMapper.createObjectNode();
                    mainAddress.put("line1", "123 Commerce Street");
                    mainAddress.put("line2", "Tech Park");
                    mainAddress.put("city", "Mumbai");
                    mainAddress.put("state", "Maharashtra");
                    mainAddress.put("country", "India");
                    mainAddress.put("pincode", "400001");

                    mainContactInfo.set("address", mainAddress);

                    // Set metadata
                    mainMetadata.set("storeInfo", mainStoreInfo);
                    mainMetadata.set("contactInfo", mainContactInfo);

                    // Convert ObjectNode to Map for Store metadata
                    Map<String, Object> mainMetadataMap = objectMapper.convertValue(mainMetadata, Map.class);
                    mainStore.setMetadata(mainMetadataMap);

                    // Save main store
                    mainStore = storeRepository.save(mainStore);
                    // Store uses domain instead of code
                    storeMap.put(mainStore.getDomain(), mainStore);

                    // Create specialty store if applicable
                    if (!merchantCode.equals("BB-001") && !merchantCode.equals("MG-001")) {
                        String specialtyStoreCode = merchantCode + "_SPECIALTY";
                        String specialtyStoreName = merchantName + " Specialty Store";
                        String specialtyStoreDomain = "specialty." + merchantCode.toLowerCase() + ".com";

                        Store specialtyStore = Store.builder()
                                // Store doesn't have code method, using domain instead
                                .domain(specialtyStoreCode)
                                .name(specialtyStoreName)
                                .domain(specialtyStoreDomain)
                                .locale("en_IN")
                                .currency("INR")
                                .description("Specialty store for " + merchantName)
                                .active(true)
                                .storeType(StoreType.ONLINE)
                                .status("ACTIVE")
                                .timezone("Asia/Kolkata")
                                .merchant(merchant)
                                .build();

                        // Create specialty store metadata (similar to main store)
                        ObjectNode specialtyMetadata = objectMapper.createObjectNode();
                        specialtyMetadata.setAll(mainMetadata);

                        // Update contact info for specialty store
                        ((ObjectNode) specialtyMetadata.get("contactInfo")).put("email", "support@" + specialtyStoreDomain);

                        // Convert ObjectNode to Map for Store metadata
                        Map<String, Object> specialtyMetadataMap = objectMapper.convertValue(specialtyMetadata, Map.class);
                        specialtyStore.setMetadata(specialtyMetadataMap);

                        // Save specialty store
                        specialtyStore = storeRepository.save(specialtyStore);
                        // Store uses domain instead of code
                        storeMap.put(specialtyStore.getDomain(), specialtyStore);
                    }
                }

                log.info("Successfully created {} sample stores", storeMap.size());
            }
        } catch (Exception e) {
            log.error("Error loading stores", e);
            throw new RuntimeException("Failed to load stores: " + e.getMessage(), e);
        }

        return storeMap;
    }

    /**
     * Creates prices and inventory for products
     * @param productMap Map of product code to Product entity
     * @param merchantMap Map of merchant code to Merchant entity
     * @param channelMap Map of channel code to Channel entity
     * @param sellerMap Map of seller code to Seller entity
     */
    private void createPricesAndInventory(Map<String, Product> productMap,
                                          Map<String, Merchant> merchantMap,
                                          Map<String, Channel> channelMap,
                                          Map<String, Seller> sellerMap) {
        log.info("Creating prices and inventory...");

        try {
            // Check if prices already exist
            List<ProductPrice> existingPrices = productPriceRepository.findAll();
            if (!existingPrices.isEmpty()) {
                log.info("Found {} existing prices, skipping price creation", existingPrices.size());
                return;
            }

            int priceCount = 0;
            int inventoryCount = 0;

            // Load prices and inventory from CSV file
            Resource resource = new ClassPathResource("csv/prices_inventory.csv");
            if (resource.exists()) {
                log.info("Loading prices and inventory from CSV file");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    // Skip header
                    String line = reader.readLine();

                    while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                        String[] values = parseCSVLine(line);

                        if (values.length < 13) {
                            log.warn("Invalid price/inventory data: {}", line);
                            continue;
                        }

                        String productCode = values[0];
                        String merchantCode = values[1];
                        String channelCode = values[2];
                        String sellerCode = values[3];
                        BigDecimal basePrice = new BigDecimal(values[4]);
                        BigDecimal salePrice = new BigDecimal(values[5]);
                        String currency = values[6];
                        // We don't need to parse dates as they're not used in the entity
                        int stockLevel = Integer.parseInt(values[9]);
                        int reserved = Integer.parseInt(values[12]);

                        // Get entities
                        Product product = productMap.get(productCode);
                        Merchant merchant = merchantMap.get(merchantCode);
                        Channel channel = channelMap.get(channelCode);
                        Seller seller = sellerMap.get(sellerCode);

                        if (product == null || merchant == null || channel == null || seller == null) {
                            log.warn("Missing entity reference for price/inventory: product={}, merchant={}, channel={}, seller={}",
                                    productCode, merchantCode, channelCode, sellerCode);
                            continue;
                        }

                        // Create price
                        ProductPrice price = ProductPriceBuilder.createPrice(product, merchant, channel, seller);
                        productPriceRepository.save(price);
                        priceCount++;

                        // Create inventory
                        ProductInventory inventory = ProductInventoryBuilder.createInventory(product, merchant, channel, seller);
                        productInventoryRepository.save(inventory);
                        inventoryCount++;
                    }

                    log.info("Successfully loaded {} prices and {} inventory records from CSV", priceCount, inventoryCount);
                }
            } else {
                log.info("Prices and inventory CSV file not found, creating sample data");

                // Create sample prices and inventory for each product
                for (Product product : productMap.values()) {
                    // Get product merchant
                    Merchant merchant = product.getMerchant();

                    if (merchant == null) {
                        log.warn("Product {} has no merchant, skipping price/inventory creation", product.getCode());
                        continue;
                    }

                    // Get channels and sellers for this merchant
                    // Channel doesn't have a direct merchant relationship, use store's merchant
                    List<Channel> channels = channelRepository.findAll().stream()
                            .filter(c -> c.getStore() != null && c.getStore().getMerchant() != null &&
                                    c.getStore().getMerchant().getId().equals(merchant.getId()))
                            .collect(Collectors.toList());
                    // Seller has merchantId, not direct merchant reference
                    List<Seller> sellers = sellerRepository.findAll().stream()
                            .filter(s -> s.getMerchantId() != null && s.getMerchantId().equals(merchant.getId()))
                            .collect(Collectors.toList());

                    if (channels.isEmpty() || sellers.isEmpty()) {
                        log.warn("Merchant {} has no channels or sellers, skipping price/inventory for product {}",
                                merchant.getCode(), product.getCode());
                        continue;
                    }

                    // Create price and inventory for each channel and seller
                    for (Channel channel : channels) {
                        for (Seller seller : sellers) {
                            try {
                                // Generate random price between 500 and 5000
                                BigDecimal price = new BigDecimal(500 + Math.random() * 4500)
                                        .setScale(2, RoundingMode.HALF_UP);

                                // Create price entity
                                ProductPrice productPrice = ProductPrice.builder()
                                        .product(product)
                                        .merchant(merchant)
                                        .channel(channel)
                                        .seller(seller)
                                        .price(price)
                                        .currency("INR")
                                        .isActive(true)
                                        .createdBy("SYSTEM")
                                        .build();

                                // Save price
                                productPriceRepository.save(productPrice);
                                priceCount++;

                                // Generate random stock level between 50 and 200
                                int stockLevel = 50 + (int)(Math.random() * 150);
                                int reserved = (int)(stockLevel * 0.05); // 5% reserved

                                // Create inventory
                                ProductInventory inventory = ProductInventory.builder()
                                        .product(product)
                                        .merchant(merchant)
                                        .channel(channel)
                                        .seller(seller)
                                        .quantity(stockLevel)
                                        .reservedQuantity(reserved)
                                        .createdBy("SYSTEM")
                                        .build();

                                // Save inventory
                                productInventoryRepository.save(inventory);
                                inventoryCount++;
                            } catch (Exception e) {
                                log.warn("Error creating price/inventory for product {}, channel {}, seller {}",
                                        product.getCode(), channel.getCode(), seller.getCode(), e);
                            }
                        }
                    }
                }

                log.info("Successfully created {} sample prices and {} inventory records", priceCount, inventoryCount);
            }
        } catch (Exception e) {
            log.error("Error creating prices and inventory", e);
            throw new RuntimeException("Failed to create prices and inventory: " + e.getMessage(), e);
        }
    }
}
