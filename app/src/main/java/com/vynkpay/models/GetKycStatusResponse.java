package com.vynkpay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetKycStatusResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("bank_details")
        @Expose
        private BankDetails bankDetails;
        @SerializedName("payment_modes")
        @Expose
        private List<Object> paymentModes = null;
        @SerializedName("userdata")
        @Expose
        private Userdata userdata;
        @SerializedName("kyc_status")
        @Expose
        private KycStatus kycStatus;

        public BankDetails getBankDetails() {
            return bankDetails;
        }

        public void setBankDetails(BankDetails bankDetails) {
            this.bankDetails = bankDetails;
        }

        public List<Object> getPaymentModes() {
            return paymentModes;
        }

        public void setPaymentModes(List<Object> paymentModes) {
            this.paymentModes = paymentModes;
        }

        public Userdata getUserdata() {
            return userdata;
        }

        public void setUserdata(Userdata userdata) {
            this.userdata = userdata;
        }

        public KycStatus getKycStatus() {
            return kycStatus;
        }

        public void setKycStatus(KycStatus kycStatus) {
            this.kycStatus = kycStatus;
        }


        public class BankDetails {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("fund_account_id")
            @Expose
            private String fundAccountId;
            @SerializedName("name_in_bank")
            @Expose
            private String nameInBank;
            @SerializedName("bank_name")
            @Expose
            private String bankName;
            @SerializedName("branch_address")
            @Expose
            private String branchAddress;
            @SerializedName("account_number")
            @Expose
            private String accountNumber;
            @SerializedName("ifsc_code")
            @Expose
            private String ifscCode;
            @SerializedName("branch_code")
            @Expose
            private String branchCode;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("cheque_receipt")
            @Expose
            private String chequeReceipt;
            @SerializedName("path")
            @Expose
            private String path;
            @SerializedName("nid_remarks")
            @Expose
            private String nidRemarks;
            @SerializedName("account_type")
            @Expose
            private String accountType;
            @SerializedName("pan")
            @Expose
            private String pan;
            @SerializedName("other_detail")
            @Expose
            private String otherDetail;
            @SerializedName("created_by")
            @Expose
            private String createdBy;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("is_default")
            @Expose
            private String isDefault;
            @SerializedName("isactive")
            @Expose
            private String isactive;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getFundAccountId() {
                return fundAccountId;
            }

            public void setFundAccountId(String fundAccountId) {
                this.fundAccountId = fundAccountId;
            }

            public String getNameInBank() {
                return nameInBank;
            }

            public void setNameInBank(String nameInBank) {
                this.nameInBank = nameInBank;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public String getBranchAddress() {
                return branchAddress;
            }

            public void setBranchAddress(String branchAddress) {
                this.branchAddress = branchAddress;
            }

            public String getAccountNumber() {
                return accountNumber;
            }

            public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
            }

            public String getIfscCode() {
                return ifscCode;
            }

            public void setIfscCode(String ifscCode) {
                this.ifscCode = ifscCode;
            }

            public String getBranchCode() {
                return branchCode;
            }

            public void setBranchCode(String branchCode) {
                this.branchCode = branchCode;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getChequeReceipt() {
                return chequeReceipt;
            }

            public void setChequeReceipt(String chequeReceipt) {
                this.chequeReceipt = chequeReceipt;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getNidRemarks() {
                return nidRemarks;
            }

            public void setNidRemarks(String nidRemarks) {
                this.nidRemarks = nidRemarks;
            }

            public String getAccountType() {
                return accountType;
            }

            public void setAccountType(String accountType) {
                this.accountType = accountType;
            }

            public String getPan() {
                return pan;
            }

            public void setPan(String pan) {
                this.pan = pan;
            }

            public String getOtherDetail() {
                return otherDetail;
            }

            public void setOtherDetail(String otherDetail) {
                this.otherDetail = otherDetail;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(String isDefault) {
                this.isDefault = isDefault;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }
        }

        public class KycStatus {

            @SerializedName("staus")
            @Expose
            private String staus;
            @SerializedName("message")
            @Expose
            private String message;

            public String getStaus() {
                return staus;
            }

            public void setStaus(String staus) {
                this.staus = staus;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }


        public class Userdata {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_type")
            @Expose
            private String userType;
            @SerializedName("ref_by")
            @Expose
            private String refBy;
            @SerializedName("parent_id")
            @Expose
            private String parentId;
            @SerializedName("leg")
            @Expose
            private String leg;
            @SerializedName("left_count")
            @Expose
            private String leftCount;
            @SerializedName("right_count")
            @Expose
            private String rightCount;
            @SerializedName("designation")
            @Expose
            private String designation;
            @SerializedName("designation_date")
            @Expose
            private String designationDate;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("userpin")
            @Expose
            private String userpin;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("password_text")
            @Expose
            private String passwordText;
            @SerializedName("loginpassword")
            @Expose
            private String loginpassword;
            @SerializedName("full_name")
            @Expose
            private String fullName;
            @SerializedName("firstname")
            @Expose
            private String firstname;
            @SerializedName("middlename")
            @Expose
            private Object middlename;
            @SerializedName("lastname")
            @Expose
            private Object lastname;
            @SerializedName("emailid")
            @Expose
            private String emailid;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("mobile_code")
            @Expose
            private String mobileCode;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("country_code")
            @Expose
            private String countryCode;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("country")
            @Expose
            private String country;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("email_status")
            @Expose
            private String emailStatus;
            @SerializedName("otp")
            @Expose
            private String otp;
            @SerializedName("createdate")
            @Expose
            private Object createdate;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("modified_date")
            @Expose
            private String modifiedDate;
            @SerializedName("isactive")
            @Expose
            private String isactive;
            @SerializedName("ref_id")
            @Expose
            private String refId;
            @SerializedName("tokenid")
            @Expose
            private Object tokenid;
            @SerializedName("device_token")
            @Expose
            private String deviceToken;
            @SerializedName("device_type")
            @Expose
            private String deviceType;
            @SerializedName("referalid")
            @Expose
            private String referalid;
            @SerializedName("gcmid")
            @Expose
            private Object gcmid;
            @SerializedName("loginid")
            @Expose
            private Object loginid;
            @SerializedName("pin")
            @Expose
            private String pin;
            @SerializedName("gender")
            @Expose
            private String gender;
            @SerializedName("bActive")
            @Expose
            private String bActive;
            @SerializedName("photo")
            @Expose
            private Object photo;
            @SerializedName("photo_thumb")
            @Expose
            private String photoThumb;
            @SerializedName("forget_pass_otp")
            @Expose
            private Object forgetPassOtp;
            @SerializedName("wallet_amount")
            @Expose
            private String walletAmount;
            @SerializedName("fb_id")
            @Expose
            private Object fbId;
            @SerializedName("tele_bot_id")
            @Expose
            private Object teleBotId;
            @SerializedName("pan_number")
            @Expose
            private String panNumber;
            @SerializedName("aadhar_number")
            @Expose
            private String aadharNumber;
            @SerializedName("gst_number")
            @Expose
            private String gstNumber;
            @SerializedName("paid_status")
            @Expose
            private String paidStatus;
            @SerializedName("paid_date")
            @Expose
            private String paidDate;
            @SerializedName("default_payment_mode")
            @Expose
            private String defaultPaymentMode;
            @SerializedName("role")
            @Expose
            private String role;
            @SerializedName("level")
            @Expose
            private String level;
            @SerializedName("enroll_feeId")
            @Expose
            private String enrollFeeId;
            @SerializedName("profile_pic")
            @Expose
            private Object profilePic;
            @SerializedName("email_token")
            @Expose
            private String emailToken;
            @SerializedName("wallet_token")
            @Expose
            private String walletToken;
            @SerializedName("password_token")
            @Expose
            private String passwordToken;
            @SerializedName("password_status")
            @Expose
            private String passwordStatus;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("bit_address")
            @Expose
            private String bitAddress;
            @SerializedName("bit_address_status")
            @Expose
            private String bitAddressStatus;
            @SerializedName("bit_otp")
            @Expose
            private String bitOtp;
            @SerializedName("eth_address")
            @Expose
            private String ethAddress;
            @SerializedName("eth_address_status")
            @Expose
            private String ethAddressStatus;
            @SerializedName("eth_otp")
            @Expose
            private String ethOtp;
            @SerializedName("pem_address")
            @Expose
            private String pemAddress;
            @SerializedName("pem_address_status")
            @Expose
            private String pemAddressStatus;
            @SerializedName("pem_otp")
            @Expose
            private String pemOtp;


            @SerializedName("ppl_address")
            @Expose
            private String pplAddress;


            @SerializedName("ppl_address_status")
            @Expose
            private String pplAddressStatus;


            @SerializedName("trx_address")
            @Expose
            private String trx_address;


            @SerializedName("trx_address_status")
            @Expose
            private String trx_address_status;


            @SerializedName("ppl_otp")
            @Expose
            private String pplOtp;
            @SerializedName("payeer_account")
            @Expose
            private String payeerAccount;
            @SerializedName("payeer_otp")
            @Expose
            private String payeerOtp;
            @SerializedName("last_resetPass_at")
            @Expose
            private Object lastResetPassAt;
            @SerializedName("paid_by")
            @Expose
            private String paidBy;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("modified_by")
            @Expose
            private String modifiedBy;
            @SerializedName("roi_status")
            @Expose
            private String roiStatus;
            @SerializedName("mobile_number")
            @Expose
            private String mobileNumber;
            @SerializedName("bio")
            @Expose
            private String bio;
            @SerializedName("avatar")
            @Expose
            private String avatar;
            @SerializedName("account_status")
            @Expose
            private String accountStatus;
            @SerializedName("kyc_status")
            @Expose
            private String kycStatus;
            @SerializedName("mobile_number_status")
            @Expose
            private String mobileNumberStatus;
            @SerializedName("referral_id")
            @Expose
            private Object referralId;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("referral_code")
            @Expose
            private String referralCode;
            @SerializedName("remember_token")
            @Expose
            private String rememberToken;
            @SerializedName("otp_expires_at")
            @Expose
            private String otpExpiresAt;
            @SerializedName("recovery_token")
            @Expose
            private String recoveryToken;
            @SerializedName("recovery_token_expires_at")
            @Expose
            private Object recoveryTokenExpiresAt;
            @SerializedName("remitter_id")
            @Expose
            private String remitterId;
            @SerializedName("otp_sms")
            @Expose
            private String otpSms;
            @SerializedName("p_otp")
            @Expose
            private String pOtp;
            @SerializedName("notifi_sound")
            @Expose
            private String notifiSound;
            @SerializedName("notifi_display")
            @Expose
            private String notifiDisplay;
            @SerializedName("f_name")
            @Expose
            private String fName;
            @SerializedName("l_name")
            @Expose
            private Object lName;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("country_id")
            @Expose
            private String countryId;
            @SerializedName("std_code")
            @Expose
            private String stdCode;
            @SerializedName("zip_code")
            @Expose
            private String zipCode;
            @SerializedName("address_id_status")
            @Expose
            private String addressIdStatus;
            @SerializedName("national_id_status")
            @Expose
            private String nationalIdStatus;
            @SerializedName("addressproof")
            @Expose
            private String addressproof;
            @SerializedName("addressproofthumb")
            @Expose
            private String addressproofthumb;
            @SerializedName("addressproofpath")
            @Expose
            private String addressproofpath;
            @SerializedName("nationalid")
            @Expose
            private String nationalid;
            @SerializedName("nationalidthumb")
            @Expose
            private String nationalidthumb;
            @SerializedName("nationalidpath")
            @Expose
            private String nationalidpath;
            @SerializedName("designation_user")
            @Expose
            private String designationUser;
            @SerializedName("kycst")
            @Expose
            private String kycst;
            @SerializedName("state_name")
            @Expose
            private Object stateName;
            @SerializedName("addressproof_path")
            @Expose
            private String addressproofPath;
            @SerializedName("nationalid_path")
            @Expose
            private String nationalidPath;


            public String getTrx_address() {
                return trx_address;
            }

            public void setTrx_address(String trx_address) {
                this.trx_address = trx_address;
            }

            public String getTrx_address_status() {
                return trx_address_status;
            }

            public void setTrx_address_status(String trx_address_status) {
                this.trx_address_status = trx_address_status;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getRefBy() {
                return refBy;
            }

            public void setRefBy(String refBy) {
                this.refBy = refBy;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getLeg() {
                return leg;
            }

            public void setLeg(String leg) {
                this.leg = leg;
            }

            public String getLeftCount() {
                return leftCount;
            }

            public void setLeftCount(String leftCount) {
                this.leftCount = leftCount;
            }

            public String getRightCount() {
                return rightCount;
            }

            public void setRightCount(String rightCount) {
                this.rightCount = rightCount;
            }

            public String getDesignation() {
                return designation;
            }

            public void setDesignation(String designation) {
                this.designation = designation;
            }

            public String getDesignationDate() {
                return designationDate;
            }

            public void setDesignationDate(String designationDate) {
                this.designationDate = designationDate;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserpin() {
                return userpin;
            }

            public void setUserpin(String userpin) {
                this.userpin = userpin;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPasswordText() {
                return passwordText;
            }

            public void setPasswordText(String passwordText) {
                this.passwordText = passwordText;
            }

            public String getLoginpassword() {
                return loginpassword;
            }

            public void setLoginpassword(String loginpassword) {
                this.loginpassword = loginpassword;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getFirstname() {
                return firstname;
            }

            public void setFirstname(String firstname) {
                this.firstname = firstname;
            }

            public Object getMiddlename() {
                return middlename;
            }

            public void setMiddlename(Object middlename) {
                this.middlename = middlename;
            }

            public Object getLastname() {
                return lastname;
            }

            public void setLastname(Object lastname) {
                this.lastname = lastname;
            }

            public String getEmailid() {
                return emailid;
            }

            public void setEmailid(String emailid) {
                this.emailid = emailid;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobileCode() {
                return mobileCode;
            }

            public void setMobileCode(String mobileCode) {
                this.mobileCode = mobileCode;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getEmailStatus() {
                return emailStatus;
            }

            public void setEmailStatus(String emailStatus) {
                this.emailStatus = emailStatus;
            }

            public String getOtp() {
                return otp;
            }

            public void setOtp(String otp) {
                this.otp = otp;
            }

            public Object getCreatedate() {
                return createdate;
            }

            public void setCreatedate(Object createdate) {
                this.createdate = createdate;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getModifiedDate() {
                return modifiedDate;
            }

            public void setModifiedDate(String modifiedDate) {
                this.modifiedDate = modifiedDate;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }

            public String getRefId() {
                return refId;
            }

            public void setRefId(String refId) {
                this.refId = refId;
            }

            public Object getTokenid() {
                return tokenid;
            }

            public void setTokenid(Object tokenid) {
                this.tokenid = tokenid;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public String getReferalid() {
                return referalid;
            }

            public void setReferalid(String referalid) {
                this.referalid = referalid;
            }

            public Object getGcmid() {
                return gcmid;
            }

            public void setGcmid(Object gcmid) {
                this.gcmid = gcmid;
            }

            public Object getLoginid() {
                return loginid;
            }

            public void setLoginid(Object loginid) {
                this.loginid = loginid;
            }

            public String getPin() {
                return pin;
            }

            public void setPin(String pin) {
                this.pin = pin;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBActive() {
                return bActive;
            }

            public void setBActive(String bActive) {
                this.bActive = bActive;
            }

            public Object getPhoto() {
                return photo;
            }

            public void setPhoto(Object photo) {
                this.photo = photo;
            }

            public String getPhotoThumb() {
                return photoThumb;
            }

            public void setPhotoThumb(String photoThumb) {
                this.photoThumb = photoThumb;
            }

            public Object getForgetPassOtp() {
                return forgetPassOtp;
            }

            public void setForgetPassOtp(Object forgetPassOtp) {
                this.forgetPassOtp = forgetPassOtp;
            }

            public String getWalletAmount() {
                return walletAmount;
            }

            public void setWalletAmount(String walletAmount) {
                this.walletAmount = walletAmount;
            }

            public Object getFbId() {
                return fbId;
            }

            public void setFbId(Object fbId) {
                this.fbId = fbId;
            }

            public Object getTeleBotId() {
                return teleBotId;
            }

            public void setTeleBotId(Object teleBotId) {
                this.teleBotId = teleBotId;
            }

            public String getPanNumber() {
                return panNumber;
            }

            public void setPanNumber(String panNumber) {
                this.panNumber = panNumber;
            }

            public String getAadharNumber() {
                return aadharNumber;
            }

            public void setAadharNumber(String aadharNumber) {
                this.aadharNumber = aadharNumber;
            }

            public String getGstNumber() {
                return gstNumber;
            }

            public void setGstNumber(String gstNumber) {
                this.gstNumber = gstNumber;
            }

            public String getPaidStatus() {
                return paidStatus;
            }

            public void setPaidStatus(String paidStatus) {
                this.paidStatus = paidStatus;
            }

            public String getPaidDate() {
                return paidDate;
            }

            public void setPaidDate(String paidDate) {
                this.paidDate = paidDate;
            }

            public String getDefaultPaymentMode() {
                return defaultPaymentMode;
            }

            public void setDefaultPaymentMode(String defaultPaymentMode) {
                this.defaultPaymentMode = defaultPaymentMode;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getEnrollFeeId() {
                return enrollFeeId;
            }

            public void setEnrollFeeId(String enrollFeeId) {
                this.enrollFeeId = enrollFeeId;
            }

            public Object getProfilePic() {
                return profilePic;
            }

            public void setProfilePic(Object profilePic) {
                this.profilePic = profilePic;
            }

            public String getEmailToken() {
                return emailToken;
            }

            public void setEmailToken(String emailToken) {
                this.emailToken = emailToken;
            }

            public String getWalletToken() {
                return walletToken;
            }

            public void setWalletToken(String walletToken) {
                this.walletToken = walletToken;
            }

            public String getPasswordToken() {
                return passwordToken;
            }

            public void setPasswordToken(String passwordToken) {
                this.passwordToken = passwordToken;
            }

            public String getPasswordStatus() {
                return passwordStatus;
            }

            public void setPasswordStatus(String passwordStatus) {
                this.passwordStatus = passwordStatus;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getBitAddress() {
                return bitAddress;
            }

            public void setBitAddress(String bitAddress) {
                this.bitAddress = bitAddress;
            }

            public String getBitAddressStatus() {
                return bitAddressStatus;
            }

            public void setBitAddressStatus(String bitAddressStatus) {
                this.bitAddressStatus = bitAddressStatus;
            }

            public String getBitOtp() {
                return bitOtp;
            }

            public void setBitOtp(String bitOtp) {
                this.bitOtp = bitOtp;
            }

            public String getEthAddress() {
                return ethAddress;
            }

            public void setEthAddress(String ethAddress) {
                this.ethAddress = ethAddress;
            }

            public String getEthAddressStatus() {
                return ethAddressStatus;
            }

            public void setEthAddressStatus(String ethAddressStatus) {
                this.ethAddressStatus = ethAddressStatus;
            }

            public String getEthOtp() {
                return ethOtp;
            }

            public void setEthOtp(String ethOtp) {
                this.ethOtp = ethOtp;
            }

            public String getPemAddress() {
                return pemAddress;
            }

            public void setPemAddress(String pemAddress) {
                this.pemAddress = pemAddress;
            }

            public String getPemAddressStatus() {
                return pemAddressStatus;
            }

            public void setPemAddressStatus(String pemAddressStatus) {
                this.pemAddressStatus = pemAddressStatus;
            }

            public String getPemOtp() {
                return pemOtp;
            }

            public void setPemOtp(String pemOtp) {
                this.pemOtp = pemOtp;
            }

            public String getPplAddress() {
                return pplAddress;
            }

            public void setPplAddress(String pplAddress) {
                this.pplAddress = pplAddress;
            }

            public String getPplAddressStatus() {
                return pplAddressStatus;
            }

            public void setPplAddressStatus(String pplAddressStatus) {
                this.pplAddressStatus = pplAddressStatus;
            }

            public String getPplOtp() {
                return pplOtp;
            }

            public void setPplOtp(String pplOtp) {
                this.pplOtp = pplOtp;
            }

            public String getPayeerAccount() {
                return payeerAccount;
            }

            public void setPayeerAccount(String payeerAccount) {
                this.payeerAccount = payeerAccount;
            }

            public String getPayeerOtp() {
                return payeerOtp;
            }

            public void setPayeerOtp(String payeerOtp) {
                this.payeerOtp = payeerOtp;
            }

            public Object getLastResetPassAt() {
                return lastResetPassAt;
            }

            public void setLastResetPassAt(Object lastResetPassAt) {
                this.lastResetPassAt = lastResetPassAt;
            }

            public String getPaidBy() {
                return paidBy;
            }

            public void setPaidBy(String paidBy) {
                this.paidBy = paidBy;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getModifiedBy() {
                return modifiedBy;
            }

            public void setModifiedBy(String modifiedBy) {
                this.modifiedBy = modifiedBy;
            }

            public String getRoiStatus() {
                return roiStatus;
            }

            public void setRoiStatus(String roiStatus) {
                this.roiStatus = roiStatus;
            }

            public String getMobileNumber() {
                return mobileNumber;
            }

            public void setMobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
            }

            public String getBio() {
                return bio;
            }

            public void setBio(String bio) {
                this.bio = bio;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getAccountStatus() {
                return accountStatus;
            }

            public void setAccountStatus(String accountStatus) {
                this.accountStatus = accountStatus;
            }

            public String getKycStatus() {
                return kycStatus;
            }

            public void setKycStatus(String kycStatus) {
                this.kycStatus = kycStatus;
            }

            public String getMobileNumberStatus() {
                return mobileNumberStatus;
            }

            public void setMobileNumberStatus(String mobileNumberStatus) {
                this.mobileNumberStatus = mobileNumberStatus;
            }

            public Object getReferralId() {
                return referralId;
            }

            public void setReferralId(Object referralId) {
                this.referralId = referralId;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getReferralCode() {
                return referralCode;
            }

            public void setReferralCode(String referralCode) {
                this.referralCode = referralCode;
            }

            public String getRememberToken() {
                return rememberToken;
            }

            public void setRememberToken(String rememberToken) {
                this.rememberToken = rememberToken;
            }

            public String getOtpExpiresAt() {
                return otpExpiresAt;
            }

            public void setOtpExpiresAt(String otpExpiresAt) {
                this.otpExpiresAt = otpExpiresAt;
            }

            public String getRecoveryToken() {
                return recoveryToken;
            }

            public void setRecoveryToken(String recoveryToken) {
                this.recoveryToken = recoveryToken;
            }

            public Object getRecoveryTokenExpiresAt() {
                return recoveryTokenExpiresAt;
            }

            public void setRecoveryTokenExpiresAt(Object recoveryTokenExpiresAt) {
                this.recoveryTokenExpiresAt = recoveryTokenExpiresAt;
            }

            public String getRemitterId() {
                return remitterId;
            }

            public void setRemitterId(String remitterId) {
                this.remitterId = remitterId;
            }

            public String getOtpSms() {
                return otpSms;
            }

            public void setOtpSms(String otpSms) {
                this.otpSms = otpSms;
            }

            public String getPOtp() {
                return pOtp;
            }

            public void setPOtp(String pOtp) {
                this.pOtp = pOtp;
            }

            public String getNotifiSound() {
                return notifiSound;
            }

            public void setNotifiSound(String notifiSound) {
                this.notifiSound = notifiSound;
            }

            public String getNotifiDisplay() {
                return notifiDisplay;
            }

            public void setNotifiDisplay(String notifiDisplay) {
                this.notifiDisplay = notifiDisplay;
            }

            public String getFName() {
                return fName;
            }

            public void setFName(String fName) {
                this.fName = fName;
            }

            public Object getLName() {
                return lName;
            }

            public void setLName(Object lName) {
                this.lName = lName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCountryId() {
                return countryId;
            }

            public void setCountryId(String countryId) {
                this.countryId = countryId;
            }

            public String getStdCode() {
                return stdCode;
            }

            public void setStdCode(String stdCode) {
                this.stdCode = stdCode;
            }

            public String getZipCode() {
                return zipCode;
            }

            public void setZipCode(String zipCode) {
                this.zipCode = zipCode;
            }

            public String getAddressIdStatus() {
                return addressIdStatus;
            }

            public void setAddressIdStatus(String addressIdStatus) {
                this.addressIdStatus = addressIdStatus;
            }

            public String getNationalIdStatus() {
                return nationalIdStatus;
            }

            public void setNationalIdStatus(String nationalIdStatus) {
                this.nationalIdStatus = nationalIdStatus;
            }

            public String getAddressproof() {
                return addressproof;
            }

            public void setAddressproof(String addressproof) {
                this.addressproof = addressproof;
            }

            public String getAddressproofthumb() {
                return addressproofthumb;
            }

            public void setAddressproofthumb(String addressproofthumb) {
                this.addressproofthumb = addressproofthumb;
            }

            public String getAddressproofpath() {
                return addressproofpath;
            }

            public void setAddressproofpath(String addressproofpath) {
                this.addressproofpath = addressproofpath;
            }

            public String getNationalid() {
                return nationalid;
            }

            public void setNationalid(String nationalid) {
                this.nationalid = nationalid;
            }

            public String getNationalidthumb() {
                return nationalidthumb;
            }

            public void setNationalidthumb(String nationalidthumb) {
                this.nationalidthumb = nationalidthumb;
            }

            public String getNationalidpath() {
                return nationalidpath;
            }

            public void setNationalidpath(String nationalidpath) {
                this.nationalidpath = nationalidpath;
            }

            public String getDesignationUser() {
                return designationUser;
            }

            public void setDesignationUser(String designationUser) {
                this.designationUser = designationUser;
            }

            public String getKycst() {
                return kycst;
            }

            public void setKycst(String kycst) {
                this.kycst = kycst;
            }

            public Object getStateName() {
                return stateName;
            }

            public void setStateName(Object stateName) {
                this.stateName = stateName;
            }

            public String getAddressproofPath() {
                return addressproofPath;
            }

            public void setAddressproofPath(String addressproofPath) {
                this.addressproofPath = addressproofPath;
            }

            public String getNationalidPath() {
                return nationalidPath;
            }

            public void setNationalidPath(String nationalidPath) {
                this.nationalidPath = nationalidPath;
            }

        }

        }
    }
