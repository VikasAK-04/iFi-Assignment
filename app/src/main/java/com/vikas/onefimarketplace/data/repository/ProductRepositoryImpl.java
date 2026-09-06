package com.vikas.onefimarketplace.data.repository;

import com.vikas.onefimarketplace.data.datasource.ProductDataSource;
import com.vikas.onefimarketplace.data.model.Product;

import java.util.List;

/**
 * Implementation of ProductRepository delegating calls to ProductDataSource.
 */
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDataSource dataSource;

    public ProductRepositoryImpl(ProductDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void getProducts(ProductDataSource.Callback<List<Product>> callback) {
        dataSource.getProducts(callback);
    }

    @Override
    public void getProductById(String id, ProductDataSource.Callback<Product> callback) {
        dataSource.getProductById(id, callback);
    }

    @Override
    public void searchProducts(String query, String category, ProductDataSource.Callback<List<Product>> callback) {
        dataSource.searchProducts(query, category, callback);
    }
}
