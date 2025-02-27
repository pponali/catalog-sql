package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.*;
import com.scaler.repository.*;
import com.scaler.builder.*;
import com.scaler.service.CategoryFeatureTemplateService;
import com.scaler.service.impl.ProductFeatureValueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.scaler.constants.FeatureConstants.*;


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

            // Create catalogs
            Catalog fashionCatalog = catalogRepository.save(CatalogBuilder.createFashionCatalog(tataCliq));
            Catalog groceryCatalog = catalogRepository.save(CatalogBuilder.createGroceryCatalog(bigBasket));
            Catalog pharmaCatalog = catalogRepository.save(CatalogBuilder.createPharmaCatalog(tata1mg));
            Catalog cromaCatalog = catalogRepository.save(CatalogBuilder.createElectronicsCatalog(croma));
            Catalog jewelryCatalog = catalogRepository.save(CatalogBuilder.createJewelryCatalog(tanishq));

            // Create store-catalog relationships
            storeCatalogRepository.save(StoreCatalogBuilder.createStoreCatalog(tataCliqStore, fashionCatalog));
            storeCatalogRepository.save(StoreCatalogBuilder.createStoreCatalog(tata1mgStore, pharmaCatalog));
            storeCatalogRepository.save(StoreCatalogBuilder.createStoreCatalog(bigBasketStore, groceryCatalog));
            storeCatalogRepository.save(StoreCatalogBuilder.createStoreCatalog(cromaStore, cromaCatalog));
            storeCatalogRepository.save(StoreCatalogBuilder.createStoreCatalog(tanishqStore, jewelryCatalog));

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

            categoryFeatureTemplateRepository.save(laptopProcessor);
            categoryFeatureTemplateRepository.save(laptopRam);
            categoryFeatureTemplateRepository.save(laptopStorage);

            // Create product features
            ProductFeature processor = ProductFeatureBuilder.createLaptopProcessorFeature(laptopProcessor);
            ProductFeature ram = ProductFeatureBuilder.createLaptopRamFeature(laptopRam, gbUnit);
            ProductFeature storage = ProductFeatureBuilder.createLaptopStorageFeature(laptopStorage, tbUnit);

            productFeatureRepository.save(processor);
            productFeatureRepository.save(ram);
            productFeatureRepository.save(storage);


            // Create products
            Product macBookPro = productRepository.save(ProductBuilder.createMacBookPro(laptopCategory, tataCliq));
            Product dellXPS = productRepository.save(ProductBuilder.createDellXPS(laptopCategory, tataCliq));
            Product iPhone = productRepository.save(ProductBuilder.createIPhone(smartphoneCategory, tataCliq));
            Product goldNecklace = productRepository.save(ProductBuilder.createGoldNecklace(necklaceCategory, tanishq));
            Product goldBangles = productRepository.save(ProductBuilder.createGoldBangles(bangleCategory, tanishq));

            // Create feature mappings and values for MacBook Pro
            ProductFeatureMapping macBookProcessor = createAndSaveMapping(macBookPro, processor);
            ProductFeatureMapping macBookRam = createAndSaveMapping(macBookPro, ram);
            ProductFeatureMapping macBookStorage = createAndSaveMapping(macBookPro, storage);

            ProductFeatureValue processorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(processor, "Apple M2 Pro"));
            ProductFeatureValue ramValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(ram, "16GB"));
            ProductFeatureValue storageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(storage, "512GB"));

            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookProcessor, processorValue, 1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookRam, ramValue, 2, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(macBookStorage, storageValue, 3, true));

            // Create feature mappings and values for Dell XPS
            ProductFeatureMapping dellProcessor = createAndSaveMapping(dellXPS, processor);
            ProductFeatureMapping dellRam = createAndSaveMapping(dellXPS, ram);
            ProductFeatureMapping dellStorage = createAndSaveMapping(dellXPS, storage);

            ProductFeatureValue dellProcessorValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(processor, "Intel i9-13900H"));
            ProductFeatureValue dellRamValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(ram, "32GB"));
            ProductFeatureValue dellStorageValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(storage, "1TB"));

            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellProcessor, dellProcessorValue, 1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellRam, dellRamValue, 2, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(dellStorage, dellStorageValue, 3, true));

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

            // Save all products with their channel relationships
            productRepository.save(macBookPro);
            productRepository.save(dellXPS);
            productRepository.save(iPhone);
            productRepository.save(goldNecklace);
            productRepository.save(goldBangles);

            // Create feature templates for Jewellery
            CategoryFeatureTemplate goldPurityProductFeature = FeatureTemplateBuilder.createGoldPurityTemplate(necklaceCategory);
            CategoryFeatureTemplate goldWeightProductFeature = FeatureTemplateBuilder.createGoldWeightTemplate(bangleCategory, gbUnit);

            categoryFeatureTemplateRepository.save(goldPurityProductFeature);
            categoryFeatureTemplateRepository.save(goldWeightProductFeature);

            // Create features for jewelry
            ProductFeature goldPurity = productFeatureRepository.save(ProductFeatureBuilder.createGoldPurityFeature(goldPurityProductFeature));
            ProductFeature goldWeight = productFeatureRepository.save(ProductFeatureBuilder.createGoldWeightFeature(goldWeightProductFeature, gramUnit));

            // Create feature mappings and values for Gold Necklace
            ProductFeatureMapping necklacePurity = createAndSaveMapping(goldNecklace, goldPurity);
            ProductFeatureMapping necklaceWeight = createAndSaveMapping(goldNecklace, goldWeight);

            ProductFeatureValue purityValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldPurity, "22K"));
            ProductFeatureValue weightValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldWeight, "50"));

            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(necklacePurity, purityValue, 1, true));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(necklaceWeight, weightValue, 2, true));

            // Create feature mappings and values for Gold Bangles
            ProductFeatureMapping banglePurity = createAndSaveMapping(goldBangles, goldPurity);
            ProductFeatureMapping bangleWeight = createAndSaveMapping(goldBangles, goldWeight);

            // Reuse the same purity value for bangles
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(banglePurity, purityValue, 1, true));

            // Create new weight value for bangles
            ProductFeatureValue bangleWeightValue = productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(goldWeight, "30"));
            productFeatureValueMappingRepository.save(ProductFeatureValueMappingBuilder.createMapping(bangleWeight, bangleWeightValue, 2, true));

            // Create sample product prices and inventory
            createSamplePricesAndInventory(tataCliq, tataCliqEcom, tataCliqSeller);
            createSamplePricesAndInventory(croma, cromaEcom, cromaSeller);
            createSamplePricesAndInventory(bigBasket, bigBasketEcom, bigBasketSeller);
            createSamplePricesAndInventory(tanishq, tataCliqEcom, tanishqSeller);
            createSamplePricesAndInventory(tata1mg, tataCliqEcom, tata1mgSeller);

            SellerProduct macProSeller = SellerProductBuilder.createSellerProduct(tataCliqSeller,macBookPro, tataCliq);
            SellerProduct banglesSeller = SellerProductBuilder.createSellerProduct(cromaSeller, goldBangles, croma);


            sellerProductRepository.save(macProSeller);
            sellerProductRepository.save(banglesSeller);


        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize demo data: " + e.getMessage(), e);
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
