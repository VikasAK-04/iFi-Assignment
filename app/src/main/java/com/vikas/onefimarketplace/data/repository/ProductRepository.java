package com.vikas.onefimarketplace.data.repository;

import com.vikas.onefimarketplace.data.datasource.ProductDataSource;
import com.vikas.onefimarketplace.data.model.Product;

import java.util.List;

/**
 * Repository interface isolating the UI layer from data origin.
 */
public interface ProductRepository {

    void getProducts(ProductDataSource.Callback<List<Product>> callback);

    void getProductById(String id, ProductDataSource.Callback<Product> callback);

    void searchProducts(String query, String category, ProductDataSource.Callback<List<Product>> callback);
}
