package com.vikas.onefimarketplace.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vikas.onefimarketplace.MainActivity;
import com.vikas.onefimarketplace.R;
import com.vikas.onefimarketplace.databinding.DialogConfirmationBinding;

import java.util.Random;

/**
 * Confirmation dialog indicating purchase application has been successfully prepared.
 */
public class ConfirmationDialogFragment extends DialogFragment {

    private DialogConfirmationBinding binding;

    public static ConfirmationDialogFragment newInstance() {
        return new ConfirmationDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogConfirmationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int randomRefNumber = 100000 + new Random().nextInt(900000);
        binding.tvRefId.setText(getString(R.string.ref_id_format, String.valueOf(randomRefNumber)));

        binding.btnDoneConfirmation.setOnClickListener(v -> {
            dismiss();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).resetToMarketplace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
