package com.vikas.onefimarketplace.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vikas.onefimarketplace.data.repository.ProductRepository;

/**
 * Custom Factory for instantiating ViewModels with ProductRepository dependency.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final ProductRepository repository;

    public ViewModelFactory(ProductRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MarketplaceViewModel.class)) {
            return (T) new MarketplaceViewModel(repository);
        } else if (modelClass.isAssignableFrom(ProductDetailViewModel.class)) {
            return (T) new ProductDetailViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
