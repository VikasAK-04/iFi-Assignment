package com.vikas.onefimarketplace.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vikas.onefimarketplace.data.datasource.ProductDataSource;

import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.Resource;
import com.vikas.onefimarketplace.data.repository.ProductRepository;

import java.util.List;

/**
 * ViewModel managing Marketplace list UI state (Loading, Success, Error, Empty).
 */
public class MarketplaceViewModel extends ViewModel {

    private final ProductRepository repository;
    private final MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> selectedCategoryLiveData = new MutableLiveData<>("All");
    private final MutableLiveData<String> searchQueryLiveData = new MutableLiveData<>("");

    public MarketplaceViewModel(ProductRepository repository) {
        this.repository = repository;
        loadProducts();
    }

    public LiveData<Resource<List<Product>>> getProductsState() {
        return productsLiveData;
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategoryLiveData;
    }

    public LiveData<String> getSearchQuery() {
        return searchQueryLiveData;
    }

    public void loadProducts() {
        fetchProducts(searchQueryLiveData.getValue(), selectedCategoryLiveData.getValue());
    }

    public void setCategory(String category) {
        if (category == null) category = "All";
        selectedCategoryLiveData.setValue(category);
        fetchProducts(searchQueryLiveData.getValue(), category);
    }

    public void setSearchQuery(String query) {
        if (query == null) query = "";
        searchQueryLiveData.setValue(query);
        fetchProducts(query, selectedCategoryLiveData.getValue());
    }

    public void retry() {
        loadProducts();
    }

    private void fetchProducts(String query, String category) {
        productsLiveData.setValue(Resource.loading(null));

        repository.searchProducts(query, category, new ProductDataSource.Callback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                if (result == null || result.isEmpty()) {
                    productsLiveData.setValue(Resource.empty());
                } else {
                    productsLiveData.setValue(Resource.success(result));
                }
            }

            @Override
            public void onError(Exception exception) {
                String errorMsg = exception != null && exception.getMessage() != null
                        ? exception.getMessage()
                        : "Failed to load products";
                productsLiveData.setValue(Resource.error(errorMsg, null));
            }
        });
    }
}
