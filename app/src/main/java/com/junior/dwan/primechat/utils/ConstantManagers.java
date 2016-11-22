package com.junior.dwan.primechat.utils;

/**
 * Created by Might on 18.11.2016.
 */

public interface ConstantManagers {
    //Preferences
    String PREFERENCES_LOGIN = "PREFERENCES_LOGIN";
    String PREFERENCES_PASS = "PREFERENCES_PASS";
      //check for a new messages
    String PREFERENCES_CHECK_LOGIN = "PREFERENCES_CHECK_NEW_LOGIN";
    String PREFERENCES_CHECK_DATE = "PREFERENCES_CHECK_NEW_DATE";
    String PREFERENCES_CHECK_TEXT = "PREFERENCES_CHECK_NEW_TEXT";

    //Handler
    int STATUS_NONE = 0; // нет подключения
    int STATUS_CONNECTING = 1; // подключаемся
    int STATUS_SEND_MESSAGES = 2; // Отправлено
    int STATUS_SEND_NOT_MESSAGES = 3; // не отправлено
    int STATUS_GOT_MESSAGES = 4; // получены чаты с сервера



}
