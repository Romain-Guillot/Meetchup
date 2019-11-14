package com.progmobile.meetchup.utils.form_data_with_validators;


import android.content.Context;

import com.progmobile.meetchup.R;

/**
 * Basic validator for confirmation password (it takes the original FormData password in its
 * constructor)
 * See {@link Validator}
 */
public class PasswordConfirmationValidator implements Validator {

    private final FormData originalPassword;


    public PasswordConfirmationValidator(FormData originalPassword) {
        this.originalPassword = originalPassword;
    }

    @Override
    public boolean isValid(String value) {
        return value != null && value.equals(originalPassword.getValue());
    }

    @Override
    public String errorMessage(Context context) {
        return context.getString(R.string.validator_password_confirm);
    }
}