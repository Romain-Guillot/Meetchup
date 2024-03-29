package com.progmobile.meetchup.ui.authentication.sign_in_form;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.progmobile.meetchup.R;
import com.progmobile.meetchup.utils.FormFragment;
import com.progmobile.meetchup.utils.form_views.TextFormLayout;

import java.util.Arrays;


/**
 * Subclass of FormFragment to handle the sign-in form.
 * See the FormFragment documentation to more details.
 * <p>
 * This fragment retrieve the following SignInViewModel associated.
 * It also init the form fields layout ...
 * - email
 * - password
 * ... and init the FormFragment with these layouts and the FormData associated in the
 * SignInViewModel.
 */
public class SignInFragment extends FormFragment {

    private SignInViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We "link" the view model to the activity to keep the same view model, even is the fragment instance change
        viewModel = ViewModelProviders.of(getActivity()).get(SignInViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication_signin, container, false);

        TextFormLayout emailLayout = view.findViewById(R.id.auth_signin_email_layout);
        emailLayout.bindFormData(viewModel.emailLive);
        TextFormLayout passwordLayout = view.findViewById(R.id.auth_signin_password_layout);
        passwordLayout.bindFormData(viewModel.passwordLive);

        Button submitButton = view.findViewById(R.id.form_submit_btn);

        init(
                viewModel,
                Arrays.asList(emailLayout, passwordLayout),
                submitButton,
                getString(R.string.auth_signin_btn),
                getString(R.string.loading_btn),
                null
        );

        return view;
    }
}
