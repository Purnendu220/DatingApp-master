package com.quintus.labs.datingapp.Utils;

import android.util.Log;

import com.quintus.labs.datingapp.MyApplication;


public class LogUtils {
    public static void networkError(String msg) {
        log("Network", "networkError" + msg);
    }

    public static void networkSuccess(String msg) {
        log("Network", "networkSuccess " + msg);
    }

    public static void xmppDebug(String msg) {
        log("XmppVarun", "Varun XMPP : " + msg);
    }

    public static void database(String msg) {
        log("DataBaseVarun", "SQL: " + msg);
    }

    public static void debug(String msg) {
        log("VarunDebug", msg);
    }





    public static void newMessagesXMPP(String msg) {
        log("newMessagesXMPP", "Varun XMPP : " + msg);
    }

    public static void newCheckerXMPP(String msg) {
        log("newCheckerXMPP", msg);
    }



    private static void log(String tag, String msg) {
        if (MyApplication.SHOW_LOG) {
            Log.d(tag, msg);
        }
    }


}
