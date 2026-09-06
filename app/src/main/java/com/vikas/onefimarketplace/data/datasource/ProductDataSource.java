package com.vikas.onefimarketplace.data.datasource;

import com.vikas.onefimarketplace.data.model.Product;

import java.util.List;

/**
 * Data source contract for products.
 * Abstracted so MockProductDataSource can easily be swapped for ApiProductDataSource.
 */
public interface ProductDataSource {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception exception);
    }

    void getProducts(Callback<List<Product>> callback);

    void getProductById(String id, Callback<Product> callback);

    void searchProducts(String query, String category, Callback<List<Product>> callback);
}
