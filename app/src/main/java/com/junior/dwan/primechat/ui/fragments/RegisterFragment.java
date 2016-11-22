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

public class RegisterFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.et_register_email)
    EditText mRegisterEmailET;
    @BindView(R.id.et_register_password)
    EditText mRegisterPassET;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.coordinator_register)

    CoordinatorLayout mCoordinatorLayout;
    private Unbinder unbind;
    DataManager mDataManager;
    LoginInfo mLoginInfo;
    Handler mHandler;

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
                        clearResponseAndSaveData();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbind = ButterKnife.bind(this, view);
        registerBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (CheckOnline.isOnline(getActivity())) {
            if (ValidateEditText.checkForValidEditText(mRegisterEmailET))
                mLoginInfo.setLogin(mRegisterEmailET.getText().toString());

            if (ValidateEditText.checkForValidEditText(mRegisterPassET))
                mLoginInfo.setPassword(mRegisterPassET.getText().toString());

            if (ValidateEditText.isReadyForTransfer(mLoginInfo)) {

                registerCheck();

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


    private void registerCheck() {
        String login = mLoginInfo.getLogin();
        String password = mLoginInfo.getPassword();
        String action = "save";


        Call<ResponseBody> call = mDataManager.createUser(action, login, password);
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
                    if (r[0].equals("Saved")) {
                        Log.i("TAG", r[0] + " ");
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_CONNECTING);
                    } else if (r[0].equals("Incorrect")) {
                        Snackbar snackbar = SnackBars.showSnackBar(getActivity(),
                                mCoordinatorLayout, getString(R.string.register_toast_login_used));
                        snackbar.show();
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_NONE);
                        Log.i("TAGTAG", " incorrect data");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("TAGTAG", "Connection failed");
            }
        });
    }

    private void clearResponseAndSaveData() {
        String email = mLoginInfo.getLogin();
        String pass = mLoginInfo.getPassword();
        mDataManager.getPreferencesManager().saveUserProfileData(email, pass);
        Log.i("TAG", "LoginInfo register was saved to Pref" + email + " " + pass);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
