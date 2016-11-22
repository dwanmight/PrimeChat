package com.junior.dwan.primechat.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junior.dwan.primechat.R;
import com.junior.dwan.primechat.data.managers.UserChatInfo;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by Might on 19.11.2016.
 */

public class ArrayListUtils {

    public static ArrayList<UserChatInfo> clearListFromNull(ArrayList<UserChatInfo> list) {
        ArrayList<UserChatInfo> tmpChatList = new ArrayList<UserChatInfo>();
        int size = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText() != null) {
                tmpChatList.add(size, list.get(i));
                size++;
            }
        }
        return tmpChatList;
    }

    public static void convertResultToUserChat(String[] mass,ArrayList<UserChatInfo> list) {
        list.clear();
        for (int i = 0; i < mass.length; i++) {
            UserChatInfo userChatInfo = new UserChatInfo();
            list.add(userChatInfo);
            if (mass[i].contains("resId")) {
            } else if (mass[i].contains("resAuthor")) {
                String s = mass[i];
                s = s.replace("resAuthor", "");
                list.get(i).setLogin(s);
            } else if (mass[i].contains("resData")) {
                String s = mass[i];
                s = s.replace("resData", "");
                list.get(i - 1).setDate(s);
            } else if (mass[i].contains("resText")) {
                String s = mass[i];
                s = s.replace("resText", "");
                s = s.replace("%20", " ");
                list.get(i - 2).setText(s);
            } else
                break;
        }
    }

}
