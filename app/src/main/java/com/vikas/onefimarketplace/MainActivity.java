package com.vikas.onefimarketplace;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vikas.onefimarketplace.data.model.EmiPlan;
import com.vikas.onefimarketplace.data.model.Product;
import com.vikas.onefimarketplace.data.model.ProductVariant;
import com.vikas.onefimarketplace.databinding.ActivityMainBinding;
import com.vikas.onefimarketplace.ui.fragment.ConfirmationDialogFragment;
import com.vikas.onefimarketplace.ui.fragment.ProductDetailFragment;
import com.vikas.onefimarketplace.ui.fragment.ReviewFragment;
import com.vikas.onefimarketplace.ui.fragment.ShopFragment;

/**
 * Main Activity hosting authentic 1Fi 5-item bottom navigation and marketplace fragment stack.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupBottomNavigation();

        if (savedInstanceState == null) {
            // Default load Shop tab
            loadRootFragment(ShopFragment.newInstance());
            binding.bottomNavigation.setSelectedItemId(R.id.nav_shop);
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_shop) {
                clearBackStack();
                loadRootFragment(ShopFragment.newInstance());
                return true;
            } else if (itemId == R.id.nav_home) {
                showToast("1Fi Home");
                return true;
            } else if (itemId == R.id.nav_emi_dues) {
                showToast("1Fi EMI Dues");
                return true;
            } else if (itemId == R.id.nav_limit) {
                showToast("1Fi Investment Limit");
                return true;
            } else if (itemId == R.id.nav_profile) {
                showToast("1Fi Profile");
                return true;
            }
            return false;
        });
    }

    private void showToast(String label) {
        Toast.makeText(this, label + " section selected", Toast.LENGTH_SHORT).show();
    }

    private void loadRootFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                )
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void clearBackStack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void navigateToMarketplace() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_shop);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof ShopFragment) {
            ((ShopFragment) currentFragment).selectTab(2);
        } else {
            ShopFragment shopFragment = ShopFragment.newInstance();
            loadRootFragment(shopFragment);
            shopFragment.selectTab(2);
        }
    }

    public void navigateToProductDetail(String productId) {
        pushFragment(ProductDetailFragment.newInstance(productId));
    }

    public void navigateToReview(Product product, ProductVariant variant, EmiPlan emiPlan) {
        pushFragment(ReviewFragment.newInstance(product, variant, emiPlan));
    }

    public void showConfirmationDialog() {
        ConfirmationDialogFragment dialog = ConfirmationDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "ConfirmationDialog");
    }

    public void resetToMarketplace() {
        clearBackStack();
        navigateToMarketplace();
    }
}