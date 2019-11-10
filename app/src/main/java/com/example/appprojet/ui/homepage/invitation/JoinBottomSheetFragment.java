package com.example.appprojet.ui.homepage.invitation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.appprojet.R;
import com.example.appprojet.utils.SnackbarFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class JoinBottomSheetFragment extends BottomSheetDialogFragment {

    private JoinBottomSheetViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(JoinBottomSheetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        showKeyboard();
        View view = inflater.inflate(R.layout.fragment_join_event, container, false);

        TextInputLayout invitKeyTextLayout = view.findViewById(R.id.join_event_key_layout);
        EditText editText = invitKeyTextLayout.getEditText();
        editText.requestFocus();
        Button submitButton = view.findViewById(R.id.join_event_btn);

        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.keyFormData = s.toString();
            }
        });

        submitButton.setOnClickListener(v -> {
            viewModel.submitForm();
        });

        viewModel.isLoadingLive.observe(this, isLoading -> {
            submitButton.setEnabled(!isLoading);
            submitButton.setText(isLoading ? R.string.loading_btn : R.string.join_event_btn);
        });

        viewModel.errorLive.observe(this, error -> {
            String message = error.getContentIfNotHandled();
            if (message != null)
                SnackbarFactory.showTopErrorSnackbar(getActivity().findViewById(R.id.container), message);
        });

        viewModel.successLive.observe(this, success -> {
            if (success.getContentIfNotHandled() != null) {
                SnackbarFactory.showTopSuccessSnackbar(getActivity().findViewById(R.id.container), "Event added !");
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeKeyboard();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.e(">>>>>>>>>", "CREATE DIALOG");
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
        Log.e(">>>>>>>>>", "SHOW");
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}