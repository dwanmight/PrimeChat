package com.junior.dwan.primechat.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.junior.dwan.primechat.R;
import com.junior.dwan.primechat.data.managers.DataManager;
import com.junior.dwan.primechat.data.managers.LoginInfo;
import com.junior.dwan.primechat.data.managers.UserChatInfo;
import com.junior.dwan.primechat.utils.ArrayListUtils;
import com.junior.dwan.primechat.utils.ConstantManagers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Might on 17.11.2016.
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    DataManager mDataManager;
    Handler mHandler;
    LoginInfo mLoginInfo;
    Timer mTimer;
    TimerTask mTimerTask;
    ArrayList<UserChatInfo> mUserChatInfoArrayList;
    boolean isNewMessage;

    @BindView(R.id.chat_message_tv)
    EditText mMessageET;
    @BindView(R.id.chat_send_btn)
    TextView mSendBtn;
    @BindView(R.id.chat_scroll)
    ScrollView mScrollTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mSendBtn.setOnClickListener(this);
        mDataManager = DataManager.getINSTANCE();
        mUserChatInfoArrayList = DataManager.getINSTANCE().getUserChatInfoList();
        isNewMessage = false;
        disableKeyboard();
        loadDataLoginInfo();
        setupHandler();
        setupTimer();
        getMessageFromServer();
    }

    private void setupTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                getMessageFromServer();
            }
        };
        mTimer.schedule(mTimerTask, 0, 3000);
    }

    private void loadDataLoginInfo() {
        List<String> listData = mDataManager.getPreferencesManager().loadUserProfileData();
        mLoginInfo = new LoginInfo();
        mLoginInfo.setLogin(listData.get(0));
        mLoginInfo.setPassword(listData.get(1));
        Toast.makeText(this, getString(R.string.chat_toast_wllcome) + mLoginInfo.getLogin(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        disableKeyboard();
        sendMessageToServer();
    }

    private void sendMessageToServer() {
        if (mMessageET.getText().toString().length() == 0) {
            return;
        }
        String action = "save";
        String author = mLoginInfo.getLogin();
        String date = new SimpleDateFormat("dd:MM:yyyy/HH:mm:ss").format(new Date());
        String text = mMessageET.getText().toString();
        text = text.replace(" ", "%20");

        Call<ResponseBody> call = mDataManager.sendMessageToServer(action, author, date, text);
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
                    if (r[0].equals("Senddata")) {
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_SEND_MESSAGES);
                        Log.i("TAG", r[0] + " ");
                    } else if (r[0].equals("Incorrect")) {
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_SEND_NOT_MESSAGES);
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

    private void getMessageFromServer() {
        List<String> list = mDataManager.getPreferencesManager().loadUserProfileData();
        String login = list.get(0);
        String pass = list.get(1);

        Call<ResponseBody> call = mDataManager.getChatMessages(login, pass);
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
                    if (!r[0].equals("Incorrect")) {
                        ArrayListUtils.convertResultToUserChat(r, mUserChatInfoArrayList);
                        mHandler.sendEmptyMessage(ConstantManagers.STATUS_GOT_MESSAGES);
                    } else if (r[0].equals("Incorrect")) {
                        Log.i("TAGTAG", " incorrect data");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void disableKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupHandler() {
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case ConstantManagers.STATUS_SEND_NOT_MESSAGES:
                        return;
                    case ConstantManagers.STATUS_SEND_MESSAGES:
                        disableKeyboard();
                        mMessageET.setText("");
                        getMessageFromServer();
                        break;
                    case ConstantManagers.STATUS_GOT_MESSAGES:
                        mUserChatInfoArrayList = ArrayListUtils.clearListFromNull(mUserChatInfoArrayList);
                        if (!isNewMessage) {
                            setupViewAsAdapter();
                            mScrollTV.fullScroll(View.FOCUS_DOWN);
                            isNewMessage = true;
                        }
                        else if (!isCheckNewMessForBottomScroll()) {
                            setupViewAsAdapter();
                            mScrollTV.fullScroll(View.FOCUS_DOWN);
                        }
                }
            }
        };
        mHandler.sendEmptyMessage(ConstantManagers.STATUS_NONE);
    }

    private boolean isCheckNewMessForBottomScroll() {
        List<String> list = mDataManager.getPreferencesManager().loadUserChatForCheckNewMess();
        String login = list.get(0);
        String data = list.get(1);
        String text = list.get(2);
        int lastItem = mUserChatInfoArrayList.size() - 1;
        UserChatInfo userChatInfo = mUserChatInfoArrayList.get(lastItem);

        if ((userChatInfo.getLogin().equals(login)) & (userChatInfo.getDate().equals(data))
                & (userChatInfo.getText().equals(text))) {
            return true;
        } else {
            mDataManager.getPreferencesManager().saveUserChatForCheckNewMess(
                    userChatInfo.getLogin(),
                    userChatInfo.getDate(),
                    userChatInfo.getText());
            return false;
        }
    }

    private void setupViewAsAdapter() {
        Log.i("TAGTAG", "SETUP ADAPTER");
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.chat_linLayout);
        linLayout.removeAllViews();
        LayoutInflater ltInflater = getLayoutInflater();
        for (int i = 0; i < mUserChatInfoArrayList.size(); i++) {
            Log.i("myLogs", "i = " + i);
            View item = ltInflater.inflate(R.layout.list_item_chat, linLayout, false);
            TextView tvName = (TextView) item.findViewById(R.id.item_login);
            tvName.setText(mUserChatInfoArrayList.get(i).getLogin());
            TextView tvData = (TextView) item.findViewById(R.id.item_data);
            tvData.setText(mUserChatInfoArrayList.get(i).getDate());
            TextView tvMess = (TextView) item.findViewById(R.id.item_message);
            tvMess.setText(mUserChatInfoArrayList.get(i).getText());
            item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            linLayout.addView(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTimer.cancel();
    }
}
