package com.scaler.builder;

import com.scaler.entity.Merchant;
import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;

public class SellerProductBuilder {

    public static SellerProduct createSellerProduct(Seller seller, Product product, Merchant merchant){
        return SellerProduct.builder()
                .product(product)
                .merchant(merchant)
                .seller(seller)
                .createdBy("SYSTEM")
                .build();
    }
}
