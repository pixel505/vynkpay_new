package com.vynkpay.utils;//package com.orenda.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//
///**
// * Created by intel on 6/9/2017.
// */
//
//public class Credentials {
//    private static final String APP_KEY = "connectAdsLinks";
//    private static final String USER_ID = "user_id";
//    private static final String USER_LOGIN = "login";
//    private static final String USER_EMAIL = "email";
//
//    private static final String LOGIN_USER_NAME = "user_name";
//    private static final String COUNTRY_ID = "country_id";
//    private static final String OTP_SECRET = "otp_secret";
//    private static final String NEW_LOGIN_USER_DEVICE = "new_user";
//
//    private static final String USER_LOGIN_ID = "login_id";
//    private static final String USER_PHONE = "phone";
//    private static final String USER_STATUS = "user_status";
//    private static final String NAME = "name";
//    private static final String USER_DEVICE_TOKEN = "token_id";
//    private static final String SAVE_USER_EMAIL = "user_email";
//    private static final String SAVE_DATE = "user_date";
//    private static final String SAVE_LAST_IP = "user__last_ip";
//    private static final String ECASH_AMOUNT = "amountt";
//    private static final String LEFT_WALLET_AMOUNT = "left_wallet_amount";
//    private static final String ECASH_TRANSACTION_ID = "ecash_transaction_id";
//
//    private static final String LEFT_ECASH_AMOUNT = "left_ecash_amount";
//
//    //**********INSTALL and UNINSTALL*******************//
//    private static final String STATUS_INSTALL = "install";
//    private static final String STATUS_UNINSTALL = "uninstall";
//
//    private static final String PPD_ID = "ppd_id";
//    private static final String PPD_POINT = "ppd_point";
//    private static final String PPD_LINK = "ppd_link";
//
//    private static final String PPV_VIDEO_CODE = "ppv_code";
//    private static final String PPV_ID = "ppv_id";
//    private static final String PPV_EARN_POINT = "ppv_earn";
//
//    private static final String SAVE_IMAGE = "SAVE_IMAGE";
//    private static final String SAVE_LOGIN_DATE = "save_login_date";
//
//    private static final String SAVE_BOOL_DATE = "SAVE_BOL_DATE";
//    private static final String SAVE_INSTALL_TIMED = "save_install_timed";
//    private static final String INSTALL_DATETIME = "INSTALL_DATETIME";
//    private static final String SAVE_REFERRAL_LEVEL = "save_referral_level";
//
//    private static final String SAVE_OFFSET_VALUE = "save_offset_value";
//    private static final String PPC_RATING = "ppc_rating";
//
//    private static final String LAST_USER_LOGIN = "last_user_login";
//    private static final String USER_VERIFY = "user_verify";
//    private static final String USER_CURRENT_LOGIN = "USER_CURRENT_LOGIN";
//    private static final String USER_CURRENT_IP = "USER_CURRENT_IP";
//    private static final String COUNTRY_CODE = "country_code";
//
//    private static final String FAQ_CODE = "faq_code";
//    private static final String NOTIFY_COUNT = "notify_count";
//    private static final String BANK_TYPE_MODE = "bank_type_mode";
//    private static final String AUTO_WIDTHDRAW_REQUEST = "request_wallet_status";
//    private static final String AUTO_BITCOIN_BANK = "request_bitcoin_bank";
//    private static final String USER_OBJECT = "userObject";
//    private static final String USER_NOTIFY_GET = "user_notify";
//
//    private static final String USER_DESIGNATION = "user_designation";
//
//    private static final String SELECT_OPERATOR = "select_operator";
//    private static final String SELECT_CIRCLE = "select_circle";
//    private static final String SELECT_RECHARGE_TYPE = "recharge_type";
//
//    private static final String SELECT_PLAN_NAME = "SELECT_PLAN_NAME";
//    private static final String SELECT_PLAN_VALIDITY = "SELECT_PLAN_VALIDITY";
//    private static final String SELECT_PLAN_TALKTIME = "SELECT_PLAN_TALKTIME";
//    private static final String SELECT_PLAN_AMOUNT = "SELECT_PLAN_AMOUNT";
//    private static final String SELECT_PLAN_DESC = "SELECT_PLAN_DESC";
//    private static final String SELECT_PLAN_TYPE = "SELECT_PLAN_TYPE";
//    private static final String ELECTRIC_TYPE = "ELECTRIC_TYPE";
//
//   //****************DTH******************************//
//    private static final String DTH_PLAN_NAME = "DTH_PLAN_NAME";
//    private static final String DTH_PLAN_VALIDITY = "DTH_PLAN_VALIDITY";
//    private static final String DTH_PLAN_AMOUNT = "DTH_PLAN_AMOUNT";
//    private static final String DTH_PLAN_DESC = "DTH_PLAN_DESC";
//    private static final String DTH_PLAN_TYPE = "DTH_PLAN_TYPE";
//    private static final String DTH_PLAN_OPERATOR = "DTH_PLAN_OPERATOR";
//    private static final String SELECT_OPERATOR_ID = "SELECT_OPERATOR_ID";
//
//    public static void SaveOperatorId(String operatorId, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_OPERATOR_ID, operatorId);
//        editor.commit();
//    }
//
//    public static String getOperatorId(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_OPERATOR_ID, "");
//
//    }
//
//    public static void SaveDTHOperatorName(String dthoperatorName, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(DTH_PLAN_OPERATOR, dthoperatorName);
//        editor.commit();
//    }
//
//    public static String getDTHOperatorName(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(DTH_PLAN_OPERATOR, "");
//
//    }
//
//    public static void SaveDTHPlan(String dthplanName, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(DTH_PLAN_NAME, dthplanName);
//        editor.commit();
//    }
//
//    public static String getDTHPlan(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(DTH_PLAN_NAME, "");
//    }
//
//        public static void SaveDTHPlanValidity(String dthplanValidity, Context context) {
//            SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(DTH_PLAN_VALIDITY, dthplanValidity);
//            editor.commit();
//        }
//
//        public static String getDTHPlanValidity(Context context) {
//            SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//            return preferences.getString(DTH_PLAN_VALIDITY, "");
//    }
//
//    public static void SaveDTHPlanAmount(String dthplanAmount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(DTH_PLAN_AMOUNT, dthplanAmount);
//        editor.commit();
//    }
//
//    public static String getDTHPlanAmount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(DTH_PLAN_AMOUNT, "");
//    }
//
//    public static void SaveDTHPlanDesc(String dthplanDesc, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(DTH_PLAN_DESC, dthplanDesc);
//        editor.commit();
//    }
//
//    public static String getDTHPlanDesc(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(DTH_PLAN_DESC, "");
//
//    }
//
//    public static void SaveDTHPlanType(String dthplanType, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(DTH_PLAN_TYPE, dthplanType);
//        editor.commit();
//    }
//
//    public static String getDTHPlanType(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(DTH_PLAN_TYPE, "");
//
//    }
//    //******************************END*****************************//
//
//
//
//    public static void SaveElectricityType(String rechargeType, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(ELECTRIC_TYPE, rechargeType);
//        editor.commit();
//    }
//
//    public static String getElectricityType(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(ELECTRIC_TYPE, "");
//
//    }
//
//
//    public static void SavePlanType(String planType, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_TYPE, planType);
//        editor.commit();
//    }
//
//    public static String getPlanType(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_TYPE, "");
//
//    }
//
//
//    public static void SavePlanDesc(String planDesc, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_DESC, planDesc);
//        editor.commit();
//    }
//
//    public static String getPlanDesc(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_DESC, "");
//
//    }
//
//    public static void SavePlanAmount(String planAmount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_AMOUNT, planAmount);
//        editor.commit();
//    }
//
//    public static String getPlanAmount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_AMOUNT, "");
//    }
//
//    public static void SavePlan(String planName, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_NAME, planName);
//        editor.commit();
//    }
//
//    public static String getPlan(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_NAME, "");
//
//    }
//
//
//    public static void SavePlanValidity(String planValidity, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_VALIDITY, planValidity);
//        editor.commit();
//    }
//
//    public static String getPlanValidity(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_VALIDITY, "");
//
//    }
//
//    public static void SavePlanTalkTime(String planTalkTime, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_PLAN_TALKTIME, planTalkTime);
//        editor.commit();
//    }
//
//    public static String getPlanTalkTime(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_PLAN_TALKTIME, "");
//
//    }
//
//
//    public static void SaveRechargeType(String rechargeType, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_RECHARGE_TYPE, rechargeType);
//        editor.commit();
//    }
//
//    public static String getRechargeType(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_RECHARGE_TYPE, "");
//
//    }
//
//    public static void SaveOperatorName(String operatorName, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_OPERATOR, operatorName);
//        editor.commit();
//    }
//
//    public static String getOperatorName(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_OPERATOR, "");
//
//    }
//
//    public static void SaveCircleName(String circleName, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SELECT_CIRCLE, circleName);
//        editor.commit();
//    }
//
//    public static String getCircleName(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SELECT_CIRCLE, "");
//
//    }
//
//
//    public static void SaveDesignation(String designation, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_DESIGNATION, designation);
//        editor.commit();
//    }
//
//    public static String getDesignation(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_DESIGNATION, "");
//
//    }
//
//
//    public static void setUserObject(Context c, String userObject) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(USER_OBJECT, userObject);
//        editor.commit();
//    }
//
//    public static String getUserObject(Context ctx) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
//        String userObject = pref.getString(USER_OBJECT, null);
//        return userObject;
//    }
//
//
//    public static void SaveRequestStatus(String requestStatus, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(AUTO_WIDTHDRAW_REQUEST, requestStatus);
//        editor.commit();
//    }
//
//    public static String getRequestStatus(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(AUTO_WIDTHDRAW_REQUEST, "");
//
//    }
//
//    public static void SaveRequestBitCoinBank(int requestBitBank, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(AUTO_BITCOIN_BANK, requestBitBank);
//        editor.commit();
//    }
//
//    public static int getRequestBitCoinBank(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getInt(AUTO_BITCOIN_BANK, 0);
//
//    }
//
//
//    public static void SaveBankTypeMode(int bankType, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(BANK_TYPE_MODE, bankType);
//        editor.commit();
//    }
//
//    public static int getBankTypeMode(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getInt(BANK_TYPE_MODE, 0);
//
//    }
//
//
//    public static void SaveNotifyGetCount(int notifyGetCount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(USER_NOTIFY_GET, notifyGetCount);
//        editor.commit();
//    }
//
//    public static int getNotifyGetCount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getInt(USER_NOTIFY_GET, 1);
//
//    }
//
//
//    public static void SaveNotifyCount(int notifyCount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(NOTIFY_COUNT, notifyCount);
//        editor.commit();
//    }
//
//    public static int getNotifyCount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getInt(NOTIFY_COUNT, 0);
//
//    }
//
//    public static void SaveFaqCode(String faqCode, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(FAQ_CODE, faqCode);
//        editor.commit();
//    }
//
//    public static String getFaqCode(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(FAQ_CODE, "");
//
//    }
//
//
//    public static void SaveCountryCode(String countryCode, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(COUNTRY_CODE, countryCode);
//        editor.commit();
//    }
//
//    public static String getCountryCode(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(COUNTRY_CODE, "");
//
//    }
//
//
//    public static void SaveCurrentLogin(String currenLogin, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_CURRENT_LOGIN, currenLogin);
//        editor.commit();
//    }
//
//    public static String getCurrentLogin(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_CURRENT_LOGIN, "");
//
//    }
//
//
//    public static void SaveCurrentIp(String currenIp, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_CURRENT_IP, currenIp);
//        editor.commit();
//    }
//
//    public static String getCurrentIp(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_CURRENT_IP, "");
//
//    }
//
//
//    public static void SaveUserVerify(String verify, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_VERIFY, verify);
//        editor.commit();
//    }
//
//    public static String getUserVerify(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_VERIFY, "");
//
//    }
//
//    public static void SaveLastLoginDate(String lastloginDate, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(LAST_USER_LOGIN, lastloginDate);
//        editor.commit();
//    }
//
//    public static String getLastLoginDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(LAST_USER_LOGIN, "");
//
//    }
//
//
//    public static void SavePpcRatting(String ppcRating, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPC_RATING, ppcRating);
//        editor.commit();
//    }
//
//    public static String getPpcRatting(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPC_RATING, "");
//
//    }
//
//    public static void SaveOffSetValue(int offsetValue, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(SAVE_OFFSET_VALUE, offsetValue);
//        editor.commit();
//    }
//
//    public static int getOffSetValue(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getInt(SAVE_OFFSET_VALUE, 0);
//
//    }
//
//    //Save referral level here
//    public static void SaveReffralLevel(String saveLevel, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_REFERRAL_LEVEL, saveLevel);
//        editor.commit();
//    }
//
//    public static String getReffralLevel(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_REFERRAL_LEVEL, "");
//
//    }
//    //End
//
//    public static void SaveBooleanDate(boolean value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(SAVE_BOOL_DATE, value);
//        editor.commit();
//    }
//
//    public static boolean getBooleanDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getBoolean(SAVE_BOOL_DATE, true);
//    }
//
//
//    //Install Date
//    public static void SaveInstallDate(boolean value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(SAVE_INSTALL_TIMED, value);
//        editor.commit();
//    }
//
//    public static boolean getInstallDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getBoolean(SAVE_INSTALL_TIMED, true);
//    }
//    //Install End
//
//    //********************Save login date***************//
//    public static void setLoginDate(String saveDate, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_LOGIN_DATE, saveDate);
//        editor.commit();
//    }
//
//    public static String getLoginDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_LOGIN_DATE, "");
//    }
//    //************END***************//
//
//
//    /********************Save Install date***************/
//    public static void addInstallDate(String installDate, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(INSTALL_DATETIME, installDate);
//        editor.commit();
//    }
//
//    public static String putInstallDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(INSTALL_DATETIME, "");
//    }
//    //************END***************//
//
//    public static void SavePpvPoint(String ppvpoint, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPV_EARN_POINT, ppvpoint);
//        editor.commit();
//    }
//
//    public static String getPpvPoint(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPV_EARN_POINT, "");
//    }
//
//    public static void SavePpvId(String ppvid, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPV_ID, ppvid);
//        editor.commit();
//    }
//
//    public static String getPpvID(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPV_ID, "");
//    }
//
//
//    public static void SavePpvCode(String ppvCode, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPV_VIDEO_CODE, ppvCode);
//        editor.commit();
//    }
//
//    public static String getPpvCode(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPV_VIDEO_CODE, "");
//    }
//
//    public static void SavePpdLink(String ppdLink, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPD_LINK, ppdLink);
//        editor.commit();
//    }
//
//    public static String getPpdLink(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPD_LINK, "");
//    }
//
//    public static void SavePpdId(String ppdId, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPD_ID, ppdId);
//        editor.commit();
//    }
//
//    public static String getPpdId(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPD_ID, "");
//    }
//
//    public static void SavePpdPoint(String ppdPoint, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(PPD_POINT, ppdPoint);
//        editor.commit();
//    }
//
//    public static String getPpdPoint(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(PPD_POINT, "");
//    }
//
//
//    //Save the Install Value
//    public static void SaveInstall(String install, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(STATUS_INSTALL, install);
//        editor.commit();
//    }
//
//    public static String getInstall(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(STATUS_INSTALL, "");
//    }
//    //******END
//
//    //Save the UnInstall Value
//    public static void SaveUnInstall(String uninstall, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(STATUS_UNINSTALL, uninstall);
//        editor.commit();
//    }
//
//    public static String getUnInstall(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(STATUS_UNINSTALL, "");
//    }
//    //******END
//
//    //*******END****INSTALL and UNINSTALL*************//
//
//
//    //Save the transaction during the connect money transfer time
//    public static void SaveEcashTransactionID(String ecashTransactionID, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(ECASH_TRANSACTION_ID, ecashTransactionID);
//        editor.commit();
//    }
//
//    public static String getEcashTransactionID(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(ECASH_TRANSACTION_ID, "");
//    }
//    //End
//
//
//    public static void SaveLeftEcashAmount(String leftAmount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(LEFT_ECASH_AMOUNT, leftAmount);
//        editor.commit();
//    }
//
//    public static String getLeftEcashAmount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(LEFT_ECASH_AMOUNT, "");
//    }
//
//
//    public static void SaveLeftWalletAmount(String leftAmount, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(LEFT_WALLET_AMOUNT, leftAmount);
//        editor.commit();
//    }
//
//    public static String getLeftWalletAmount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(LEFT_WALLET_AMOUNT, "0");
//    }
//
//
//    public static void SaveUserDate(String userdate, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_DATE, userdate);
//        editor.commit();
//    }
//
//    public static String getUserSaveDate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_DATE, "");
//    }
//
//    public static void SaveUserLastIP(String mip, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_LAST_IP, mip);
//        editor.commit();
//    }
//
//    public static String getUserLastIP(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_LAST_IP, "");
//    }
//
//
//    //**************************************save Ecash Amount **********/////////////
//
//    public static void SaveECashAmount(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(ECASH_AMOUNT, id);
//        editor.commit();
//    }
//
//    public static String getECashAmount(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(ECASH_AMOUNT, "");
//    }
//
//    //***************SAVE USER EMAIL***************************
//
//    public static void SaveUserEmailId(String useremail, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_USER_EMAIL, useremail);
//        editor.commit();
//    }
//
//    public static String getUserSaveUserEmail(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_USER_EMAIL, "");
//    }
//
////*********************END*******************//
//
//
//    public static void SaveUserDeviceToken(String devicetoken, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_DEVICE_TOKEN, devicetoken);
//        editor.commit();
//    }
//
//    public static String getUserDeviceToken(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_DEVICE_TOKEN, "");
//    }
//
//
//    public static void SaveLoginUserName(String name, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(LOGIN_USER_NAME, name);
//        editor.commit();
//    }
//
//    public static String getLoginUserName(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(LOGIN_USER_NAME, "");
//    }
//
//
//    public static void SaveLoginUserID(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_LOGIN_ID, id);
//        editor.commit();
//    }
//
//
//    public static void SaveUserPhone(String phone, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_PHONE, phone);
//        editor.commit();
//    }
//
//    public static String getUserPhone(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_PHONE, "");
//    }
//
//
//    public static void SaveUserStatus(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_STATUS, id);
//        editor.commit();
//    }
//
//    public static String getUserStatus(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_STATUS, "");
//    }
//
//
//    public static void SaveLoginName(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(NAME, id);
//        editor.commit();
//    }
//
//    public static String getLoginName(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(NAME, "");
//    }
//
//    public static String getLoginUserID(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_LOGIN_ID, "");
//    }
//
//
//    public static void SaveFirstDeviceUserID(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(NEW_LOGIN_USER_DEVICE, id);
//        editor.commit();
//    }
//
//    public static String getFirstDeviceUserID(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(NEW_LOGIN_USER_DEVICE, "");
//    }
//
//
//    public static void SaveUserID(String id, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_ID, id);
//        editor.commit();
//    }
//
//    public static String getUserID(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_ID, "");
//    }
//
//
//    public static void SaveUserEmail(String email, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(USER_EMAIL, email);
//        editor.commit();
//    }
//
//    public static String getUserEmail(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(USER_EMAIL, "");
//    }
//
//    //*********OTP secret***************//
//    public static void SaveSecret(String secret, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(OTP_SECRET, secret);
//        editor.commit();
//    }
//
//    public static String getSecret(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(OTP_SECRET, "");
//    }
//
//
//    //************END**************//
//
//    public static String getCountryId(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(COUNTRY_ID, "");
//    }
//
//    public static void SaveCountryId(String countryId, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(COUNTRY_ID, countryId);
//        editor.commit();
//    }
//
//    //******************SAVE USER LOGIN*********************//
//    public static void SaveuserLogin(boolean value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(USER_LOGIN, value);
//        editor.commit();
//    }
//
//    public static boolean getSaveUserLogin(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getBoolean(USER_LOGIN, false);
//    }
//    //**********************END*************************//
//
//
//    //******************SAVE USER Registration*********************//
//    public static void SaveuserRegister(boolean value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(USER_LOGIN, value);
//        editor.commit();
//    }
//
//    public static boolean getSaveUserRegister(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getBoolean(USER_LOGIN, false);
//    }
//    //**********************END*************************//
//
//    //******************SAVE USER data*********************//
//    public static void Saveuserdate(boolean value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(USER_LOGIN, value);
//        editor.commit();
//    }
//
//    public static boolean getSaveUserdate(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getBoolean(USER_LOGIN, false);
//    }
//    //**********************END*************************//
//
//
//    //***********************SAVE LOGOUT SESSION***************//
//    public static void logout(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//    }
//    //***********************END***************//
//
//
//    public static void saveImage(String value, Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(SAVE_IMAGE, value);
//        editor.commit();
//    }
//
//    public static String getSaveImage(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
//        return preferences.getString(SAVE_IMAGE, "null");
//    }
//}
