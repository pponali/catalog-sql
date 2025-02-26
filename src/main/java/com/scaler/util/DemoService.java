package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.*;
import com.scaler.repository.*;
import com.scaler.builder.*;
import com.scaler.service.CategoryFeatureTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    private ProductFeatureMappingRepository productFeatureMappingRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @Transactional
    public void setup() throws JsonProcessingException {
        // Create merchants
        Merchant tataCliq = merchantRepository.save(MerchantBuilder.createTataCliqMerchant());
        Merchant tata1mg = merchantRepository.save(MerchantBuilder.createTata1mgMerchant());
        Merchant bigBasket = merchantRepository.save(MerchantBuilder.createBigBasketMerchant());
        Merchant croma = merchantRepository.save(MerchantBuilder.createCromaMerchant());
        Merchant tanishq = merchantRepository.save(MerchantBuilder.createTanishqMerchant());

        // Create stores
        Store tataCliqStore = storeRepository.save(StoreBuilder.createTataCliqStore(tataCliq));
        Store tata1mgStore = storeRepository.save(StoreBuilder.createTata1mgStore(tata1mg));
        Store bigBasketStore = storeRepository.save(StoreBuilder.createBigBasketStore(bigBasket));
        Store cromaStore = storeRepository.save(StoreBuilder.createCromaStore(croma));
        Store tanishqStore = storeRepository.save(StoreBuilder.createTanishqStore(tanishq));

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
        ProductFeature processor = ProductFeatureBuilder.createLaptopProcessorFeature();
        ProductFeature ram = ProductFeatureBuilder.createLaptopRamFeature(gbUnit);
        ProductFeature storage = ProductFeatureBuilder.createLaptopStorageFeature(tbUnit);

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
        
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(macBookProcessor, "Apple M2 Pro"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(macBookRam, "16GB"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(macBookStorage, "512GB"));

        // Create feature mappings and values for Dell XPS
        ProductFeatureMapping dellProcessor = createAndSaveMapping(dellXPS, processor);
        ProductFeatureMapping dellRam = createAndSaveMapping(dellXPS, ram);
        ProductFeatureMapping dellStorage = createAndSaveMapping(dellXPS, storage);
        
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createProcessorValue(dellProcessor, "Intel i9-13900H"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createRamValue(dellRam, "32GB"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createStorageValue(dellStorage, "1TB"));

        // Create features for jewelry
        ProductFeature goldPurity = productFeatureRepository.save(ProductFeatureBuilder.createGoldPurityFeature());
        ProductFeature goldWeight = productFeatureRepository.save(ProductFeatureBuilder.createGoldWeightFeature(gramUnit));

        // Create feature mappings and values for Gold Necklace
        ProductFeatureMapping necklacePurity = createAndSaveMapping(goldNecklace, goldPurity);
        ProductFeatureMapping necklaceWeight = createAndSaveMapping(goldNecklace, goldWeight);
        
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(necklacePurity, "22K"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(necklaceWeight, "50"));

        // Create feature mappings and values for Gold Bangles
        ProductFeatureMapping banglePurity = createAndSaveMapping(goldBangles, goldPurity);
        ProductFeatureMapping bangleWeight = createAndSaveMapping(goldBangles, goldWeight);
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(banglePurity, "22K"));
        productFeatureValueRepository.save(ProductFeatureValueBuilder.createFeatureValue(bangleWeight, "30"));
    }

    private ProductFeatureMapping createAndSaveMapping(Product product, ProductFeature feature) {
        ProductFeatureMapping mapping = ProductFeatureMapping.builder()
                .product(product)
                .feature(feature)
                .displayOrder(1)
                .createdBy("system")
                .lastModifiedBy("system")
                .visible(true)
                .enabled(true)
                .build();
        return productFeatureMappingRepository.save(mapping);
    }

}
