package com.scaler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.builder.*;
import com.scaler.builder.CategoryBuilder;
import com.scaler.entity.*;
import com.scaler.entity.enums.PlatformType;
import com.scaler.model.ValidationRule;
import com.scaler.repository.*;
import com.scaler.service.ProductMappingService;
import com.scaler.service.ValidationService;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for setting up demo data
 * This service has been refactored to disable automatic product data population
 * and implement a resilient CSV data loading mechanism
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DataSetupService {
    
    @Value("${app.data.load.from.csv:true}")
    private boolean loadDataFromCsv;
    
    // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Cache for entity lookups to improve performance - not used directly but kept for future use
    @SuppressWarnings("unused")
    private final Map<String, Object> entityCache = new ConcurrentHashMap<>();

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final CatalogRepository catalogRepository;
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final ChannelRepository channelRepository;
    private final PlatformRepository platformRepository;
    private final ChannelCatalogRepository channelCatalogRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryFeatureTemplateRepository categoryFeatureTemplateRepository;
    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRulesRepository validationRulesRepository;
    private final ValidationRuleFactory validationRuleFactory;
    private final CategoryValidationService categoryValidationService;
    @SuppressWarnings("unused")
    private final ProductMappingService productMappingService;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductFeatureMappingRepository productFeatureMappingRepository;
    private final ProductFeatureValueRepository productFeatureValueRepository;
    private final ProductPlatformRepository productPlatformRepository;
    private final SellerProductRepository sellerProductRepository;
    private final ProductFeatureValueMappingRepository productFeatureValueMappingRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductInventoryRepository productInventoryRepository;


    // Store map to cache loaded stores
    private Map<String, Store> storeMap;

    @Autowired
    private LoadDataFromCsvService loadDataFromCsvService;

    /**
     * Creates validation rules for a category
     *
     * @param category The category to create validation rules for
     */
    public void createValidationRulesForCategory(Category category) {
        log.info("Creating validation rules for category: {}", category.getName());

        // Create validation rules using the factory
        List<ValidationRule> rules = validationRuleFactory.createRulesForCategory(category);

        // Save the rules
        for (ValidationRule rule : rules) {
            rule.setCreatedBy("system"); // Set created_by field
            validationRuleRepository.save(rule);
        }

        // Create validation rules using the service
        List<ValidationRules> serviceRules = categoryValidationService.createValidationRulesForCategory(category);

        log.info("Created {} validation rules for category: {}", rules.size() + serviceRules.size(), category.getName());
    }

    /**
     * Sets up demo data for the catalog system
     * This method creates sample data for testing validation rules
     */
    @Transactional
    public void setup() {
        try {
            if (productFeatureMappingRepository.count() > 0) {
                // Data already exists, skip initialization
                log.info("Demo data already exists, skipping initialization");
                return;
            }

            log.info("Setting up demo data...");
            
            if (loadDataFromCsv) {
                // Load data from CSV files
                log.info("Loading data from CSV files as per configuration");
                loadDataFromCsvService.populateDataFromCsvFiles();
                return;
            }
            
            log.info("Creating hardcoded demo data as CSV loading is disabled");
            
            // Load merchants
            Map<String, Merchant> merchantMap = loadDataFromCsvService.loadMerchants();
            // Use the first merchant for sample data
            Merchant merchant = merchantMap.values().iterator().next();

            // Create sample catalogs
            Catalog catalog = createSampleCatalog(merchant);

            // Create sample categories with feature templates
            List<Category> categories = createSampleCategories(catalog, merchant);

            // Create validation rules for categories
            log.info("Creating validation rules for categories...");
            for (Category category : categories) {
                createValidationRulesForCategory(category);
            }

            // Create sample products with features
            createSampleProducts(categories, merchant, catalog);

            // This section is now controlled by the loadDataFromCsv flag
            // and will only execute when the flag is false
            
            // Create merchants with checks for existing ones
            Merchant tataCliq = merchantRepository.findByCode("TCLQ-001")
                    .orElseGet(() -> merchantRepository.save(MerchantBuilder.createTataCliqMerchant()));
            
            Merchant tata1mg = merchantRepository.findByCode("T1MG-001")
                    .orElseGet(() -> merchantRepository.save(MerchantBuilder.createTata1mgMerchant()));
            
            Merchant bigBasket = merchantRepository.findByCode("BBKT-001")
                    .orElseGet(() -> merchantRepository.save(MerchantBuilder.createBigBasketMerchant()));
            
            Merchant croma = merchantRepository.findByCode("CRMA-001")
                    .orElseGet(() -> merchantRepository.save(MerchantBuilder.createCromaMerchant()));
            
            Merchant tanishq = merchantRepository.findByCode("TNSH-001")
                    .orElseGet(() -> merchantRepository.save(MerchantBuilder.createTanishqMerchant()));

            // Create sellers first
            Seller tataCliqSeller = sellerRepository.save(SellerBuilder.createTataCliqSeller(tataCliq));
            Seller cromaSeller = sellerRepository.save(SellerBuilder.createCromaSeller(croma));
            Seller bigBasketSeller = sellerRepository.save(SellerBuilder.createBigBasketSeller(bigBasket));
            Seller tanishqSeller = sellerRepository.save(SellerBuilder.createTanishqSeller(tanishq));
            Seller tata1mgSeller = sellerRepository.save(SellerBuilder.createTata1mgSeller(tata1mg));

            // Create stores
            Store tataCliqStore = storeRepository.save(StoreBuilder.createTataCliqStore(tataCliq));
            @SuppressWarnings("unused")
            Store tata1mgStore = storeRepository.save(StoreBuilder.createTata1mgStore(tata1mg));
            Store bigBasketStore = storeRepository.save(StoreBuilder.createBigBasketStore(bigBasket));
            Store cromaStore = storeRepository.save(StoreBuilder.createCromaStore(croma));
            @SuppressWarnings("unused")
            Store tanishqStore = storeRepository.save(StoreBuilder.createTanishqStore(tanishq));

            // Create channels for each store
            // Tata CLiQ Channels
            Channel tataCliqEcom = channelRepository.save(ChannelBuilder.createTataCliqEcommerceChannel(tataCliqStore));
            Channel tataCliqMarketplace = channelRepository.save(ChannelBuilder.createTataCliqMarketplaceChannel(tataCliqStore));
            tataCliqStore.addChannel(tataCliqEcom);
            tataCliqStore.addChannel(tataCliqMarketplace);

            // Croma Channels (Hybrid - both online and offline)
            Channel cromaEcom = channelRepository.save(ChannelBuilder.createCromaEcommerceChannel(cromaStore));
            Channel cromaPhysical = channelRepository.save(ChannelBuilder.createCromaPhysicalStoreChannel(cromaStore));
            cromaStore.addChannel(cromaEcom);
            cromaStore.addChannel(cromaPhysical);

            // BigBasket Channels
            Channel bigBasketEcom = channelRepository.save(ChannelBuilder.createBigBasketEcommerceChannel(bigBasketStore));
            Channel bigBasketQuick = channelRepository.save(ChannelBuilder.createBigBasketQuickCommerceChannel(bigBasketStore));
            bigBasketStore.addChannel(bigBasketEcom);
            bigBasketStore.addChannel(bigBasketQuick);


            // Save the updated stores
            storeRepository.save(tataCliqStore);
            storeRepository.save(cromaStore);
            storeRepository.save(bigBasketStore);

            // Create platforms for TataCliq ecommerce channel
            platformRepository.save(PlatformBuilder.createMobileAppAndroid(tataCliqEcom, "CLIQ_ANDROID"));
            platformRepository.save(PlatformBuilder.createMobileAppIOS(tataCliqEcom,"CLIQ_IOS"));
            platformRepository.save(PlatformBuilder.createDesktopWeb(tataCliqEcom, "CLIQ_DESKTOP"));
            platformRepository.save(PlatformBuilder.createMobileWeb(tataCliqEcom,"CLIQ_MWEB"));
            platformRepository.save(PlatformBuilder.createKiosk(tataCliqEcom,"CLIQ_KIOSK"));

            // Create platforms for Croma ecommerce channel
            platformRepository.save(PlatformBuilder.createMobileAppAndroid(tataCliqEcom, "CROMA_ANDROID"));
            platformRepository.save(PlatformBuilder.createMobileAppIOS(tataCliqEcom,"CROMA_IOS"));
            platformRepository.save(PlatformBuilder.createDesktopWeb(tataCliqEcom, "CROMA_DESKTOP"));
            platformRepository.save(PlatformBuilder.createMobileWeb(tataCliqEcom,"CROMA_MWEB"));
            platformRepository.save(PlatformBuilder.createKiosk(tataCliqEcom,"CROMA_KIOSK"));

            // Create platforms for BigBasket ecommerce channel
            platformRepository.save(PlatformBuilder.createMobileAppAndroid(tataCliqEcom, "BIGBASKET_ANDROID"));
            platformRepository.save(PlatformBuilder.createMobileAppIOS(tataCliqEcom,"BIGBASKET_IOS"));
            platformRepository.save(PlatformBuilder.createDesktopWeb(tataCliqEcom, "BIGBASKET_DESKTOP"));
            platformRepository.save(PlatformBuilder.createMobileWeb(tataCliqEcom,"BIGBASKET_MWEB"));
            platformRepository.save(PlatformBuilder.createKiosk(tataCliqEcom,"BIGBASKET_KIOSK"));



            // Create catalogs
            Catalog fashionCatalog = catalogRepository.save(CatalogBuilder.createFashionCatalog(tataCliq));
            Catalog groceryCatalog = catalogRepository.save(CatalogBuilder.createGroceryCatalog(bigBasket));
            Catalog pharmaCatalog = catalogRepository.save(CatalogBuilder.createPharmaCatalog(tata1mg));
            Catalog cromaCatalog = catalogRepository.save(CatalogBuilder.createElectronicsCatalog(croma));
            Catalog jewelryCatalog = catalogRepository.save(CatalogBuilder.createJewelryCatalog(tanishq));

            // Create channel-catalog relationships
            // TataCliq channels
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tataCliqEcom, fashionCatalog, true));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tataCliqMarketplace, fashionCatalog, true));

            // Croma channels
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(cromaEcom, cromaCatalog, true));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(cromaPhysical, cromaCatalog, true));

            // BigBasket channels
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(bigBasketEcom, groceryCatalog, true));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(bigBasketQuick, groceryCatalog, true));

            // Create categories
            Category laptopCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createLaptopCategory(fashionCatalog, tataCliq));
            Category smartphoneCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createSmartphoneCategory(fashionCatalog, tataCliq));
            Category necklaceCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createGoldNecklaceCategory(jewelryCatalog, tanishq));
            Category bangleCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createGoldBangleCategory(jewelryCatalog, tanishq));
            @SuppressWarnings("unused")
            Category freshProduceCategory = categoryRepository.save(com.scaler.builder.CategoryBuilder.createFreshProduceCategory(groceryCatalog, bigBasket));
            @SuppressWarnings("unused")
            Category medicinesCategory = categoryRepository.save(CategoryBuilder.createMedicinesCategory(pharmaCatalog, tata1mg));

            // Create units of measure
            UnitOfMeasure gbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createGBUnit());
            UnitOfMeasure tbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createTBUnit());
            @SuppressWarnings("unused")
            UnitOfMeasure kgUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createKGUnit());
            @SuppressWarnings("unused")
            UnitOfMeasure pcsUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createPiecesUnit());
            UnitOfMeasure gramUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createGramUnit());

            // Create feature templates for laptops
            CategoryFeatureTemplate laptopProcessor = FeatureTemplateBuilder.createLaptopProcessorTemplate(laptopCategory);
            CategoryFeatureTemplate laptopRam = FeatureTemplateBuilder.createLaptopRamTemplate(laptopCategory, gbUnit);
            CategoryFeatureTemplate laptopStorage = FeatureTemplateBuilder.createLaptopStorageTemplate(laptopCategory, tbUnit);

            // Create feature templates for iPhone
            CategoryFeatureTemplate iPhoneProcessor = FeatureTemplateBuilder.createIPhoneProcessorTemplate(laptopCategory);
            CategoryFeatureTemplate iPhoneRam = FeatureTemplateBuilder.createIPhoneRamTemplate(laptopCategory, gbUnit);
            CategoryFeatureTemplate iPhoneStorage = FeatureTemplateBuilder.createIPhoneStorageTemplate(laptopCategory, tbUnit);

            // Create feature templates for Jewellery
            CategoryFeatureTemplate goldPurityProductFeature = FeatureTemplateBuilder.createGoldPurityTemplate(necklaceCategory);
            CategoryFeatureTemplate goldWeightProductFeature = FeatureTemplateBuilder.createGoldWeightTemplate(bangleCategory, gramUnit);


            categoryFeatureTemplateRepository.save(laptopProcessor);
            categoryFeatureTemplateRepository.save(laptopRam);
            categoryFeatureTemplateRepository.save(laptopStorage);

            categoryFeatureTemplateRepository.save(iPhoneProcessor);
            categoryFeatureTemplateRepository.save(iPhoneRam);
            categoryFeatureTemplateRepository.save(iPhoneStorage);


            categoryFeatureTemplateRepository.save(goldPurityProductFeature);
            categoryFeatureTemplateRepository.save(goldWeightProductFeature);

            // Create product features
            ProductFeature processor = ProductFeatureBuilder.createLaptopProcessorFeature(laptopProcessor);
            ProductFeature ram = ProductFeatureBuilder.createLaptopRamFeature(laptopRam, gbUnit);
            ProductFeature storage = ProductFeatureBuilder.createLaptopStorageFeature(laptopStorage, tbUnit);

            // Create product features
            ProductFeature iPhoneprocessor = ProductFeatureBuilder.createProcessorFeature(iPhoneProcessor);
            ProductFeature iPhoneram = ProductFeatureBuilder.createLaptopRamFeature(laptopRam, gbUnit);
            ProductFeature iPhonestorage = ProductFeatureBuilder.createLaptopStorageFeature(laptopStorage, tbUnit);


            productFeatureRepository.save(processor);
            productFeatureRepository.save(ram);
            productFeatureRepository.save(storage);

            productFeatureRepository.save(iPhoneprocessor);
            productFeatureRepository.save(iPhoneram);
            productFeatureRepository.save(iPhonestorage);


            // Create products
            Product macBookPro = productRepository.save(ProductBuilder.createMacBookPro(laptopCategory, tataCliq));
            Product dellXPS = productRepository.save(ProductBuilder.createDellXPS(laptopCategory, tataCliq));
            Product iPhone = productRepository.save(ProductBuilder.createIPhone(smartphoneCategory, tataCliq));
            Product goldNecklace = productRepository.save(ProductBuilder.createGoldNecklace(necklaceCategory, tanishq));
            Product goldBangles = productRepository.save(ProductBuilder.createGoldBangles(bangleCategory, tanishq));

            // Create feature mappings and values for MacBook Pro
            ProductFeatureMapping macBookProcessor = createAndSaveMapping(macBookPro, processor);
            ProductFeatureMapping macBookRam = createAndSaveMapping(macBookPro, ram);
            ProductFeatureMapping macBookStorage = createAndSaveMapping(macBookPro, iPhonestorage);

            ProductFeatureValue processorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(processor, "Apple M2 Pro"));
            ProductFeatureValue ramValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(ram, "16GB"));
            ProductFeatureValue storageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(iPhonestorage, "512GB"));



            // Create feature mappings and values for Dell XPS
            ProductFeatureMapping dellProcessor = createAndSaveMapping(dellXPS, processor);
            ProductFeatureMapping dellRam = createAndSaveMapping(dellXPS, ram);
            ProductFeatureMapping dellStorage = createAndSaveMapping(dellXPS, iPhonestorage);

            ProductFeatureValue dellProcessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(processor, "Intel i9-13900H"));
            ProductFeatureValue dellRamValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(ram, "32GB"));
            ProductFeatureValue dellStorageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(iPhonestorage, "1TB"));




            // Create product-channel relationships
            // MacBook Pro available on Croma (both online and offline)
            macBookPro.addProductChannel(cromaEcom);
            macBookPro.addProductChannel(cromaPhysical);

            // Dell XPS available on TataCliQ (both ecommerce and marketplace)
            dellXPS.addProductChannel(tataCliqEcom);
            dellXPS.addProductChannel(tataCliqMarketplace);

            // iPhone available on both Croma and TataCliQ
            iPhone.addProductChannel(cromaEcom);
            iPhone.addProductChannel(cromaPhysical);
            iPhone.addProductChannel(tataCliqEcom);
            iPhone.addProductChannel(tataCliqMarketplace);

            // Jewelry available in physical stores with online visibility
            goldNecklace.addProductChannel(tataCliqEcom);
            goldNecklace.addProductChannel(cromaPhysical);
            goldBangles.addProductChannel(tataCliqEcom);
            goldBangles.addProductChannel(cromaPhysical);


            // Create features for jewelry
            ProductFeature goldPurity = productFeatureRepository.save(ProductFeatureBuilder.createGoldPurityFeature(goldPurityProductFeature));
            ProductFeature goldWeight = productFeatureRepository.save(ProductFeatureBuilder.createGoldWeightFeature(goldWeightProductFeature, gramUnit));

            // Create feature mappings and values for Gold Necklace
            ProductFeatureMapping necklacePurity = createAndSaveMapping(goldNecklace, goldPurity);
            ProductFeatureMapping necklaceWeight = createAndSaveMapping(goldNecklace, goldWeight);

            ProductFeatureValue purityValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldPurity, "22K"));
            ProductFeatureValue weightValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldWeight, "50"));

            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookProcessor, processorValue, laptopCategory,1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookRam, ramValue, laptopCategory,2, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookStorage, storageValue, laptopCategory,3, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellProcessor, dellProcessorValue, laptopCategory,1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellRam, dellRamValue, laptopCategory,2, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellStorage, dellStorageValue, laptopCategory,3, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(necklacePurity, purityValue, necklaceCategory,1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(necklaceWeight, weightValue, necklaceCategory,2, true));

            // Create feature mappings and values for Gold Bangles
            @SuppressWarnings("unused")
            ProductFeatureMapping banglePurity = createAndSaveMapping(goldBangles, goldPurity);
            ProductFeatureMapping bangleWeight = createAndSaveMapping(goldBangles, goldWeight);

            // Reuse the same purity value for bangles
            //productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(banglePurity, purityValue, bangleCategory,1, true));

            // Create new weight value for bangles
            ProductFeatureValue bangleWeightValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldWeight, "30"));
            //productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(bangleWeight, bangleWeightValue, bangleCategory,2, true));

            // Save all products with their channel relationships
            productRepository.save(macBookPro);
            productRepository.save(dellXPS);
            productRepository.save(iPhone);
            productRepository.save(goldNecklace);
            productRepository.save(goldBangles);



            // Create sample product prices and inventory
            createSamplePricesAndInventory(tataCliq, tataCliqEcom, tataCliqSeller);
            createSamplePricesAndInventory(croma, cromaEcom, cromaSeller);
            createSamplePricesAndInventory(bigBasket, bigBasketEcom, bigBasketSeller);
            createSamplePricesAndInventory(tanishq, tataCliqEcom, tanishqSeller);
            createSamplePricesAndInventory(tata1mg, tataCliqEcom, tata1mgSeller);

            // Create seller-product associations for various combinations

            // 1. Channel-Seller-Category combination
            // MacBook Pro: Available through TataCliq seller on TataCliq channels in Laptop category
            SellerProduct macProTataCliq = SellerProductBuilder.createSellerProduct(tataCliqSeller, macBookPro, tataCliq);
            sellerProductRepository.save(macProTataCliq);

            // 2. Channel-Seller-Category combination
            // Dell XPS: Available through Croma seller on Croma channels in Laptop category
            SellerProduct dellXPSCroma = SellerProductBuilder.createSellerProduct(cromaSeller, dellXPS, croma);
            sellerProductRepository.save(dellXPSCroma);

            // 3. Seller-Category combination (across channels)
            // iPhone: Available through both TataCliq and Croma sellers in Smartphone category
            SellerProduct iPhoneTataCliq = SellerProductBuilder.createSellerProduct(tataCliqSeller, iPhone, tataCliq);
            SellerProduct iPhoneCroma = SellerProductBuilder.createSellerProduct(cromaSeller, iPhone, croma);
            sellerProductRepository.save(iPhoneTataCliq);
            sellerProductRepository.save(iPhoneCroma);

            //useCases(macBookPro, laptopCategory, croma, bigBasketEcom, ram, tataCliqSeller, tanishq, tataCliqMarketplace, iPhone, smartphoneCategory, bigBasket, iPhoneram, cromaSeller, iPhoneTataCliq, tanishqSeller, goldNecklace, tataCliq, goldBangles);

            // Create platforms
            Platform webPlatform = Platform.builder()
                    .name("Web")
                    .code("WEB")
                    .channel(bigBasketEcom)
                    .type(PlatformType.DESKTOP_WEB)
                    .description("Web Platform")
                    .createdBy("SYSTEM")
                    .build();
            platformRepository.save(webPlatform);

            Platform mobilePlatform = Platform.builder()
                    .name("Mobile")
                    .code("MOBILE")
                    .type(PlatformType.MOBILE_APP_IOS)
                    .channel(bigBasketEcom)
                    .description("Mobile Platform")
                    .createdBy("SYSTEM")
                    .build();
            platformRepository.save(mobilePlatform);

            Platform tabletPlatform = Platform.builder()
                    .name("Tablet")
                    .code("TABLET")
                    .type(PlatformType.MOBILE_APP_ANDROID)
                    .channel(bigBasketEcom)
                    .description("Tablet Platform")
                    .createdBy("SYSTEM")
                    .build();
            platformRepository.save(tabletPlatform);

            // Create product-platform mappings
            // MacBook Pro available on Web and Tablet
            ProductPlatform macBookWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(macBookPro, webPlatform);
            productPlatformRepository.save(macBookWebPlatform);

            ProductPlatform macBookTabletPlatform = ProductPlatformBuilder.createActiveProductPlatform(macBookPro, tabletPlatform);
            productPlatformRepository.save(macBookTabletPlatform);

            // iPhone available on all platforms
            ProductPlatform iPhoneWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(iPhone, webPlatform);
            productPlatformRepository.save(iPhoneWebPlatform);

            ProductPlatform iPhoneMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(iPhone, mobilePlatform);
            productPlatformRepository.save(iPhoneMobilePlatform);

            ProductPlatform iPhoneTabletPlatform = ProductPlatformBuilder.createActiveProductPlatform(iPhone, tabletPlatform);
            productPlatformRepository.save(iPhoneTabletPlatform);

            // Gold necklace available on Web and Mobile
            ProductPlatform necklaceWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(goldNecklace, webPlatform);
            productPlatformRepository.save(necklaceWebPlatform);

            ProductPlatform necklaceMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(goldNecklace, mobilePlatform);
            productPlatformRepository.save(necklaceMobilePlatform);

            //validationBuilder.validationRules();

            log.info("Demo data setup completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize demo data", e);
            throw new RuntimeException("Failed to initialize demo data: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a sample merchant for testing
     *
     * @return The created merchant
     */
    private Merchant createSampleMerchant() {
        log.info("Creating sample merchant");

        Merchant merchant = new Merchant();
        merchant.setCode("SAMPLE_MERCHANT");
        merchant.setName("Sample Merchant");
        merchant.setDescription("A sample merchant for testing");
        merchant.setStatus("ACTIVE");
        merchant.setContactEmail("sample@example.com");
        merchant.setCreatedBy("system");
        merchant.setCreatedDate(LocalDateTime.now());

        return merchantRepository.save(merchant);
    }

    /**
     * Creates a sample catalog for testing
     *
     * @param merchant The merchant to associate with the catalog
     * @return The created catalog
     */
    private Catalog createSampleCatalog(Merchant merchant) {
        log.info("Creating sample catalog");

        Catalog catalog = new Catalog();
        catalog.setCode("SAMPLE_CATALOG");
        catalog.setName("Sample Catalog");
        catalog.setDescription("A sample catalog for testing");
        catalog.setStatus("ACTIVE");
        catalog.setType("PRODUCT");
        catalog.setBusiness(merchant); // Assuming this is the correct method
        catalog.setCreatedBy("system");
        catalog.setCreatedDate(LocalDateTime.now());

        // Save the catalog to the database before returning it
        // This ensures it's not a transient entity when referenced by other entities
        return catalogRepository.save(catalog);
    }

    /**
     * Creates sample categories with feature templates for testing
     *
     * @param catalog  The catalog to associate with the categories
     * @param merchant The merchant to associate with the categories
     * @return The list of created categories
     */
    private List<Category> createSampleCategories(Catalog catalog, Merchant merchant) {
        log.info("Creating sample categories with feature templates");

        List<Category> categories = new ArrayList<>();

        // Create a parent category
        Category electronics = new Category();
        electronics.setCode("ELECTRONICS");
        electronics.setName("Electronics");
        electronics.setDescription("Electronic products");
        electronics.setCatalog(catalog);
        electronics.setMerchant(merchant); // Using setMerchant instead of setBusiness
        electronics.setCreatedBy("system");
        electronics.setCreatedDate(LocalDateTime.now());
        electronics = categoryRepository.save(electronics);
        categories.add(electronics);

        // Create feature templates for electronics
        createFeatureTemplateForCategory(electronics, "BRAND", "Brand", true, null, null, "Samsung,Apple,Sony,LG");
        createFeatureTemplateForCategory(electronics, "MODEL", "Model", true, null, null, null);
        createFeatureTemplateForCategory(electronics, "PRICE", "Price", true, "0", "10000", null);

        // Create a child category
        Category smartphones = new Category();
        smartphones.setCode("SMARTPHONES");
        smartphones.setName("Smartphones");
        smartphones.setDescription("Smartphone products");
        smartphones.setCatalog(catalog);
        smartphones.setMerchant(merchant); // Using setMerchant instead of setBusiness
        smartphones.setParent(electronics);
        smartphones.setCreatedBy("system");
        smartphones.setCreatedDate(LocalDateTime.now());
        smartphones = categoryRepository.save(smartphones);
        categories.add(smartphones);

        // Create feature templates for smartphones
        createFeatureTemplateForCategory(smartphones, "OS", "Operating System", true, null, null, "Android,iOS");
        createFeatureTemplateForCategory(smartphones, "SCREEN_SIZE", "Screen Size", true, "4", "7", null);
        createFeatureTemplateForCategory(smartphones, "CAMERA_MP", "Camera Megapixels", false, "8", "108", null);
        createFeatureTemplateForCategory(smartphones, "STORAGE_GB", "Storage (GB)", true, "16", "1024", "16,32,64,128,256,512,1024");

        return categories;
    }

    /**
     * Creates a feature template for a category
     *
     * @param category      The category to associate with the template
     * @param code          The template code
     * @param name          The template name
     * @param mandatory     Whether the feature is mandatory
     * @param minValue      The minimum value (for range validation)
     * @param maxValue      The maximum value (for range validation)
     * @param allowedValues The allowed values (for enumeration validation)
     * @return The created feature template
     */
    private CategoryFeatureTemplate createFeatureTemplateForCategory(
            Category category, String code, String name, boolean mandatory,
            String minValue, String maxValue, String allowedValues) {

        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
        template.setCode(code);
        template.setName(name);
        template.setMandatory(mandatory);
        template.setCategory(category);
        template.setMinValue(minValue);
        template.setMaxValue(maxValue);
        template.setAllowedValues(allowedValues);
        template.setAttributeType("STRING");
        template.setCreatedBy("system");

        // Add the template to the category
        if (category.getTemplates() == null) {
            category.setTemplates(new java.util.HashSet<>());
        }
        category.getTemplates().add(template);

        return template;
    }

    /**
     * Creates sample products with features for testing
     *
     * @param categories The categories to associate with the products
     * @param merchant   The merchant to associate with the products
     * @param catalog    The catalog to associate with the products
     */
    private void createSampleProducts(List<Category> categories, Merchant merchant, Catalog catalog) {
        log.info("Creating sample products with features");

        // Find the smartphones category
        Category smartphones = categories.stream()
                .filter(c -> c.getCode().equals("SMARTPHONES"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Smartphones category not found"));

        // Create a sample product
        Product iphone = new Product();
        iphone.setCode("IPHONE_13");
        iphone.setName("iPhone 13");
        iphone.setDescription("Apple iPhone 13");
        iphone.setStatus("ACTIVE");
        iphone.setProductType(ProductType.SIMPLE);
        iphone.setMerchant(merchant);
        iphone.setCatalog(catalog);
        iphone.setCreatedBy("system");
        iphone.setCreatedDate(LocalDateTime.now());

        // Save the product
        iphone = productRepository.save(iphone);

        // Associate the product with the smartphones category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProduct(iphone);
        productCategory.setCategory(smartphones);
        productCategory.setIsPrimary(true);
        productCategory.setMerchant(merchant); // Add merchant to the product category
        productCategory.setCreatedBy("system");
        productCategory.setCreatedDate(LocalDateTime.now());
        productCategoryRepository.save(productCategory);

        // Create features for the product based on the category templates
        for (CategoryFeatureTemplate template : smartphones.getTemplates()) {
            ProductFeature feature = new ProductFeature();
            feature.setCode(template.getCode());
            feature.setName(template.getName());
            feature.setTemplate(template);
            feature.setCreatedBy("system");
            feature.setCreatedDate(LocalDateTime.now());

            // Set feature values based on the template
            switch (template.getCode()) {
                case "BRAND":
                    feature.setDefaultValue("Apple");
                    break;
                case "MODEL":
                    feature.setDefaultValue("iPhone 13");
                    break;
                case "PRICE":
                    feature.setDefaultValue("999");
                    break;
                case "OS":
                    feature.setDefaultValue("iOS");
                    break;
                case "SCREEN_SIZE":
                    feature.setDefaultValue("6.1");
                    break;
                case "CAMERA_MP":
                    feature.setDefaultValue("12");
                    break;
                case "STORAGE_GB":
                    feature.setDefaultValue("128");
                    break;
                default:
                    break;
            }

            // Save the feature
            feature = productFeatureRepository.save(feature);

            // Create a mapping between the product and the feature
            ProductFeatureMapping mapping = new ProductFeatureMapping();
            mapping.setProduct(iphone);
            mapping.setFeature(feature);
            mapping.setCreatedBy("system");
            mapping.setCreatedDate(LocalDateTime.now());
            productFeatureMappingRepository.save(mapping);
        }
    }

    /**
     * Example method demonstrating how to use the validation rules
     * This method would be moved to an appropriate service
     *
     * @param productFeatureValue The product feature value to validate
     * @param validationService   The validation service
     * @return List of validation error messages
     */
    public List<String> validateFeatureValueExample(
            ProductFeatureValue productFeatureValue,
            ValidationService validationService) {

        // Get validation rules for the feature from the database
        List<ValidationRules> dbRules = validationRulesRepository.findByTemplateId(
                productFeatureValue.getProductFeature().getTemplate().getId());

        // Convert database rules to validation rule models
        List<ValidationRule> validationRules = new java.util.ArrayList<>();
        for (ValidationRules dbRule : dbRules) {
            validationRules.add(validationService.convertToValidationRule(dbRule));
        }

        // Add a required rule if needed
        if (productFeatureValue.getProductFeature().getTemplate().isMandatory()) {
            validationRules.add(ValidationRule.createRequiredRule(
                    "REQUIRED_" + productFeatureValue.getProductFeature().getCode(),
                    "Required " + productFeatureValue.getProductFeature().getName()
            ));
        }

        // Validate using the rules engine
        return validationService.validateProductFeatureValue(productFeatureValue, validationRules);
    }

    private ProductFeatureMapping createAndSaveMapping(Product product, ProductFeature feature) {
        if (product == null || feature == null) {
            throw new IllegalArgumentException("Product and feature must not be null");
        }
        return productFeatureMappingRepository.save(
                ProductFeatureMapping.builder()
                        .product(product)
                        .feature(feature)
                        .createdBy("SYSTEM")
                        .build()
        );
    }

    /**
     *
     * @param merchant
     * @param channel
     * @param seller
     */
    private void createSamplePricesAndInventory(Merchant merchant, Channel channel, Seller seller) {
        // Get all products for the merchant
        List<Product> products = productRepository.findByMerchantId(merchant.getId());

        for (Product product : products) {
            // Create price for each product using builder
            ProductPrice price = ProductPriceBuilder.createPrice(product, merchant, channel, seller);
            productPriceRepository.save(price);

            // Create inventory for each product using builder
            ProductInventory inventory = ProductInventoryBuilder.createInventory(product, merchant, channel, seller);
            productInventoryRepository.save(inventory);
        }
    }
    

}
