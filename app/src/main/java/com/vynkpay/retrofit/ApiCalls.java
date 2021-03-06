package com.vynkpay.retrofit;


import com.vynkpay.models.EcashModelClass;
import com.vynkpay.models.GetKycStatusResponse;
import com.vynkpay.models.IntGeneralBonusResponse;
import com.vynkpay.models.UpdatebankResponse;
import com.vynkpay.retrofit.model.AddMoneyRazorResponse;
import com.vynkpay.retrofit.model.AppVersionResponse;
import com.vynkpay.retrofit.model.CheckWaletOtp;
import com.vynkpay.retrofit.model.CloseTicketResponse;
import com.vynkpay.retrofit.model.Club1Response;
import com.vynkpay.retrofit.model.CommunityDetailResponse;
import com.vynkpay.retrofit.model.ConversionResponse;
import com.vynkpay.retrofit.model.DeleteNotificationResponse;
import com.vynkpay.retrofit.model.GenerateticketResponse;
import com.vynkpay.retrofit.model.GenerationBonusResponse;
import com.vynkpay.retrofit.model.GetBitAddressResponse;
import com.vynkpay.retrofit.model.GetChatResponse;
import com.vynkpay.retrofit.model.GetCountryResponse;
import com.vynkpay.retrofit.model.GetInvoiceDetailResponse;
import com.vynkpay.retrofit.model.GetInvoiceResponse;
import com.vynkpay.retrofit.model.GetNonWalletResponse;
import com.vynkpay.retrofit.model.GetOpenCloseTicket;
import com.vynkpay.retrofit.model.GetPackageDetailResponse;
import com.vynkpay.retrofit.model.GetPackageResponse;
import com.vynkpay.retrofit.model.GetProfileResponse;
import com.vynkpay.retrofit.model.GetTicketDepartment;
import com.vynkpay.retrofit.model.GetUserPackageResponse;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.retrofit.model.LoginResponse;
import com.vynkpay.retrofit.model.LogoutResponse;
import com.vynkpay.retrofit.model.NotificationReadResponse;
import com.vynkpay.retrofit.model.NotificationResponse;
import com.vynkpay.retrofit.model.OtpVerifyLoginResponse;
import com.vynkpay.retrofit.model.OtpVerifyResponse;
import com.vynkpay.retrofit.model.PaidItemResponse;
import com.vynkpay.retrofit.model.PayResponse;
import com.vynkpay.retrofit.model.ReddemAmountResponse;
import com.vynkpay.retrofit.model.ReferLinkResponse;
import com.vynkpay.retrofit.model.ReferalBonusResponse;
import com.vynkpay.retrofit.model.ReferalsResponse;
import com.vynkpay.retrofit.model.RegisterResponse;
import com.vynkpay.retrofit.model.ReplyChatResponse;
import com.vynkpay.retrofit.model.RequestCashResponse;
import com.vynkpay.retrofit.model.SendBitResponse;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.retrofit.model.StatementResponse;
import com.vynkpay.retrofit.model.SubmitConversionResponse;
import com.vynkpay.retrofit.model.TeamSummaryResponse;
import com.vynkpay.retrofit.model.TransferMoney;
import com.vynkpay.retrofit.model.UpdateImageResponse;
import com.vynkpay.retrofit.model.VerifyBitResponse;
import com.vynkpay.retrofit.model.VerifyPinResponse;
import com.vynkpay.retrofit.model.WithdrawalTypeTesponse;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiCalls {

    // app version api
    @FormUrlEncoded
    @POST("api/auth/appVersion")
    Call<AppVersionResponse> appVersionMethod(
            @Field("version") String version);

    // login api
    @FormUrlEncoded
    @POST("api/auth/login")
    Call<LoginResponse> loginMethod(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_type") String device_type,
            @Field("device_id") String device_id);

    @POST("api/auth/logout")
    Call<LogoutResponse> logoutMethod(
            @Header("access_token") String access_token);

    @GET("api/auth/country_list")
    Call<GetCountryResponse> getCountry();

    @GET("account/frontend/api_all_states")
    Call<String> getAllState();


    @FormUrlEncoded
    @POST("account/frontend/register_user")
    Call<RegisterResponse> registerMethod(
            @Field("user_refferal") String user_refferal,
            @Field("ufirstname") String ufirstname,
            @Field("u_username") String u_username,
            @Field("country") String country,
            @Field("u_email") String u_email,
            @Field("country_code") String country_code,
            @Field("umobile") String umobile,
            @Field("u_password") String u_password,
            @Field("u_cpassword") String u_cpassword,
            @Field("customControlAutosizing") String customControlAutosizing,
            @Field("affiliateagreement") String affiliateagreement,
            @Field("u_pan") String u_pan,
            @Field("u_aadhar") String u_aadhar,
            @Field("state") String state);

    @FormUrlEncoded
    @POST("account/frontend/otp_verification_sighup")
    Call<OtpVerifyResponse> otpVerifyMethod(
            @Field("temp_id") String temp_id,
            @Field("otp") String otp);

    @FormUrlEncoded
    @POST("account/frontend/login_otp_verification")
    Call<OtpVerifyLoginResponse> otpLoginVerifyMethod(
            @Field("temp_id") String temp_id,
            @Field("otp") String otp);

    @GET("api/user")
    Call<GetProfileResponse> getProfileMethod(
            @Header("access_token") String access_token);


    @Multipart
    @POST("account/api_accounts/submit_kyc")
    Call<UpdateImageResponse> uploadImage(
            @Header("access_token") String access_token, @Part MultipartBody.Part national_id, @Part MultipartBody.Part address_proof

    );


    @Multipart
    @POST("account/api_accounts/submit_kyc")
    Call<UpdateImageResponse> uploadNationalImage(
            @Header("access_token") String access_token, @Part MultipartBody.Part national_id
    );


    @Multipart
    @POST("account/api_accounts/submit_kyc")
    Call<UpdateImageResponse> uploadAddressImage(
            @Header("access_token") String access_token, @Part MultipartBody.Part address_proof

    );

    @FormUrlEncoded
    @POST("api/auth/pin_verify")
    Call<VerifyPinResponse> verifyPinMethod(
            @Header("access_token") String access_token,
            @Field("pin") String pin,
            @Field("device_type") String device_type,
            @Field("device_id") String device_id);


    @GET("api/wallet")
    Call<GetWalletResponse> walletAmountMethod(
            @Header("access_token") String access_token);


    @FormUrlEncoded
    @POST("api/operator/redeem_amount")
    Call<ReddemAmountResponse> redeemAmountMethod(
            @Header("access_token") String access_token,
            @Field("id") String id,
            @Field("amount") String amount);


    @FormUrlEncoded
    @POST("api/wallet/add_money_razorpay")
    Call<AddMoneyRazorResponse> addMoneyRazorMethod(
            @Header("access_token") String access_token,
            @Field("razorpay_payment_id") String razorpay_payment_id,
            @Field("amount") String amount);


    @Multipart
    @POST("account/api_accounts/save_bank_details")
    Call<UpdatebankResponse> uploadBank(
            @Header("access_token") String access_token,
            @Part MultipartBody.Part bnk_cheque,
            @Part("bnk_beneficery") RequestBody bnk_beneficery,
            @Part("bnk_name") RequestBody bnk_name,
            @Part("branch_address") RequestBody branch_address,
            @Part("bnk_accountno") RequestBody bnk_accountno,
            @Part("bnk_ifsc") RequestBody bnk_ifsc,
            @Part("bnk_branchcode") RequestBody bnk_branchcode,
            @Part("pan") RequestBody pan,
            @Part("account_type") RequestBody account_type

    );


    @GET("account/api_accounts/kyc_details")
    Call<GetKycStatusResponse> kycStatusMethod(
            @Header("access_token") String access_token);


    @FormUrlEncoded
    @POST("account/api_accounts/bitadress")
    Call<GetBitAddressResponse> bitAddress(
            @Header("access_token") String access_token,
            @Field("bit_address") String bit_address

    );

    @POST("account/api_accounts/send_bit_otp")
    Call<SendBitResponse> sendBit(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_bit_otp")
    Call<VerifyBitResponse> sendBitverify(
            @Header("access_token") String access_token,
            @Field("btc_otp") String btc_otp);

    @Multipart
    @POST("account/api_accounts/generateTicket")
    Call<GenerateticketResponse> generateTicket(
            @Header("access_token") String access_token,
            @Part List<MultipartBody.Part> image_id,
            @Part("department") RequestBody department,
            @Part("subject") RequestBody subject,
            @Part("body") RequestBody body);


    @POST("account/api_accounts/ticket_department")
    Call<GetTicketDepartment> getTicketDepartment(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/support")
    Call<GetOpenCloseTicket> getOpenCloseTicket(
            @Header("access_token") String access_token);


    @FormUrlEncoded
    @POST("account/api_accounts/support_ticket_detail")
    Call<GetChatResponse> getChat(
            @Header("access_token") String access_token,
            @Field("ticket_id") String ticket_id);


    @Multipart
    @POST("account/api_accounts/TicketReply")
    Call<ReplyChatResponse> replyChat(
            @Header("access_token") String access_token,
            @Part List<MultipartBody.Part> image_id,
            @Part("body") RequestBody body,
            @Part("ref_id") RequestBody ref_id);


    @FormUrlEncoded
    @POST("account/api_accounts/closeTicket")
    Call<CloseTicketResponse> closeTicket(
            @Header("access_token") String access_token,
            @Field("ticketid") String ticket_id);

    @POST("account/api_accounts/referrals")
    Call<ReferalsResponse> getReferal(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/teamsummary")
    Call<TeamSummaryResponse> getTeam(
            @Header("access_token") String access_token,@Field("per_page") String per_page);

    @POST("account/api_accounts/ref_profit")
    Call<ReferalBonusResponse> getreferalBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/profit_roi")
    Call<ReferalBonusResponse> getWeeklyBonus(
            @Header("access_token") String access_token);

    //}/account/api_accounts/profit_roi_detail?view=2021-05-27
    @FormUrlEncoded
    @POST("account/api_accounts/profit_roi_detail")
    Call<ReferalBonusResponse> getWeeklyBonusDetail(
            @Header("access_token") String access_token, @Field("view") String view);

    @POST("account/api_accounts/add_ref_profit")
    Call<GenerationBonusResponse> getGenerationBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/pool_profit")
    Call<GenerationBonusResponse> getPoolBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/lead_bonus")
    Call<GenerationBonusResponse> getleadBonus(
            @Header("access_token") String access_token);


    @POST("account/api_accounts/ambassador_bonus")
    Call<GenerationBonusResponse> getAmbasadarBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/team_profit")
    Call<GenerationBonusResponse> getApraisalBonus(
            @Header("access_token") String access_token);


    @POST("account/api_accounts/chairman_bonus")
    Call<GenerationBonusResponse> getChairBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/invoice")
    Call<GetInvoiceResponse> getInvoice(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/invoice_detail")
    Call<GetInvoiceDetailResponse> getInvoiceDetail(
            @Header("access_token") String access_token,
            @Field("purchaseid") String purchaseid

    );

    @POST("account/api_accounts/add_investment")
    Call<GetPackageResponse> getPackage(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/request_payment")
    Call<GetPackageDetailResponse> getPackageDetails(
            @Header("access_token") String access_token);

    @Multipart
    @POST("account/api_accounts/ecashpost")
    Call<RequestCashResponse> reqstCash(
            @Header("access_token") String access_token,
            @Part("amount") RequestBody amount,
            @Part("detosited_at") RequestBody detosited_at,
            @Part("created_date") RequestBody created_date,
            @Part("txn_no") RequestBody txn_no,
            @Part("remarks") RequestBody remarks,
            @Part MultipartBody.Part upload_ch);

    @FormUrlEncoded
    @POST("account/api_accounts/paidUserWallet")
    Call<PayResponse> pay(
            @Header("access_token") String access_token,
            @Field("package_id") String package_id);

    @FormUrlEncoded
    @POST("account/api_accounts/searchusername")
    Call<GetUserResponse> getUser(
            @Header("access_token") String access_token,
            @Field("username") String username,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("account/api_accounts/TransferMetherCredit_working")
    Call<TransferMoney> transferMoney(
            @Header("access_token") String access_token,
            @Field("pin") String pin,
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("remarks") String remarks
    );


    @FormUrlEncoded
    @POST("account/api_accounts/TransferMetherCredit_mcash")
    Call<TransferMoney> transfermMoney(
            @Header("access_token") String access_token,
            @Field("pin") String pin,
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("remarks") String remarks
    );

    @FormUrlEncoded
    @POST("account/api_accounts/TransferMetherCredit_cashback")
    Call<TransferMoney> transfermVMoney(
            @Header("access_token") String access_token,
            @Field("pin") String pin,
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("remarks") String remarks
    );

  @GET("account/api_accounts/dashboard")
    Call<GetNonWalletResponse> noindianWallet(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/refer_link")
    Call<ReferLinkResponse> getLinks(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/searchusername_paid_team")
    Call<GetUserResponse> getUserAf(
            @Header("access_token") String access_token,
            @Field("username") String username);

    @FormUrlEncoded
    @POST("account/api_accounts/getUserLatestPackage")
    Call<GetUserPackageResponse> getUserPackage(
            @Header("access_token") String access_token,
            @Field("user_id") String user_id);


    @POST("account/api_accounts/send_wallet_otp")
    Call<SendWaletOtp> getWalletOtp(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/paid_team_submit")
    Call<PaidItemResponse> payitem(
            @Header("access_token") String access_token,
            @Field("user_id") String user_id,
            @Field("package") String packag,
            @Field("otp") String otp);

    @POST("account/api_accounts/eCash")
    Call<EcashModelClass> getECash(
            @Header("access_token") String access_token);

    @POST("api/notification")
    Call<NotificationResponse> getNotifications(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("api/notification")
    Call<NotificationReadResponse> readNotification(
            @Header("access_token") String access_token,
            @Field("notification_id") String notification_id);


    @FormUrlEncoded
    @POST("api/notification/delete")
    Call<DeleteNotificationResponse> deleteNotification(
            @Header("access_token") String access_token,
            @Field("notification_id") String notification_id);

    @POST("account/api_accounts/community_detail")
    Call<CommunityDetailResponse> getCommunityDetail(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/user_stattement")
    Call<StatementResponse> getstatement(
            @Header("access_token") String access_token,
            @Field("user_id") String user_id);

    @POST("account/api_accounts/conversion")
    Call<ConversionResponse> getConversions(
            @Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/check_wallet_otp")
    Call<CheckWaletOtp> checkotp(
            @Header("access_token") String access_token,
            @Field("wallet_otp") String wallet_otp);

    @FormUrlEncoded
    @POST("account/api_accounts/conversion_submit")
    Call<SubmitConversionResponse> submitConversion(
            @Header("access_token") String access_token,
            @Field("action") String action ,
            @Field("blnc") String blnc);

    @POST("account/api_accounts/working_summary")
    Call<WithdrawalTypeTesponse> getWithdrawalType(
            @Header("access_token") String access_token);

    /*New*/
    @POST("account/api_accounts/settings")
    Call<String> getTransferSettings(@Header("access_token") String access_token);

    @POST("account/api_accounts/volume_bonus")
    Call<GenerationBonusResponse> getVolumeBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/performance_bonus")
    Call<GenerationBonusResponse> getPerformanceBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/shopping_bonus")
    Call<GenerationBonusResponse> getShoppingBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/club1")
    Call<Club1Response> getClub1(@Header("access_token") String access_token);

    @POST("account/api_accounts/club2")
    Call<Club1Response> getClub2(@Header("access_token") String access_token);

    @POST("account/api_accounts/club3")
    Call<Club1Response> getClub3(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/frontend/register_customer")
    Call<String> registerCustomer(@Field("country_code") String country_code, @Field("umobile") String umobile,@Field("user_refferal") String user_refferal);

    /*account/frontend/register_customer
    country_code     umobile   u_password  fields name*/

    @POST("account/api_accounts/shops")
    Call<String> getShops(@Header("access_token") String access_token);

    @POST("account/api_accounts/cashback")
    Call<String> getCashBack(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/getDeals")
    Call<String> shopSearch(@Header("access_token") String access_token,@Field("search") String search,@Field("category") String category);

    @FormUrlEncoded
    @POST("account/frontend/register_international_customer")
    Call<String> registerByEmail(@Field("u_email") String u_email,@Field("user_refferal") String user_refferal);

    @FormUrlEncoded
    @POST("account/api_accounts/notification_setting")
    Call<String> notificationSoundOnOff(@Header("access_token") String access_token,@Field("sound") String sound,@Field("display") String display);

    @FormUrlEncoded
    @POST("account/api_accounts/referrals")
    //startdate, enddate, search_by
    Call<ReferalsResponse> getReferralsFilter(@Header("access_token") String access_token, @Field("startdate") String startdate,@Field("enddate") String enddate,@Field("search_by") String search_by, @Field("per_page") String per_page);

    @FormUrlEncoded
    @POST("account/api_accounts/teamsummary")
    Call<TeamSummaryResponse> getTeamFilter(@Header("access_token") String access_token, @Field("startdate") String startdate,@Field("enddate") String enddate,@Field("search_by") String search_by, @Field("per_page") String per_page);

    /*ETH*/

    @FormUrlEncoded
    @POST("account/api_accounts/ethadress")
    Call<GetBitAddressResponse> addETHAddress(@Header("access_token") String access_token,@Field("eth_address") String eth_address);

    @POST("account/api_accounts/send_eth_otp")
    Call<SendBitResponse> sendETHOtp(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_eth_otp")
    Call<VerifyBitResponse> sendETHverify(
            @Header("access_token") String access_token,
            @Field("eth_otp") String eth_otp);



    //ppl
    @FormUrlEncoded
    @POST("account/api_accounts/ppladress")
    Call<GetBitAddressResponse> addPPLAddress(@Header("access_token") String access_token,@Field("ppl_address") String ppl_address);

    @POST("account/api_accounts/send_ppl_otp")
    Call<SendBitResponse> sendPPLOtp(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_ppl_otp")
    Call<VerifyBitResponse> sendPplverify(
            @Header("access_token") String access_token,
            @Field("ppl_otp") String ppl_otp);




    //trx
    @FormUrlEncoded
    @POST("account/api_accounts/trxadress")
    Call<GetBitAddressResponse> addTrxAddress(@Header("access_token") String access_token,@Field("trx_address") String trx_address);

    @POST("account/api_accounts/send_trx_otp")
    Call<SendBitResponse> sendTrxOtp(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_trx_otp")
    Call<VerifyBitResponse> sendTrxverify(
            @Header("access_token") String access_token,
            @Field("trx_otp") String trx_otp);



    /*Perfect money*/
    @FormUrlEncoded
    @POST("account/api_accounts/pemadress")
    Call<GetBitAddressResponse> addPerfectAddress(@Header("access_token") String access_token,@Field("pem_address") String pem_address);

    @POST("account/api_accounts/send_pem_otp")
    Call<SendBitResponse> sendPerfectOtp(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_pem_otp")
    Call<VerifyBitResponse> sendPerfectverify(
            @Header("access_token") String access_token,
            @Field("pem_otp") String pem_otp);

    /*Payeer*/

    @FormUrlEncoded
    @POST("account/api_accounts/payeer_account")
    Call<GetBitAddressResponse> addPayeerAddress(@Header("access_token") String access_token,@Field("payeer_account") String payeer_account);

    @POST("account/api_accounts/send_payeer_otp")
    Call<SendBitResponse> sendPayeerOtp(@Header("access_token") String access_token);

    @FormUrlEncoded
    @POST("account/api_accounts/verify_payeer_otp")
    Call<VerifyBitResponse> sendPayeerverify(
            @Header("access_token") String access_token,
            @Field("payeer_otp") String payeer_otp);

    /*new Int*/

    @POST("account/api_accounts/ref_profit_token")
    Call<ReferalBonusResponse> getvyncreferalBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/volume_bonus_token")
    Call<IntGeneralBonusResponse> getvyncVolumeBonus(
            @Header("access_token") String access_token);

    @POST("account/api_accounts/performance_bonus_token")
    Call<IntGeneralBonusResponse> getvyncPerformanceBonus(
            @Header("access_token") String access_token);

    /*Vcash Opt*/
    @POST("account/api_accounts/opt_vcash_add")
    Call<String> optVCashAdd(@Header("access_token") String access_token);

    /*Dashboard*/
    @POST("account/api_accounts/dashboard_data")
    Call<String> getDashboardData(@Header("access_token") String access_token);

    /*Chart*/
    @FormUrlEncoded
    @POST("account/api_accounts/chart_data")
    Call<String> getChartData(@Header("access_token") String access_token,@Field("chart_type") String chart_type);


}


