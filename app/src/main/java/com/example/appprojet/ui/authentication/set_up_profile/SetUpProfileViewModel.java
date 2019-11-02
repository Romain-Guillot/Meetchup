package com.example.appprojet.ui.authentication.set_up_profile;

import androidx.lifecycle.MutableLiveData;

import com.example.appprojet.models.User;
import com.example.appprojet.ui.authentication.FormViewModel;
import com.example.appprojet.utils.custom_live_data.FormData;
import com.example.appprojet.utils.custom_live_data.NameValidator;
import com.example.appprojet.utils.Callback;


/**
 * ViewModel that extends the FormViewModel to handle the set up profile form data and communicate
 * with the authentication repository.
 * See the FormViewModel documentation for more details.
 *
 * There are only one form data :
 *  - the name form data
 *
 * Note: the data validation is performed by Validator instances that are directly given to the form
 * data when creating them.
 *
 * Note : this is the last step of the authentication process. When the set up processes is finished
 * this flag is set to true. There may be a more elegant solution to be found
 */
public class SetUpProfileViewModel extends FormViewModel {

    final FormData nameLive = new FormData(new NameValidator());

    final MutableLiveData<Boolean> isFinish = new MutableLiveData<>(false);


    public SetUpProfileViewModel() {
        super();
        User user = authenticationRepository.getUser();
        if (user != null)
            nameLive.setValue(user.getName());
    }

    @Override
    protected void submitForm() {
        if (validate()) {
            isLoadingLive.setValue(true);
            String name = nameLive.getValue();
            authenticationRepository.updateName(name, new Callback<User>() {
                @Override
                public void onSucceed(User result) {
                    submitCallback.onSucceed(result);
                    isFinish.setValue(true);
                }

                @Override
                public void onFail(Exception e) {
                    submitCallback.onFail(e);
                }
            });
        }
    }


    @Override
    protected boolean validate() {
        return nameLive.isValid();
    }
}
