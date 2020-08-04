package com.quintus.labs.datingapp.Utils;

import android.content.Context;
import android.content.res.Resources;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class AppConstants {

    public static final int GPS_REQUEST = 1001;
    public static final int LOCATION_REQUEST = 1000;
    public static final String ACCEPTED = "Accepted";
    public static final String REJECTED = "Rejected";


    public static class Pref {
        public static final String NAME = "dating_app";
        public static final String LOGIN_MODEL_OBJECT = "login_detail_model_object";
        public static final String USER_MODEL_OBJECT = "user_detail_model_object";
        public static final String AUTH_TOKEN = "auth_token";
        public static final String FCM_TOKEN = "fcm_token";

    }

    public static class Navigation {
        public static final int HOME_PAGE = 001;


    }


    public static class RequestCode {
        public static final int MY_PERMISSIONS_REQUEST = 101;
        public static final int VIDEO_TRIMMER_REQUEST = 102;
        public static final int MEDIA_PICKER_REQUEST = 103;
        public static final int DOCUMENT_FILES_REQUEST = 104;
        public static final int DOCUMENT_FILES_REQUEST_EXIST_MYU = 105;
        public static final int DOCUMENT_FILES_REQUEST_EXIST_MYU_SEARCH = 106;
        public static final int SCAN_CODE_REQUEST = 107;
    }

    public static class ApiParamValue {
        public static final String OBJECT_TYPE_NEWS = "news";
        public static final String AUTHENTICATION_ERROR = "401";
        public static final String FORCE_UPDATE_ERROR = "426";
        public static final String RESPONSE_ERROR = "5XX";
        public static final String SUCCESS_RESPONSE_CODE = "2XX";

        /**
         * for mediaName value
         ***/

    }




    public static class ApiParamKey {
        public static final String ID = "image-id";
        public static String DEBUG = "debug";
        public static final String USTADJI_AUTH_TOKEN = "x-auth";
        public static final String APP_LANGUAGE = "Content-Language";
        public static final String APP_TIMEZONE = "timeZone";
        public static final String APP_VERSION = "appVersion";

    }



    public static final class Url {

        public static String BASE_SERVICE_LIVE = "http://149.28.149.131:8000/";
        public static String BASE_SERVICE_BETA = "https://beta-api.ustadji.co/";
        public static String BASE_SERVICE_ALPHA = "https://qa-api.ustadji.co/";

        public static final String LOGIN = "api/v1/login";
        public static final String SIGNUP = "api/v1/user";
        public static final String CATEGORYLIST = "api/v1/getCategoryList";
        public static final String SUBCATEGORYLIST = "api/v1/getServiceList";
        public static final String ADDADRESS = "api/v1/address";
        public static final String PROVIDERLIST = "api/v1/getProviderList";
        public static final String SAVEBOOKING = "api/v1/booking";
        public static final String GETUSER = "api/v1/user";
        public static final String SENDFEEDBACK = "api/v1/help";
        public static final String LOGOUT = "api/v1/logout";
        public static final String CHANGEPASSWORD = "api/v1/change-password";
        public static final String INITIATEPAYMENT = "api/v1/booking-service/initiate-payment";
        public static final String FINALPAYMENT =  "api/v1/booking-service/payment";
        public static final String INITIATE = "api/v1/rsa/initiate";
        public static final String RSA ="api/v1/rsa";
        public static  final String RSAPayment ="api/v1/rsa/payment";
        public static final String FEEDS = "/api/v1/feeds";
        public static final String UPLOADIMAGE = "api/v1/image-upload";
        public static final String DELETEIMAGE = "api/v1/image-upload/{image-id}";
        public static final String FRIEND_LIST = "api/v1/friends";
        public static final String REQUEST_FRIEND = "api/v1/friend-user";
        public static final String CHATUPLOAD = "api/v1/chat-upload";
        public static final String PLAN_LIST = "api/v1/package";
        public static final String GET_PAYMENT_INTENT = "api/v1/initiate-payment";
        public static final String PAYMENT_URL = "/api/v1/payment";
        public static final String SUPER_LIKE = "api/v1/super-like";
        public static final String BLOCK = "api/v1/block";
        public static final String UNBLOCK = "api/v1/unblock";
        public static final String UPDATE_DEVICE_TOKEN = "/api/v1/updateToken";







    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

    private static final Pattern USERNAME_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_.]+$");



    public static boolean isEmailValid(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean isUsernameValid(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }


    public static String getDate(long milliSeconds, String REQUIRED_DATETIME_FORMAT) {
        // Create a DateFormatter object for displaying date in specified format.
        String date;
        String time;
        SimpleDateFormat formatter = new SimpleDateFormat(REQUIRED_DATETIME_FORMAT);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }



    public static String getDateOnlyFromMilli(long createddate) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(createddate);
        SimpleDateFormat chatHeaderFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        String attendanceDate = chatHeaderFormatter.format(time.getTime());
//        int hour = time.get(Calendar.HOUR);
//        int min = time.get(Calendar.MINUTE);
        return attendanceDate;
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static class Chat {

        //        public static final int TYPE_SINGLE_CHAT = 1;
//        public static final int TYPE_GROUP_CHAT = 2;
        public static final int TYPE_SINGLE_CHAT = 0;
        public static final int TYPE_GROUP_CHAT = 1;

        public static final String MESSAGE_TYPE_SENT = "sent";
        public static final String MESSAGE_TYPE_RECEIVED = "received";

        public static final int ROLE_MEMBER = 0;
        public static final int ROLE_NOT_MEMBER = -1;
        public static final int ROLE_ADMIN = 1;
        public static final int ROLE_OWNER = 2;
        public static final int ROLE_DESTROYED = -2;

        public static final String SERVER_ROLE_OWNER = "owner";
        public static final String SERVER_ROLE_MEMBER = "user";
        public static final String SERVER_ROLE_NONE = "none";

        public static final String TYPE_CHAT_IMAGE = "chat_image";
        public static final String TYPE_CHAT_TEXT = "chat_text";
        public static final String TYPE_CHAT_VIDEO = "chat_video";
        public static final String TYPE_CHAT_AUDIO = "chat_audio";
        public static final String TYPE_CHAT_DOC = "chat_doc";
        public static final String TYPE_CHAT_LOCATION = "chat_location";
        public static final String TYPE_CHAT_CONTACT = "chat_contact";

//        public static final String TYPE_GROUP_PARTICIPANT_ADDED = "group_participant_added";
//        public static final String TYPE_GROUP_PARTICIPANT_DELETED = "group_participant_deleted";
//        public static final String TYPE_GROUP_NAME_CHANGE = "group_name_change";
//        public static final String TYPE_GROUP_DELETED = "group_deleted";

        public static final String TYPE_NORMAL = "normal";
        public static final String TYPE_CHAT = "chat";
        public static final String TYPE_CHAT_GROUP = "groupchat";

        public static final String TYPE_GROUP_PARTICIPANT_ADDED = "subscribe";
        public static final String TYPE_GROUP_PARTICIPANT_DELETED = "unsubscribe";
        public static final String TYPE_GROUP_DELETED = "destroy";

        public static final String TYPE_TYPING_START = "typing_start";
        public static final String TYPE_TYPING_STOP = "typing_stop";

//        public static final int TYPE_CHAT_ID = 1;
//        public static final int TYPE_CHAT_GROUP_ID = 2;
//        public static final int TYPE_CHAT_IMAGE_ID = 3;
//        public static final int TYPE_CHAT_VIDEO_ID = 4;
//        public static final int TYPE_CHAT_AUDIO_ID = 5;
//        public static final int TYPE_CHAT_DOC_ID = 6;
//        public static final int TYPE_CHAT_LOCATION_ID = 7;
//        public static final int TYPE_CHAT_CONTACT_ID = 7;

        public static final int TYPE_TYPING_START_ID = 5;
        public static final int TYPE_TYPING_STOP_ID = 6;

        public static final int STATUS_PENDING = 1;
        public static final int STATUS_FAILED = 2;
        public static final int STATUS_SENT = 3;
        public static final int STATUS_DELIVERED = 4;
        public static final int STATUS_SEEN = 5;
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just Now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


}
