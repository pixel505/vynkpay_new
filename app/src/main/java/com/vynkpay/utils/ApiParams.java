package com.vynkpay.utils;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;

public class ApiParams {
    /**
     * App Params
     **/
    public static String email = "email";
    public static String finishLogin = "teee";
    public static String password = "password";
    public static String cn_password = "cn_password";
    public static String old_password = "old_password";
    public static String mobile_number = "mobile_number";
    public static String otp = "otp";
    public static String device_info = "device_info";
    public static String razorpikey = "razorpikey";
    public static String fcm_token = "fcm_token";
    public static String device_id = "device_id";
    public static String device_type = "device_type";
    public static String time_zone = "time_zone";
    public static String success = "success";
    public static String message = "message";
    public static String error_code = "error_code";
    public static String errors = "errors";
    public static String access_token = "access_token";
    public static String full_name = "full_name";
    public static String user_id = "user_id";
    public static String data = "data";
    public static String gender = "gender";
    public static String dob = "dob";
    public static String balance = "balance";
    public static String recovery_token = "recovery_token";
    public static String amount = "amount";
    public static String created_at = "created_at";
    public static String transaction_use_id = "id";
    public static String payment_method = "payment_method";
    public static String txn_status = "txn_status";
    public static String accept = "Accept";
    public static String circle = "circle";
    public static String operator = "operator";
    public static String recharge_type = "recharge_type";
    public static String page = "page";
    public static String type_for_prepaid = "1";
    public static String type_for_postpaid = "2";
    public static String type_for_datacard_prepaid = "6";
    public static String type_for_datacard_postpaid = "7";
    public static String type = "type";
    public static String image_url = "image_url";
    public static String operator_id = "id";
    public static String circle_id = "id";
    public static String isactive = "isactive";
    public static String application_json = "application/json";
    public static String operator_urls = "operator_urls";
    public static String type_broadband = "4";
    public static String type_electricty = "3";
    public static String operator_idd = "operator_id";
    public static String state = "state";
    public static String division = "division";
    public static String code = "code";
    public static String status = "status";
    public static String bill = "bill";
    public static String bill_date = "bill_date";
    public static String due_date = "due_date";
    public static String operator_logo = "operator_logo";
    public static String consumer_name = "consumer_name";
    public static String partial_pay = "partial_pay";
    public static String transaction = "transaction";
    public static String txn = "txn";
    public static String id = "id";
    public static String label = "label";
    public static String max_limit = "max_limit";
    public static String min_limit = "min_limit";
    public static String conn_type = "conn_type";
    public static String description = "description";
    public static String recharges = "recharges";
    public static String plan_name = "plan_name";
    public static String plan_validity = "plan_validity";
    public static String GLOBAL_PARAMS = "eSamparkIndia";
    public static String txn_type = "txn_type";
    public static String provider = "provider";
    public static String typeOfSelection = "typeOfSelection";
    public static String notifications = "notifications";


    /**
     * dmt params
     **/
    public static String remitter_id = "remitter_id";
    public static String fname = "fname";
    public static String lname = "lname";
    public static String pin = "pin";
    public static String add_info = "add_info";
    public static String remitter = "remitter";
    public static String ben_account = "ben_account";
    public static String ben_ifsc = "ben_ifsc";
    public static String name = "name";
    public static String beneficiary = "beneficiary";
    public static String beneficiary_id = "beneficiary_id";
    public static String verify = "verify";
    public static String verification_status = "verification_status";
    public static String account = "account";
    public static String ifsc = "ifsc";
    public static String bank = "bank";
    public static String remark = "remark";
    public static String mobile = "mobile";


    /**
     * giftCard params
     **/

    public static String beneficiary_first_name = "beneficiary_first_name";
    public static String beneficiary_last_name = "beneficiary_last_name";
    public static String beneficiary_mobile = "beneficiary_mobile";
    public static String product_id = "product_id";
    public static String price = "price";
    public static String quantity = "quantity";
    public static String gift_message = "gift_message";
    public static String theme = "theme";


    /**
     * theme park params
     **/
    public static String date_of_tour = "date_of_tour";
    public static String beneficiary_address_1 = "beneficiary_address_1";
    public static String beneficiary_address_2 = "beneficiary_address_2";
    public static String beneficiary_city = "beneficiary_city";
    public static String beneficiary_state = "beneficiary_state";
    public static String beneficiary_country_id = "beneficiary_country_id";
    public static String beneficiary_post_code = "beneficiary_post_code";
    public static String tour_options_id = "tour_options_id";
    public static String tour_options_title = "tour_options_title";
    public static String pricing_id = "pricing_id";
    public static String pricing_qty = "pricing_qty";
    public static String pricing_name = "pricing_name";
    public static String time = "time";
    public static String order_status = "order_status";

    /**
     * required by PayU process
     **/
    public static String productinfo = "productinfo";
    public static String mobile_recharge = "mobile_recharge";
    public static String surl = "surl";
    public static String categories = "categories";
    public static String add_money = "add_money";
    public static String furl = "furl";
    public static String hash = "payment_hash";
    public static String key = "key";
    public static String txnid = "txnid";
    public static String firstname = "firstname";
    public static String phone = "phone";
    public static String payu_response = "payu_response";
    public static String vas_for_mobile_sdk_hash = "vas_for_mobile_sdk";
    public static String payment_related_details_for_mobile_sdk_hash = "payment_related_details_for_mobile_sdk";
    public static String get_merchant_ibibo_codes_hash = "get_merchant_ibibo_codes";


    //Notification params
    public static String title;
    public static String body;
    public static String icon;
    public static String userId;
    public static String readBy;


    public static class ErrorCodeArray {
        public static String _ERROR_CODE_400 = "400"; // any validation related message
        public static String _ERROR_CODE_401 = "401";
        public static String _ERROR_CODE_111 = "111";
        public static String _ERROR_CODE_409 = "409"; // for already registered user
    }


    public static class AssetsUsed {
        public static int[] recharge_service_array = {R.mipmap.mobile_prepaid_icon,
                R.mipmap.mobile_postpaid_icon,
                R.mipmap.data_card_icon,
                R.mipmap.dth,
                R.mipmap.landline_icon,
                R.mipmap.broadband_icon,
                R.mipmap.electricity_icon,
                R.mipmap.gas_icon,
                R.mipmap.water_icon,
                R.mipmap.insurance,
                R.mipmap.train_service
        };
        public static int[] book_service_array = {R.mipmap.bus_service,
                R.mipmap.flight_service,
                R.mipmap.hotels_service,
                R.mipmap.train_service,
                R.mipmap.theme_park_service,
                R.mipmap.movie_service,
                R.mipmap.giftcard_service
        };
        public static String[] rechargeServiceUsed = {"Mobile Prepaid",
                "Mobile Postpaid",
                "Data Card",
                "DTH",
                "Landline",
                "Broadband",
                "Electricity",
                "Gas",
                "Water",
                "Insurance",
                "Train Booking"
        };
        public static String[] bookServiceUsed = {"Bus",
                "Flight",
                "Hotel",
                "Train",
                "Theme Park",
                "Movie Tickets",
                "Gift Cards"
        };
    }



    // new params sunakshi

    public static String isUpdate = "isUpdate";
}
