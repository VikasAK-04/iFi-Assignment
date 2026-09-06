package com.vikas.onefimarketplace.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vikas.onefimarketplace.MainActivity;
import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.datasource.MockProductDataSource;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.repository.ProductRepository;
import com.vikas.onefimarketplace.data.repository.ProductRepositoryImpl;
import com.vikas.onefimarketplace.databinding.FragmentShopBinding;
import com.vikas.onefimarketplace.ui.adapter.CategoryAdapter;
import com.vikas.onefimarketplace.ui.adapter.ProductAdapter;
import com.vikas.onefimarketplace.ui.viewmodel.MarketplaceViewModel;
import com.vikas.onefimarketplace.ui.viewmodel.ViewModelFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Authentic 1Fi Shop fragment with 3-tab switcher: Top Brands, Nearby Stores, and 1Fi Marketplace.
 */
public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;
    private MarketplaceViewModel viewModel;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel for Marketplace integration
        ProductRepository repository = new ProductRepositoryImpl(new MockProductDataSource());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MarketplaceViewModel.class);

        setupTabs();
        setupMarketplaceAdapters();
        setupSearchInput();
        observeViewModel();
    }

    private void setupTabs() {
        binding.tabTopBrands.setOnClickListener(v -> selectTab(0));
        binding.tabNearbyStores.setOnClickListener(v -> selectTab(1));
        binding.tabMarketplace.setOnClickListener(v -> selectTab(2));
    }

    public void selectTab(int tabIndex) {
        if (binding == null) return;

        // Reset tab backgrounds
        binding.tabTopBrands.setBackgroundResource(0);
        binding.tabTopBrands.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_text_secondary));

        binding.tabNearbyStores.setBackgroundResource(0);
        binding.tabNearbyStores.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_text_secondary));

        binding.tabMarketplace.setBackgroundResource(0);
        binding.tabMarketplace.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_text_secondary));

        // Hide all views
        binding.viewTopBrands.setVisibility(View.GONE);
        binding.viewNearbyStores.setVisibility(View.GONE);
        binding.viewMarketplaceCatalog.setVisibility(View.GONE);

        if (tabIndex == 0) {
            binding.tabTopBrands.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabTopBrands.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_purple_primary));
            binding.viewTopBrands.setVisibility(View.VISIBLE);
            binding.etShopSearch.setHint("Search online stores...");
        } else if (tabIndex == 1) {
            binding.tabNearbyStores.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabNearbyStores.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_purple_primary));
            binding.viewNearbyStores.setVisibility(View.VISIBLE);
            binding.etShopSearch.setHint("Search nearby stores...");
        } else {
            binding.tabMarketplace.setBackgroundResource(R.drawable.bg_tab_selected);
            binding.tabMarketplace.setTextColor(ContextCompat.getColor(requireContext(), R.color.onefi_purple_primary));
            binding.viewMarketplaceCatalog.setVisibility(View.VISIBLE);
            binding.etShopSearch.setHint("Search 1Fi Marketplace...");
        }
    }

    private void setupMarketplaceAdapters() {
        // Product Adapter
        productAdapter = new ProductAdapter(product -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToProductDetail(product.getId());
            }
        });
        binding.rvShopProducts.setAdapter(productAdapter);

        // Category Filter Chips
        List<String> categories = Arrays.asList("All", "Smartphones", "Laptops", "Wearables", "Audio", "TV & Display");
        categoryAdapter = new CategoryAdapter(category -> viewModel.setCategory(category));
        binding.rvShopCategoryChips.setAdapter(categoryAdapter);
        categoryAdapter.setCategories(categories, "All");
    }

    private void setupSearchInput() {
        binding.etShopSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.viewMarketplaceCatalog.getVisibility() != View.VISIBLE && s.length() > 0) {
                    // Automatically switch to Marketplace tab on search
                    selectTab(2);
                }
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnShopRetry.setOnClickListener(v -> viewModel.retry());

        binding.btnShopClearSearch.setOnClickListener(v -> {
            binding.etShopSearch.setText("");
            viewModel.setCategory("All");
        });
    }

    private void observeViewModel() {
        viewModel.getSelectedCategory().observe(getViewLifecycleOwner(), category -> {
            if (categoryAdapter != null) {
                categoryAdapter.setSelectedCategory(category);
            }
        });

        viewModel.getProductsState().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        showSuccess(resource.getData());
                    } else {
                        showEmpty();
                    }
                    break;
                case EMPTY:
                    showEmpty();
                    break;
                case ERROR:
                    showError(resource.getMessage());
                    break;
            }
        });
    }

    private void showLoading() {
        binding.layoutShopLoading.setVisibility(View.VISIBLE);
        binding.rvShopProducts.setVisibility(View.GONE);
        binding.layoutShopError.setVisibility(View.GONE);
        binding.layoutShopEmpty.setVisibility(View.GONE);
    }

    private void showSuccess(List<Product> products) {
        binding.layoutShopLoading.setVisibility(View.GONE);
        binding.rvShopProducts.setVisibility(View.VISIBLE);
        binding.layoutShopError.setVisibility(View.GONE);
        binding.layoutShopEmpty.setVisibility(View.GONE);

        productAdapter.setProducts(products);
        binding.tvShopProductCount.setText(getString(R.string.products_count_format, products.size()));
    }

    private void showEmpty() {
        binding.layoutShopLoading.setVisibility(View.GONE);
        binding.rvShopProducts.setVisibility(View.GONE);
        binding.layoutShopError.setVisibility(View.GONE);
        binding.layoutShopEmpty.setVisibility(View.VISIBLE);
        binding.tvShopProductCount.setText(getString(R.string.products_count_format, 0));
    }

    private void showError(String message) {
        binding.layoutShopLoading.setVisibility(View.GONE);
        binding.rvShopProducts.setVisibility(View.GONE);
        binding.layoutShopEmpty.setVisibility(View.GONE);
        binding.layoutShopError.setVisibility(View.VISIBLE);

        if (message != null && !message.isEmpty()) {
            binding.tvShopErrorMessage.setText(message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
