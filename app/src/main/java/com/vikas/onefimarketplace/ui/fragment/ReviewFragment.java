package com.vikas.onefimarketplace.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vikas.onefimarketplace.MainActivity;
import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.ProductVariant;
import com.vikas.onefimarketplace.databinding.FragmentReviewBinding;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Review fragment summarizing product, selected variant, and EMI financial details before confirmation.
 */
public class ReviewFragment extends Fragment {

    private static final String ARG_PRODUCT = "arg_product";
    private static final String ARG_VARIANT = "arg_variant";
    private static final String ARG_EMI_PLAN = "arg_emi_plan";

    private FragmentReviewBinding binding;
    private Product product;
    private ProductVariant variant;
    private EmiPlan emiPlan;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public static ReviewFragment newInstance(Product product, ProductVariant variant, EmiPlan emiPlan) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        args.putSerializable(ARG_VARIANT, variant);
        args.putSerializable(ARG_EMI_PLAN, emiPlan);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewFragment() {
        currencyFormat.setMaximumFractionDigits(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);
            variant = (ProductVariant) getArguments().getSerializable(ARG_VARIANT);
            emiPlan = (EmiPlan) getArguments().getSerializable(ARG_EMI_PLAN);
        }

        setupClickListeners();
        displayOrderSummary();
    }

    private void setupClickListeners() {
        binding.btnBackReview.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnConfirmPurchase.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showConfirmationDialog();
            }
        });
    }

    private void displayOrderSummary() {
        if (product == null) return;

        binding.ivReviewProductImage.setImageResource(product.getImageResId());
        binding.tvReviewBrand.setText(product.getBrand());
        binding.tvReviewProductName.setText(product.getName());

        // Variant text
        String variantText = variant != null
                ? "Selected Variant: " + variant.getValue()
                : "Standard Variant";
        binding.tvReviewSelectedVariant.setText(variantText);

        // Price calculation
        double extraPrice = variant != null ? variant.getAdditionalPrice() : 0.0;
        double totalPrice = product.getBasePrice() + extraPrice;

        binding.tvReviewBasePrice.setText(currencyFormat.format(totalPrice));

        if (emiPlan != null) {
            binding.tvReviewTenure.setText(getString(R.string.tenure_format, emiPlan.getTenureMonths())
                    + (emiPlan.isNoCostEmi() ? " (0% No Cost)" : ""));

            double monthly = emiPlan.calculateMonthlyAmount(totalPrice);
            binding.tvReviewMonthlyEmi.setText(getString(R.string.monthly_amount_format,
                    currencyFormat.format(monthly).replace("₹", "")));

            double totalPayable = emiPlan.calculateTotalAmount(totalPrice);
            binding.tvReviewTotalPayable.setText(currencyFormat.format(totalPayable));

            String feeStr = emiPlan.getProcessingFee() <= 0
                    ? getString(R.string.processing_fee_free)
                    : currencyFormat.format(emiPlan.getProcessingFee());
            binding.tvReviewProcessingFee.setText(feeStr);
        } else {
            binding.tvReviewTenure.setText("Full Payment");
            binding.tvReviewMonthlyEmi.setText(currencyFormat.format(totalPrice));
            binding.tvReviewTotalPayable.setText(currencyFormat.format(totalPrice));
            binding.tvReviewProcessingFee.setText(getString(R.string.processing_fee_free));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
