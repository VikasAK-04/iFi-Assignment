package com.vikas.onefimarketplace.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vikas.onefimarketplace.MainActivity;
import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.datasource.MockProductDataSource;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.repository.ProductRepository;
import com.vikas.onefimarketplace.data.repository.ProductRepositoryImpl;
import com.vikas.onefimarketplace.databinding.FragmentMarketplaceBinding;
import com.vikas.onefimarketplace.ui.adapter.CategoryAdapter;
import com.vikas.onefimarketplace.ui.adapter.ProductAdapter;
import com.vikas.onefimarketplace.ui.viewmodel.MarketplaceViewModel;
import com.vikas.onefimarketplace.ui.viewmodel.ViewModelFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 1Fi Marketplace catalog screen handling products list, search, category chips, and UI states.
 */
public class MarketplaceFragment extends Fragment {

    private FragmentMarketplaceBinding binding;
    private MarketplaceViewModel viewModel;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;

    public static MarketplaceFragment newInstance() {
        return new MarketplaceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMarketplaceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Repository and ViewModel
        ProductRepository repository = new ProductRepositoryImpl(new MockProductDataSource());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(MarketplaceViewModel.class);

        setupAdapters();
        setupSearchInput();
        setupClickListeners();
        observeViewModel();
    }

    private void setupAdapters() {
        // Product Adapter
        productAdapter = new ProductAdapter(product -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToProductDetail(product.getId());
            }
        });
        binding.rvProducts.setAdapter(productAdapter);

        // Category Adapter
        List<String> categories = Arrays.asList("All", "Smartphones", "Laptops", "Wearables", "Audio", "TV & Display");
        categoryAdapter = new CategoryAdapter(category -> viewModel.setCategory(category));
        binding.rvCategoryChips.setAdapter(categoryAdapter);
        categoryAdapter.setCategories(categories, "All");
    }

    private void setupSearchInput() {
        binding.etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        binding.btnBackHeader.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnRetry.setOnClickListener(v -> viewModel.retry());

        binding.btnClearSearch.setOnClickListener(v -> {
            binding.etSearchQuery.setText("");
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
                    if (resource.getData() != null) {
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
        binding.layoutLoading.setVisibility(View.VISIBLE);
        binding.rvProducts.setVisibility(View.GONE);
        binding.layoutError.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.GONE);
    }

    private void showSuccess(List<Product> products) {
        binding.layoutLoading.setVisibility(View.GONE);
        binding.rvProducts.setVisibility(View.VISIBLE);
        binding.layoutError.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.GONE);

        productAdapter.setProducts(products);
        binding.tvProductCount.setText(getString(R.string.products_count_format, products.size()));
    }

    private void showEmpty() {
        binding.layoutLoading.setVisibility(View.GONE);
        binding.rvProducts.setVisibility(View.GONE);
        binding.layoutError.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.VISIBLE);
        binding.tvProductCount.setText(getString(R.string.products_count_format, 0));
    }

    private void showError(String message) {
        binding.layoutLoading.setVisibility(View.GONE);
        binding.rvProducts.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.GONE);
        binding.layoutError.setVisibility(View.VISIBLE);

        if (message != null && !message.isEmpty()) {
            binding.tvErrorMessage.setText(message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
