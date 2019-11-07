package com.example.appprojet.utils;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appprojet.R;
import com.example.appprojet.utils.form_data_with_validators.FormData;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Iterator;
import java.util.List;


/**
 * Handle form UI, the business logic is handle by a FormViewModel associated.
 *
 * The fragment layout required these following views to handle the form :
 *  - a submit button with the id "form_submit_btn"
 *  - a text view to display errors with the id "form_error_textview"
 *
 * To init the fragment call the init method with the required parameters. The method will make the
 * links between the form text fields and the fragment form data that holds the form
 * data.
 *
 * It also initialize the listener on the submit button to notify the view model to process
 * the form. Finally, the init method observe the FormViewModel errorLive and isLoadingLive flags to
 * update to UI accordingly.
 *
 * See the FormViewModel for more details about the form process.
 */
public abstract class FormFragment extends Fragment {

    /**
     * main function, refer to the class documentation
     *
     * Required the following parameters :
     *  - view: the fragment view
     *  - viewModel: the fragment view model
     *  - textInputLayoutList: all form fields layouts
     *  - formDataList: form data corresponding to the fields layout
     *      i.e. formDataList[i] refers to the input text included in the layout textInputLayoutList[i]
     */
    protected void init(View view, FormViewModel viewModel, List<TextInputLayout> textInputLayoutList, List<FormData> formDataList, String submitText, String loadingText) {
        // init views
        Button submitButton = view.findViewById(R.id.form_submit_btn);
        TextView errorTextView = view.findViewById(R.id.form_error_textview);

        // throw an exception if the parameters of the view fragment is invalid
        if (textInputLayoutList.size() != formDataList.size() || submitButton == null || errorTextView == null)
            throw new RuntimeException("Wrong usage of the FormFragment");

        // link all form edit texts with the live data
        Iterator<TextInputLayout> textInputLayoutsIterator = textInputLayoutList.iterator();
        Iterator<FormData> formLiveDataIterator = formDataList.iterator();
        while (textInputLayoutsIterator.hasNext() && formLiveDataIterator.hasNext()) {
            setOnFieldChanged(textInputLayoutsIterator.next(), formLiveDataIterator.next());
        }

        // handle the submit button
        // -> notify the view model to process the form
        // -> update the field layouts errors to indicate the bad formatting errors
        submitButton.setOnClickListener(v -> {
            viewModel.submitForm();
            Iterator<TextInputLayout> _textInputLayoutsIterator = textInputLayoutList.iterator();
            Iterator<FormData> _formLiveDataIterator = formDataList.iterator();
            while (_textInputLayoutsIterator.hasNext() && _formLiveDataIterator.hasNext()) {
                setLayoutFieldError(_textInputLayoutsIterator.next(),  _formLiveDataIterator.next());
            }
        });

        // update the submit button based on the loading form status
        viewModel.isLoadingLive.observe(this, isLoading -> {
            submitButton.setEnabled(!isLoading);
            submitButton.setText(isLoading ? loadingText : submitText);
        });

        // update the submit error text based on the error message (if any)
        viewModel.errorLive.observe(this, error -> {
            errorTextView.setVisibility(error != null && !error.isEmpty() ? View.VISIBLE : View.GONE);
            errorTextView.setText(error);
        });
    }

    /**
     * Add listeners to the editTextLayout edit text to change the corresponding editTextData when
     * the edit text content changed.
     * We also set the layout edit text error (if any) when we unfocused the edit text
     */
    protected void setOnFieldChanged(TextInputLayout editTextLayout, FormData editTextData) {
        EditText editText = editTextLayout.getEditText();

        if (editText == null)
            throw new RuntimeException("No edit text attached to the text input layout");

        editText.setText(editTextData.getValue());

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextData.setValue(s.toString());
            }
        });

        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) setLayoutFieldError(editTextLayout, editTextData);
            else editTextLayout.setError(null);
        });
    }

    /** Set the error indicator of the layout is the formData is not valid*/
    protected void setLayoutFieldError(TextInputLayout layout, FormData formData) {
        layout.setError(!formData.isValid() ? formData.getError(getContext()) : null);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new RuntimeException("Unexpected fragment creation");
    }
}