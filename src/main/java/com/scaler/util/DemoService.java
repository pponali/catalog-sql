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

            // 1mg Channels
            Channel tata1mgEcom = channelRepository.save(ChannelBuilder.createTata1mgEcommerceChannel(tata1mgStore));
            Channel tata1mgPhysical = channelRepository.save(ChannelBuilder.createTata1mgPhysicalStoreChannel(tata1mgStore));
            tata1mgStore.addChannel(tata1mgEcom);
            tata1mgStore.addChannel(tata1mgPhysical);

            // Tanishq Channels
            Channel tanishqEcom = channelRepository.save(ChannelBuilder.createTanishqEcommerceChannel(tanishqStore));
            Channel tanishqPhysical = channelRepository.save(ChannelBuilder.createTanishqPhysicalStoreChannel(tanishqStore));
            tanishqStore.addChannel(tanishqEcom);
            tanishqStore.addChannel(tanishqPhysical);

            // Save the updated stores
            storeRepository.save(tataCliqStore);
            storeRepository.save(cromaStore);
            storeRepository.save(bigBasketStore);
            storeRepository.save(tata1mgStore);
            storeRepository.save(tanishqStore);

            // Create platforms for TataCliq ecommerce channel
            Platform cliqAndroid = platformRepository.save(PlatformBuilder.createMobileAppAndroid(tataCliqEcom, "CLIQ_ANDROID"));
            Platform cliqIOS = platformRepository.save(PlatformBuilder.createMobileAppIOS(tataCliqEcom,"CLIQ_IOS"));
            Platform cliqDesktop = platformRepository.save(PlatformBuilder.createDesktopWeb(tataCliqEcom, "CLIQ_DESKTOP"));
            Platform cliqMweb = platformRepository.save(PlatformBuilder.createMobileWeb(tataCliqEcom,"CLIQ_MWEB"));
            Platform cliqKiosk = platformRepository.save(PlatformBuilder.createKiosk(tataCliqEcom,"CLIQ_KIOSK"));

            // Create platforms for Croma ecommerce channel
            Platform cromaAndroid = platformRepository.save(PlatformBuilder.createMobileAppAndroid(cromaEcom, "CROMA_ANDROID"));
            Platform cromaIOS = platformRepository.save(PlatformBuilder.createMobileAppIOS(cromaEcom,"CROMA_IOS"));
            Platform cromaDesktop = platformRepository.save(PlatformBuilder.createDesktopWeb(cromaEcom, "CROMA_DESKTOP"));
            Platform cromaMweb = platformRepository.save(PlatformBuilder.createMobileWeb(cromaEcom,"CROMA_MWEB"));
            Platform cromaKiosk = platformRepository.save(PlatformBuilder.createKiosk(cromaEcom,"CROMA_KIOSK"));

            // Create platforms for BigBasket ecommerce channel
            Platform bbAndroid = platformRepository.save(PlatformBuilder.createMobileAppAndroid(bigBasketEcom, "BIGBASKET_ANDROID"));
            Platform bbIOS = platformRepository.save(PlatformBuilder.createMobileAppIOS(bigBasketEcom,"BIGBASKET_IOS"));
            Platform bbDesktop = platformRepository.save(PlatformBuilder.createDesktopWeb(bigBasketEcom, "BIGBASKET_DESKTOP"));
            Platform bbMweb = platformRepository.save(PlatformBuilder.createMobileWeb(bigBasketEcom,"BIGBASKET_MWEB"));
            Platform bbKiosk = platformRepository.save(PlatformBuilder.createKiosk(bigBasketEcom,"BIGBASKET_KIOSK"));

            // Create platforms for 1mg ecommerce channel
            Platform mgAndroid = platformRepository.save(PlatformBuilder.createMobileAppAndroid(tata1mgEcom, "1MG_ANDROID"));
            Platform mgIOS = platformRepository.save(PlatformBuilder.createMobileAppIOS(tata1mgEcom,"1MG_IOS"));
            Platform mgDesktop = platformRepository.save(PlatformBuilder.createDesktopWeb(tata1mgEcom, "1MG_DESKTOP"));
            Platform mgMweb = platformRepository.save(PlatformBuilder.createMobileWeb(tata1mgEcom,"1MG_MWEB"));

            // Create platforms for Tanishq ecommerce channel
            Platform tanishqAndroid = platformRepository.save(PlatformBuilder.createMobileAppAndroid(tanishqEcom, "TANISHQ_ANDROID"));
            Platform tanishqIOS = platformRepository.save(PlatformBuilder.createMobileAppIOS(tanishqEcom,"TANISHQ_IOS"));
            Platform tanishqDesktop = platformRepository.save(PlatformBuilder.createDesktopWeb(tanishqEcom, "TANISHQ_DESKTOP"));
            Platform tanishqMweb = platformRepository.save(PlatformBuilder.createMobileWeb(tanishqEcom,"TANISHQ_MWEB"));


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

            // 1mg channels
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tata1mgEcom, pharmaCatalog, true));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tata1mgPhysical, pharmaCatalog, true));

            // Tanishq channels
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tanishqEcom, jewelryCatalog, true));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tanishqPhysical, jewelryCatalog, true));

            // Cross-catalog relationships for testing
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tataCliqEcom, cromaCatalog, false));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(tataCliqEcom, jewelryCatalog, false));
            channelCatalogRepository.save(ChannelCatalogBuilder.createChannelCatalog(cromaEcom, fashionCatalog, false));

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
            CategoryFeatureTemplate laptopProcessorTemplate = FeatureTemplateBuilder.createLaptopProcessorTemplate(laptopCategory);
            CategoryFeatureTemplate laptopRamTemplate = FeatureTemplateBuilder.createLaptopRamTemplate(laptopCategory, gbUnit);
            CategoryFeatureTemplate laptopStorageTemplate = FeatureTemplateBuilder.createLaptopStorageTemplate(laptopCategory, tbUnit);

            // Create feature templates for iPhone
            CategoryFeatureTemplate iPhoneProcessorTemplate = FeatureTemplateBuilder.createIPhoneProcessorTemplate(laptopCategory);
            CategoryFeatureTemplate iPhoneRamCategoryFeatureTemplate = FeatureTemplateBuilder.createIPhoneRamTemplate(laptopCategory, gbUnit);
            CategoryFeatureTemplate iPhoneStorageTemplate = FeatureTemplateBuilder.createIPhoneStorageTemplate(laptopCategory, tbUnit);

            // Create feature templates for Jewellery
            CategoryFeatureTemplate goldPurityTemplate = FeatureTemplateBuilder.createGoldPurityTemplate(necklaceCategory);
            CategoryFeatureTemplate goldWeightTemplate = FeatureTemplateBuilder.createGoldWeightTemplate(bangleCategory, gbUnit);


            categoryFeatureTemplateRepository.save(laptopProcessorTemplate);
            categoryFeatureTemplateRepository.save(laptopRamTemplate);
            categoryFeatureTemplateRepository.save(laptopStorageTemplate);

            categoryFeatureTemplateRepository.save(iPhoneProcessorTemplate);
            categoryFeatureTemplateRepository.save(iPhoneRamCategoryFeatureTemplate);
            categoryFeatureTemplateRepository.save(iPhoneStorageTemplate);


            categoryFeatureTemplateRepository.save(goldPurityTemplate);
            categoryFeatureTemplateRepository.save(goldWeightTemplate);

            // Create product features
            ProductFeature laptopProcessorFeature = ProductFeatureBuilder.createLaptopProcessorFeature(laptopProcessorTemplate);
            ProductFeature laptopRamFeature = ProductFeatureBuilder.createLaptopRamFeature(laptopRamTemplate, gbUnit);
            ProductFeature laptopStorageFeature = ProductFeatureBuilder.createLaptopStorageFeature(laptopStorageTemplate, tbUnit);

            // Create product features
            ProductFeature iphoneprocessor = ProductFeatureBuilder.createProcessorFeature(iPhoneProcessorTemplate);
            ProductFeature iPhoneram = ProductFeatureBuilder.createLaptopRamFeature(laptopRamTemplate, gbUnit);
            ProductFeature iPhonestorage = ProductFeatureBuilder.createLaptopStorageFeature(laptopStorageTemplate, tbUnit);


            productFeatureRepository.save(laptopProcessorFeature);
            productFeatureRepository.save(laptopRamFeature);
            productFeatureRepository.save(laptopStorageFeature);

            productFeatureRepository.save(iphoneprocessor);
            productFeatureRepository.save(iPhoneram);
            productFeatureRepository.save(iPhonestorage);


               // Create products
            Product macBookProProduct = productRepository.save(ProductBuilder.createMacBookPro(laptopCategory, tataCliq));
            Product dellXPS = productRepository.save(ProductBuilder.createDellXPS(laptopCategory, tataCliq));
            Product iPhone = productRepository.save(ProductBuilder.createIPhone(smartphoneCategory, tataCliq));
            Product goldNecklace = productRepository.save(ProductBuilder.createGoldNecklace(necklaceCategory, tanishq));
            Product goldBangles = productRepository.save(ProductBuilder.createGoldBangles(bangleCategory, tanishq));
            
            // Additional products for testing
            Product samsungGalaxy = productRepository.save(ProductBuilder.createSamsungGalaxy(smartphoneCategory, tataCliq));
            Product appleWatch = productRepository.save(ProductBuilder.createAppleWatch(smartphoneCategory, tataCliq));
            Product hpSpectre = productRepository.save(ProductBuilder.createHPSpectre(laptopCategory, croma));
            Product diamondRing = productRepository.save(ProductBuilder.createDiamondRing(necklaceCategory, tanishq));
            Product paracetamol = productRepository.save(ProductBuilder.createParacetamol(medicinesCategory, tata1mg));
            Product freshApples = productRepository.save(ProductBuilder.createFreshApples(freshProduceCategory, bigBasket));


            ProductFeatureValue processorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(macBookProProduct,laptopProcessorFeature, "Apple M2 Pro"));
            ProductFeatureValue ramValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(macBookProProduct, laptopRamFeature, "16GB"));
            ProductFeatureValue storageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(macBookProProduct, iPhonestorage, "512GB"));

            ProductFeatureValue iPhoneprocessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(iPhone,iphoneprocessor, "M2"));

            ProductFeatureValue dellProcessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(dellXPS, laptopProcessorFeature, "Intel i9-13900H"));
            ProductFeatureValue dellRamValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(dellXPS, laptopRamFeature, "32GB"));
            ProductFeatureValue dellStorageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(dellXPS, iPhonestorage, "1TB"));

            // Additional feature values for testing
            ProductFeatureValue samsungProcessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(samsungGalaxy, iphoneprocessor, "Snapdragon 8 Gen 2"));
            ProductFeatureValue samsungRamValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(samsungGalaxy, iPhoneram, "12GB"));
            ProductFeatureValue samsungStorageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(samsungGalaxy, iPhonestorage, "256GB"));

            ProductFeatureValue hpProcessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(hpSpectre, laptopProcessorFeature, "Intel i7-1165G7"));
            ProductFeatureValue hpRamValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(hpSpectre, laptopRamFeature, "16GB"));
            ProductFeatureValue hpStorageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(hpSpectre, laptopStorageFeature, "512GB"));


            // Create product-channel relationships
            // MacBook Pro available on Croma (both online and offline)
            macBookProProduct.addProductChannel(cromaEcom);
            macBookProProduct.addProductChannel(cromaPhysical);
            macBookProProduct.addProductChannel(tataCliqEcom); // Also available on TataCliq

            // Dell XPS available on TataCliQ (both ecommerce and marketplace)
            dellXPS.addProductChannel(tataCliqEcom);
            dellXPS.addProductChannel(tataCliqMarketplace);
            dellXPS.addProductChannel(cromaEcom); // Also available on Croma

            // iPhone available on both Croma and TataCliQ
            iPhone.addProductChannel(cromaEcom);
            iPhone.addProductChannel(cromaPhysical);
            iPhone.addProductChannel(tataCliqEcom);
            iPhone.addProductChannel(tataCliqMarketplace);

            // Jewelry available in physical stores with online visibility
            goldNecklace.addProductChannel(tanishqEcom);
            goldNecklace.addProductChannel(tanishqPhysical);
            goldNecklace.addProductChannel(tataCliqEcom); // Also available on TataCliq
            
            goldBangles.addProductChannel(tanishqEcom);
            goldBangles.addProductChannel(tanishqPhysical);
            
            // Additional product-channel relationships for testing
            samsungGalaxy.addProductChannel(tataCliqEcom);
            samsungGalaxy.addProductChannel(cromaEcom);
            
            appleWatch.addProductChannel(tataCliqEcom);
            appleWatch.addProductChannel(cromaEcom);
            
            hpSpectre.addProductChannel(cromaEcom);
            hpSpectre.addProductChannel(cromaPhysical);
            
            diamondRing.addProductChannel(tanishqEcom);
            diamondRing.addProductChannel(tanishqPhysical);
            
            paracetamol.addProductChannel(tata1mgEcom);
            paracetamol.addProductChannel(tata1mgPhysical);
            
            freshApples.addProductChannel(bigBasketEcom);
            freshApples.addProductChannel(bigBasketQuick);


            // Create features for jewelry
            ProductFeature goldPurity = productFeatureRepository.save(ProductFeatureBuilder.createGoldPurityFeature(goldPurityTemplate));
            ProductFeature goldWeight = productFeatureRepository.save(ProductFeatureBuilder.createGoldWeightFeature(goldWeightTemplate, gramUnit));



            ProductFeatureValue purityValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldNecklace, goldPurity, "22K"));
            ProductFeatureValue weightValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldNecklace, goldWeight, "50"));

            // Create feature value mappings for MacBook Pro
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookProProduct, laptopProcessorFeature, processorValue, laptopProcessorTemplate, 1));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookProProduct, laptopRamFeature, ramValue, laptopRamTemplate, 2));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookProProduct, laptopStorageFeature, storageValue, laptopStorageTemplate, 3));

            // Create feature value mappings for Dell XPS
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellXPS, laptopProcessorFeature, dellProcessorValue, laptopProcessorTemplate, 1));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellXPS, laptopRamFeature, dellRamValue, laptopRamTemplate, 2));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellXPS, laptopStorageFeature, dellStorageValue, laptopStorageTemplate, 3));

            // Create feature value mappings for jewelry
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(goldNecklace, goldPurity, purityValue, goldPurityTemplate, 1));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(goldNecklace, goldWeight, weightValue, goldWeightTemplate, 2));

            // Additional feature value mappings for testing
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(samsungGalaxy, iphoneprocessor, samsungProcessorValue, iPhoneProcessorTemplate, 1));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(samsungGalaxy, iPhoneram, samsungRamValue, iPhoneRamCategoryFeatureTemplate, 2));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(samsungGalaxy, iPhonestorage, samsungStorageValue, iPhoneStorageTemplate, 3));

            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(hpSpectre, laptopProcessorFeature, hpProcessorValue, laptopProcessorTemplate, 1));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(hpSpectre, laptopRamFeature, hpRamValue, laptopRamTemplate, 2));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(hpSpectre, laptopStorageFeature, hpStorageValue, laptopStorageTemplate, 3));


            // Save all products with their channel relationships
            productRepository.save(macBookProProduct);
            productRepository.save(dellXPS);
            productRepository.save(iPhone);
            productRepository.save(goldNecklace);
            productRepository.save(goldBangles);
            productRepository.save(samsungGalaxy);
            productRepository.save(appleWatch);
            productRepository.save(hpSpectre);
            productRepository.save(diamondRing);
            productRepository.save(paracetamol);
            productRepository.save(freshApples);


            // Create sample product prices and inventory
            createSamplePricesAndInventory(tataCliq, tataCliqEcom, tataCliqSeller);
            createSamplePricesAndInventory(croma, cromaEcom, cromaSeller);
            createSamplePricesAndInventory(bigBasket, bigBasketEcom, bigBasketSeller);
            createSamplePricesAndInventory(tanishq, tanishqEcom, tanishqSeller);
            createSamplePricesAndInventory(tata1mg, tata1mgEcom, tata1mgSeller);

            // Create seller-product associations for various combinations

            // 1. Channel-Seller-Category combination
            // MacBook Pro: Available through TataCliq seller on TataCliq channels in Laptop category
            SellerProduct macProTataCliq = SellerProductBuilder.createSellerProduct(tataCliqSeller, macBookProProduct, tataCliq);
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
            
            // Additional seller-product associations for testing
            SellerProduct samsungTataCliq = SellerProductBuilder.createSellerProduct(tataCliqSeller, samsungGalaxy, tataCliq);
            SellerProduct samsungCroma = SellerProductBuilder.createSellerProduct(cromaSeller, samsungGalaxy, croma);
            sellerProductRepository.save(samsungTataCliq);
            sellerProductRepository.save(samsungCroma);
            
            SellerProduct appleWatchTataCliq = SellerProductBuilder.createSellerProduct(tataCliqSeller, appleWatch, tataCliq);
            sellerProductRepository.save(appleWatchTataCliq);
            
            SellerProduct hpSpectreCroma = SellerProductBuilder.createSellerProduct(cromaSeller, hpSpectre, croma);
            sellerProductRepository.save(hpSpectreCroma);
            
            SellerProduct diamondRingTanishq = SellerProductBuilder.createSellerProduct(tanishqSeller, diamondRing, tanishq);
            sellerProductRepository.save(diamondRingTanishq);
            
            SellerProduct paracetamol1mg = SellerProductBuilder.createSellerProduct(tata1mgSeller, paracetamol, tata1mg);
            sellerProductRepository.save(paracetamol1mg);
            
            SellerProduct freshApplesBB = SellerProductBuilder.createSellerProduct(bigBasketSeller, freshApples, bigBasket);
            sellerProductRepository.save(freshApplesBB);

            useCases(macBookProProduct, laptopCategory, croma, bigBasketEcom, laptopRamFeature, tataCliqSeller, tanishq, tataCliqMarketplace, iPhone, smartphoneCategory, bigBasket, iPhoneram, cromaSeller, iPhoneTataCliq, tanishqSeller, goldNecklace, tataCliq, goldBangles);

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
            ProductPlatform macBookWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(macBookProProduct, webPlatform);
            productPlatformRepository.save(macBookWebPlatform);

            ProductPlatform macBookTabletPlatform = ProductPlatformBuilder.createActiveProductPlatform(macBookProProduct, tabletPlatform);
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
            
            // Additional product-platform mappings for testing
            ProductPlatform dellXPSWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(dellXPS, webPlatform);
            productPlatformRepository.save(dellXPSWebPlatform);
            
            ProductPlatform dellXPSMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(dellXPS, mobilePlatform);
            productPlatformRepository.save(dellXPSMobilePlatform);
            
            ProductPlatform samsungWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(samsungGalaxy, webPlatform);
            productPlatformRepository.save(samsungWebPlatform);
            
            ProductPlatform samsungMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(samsungGalaxy, mobilePlatform);
            productPlatformRepository.save(samsungMobilePlatform);
            
            ProductPlatform samsungTabletPlatform = ProductPlatformBuilder.createActiveProductPlatform(samsungGalaxy, tabletPlatform);
            productPlatformRepository.save(samsungTabletPlatform);
            
            ProductPlatform appleWatchMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(appleWatch, mobilePlatform);
            productPlatformRepository.save(appleWatchMobilePlatform);
            
            ProductPlatform hpSpectreWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(hpSpectre, webPlatform);
            productPlatformRepository.save(hpSpectreWebPlatform);
            
            ProductPlatform diamondRingWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(diamondRing, webPlatform);
            productPlatformRepository.save(diamondRingWebPlatform);
            
            ProductPlatform paracetamolWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(paracetamol, webPlatform);
            productPlatformRepository.save(paracetamolWebPlatform);
            
            ProductPlatform freshApplesWebPlatform = ProductPlatformBuilder.createActiveProductPlatform(freshApples, webPlatform);
            productPlatformRepository.save(freshApplesWebPlatform);
            
            ProductPlatform freshApplesMobilePlatform = ProductPlatformBuilder.createActiveProductPlatform(freshApples, mobilePlatform);
            productPlatformRepository.save(freshApplesMobilePlatform);

            //validationBuilder.validationRules();

            // Create comprehensive combinations of relationships

            // 1. Channel-Catalog-Product Combinations
            // Assign MacBook Pro to TataCliq Fashion Catalog
            ChannelCatalog macBookChannelCatalog = ChannelCatalogBuilder.createChannelCatalog(bigBasketEcom, fashionCatalog, false);
            channelCatalogRepository.save(macBookChannelCatalog);

            // 2. Product-Channel Combinations
            // Create product-channel for MacBook Pro
            ProductChannel macBookChannel = ProductChannelBuilder.createEcommerceProductChannel(macBookProProduct, bigBasketEcom);
            productChannelRepository.save(macBookChannel);
            
            // Note: We don't need to create additional ProductCategory entries here
            // because they were already created in the ProductBuilder methods

            // 3. Product Feature Combinations
            // Create feature mapping for MacBook Pro RAM


            // Create feature value for 32GB RAM
            ProductFeatureValue ram32GB = ProductFeatureValueBuilder.createRamValue(iPhone, laptopRamFeature, "32GB");
            productFeatureValueRepository.save(ram32GB);

            // Map the 32GB RAM value to MacBook Pro
            ProductFeatureValueMapping ramMapping = ProductFeatureValueMappingBuilder.createMapping(iPhone, iphoneprocessor, iPhoneprocessorValue, iPhoneProcessorTemplate, 2);
            productFeatureValueMappingRepository.save(ramMapping);

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






}
