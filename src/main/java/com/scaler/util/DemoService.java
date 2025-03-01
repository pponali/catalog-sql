package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.builder.*;
import com.scaler.entity.*;
import com.scaler.entity.enums.PlatformType;
import com.scaler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DemoService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private ChannelCatalogRepository channelCatalogRepository;

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

    @Autowired
    private ProductFeatureValueMappingRepository productFeatureValueMappingRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    private ProductFeatureMappingRepository productFeatureMappingRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ProductChannelRepository productChannelRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;
    @Autowired
    private SellerProductRepository sellerProductRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private ValidationRuleRepository validationRuleRepository;

    @Autowired
    private ValidationRulesRepository validationRulesRepository;

    @Autowired
    ValidationBuilder validationBuilder;

    @Autowired
    private ProductPlatformRepository productPlatformRepository;



    @Transactional
    public void setup() {
        try {
            if (merchantRepository.count() > 0) {
                // Data already exists, skip initialization
                return;
            }


            // Create merchants
            Merchant tataCliq = merchantRepository.save(MerchantBuilder.createTataCliqMerchant());
            Merchant tata1mg = merchantRepository.save(MerchantBuilder.createTata1mgMerchant());
            Merchant bigBasket = merchantRepository.save(MerchantBuilder.createBigBasketMerchant());
            Merchant croma = merchantRepository.save(MerchantBuilder.createCromaMerchant());
            Merchant tanishq = merchantRepository.save(MerchantBuilder.createTanishqMerchant());

            // Create sellers first
            Seller tataCliqSeller = sellerRepository.save(SellerBuilder.createTataCliqSeller(tataCliq));
            Seller cromaSeller = sellerRepository.save(SellerBuilder.createCromaSeller(croma));
            Seller bigBasketSeller = sellerRepository.save(SellerBuilder.createBigBasketSeller(bigBasket));
            Seller tanishqSeller = sellerRepository.save(SellerBuilder.createTanishqSeller(tanishq));
            Seller tata1mgSeller = sellerRepository.save(SellerBuilder.createTata1mgSeller(tata1mg));

            // Create stores
            Store tataCliqStore = storeRepository.save(StoreBuilder.createTataCliqStore(tataCliq));
            Store tata1mgStore = storeRepository.save(StoreBuilder.createTata1mgStore(tata1mg));
            Store bigBasketStore = storeRepository.save(StoreBuilder.createBigBasketStore(bigBasket));
            Store cromaStore = storeRepository.save(StoreBuilder.createCromaStore(croma));
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
            Category laptopCategory = categoryRepository.save(CategoryBuilder.createLaptopCategory(fashionCatalog, tataCliq));
            Category smartphoneCategory = categoryRepository.save(CategoryBuilder.createSmartphoneCategory(fashionCatalog, tataCliq));
            Category necklaceCategory = categoryRepository.save(CategoryBuilder.createGoldNecklaceCategory(jewelryCatalog, tanishq));
            Category bangleCategory = categoryRepository.save(CategoryBuilder.createGoldBangleCategory(jewelryCatalog, tanishq));
            Category freshProduceCategory = categoryRepository.save(CategoryBuilder.createFreshProduceCategory(groceryCatalog, bigBasket));
            Category medicinesCategory = categoryRepository.save(CategoryBuilder.createMedicinesCategory(pharmaCatalog, tata1mg));

            // Create units of measure
            UnitOfMeasure gbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createGBUnit());
            UnitOfMeasure tbUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createTBUnit());
            UnitOfMeasure kgUnit = unitOfMeasureRepository.save(UnitOfMeasureBuilder.createKGUnit());
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
            CategoryFeatureTemplate goldWeightProductFeature = FeatureTemplateBuilder.createGoldWeightTemplate(bangleCategory, gbUnit);


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


        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize demo data: " + e.getMessage(), e);
        }
    }

    /**
     *
     * @param macBookPro
     * @param laptopCategory
     * @param croma
     * @param bigBasketEcom
     * @param ram
     * @param tataCliqSeller
     * @param tanishq
     * @param tataCliqMarketplace
     * @param iPhone
     * @param smartphoneCategory
     * @param bigBasket
     * @param iPhoneram
     * @param cromaSeller
     * @param iPhoneTataCliq
     * @param tanishqSeller
     * @param goldNecklace
     * @param tataCliq
     * @param goldBangles
     * @throws JsonProcessingException
     */
    private void useCases(Product macBookPro, Category laptopCategory, Merchant croma, Channel bigBasketEcom, ProductFeature ram, Seller tataCliqSeller, Merchant tanishq, Channel tataCliqMarketplace, Product iPhone, Category smartphoneCategory, Merchant bigBasket, ProductFeature iPhoneram, Seller cromaSeller, SellerProduct iPhoneTataCliq, Seller tanishqSeller, Product goldNecklace, Merchant tataCliq, Product goldBangles) throws JsonProcessingException {
        // Create comprehensive combinations of relationships

        // 1. Channel-Catalog-Product Combinations
        // Assign MacBook Pro to TataCliq Fashion Catalog
        //ChannelCatalog macBookChannelCatalog = ChannelCatalogBuilder.createChannelCatalog(tataCliqEcom, fashionCatalog, false);
        //channelCatalogRepository.save(macBookChannelCatalog);

        // 2. Product-Category-Channel Combinations
        // Assign MacBook Pro to Laptop category in TataCliq channel
        //ProductCategory macBookCategory = ProductCategoryBuilder.createProductCategory(macBookPro, laptopCategory, croma);
        //productCategoryRepository.save(macBookCategory);
        //ProductChannel macBookChannel = ProductChannelBuilder.createEcommerceProductChannel(macBookPro, bigBasketEcom);
        //productChannelRepository.save(macBookChannel);

        // 3. Product Feature Combinations
        // Create feature mapping for MacBook Pro RAM
        //ProductFeatureMapping macBookRamFeature = ProductFeatureMappingBuilder.createFeatureMapping(macBookPro, ram);
        //productFeatureMappingRepository.save(macBookRamFeature);

        // Create feature value for 32GB RAM
        //ProductFeatureValue ram32GB = ProductFeatureValueBuilder.createRamValue(ram, "32GB");
        //productFeatureValueRepository.save(ram32GB);

        // Map the 32GB RAM value to MacBook Pro
        //ProductFeatureValueMapping ramMapping = ProductFeatureValueMappingBuilder.createMapping(macBookRamFeature, ram32GB, laptopCategory, 2, false);
        //productFeatureValueMappingRepository.save(ramMapping);

        // 4. Seller-Product-Channel Combinations
        // Create seller product for MacBook Pro
        //SellerProduct macBookSellerProduct = SellerProductBuilder.createSellerProduct(tataCliqSeller, macBookPro, tanishq);
        //sellerProductRepository.save(macBookSellerProduct);

        // 5. Cross-Channel Product Availability
        // Make MacBook Pro available on Croma channel as well
        //ProductChannel macBookCromaChannel = ProductChannelBuilder.createMarketplaceProductChannel(macBookPro, tataCliqMarketplace);
        //productChannelRepository.save(macBookCromaChannel);

        // 6. Multiple Feature Values for a Product
        // Add another RAM option for MacBook Pro
        //ProductFeatureValue ram64GB = ProductFeatureValueBuilder.createRamValue(ram, "64GB");
        //productFeatureValueRepository.save(ram64GB);

        //ProductFeatureValueMapping ram64GBMapping = ProductFeatureValueMappingBuilder.createMapping(macBookRamFeature, ram64GB, laptopCategory, 2, false);
        //productFeatureValueMappingRepository.save(ram64GBMapping);

        // 7. Cross-Category Product
        // Add iPhone to both Smartphone and Electronics categories
        //ProductCategory iPhoneSmartphoneCategory = ProductCategoryBuilder.createProductCategory(iPhone, smartphoneCategory, bigBasket);
        //productCategoryRepository.save(iPhoneSmartphoneCategory);

        // 8. Channel-Specific Product Features
        // Create channel-specific RAM feature for TataCliq
        //ProductFeatureMapping iPhoneRamFeature = ProductFeatureMappingBuilder.createFeatureMapping(iPhone,iPhoneram);
        //productFeatureMappingRepository.save(iPhoneRamFeature);

        //ProductFeatureValue iPhoneRam8GB = ProductFeatureValueBuilder.createIphoneFeature(iPhoneram, "8GB");
        //productFeatureValueRepository.save(iPhoneRam8GB);

        //ProductFeatureValueMapping iPhoneRamMapping = ProductFeatureValueMappingBuilder.createMapping(iPhoneRamFeature, iPhoneRam8GB, smartphoneCategory, 1, true);
        //productFeatureValueMappingRepository.save(iPhoneRamMapping);

        //SellerProduct iPhoneCromaSellerProduct = SellerProductBuilder.createSellerProduct(cromaSeller, iPhone, bigBasket);
        //sellerProductRepository.save(iPhoneTataCliq);
        //sellerProductRepository.save(iPhoneCromaSellerProduct);

        // 4. Channel-Category combination (multiple sellers)
        // Gold Necklace: Available through Tanishq seller in Necklace category
        //SellerProduct necklaceTanishq = SellerProductBuilder.createSellerProduct(tanishqSeller, goldNecklace, tataCliq);
        //sellerProductRepository.save(necklaceTanishq);

        // 5. Channel-Seller combination
        // Gold Bangles: Available through Tanishq seller on TataCliq channel
        //SellerProduct banglesTanishq = SellerProductBuilder.createSellerProduct(tanishqSeller, goldBangles, tataCliq);
        //sellerProductRepository.save(banglesTanishq);
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




}
