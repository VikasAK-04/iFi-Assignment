package com.vikas.onefimarketplace.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.databinding.ItemProductCardBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for rendering product cards in 1Fi Marketplace catalog.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final OnProductClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public ProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
        currencyFormat.setMaximumFractionDigits(0);
    }

    public void setProducts(List<Product> newProducts) {
        this.products.clear();
        if (newProducts != null) {
            this.products.addAll(newProducts);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductCardBinding binding = ItemProductCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductCardBinding binding;

        ViewHolder(ItemProductCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.tvBrand.setText(product.getBrand());
            binding.tvProductName.setText(product.getName());
            binding.tvRating.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));

            // Format Base Price
            String formattedPrice = currencyFormat.format(product.getBasePrice());
            binding.tvPrice.setText(formattedPrice);

            // Starting Monthly EMI
            double startingMonthly = product.getStartingMonthlyEmi();
            String formattedMonthly = currencyFormat.format(startingMonthly);
            binding.tvStartingEmi.setText(binding.getRoot().getContext().getString(
                    R.string.starting_emi_format, formattedMonthly.replace("₹", "")));

            // No-Cost EMI Tag Visibility
            if (product.hasNoCostEmi()) {
                binding.tvTagNoCost.setVisibility(View.VISIBLE);
            } else {
                binding.tvTagNoCost.setVisibility(View.GONE);
            }

            // Image Resource
            binding.ivProductImage.setImageResource(product.getImageResId());

            // Click Handlers
            View.OnClickListener clickListener = v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            };
            binding.getRoot().setOnClickListener(clickListener);
            binding.btnViewDetails.setOnClickListener(clickListener);
        }
    }
}
