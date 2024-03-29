package com.progmobile.meetchup.utils.form_data_with_validators;


import android.content.Context;

import com.progmobile.meetchup.R;

/**
 * Basic validator for confirmation password (it takes the original FormData password in its
 * constructor)
 * See {@link Validator}
 */
public class PasswordConfirmationValidator implements Validator<String> {

    private final FormData<String> originalPassword;


    public PasswordConfirmationValidator(FormData<String> originalPassword) {
        this.originalPassword = originalPassword;
    }

    @Override
    public boolean isValid(String value) {
        return value != null && value.equals(originalPassword.getValue());
    }

    @Override
    public boolean isValid(String value, boolean required) {
        if (required) return isValid(value);
        return value == null || value.equals(originalPassword.getValue());
    }

    @Override
    public String errorMessage(Context context) {
        return context.getString(R.string.validator_password_confirm);
    }
}
