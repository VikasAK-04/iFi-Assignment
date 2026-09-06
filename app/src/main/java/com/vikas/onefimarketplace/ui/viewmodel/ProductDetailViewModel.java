package com.vikas.onefimarketplace.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vikas.onefimarketplace.data.datasource.ProductDataSource;
import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.ProductVariant;
import com.vikas.onefimarketplace.data.model.Resource;
import com.vikas.onefimarketplace.data.repository.ProductRepository;

/**
 * ViewModel managing Product Detail, Variant selection, and EMI calculation state.
 */
public class ProductDetailViewModel extends ViewModel {

    private final ProductRepository repository;

    private final MutableLiveData<Resource<Product>> productStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProductVariant> selectedVariantLiveData = new MutableLiveData<>();
    private final MutableLiveData<EmiPlan> selectedEmiPlanLiveData = new MutableLiveData<>();
    private final MutableLiveData<Double> calculatedTotalPriceLiveData = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> calculatedMonthlyEmiLiveData = new MutableLiveData<>(0.0);

    public ProductDetailViewModel(ProductRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<Product>> getProductState() {
        return productStateLiveData;
    }

    public LiveData<ProductVariant> getSelectedVariant() {
        return selectedVariantLiveData;
    }

    public LiveData<EmiPlan> getSelectedEmiPlan() {
        return selectedEmiPlanLiveData;
    }

    public LiveData<Double> getCalculatedTotalPrice() {
        return calculatedTotalPriceLiveData;
    }

    public LiveData<Double> getCalculatedMonthlyEmi() {
        return calculatedMonthlyEmiLiveData;
    }

    public void loadProductDetails(String productId) {
        productStateLiveData.setValue(Resource.loading(null));

        repository.getProductById(productId, new ProductDataSource.Callback<Product>() {
            @Override
            public void onSuccess(Product product) {
                productStateLiveData.setValue(Resource.success(product));

                // Default select first variant if available
                if (product.getVariants() != null && !product.getVariants().isEmpty()) {
                    selectVariant(product.getVariants().get(0), product);
                } else {
                    recalculatePrices(product, null, selectedEmiPlanLiveData.getValue());
                }

                // Default select first EMI plan if available
                if (product.getEmiPlans() != null && !product.getEmiPlans().isEmpty()) {
                    selectEmiPlan(product.getEmiPlans().get(0), product);
                }
            }

            @Override
            public void onError(Exception exception) {
                String msg = exception != null && exception.getMessage() != null
                        ? exception.getMessage()
                        : "Failed to load product details";
                productStateLiveData.setValue(Resource.error(msg, null));
            }
        });
    }

    public void selectVariant(ProductVariant variant, Product currentProduct) {
        selectedVariantLiveData.setValue(variant);
        recalculatePrices(currentProduct, variant, selectedEmiPlanLiveData.getValue());
    }

    public void selectEmiPlan(EmiPlan emiPlan, Product currentProduct) {
        selectedEmiPlanLiveData.setValue(emiPlan);
        recalculatePrices(currentProduct, selectedVariantLiveData.getValue(), emiPlan);
    }

    private void recalculatePrices(Product product, ProductVariant variant, EmiPlan emiPlan) {
        if (product == null) return;

        double base = product.getBasePrice();
        double extra = variant != null ? variant.getAdditionalPrice() : 0.0;
        double totalPrice = base + extra;

        calculatedTotalPriceLiveData.setValue(totalPrice);

        if (emiPlan != null) {
            double monthly = emiPlan.calculateMonthlyAmount(totalPrice);
            calculatedMonthlyEmiLiveData.setValue(monthly);
        } else {
            calculatedMonthlyEmiLiveData.setValue(totalPrice);
        }
    }
}
