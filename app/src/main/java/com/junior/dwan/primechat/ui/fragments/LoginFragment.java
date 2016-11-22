package com.junior.dwan.primechat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.junior.dwan.primechat.R;
import com.junior.dwan.primechat.data.managers.DataManager;
import com.junior.dwan.primechat.data.managers.LoginInfo;
import com.junior.dwan.primechat.ui.activities.ChatActivity;
import com.junior.dwan.primechat.utils.CheckOnline;
import com.junior.dwan.primechat.utils.ConstantManagers;
import com.junior.dwan.primechat.utils.SnackBars;
import com.junior.dwan.primechat.utils.ValidateEditText;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Might on 17.11.2016.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    DataManager mDataManager;
    LoginInfo mLoginInfo;
    Handler mHandler;
    @BindView(R.id.et_login_email)
    EditText mLoginEmailET;
    @BindView(R.id.et_login_password)
    EditText mLoginPassET;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.coordinator_login)
    CoordinatorLayout mCoordinatorLayout;
    private Unbinder unbind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getINSTANCE();
        mLoginInfo = new LoginInfo();
        setupHandler();

    }

    private void setupHandler() {
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case ConstantManagers.STATUS_NONE:
                        return;
                    case ConstantManagers.STATUS_CONNECTING:
                        startChatActivity();
                        break;
                }
            }
        };
        mHandler.sendEmptyMessage(ConstantManagers.STATUS_NONE);
    }

    private void startChatActivity() {
        Intent i = new Intent(getActivity(), ChatActivity.class);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbind = ButterKnife.bind(this, view);
        loginBtn.setOnClickListener(this);

        mDataManager.getPreferencesManager().saveUserProfileData("data", "value");


        return view;
    }

    @Override
    public void onClick(View v) {
        if (CheckOnline.isOnline(getActivity())) {
            if (ValidateEditText.checkForValidEditText(mLoginEmailET))
                mLoginInfo.setLogin(mLoginEmailET.getText().toString());

            if (ValidateEditText.checkForValidEditText(mLoginPassET))
                mLoginInfo.setPassword(mLoginPassET.getText().toString());

            if (ValidateEditText.isReadyForTransfer(mLoginInfo)) {

                loginCheck();
            } else {
                Snackbar snackbar = SnackBars.showSnackBar(getActivity(),
                        mCoordinatorLayout, getString(R.string.login_toast_mess_invalid));
                snackbar.show();
            }
        } else {
            Snackbar snackbar = SnackBars.showSnackBar(getActivity(),
                    mCoordinatorLayout, getString(R.string.login_toast_mess_internet));
            snackbar.show();
        }
    }


    private void loginCheck() {
        String login = mLoginInfo.getLogin();
        String password = mLoginInfo.getPassword();

        Call<ResponseBody> call = mDataManager.getloginInfo(login, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    String s = null;
                    String r[];
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    r = s.split(" ");
                    if (r[0].equals("false")) {
                        Snackbar snackbar = SnackBars.showSnackBar(getActivity(),
                                mCoordinatorLayout, getString(R.string.login_toast_incorrect_data));
                        snackbar.show();
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_NONE);
                    } else {
                        clearResponseAndSaveData(r);
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_CONNECTING);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("TAGTAG", "Connection failed");
            }
        });
    }

    private void clearResponseAndSaveData(String[] r) {
        String email = r[0].replace("responceLogin", "");
        String pass = r[1].replace("responcePass", "");
        mDataManager.getPreferencesManager().saveUserProfileData(email, pass);
        Log.i("TAG", email + " " + pass);
        Log.i("TAG", "LoginInfo was saved to Pref");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
