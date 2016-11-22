package com.junior.dwan.primechat.utils;

import android.widget.EditText;

import com.junior.dwan.primechat.data.managers.LoginInfo;

/**
 * Created by Might on 18.11.2016.
 */

public class ValidateEditText {

    public ValidateEditText() {
    }

    public static  boolean checkForValidEditText(EditText isValidET) {

        if (isValidET.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static  boolean isReadyForTransfer(LoginInfo loginInfo) {
        if (loginInfo.getLogin() != null & loginInfo.getPassword() != null) {
            if (loginInfo.isValid())
                return true;
        } else
            return false;
        return false;
    }
}
