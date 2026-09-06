package com.vikas.onefimarketplace.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.databinding.ItemEmiPlanCardBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for EMI option selection cards in Product Details.
 */
public class EmiPlanAdapter extends RecyclerView.Adapter<EmiPlanAdapter.ViewHolder> {

    public interface OnEmiPlanClickListener {
        void onEmiPlanClick(EmiPlan plan);
    }

    private final List<EmiPlan> plans = new ArrayList<>();
    private EmiPlan selectedPlan;
    private double currentTotalPrice = 0.0;
    private final OnEmiPlanClickListener listener;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public EmiPlanAdapter(OnEmiPlanClickListener listener) {
        this.listener = listener;
        currencyFormat.setMaximumFractionDigits(0);
    }

    public void setEmiPlans(List<EmiPlan> newPlans, EmiPlan selected, double totalPrice) {
        this.plans.clear();
        if (newPlans != null) {
            this.plans.addAll(newPlans);
        }
        this.selectedPlan = selected;
        this.currentTotalPrice = totalPrice;
        notifyDataSetChanged();
    }

    public void setSelectedPlan(EmiPlan selected) {
        this.selectedPlan = selected;
        notifyDataSetChanged();
    }

    public void updateTotalPrice(double totalPrice) {
        this.currentTotalPrice = totalPrice;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEmiPlanCardBinding binding = ItemEmiPlanCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmiPlan plan = plans.get(position);
        boolean isSelected = selectedPlan != null && selectedPlan.getId().equals(plan.getId());
        holder.bind(plan, isSelected, currentTotalPrice);
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemEmiPlanCardBinding binding;

        ViewHolder(ItemEmiPlanCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EmiPlan plan, boolean isSelected, double totalPrice) {
            // Tenure Title
            binding.tvTenure.setText(binding.getRoot().getContext().getString(
                    R.string.tenure_format, plan.getTenureMonths()));

            // No-Cost Badge Visibility
            if (plan.isNoCostEmi()) {
                binding.tvNoCostBadge.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoCostBadge.setVisibility(View.GONE);
            }

            // Monthly EMI
            double monthly = plan.calculateMonthlyAmount(totalPrice);
            binding.tvMonthlyAmount.setText(binding.getRoot().getContext().getString(
                    R.string.monthly_amount_format, currencyFormat.format(monthly).replace("₹", "")));

            // Total Payable Details
            double totalPayable = plan.calculateTotalAmount(totalPrice);
            String feeStr = plan.getProcessingFee() <= 0
                    ? binding.getRoot().getContext().getString(R.string.processing_fee_free)
                    : currencyFormat.format(plan.getProcessingFee());

            binding.tvTotalPayable.setText(String.format(Locale.getDefault(),
                    "Total Payable: %s • Processing fee: %s",
                    currencyFormat.format(totalPayable), feeStr));

            // Selected UI State
            if (isSelected) {
                binding.cardEmiPlan.setCardBackgroundColor(binding.getRoot().getContext().getColor(R.color.onefi_purple_surface));
                binding.cardEmiPlan.setStrokeColor(binding.getRoot().getContext().getColor(R.color.onefi_purple_primary));
                binding.cardEmiPlan.setStrokeWidth(4);
                binding.ivRadioState.setImageResource(R.drawable.ic_radio_selected);
            } else {
                binding.cardEmiPlan.setCardBackgroundColor(binding.getRoot().getContext().getColor(R.color.onefi_card_bg));
                binding.cardEmiPlan.setStrokeColor(binding.getRoot().getContext().getColor(R.color.onefi_border));
                binding.cardEmiPlan.setStrokeWidth(2);
                binding.ivRadioState.setImageResource(R.drawable.ic_radio_unselected);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEmiPlanClick(plan);
                }
            });
        }
    }
}
