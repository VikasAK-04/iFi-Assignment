package com.vikas.onefimarketplace.ui.fragment;

import android.os.Bundle;
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
import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.ProductVariant;
import com.vikas.onefimarketplace.data.repository.ProductRepository;
import com.vikas.onefimarketplace.data.repository.ProductRepositoryImpl;
import com.vikas.onefimarketplace.databinding.FragmentProductDetailBinding;
import com.vikas.onefimarketplace.ui.adapter.EmiPlanAdapter;
import com.vikas.onefimarketplace.ui.adapter.VariantAdapter;
import com.vikas.onefimarketplace.ui.viewmodel.ProductDetailViewModel;
import com.vikas.onefimarketplace.ui.viewmodel.ViewModelFactory;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Product detail view featuring variant selection, dynamic EMI calculations, and purchase initiation.
 */
public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "arg_product_id";

    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private VariantAdapter variantAdapter;
    private EmiPlanAdapter emiPlanAdapter;
    private Product currentProduct;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public static ProductDetailFragment newInstance(String productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductDetailFragment() {
        currencyFormat.setMaximumFractionDigits(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String productId = getArguments() != null ? getArguments().getString(ARG_PRODUCT_ID) : null;

        // Initialize ViewModel
        ProductRepository repository = new ProductRepositoryImpl(new MockProductDataSource());
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ProductDetailViewModel.class);

        setupAdapters();
        setupClickListeners();
        observeViewModel();

        if (productId != null) {
            viewModel.loadProductDetails(productId);
        }
    }

    private void setupAdapters() {
        // Variant Adapter
        variantAdapter = new VariantAdapter(variant -> {
            if (currentProduct != null) {
                viewModel.selectVariant(variant, currentProduct);
            }
        });
        binding.rvVariants.setAdapter(variantAdapter);

        // EMI Plan Adapter
        emiPlanAdapter = new EmiPlanAdapter(plan -> {
            if (currentProduct != null) {
                viewModel.selectEmiPlan(plan, currentProduct);
            }
        });
        binding.rvEmiPlans.setAdapter(emiPlanAdapter);
    }

    private void setupClickListeners() {
        binding.btnBackDetail.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnProceedEmi.setOnClickListener(v -> {
            if (currentProduct == null) return;
            ProductVariant selectedVariant = viewModel.getSelectedVariant().getValue();
            EmiPlan selectedPlan = viewModel.getSelectedEmiPlan().getValue();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToReview(currentProduct, selectedVariant, selectedPlan);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getProductState().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            if (resource.isSuccess() && resource.getData() != null) {
                currentProduct = resource.getData();
                displayProductDetails(currentProduct);
            }
        });

        viewModel.getSelectedVariant().observe(getViewLifecycleOwner(), variant -> {
            if (variantAdapter != null) {
                variantAdapter.setSelectedVariant(variant);
            }
        });

        viewModel.getSelectedEmiPlan().observe(getViewLifecycleOwner(), plan -> {
            if (emiPlanAdapter != null) {
                emiPlanAdapter.setSelectedPlan(plan);
            }
        });

        viewModel.getCalculatedTotalPrice().observe(getViewLifecycleOwner(), totalPrice -> {
            if (totalPrice != null && currentProduct != null) {
                binding.tvDetailPrice.setText(currencyFormat.format(totalPrice));
                binding.tvBottomTotalPayable.setText(getString(R.string.total_payable_format,
                        currencyFormat.format(totalPrice)));

                if (emiPlanAdapter != null) {
                    emiPlanAdapter.updateTotalPrice(totalPrice);
                }
            }
        });

        viewModel.getCalculatedMonthlyEmi().observe(getViewLifecycleOwner(), monthlyEmi -> {
            if (monthlyEmi != null) {
                String formattedMonthly = currencyFormat.format(monthlyEmi);
                binding.tvBottomMonthlyEmi.setText(getString(R.string.monthly_amount_format,
                        formattedMonthly.replace("₹", "")));
            }
        });
    }

    private void displayProductDetails(Product product) {
        binding.tvDetailTitleHeader.setText(product.getName());
        binding.tvDetailBrand.setText(product.getBrand());
        binding.tvDetailName.setText(product.getName());
        binding.tvDetailRating.setText(String.format(Locale.getDefault(), "%.1f (%d reviews)",
                product.getRating(), product.getReviewCount()));
        binding.tvDetailDescription.setText(product.getDescription());
        binding.ivHeroImage.setImageResource(product.getImageResId());

        // Visibility of No-Cost EMI Tag
        if (product.hasNoCostEmi()) {
            binding.tvDetailNoCostTag.setVisibility(View.VISIBLE);
        } else {
            binding.tvDetailNoCostTag.setVisibility(View.GONE);
        }

        // Format Specs
        StringBuilder specsBuilder = new StringBuilder();
        if (product.getFeatures() != null) {
            for (String feature : product.getFeatures()) {
                specsBuilder.append("• ").append(feature).append("\n");
            }
        }
        binding.tvKeyFeatures.setText(specsBuilder.toString().trim());

        // Bind variants & EMI plans
        ProductVariant currentVariant = viewModel.getSelectedVariant().getValue();
        variantAdapter.setVariants(product.getVariants(), currentVariant);

        EmiPlan currentPlan = viewModel.getSelectedEmiPlan().getValue();
        double totalPrice = viewModel.getCalculatedTotalPrice().getValue() != null
                ? viewModel.getCalculatedTotalPrice().getValue()
                : product.getBasePrice();

        emiPlanAdapter.setEmiPlans(product.getEmiPlans(), currentPlan, totalPrice);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
