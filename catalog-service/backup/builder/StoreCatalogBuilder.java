package com.scaler.builder;

import com.scaler.entity.Store;
import com.scaler.entity.Catalog;
import com.scaler.entity.StoreCatalog;

public class StoreCatalogBuilder {
    
    public static StoreCatalog createStoreCatalog(Store store, Catalog catalog) {
        return StoreCatalog.builder()
                .store(store)
                .catalog(catalog)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
