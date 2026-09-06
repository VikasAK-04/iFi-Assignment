package com.vikas.onefimarketplace.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.model.ProductVariant;
import com.vikas.onefimarketplace.databinding.ItemVariantChipBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for product variant choice chips in Product Details.
 */
public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.ViewHolder> {

    public interface OnVariantClickListener {
        void onVariantClick(ProductVariant variant);
    }

    private final List<ProductVariant> variants = new ArrayList<>();
    private ProductVariant selectedVariant;
    private final OnVariantClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public VariantAdapter(OnVariantClickListener listener) {
        this.listener = listener;
        currencyFormat.setMaximumFractionDigits(0);
    }

    public void setVariants(List<ProductVariant> newVariants, ProductVariant selected) {
        this.variants.clear();
        if (newVariants != null) {
            this.variants.addAll(newVariants);
        }
        this.selectedVariant = selected;
        notifyDataSetChanged();
    }

    public void setSelectedVariant(ProductVariant selected) {
        this.selectedVariant = selected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVariantChipBinding binding = ItemVariantChipBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductVariant variant = variants.get(position);
        boolean isSelected = selectedVariant != null && selectedVariant.getId().equals(variant.getId());
        holder.bind(variant, isSelected);
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemVariantChipBinding binding;

        ViewHolder(ItemVariantChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductVariant variant, boolean isSelected) {
            String text = variant.getValue();
            if (variant.getAdditionalPrice() > 0) {
                text += " (+" + currencyFormat.format(variant.getAdditionalPrice()) + ")";
            }
            binding.tvVariantValue.setText(text);

            if (isSelected) {
                binding.tvVariantValue.setBackgroundResource(R.drawable.bg_chip_selected);
                binding.tvVariantValue.setTextColor(ContextCompat.getColor(
                        binding.getRoot().getContext(), R.color.white));
            } else {
                binding.tvVariantValue.setBackgroundResource(R.drawable.bg_chip_unselected);
                binding.tvVariantValue.setTextColor(ContextCompat.getColor(
                        binding.getRoot().getContext(), R.color.onefi_text_primary));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVariantClick(variant);
                }
            });
        }
    }
}
