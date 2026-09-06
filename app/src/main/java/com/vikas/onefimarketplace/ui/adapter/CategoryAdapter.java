package com.vikas.onefimarketplace.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.databinding.ItemCategoryChipBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Marketplace category filter chips.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    private final List<String> categories = new ArrayList<>();
    private String selectedCategory = "All";
    private final OnCategoryClickListener listener;

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<String> newCategories, String selected) {
        this.categories.clear();
        if (newCategories != null) {
            this.categories.addAll(newCategories);
        }
        this.selectedCategory = selected != null ? selected : "All";
        notifyDataSetChanged();
    }

    public void setSelectedCategory(String selected) {
        this.selectedCategory = selected != null ? selected : "All";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryChipBinding binding = ItemCategoryChipBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category, category.equalsIgnoreCase(selectedCategory));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryChipBinding binding;

        ViewHolder(ItemCategoryChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String category, boolean isSelected) {
            binding.tvCategoryName.setText(category);

            if (isSelected) {
                binding.tvCategoryName.setBackgroundResource(R.drawable.bg_chip_selected);
                binding.tvCategoryName.setTextColor(ContextCompat.getColor(
                        binding.getRoot().getContext(), R.color.white));
            } else {
                binding.tvCategoryName.setBackgroundResource(R.drawable.bg_chip_unselected);
                binding.tvCategoryName.setTextColor(ContextCompat.getColor(
                        binding.getRoot().getContext(), R.color.onefi_text_secondary));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
