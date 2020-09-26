package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatementResponse {
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

        @SerializedName("user_data")
        @Expose
        private UserData userData;
        @SerializedName("total_earning")
        @Expose
        private String totalEarning;
        @SerializedName("bonus_wallet_balance")
        @Expose
        private String bonusWalletBalance;
        @SerializedName("bonus_wallet_received")
        @Expose
        private String bonusWalletReceived;
        @SerializedName("bonus_wallet_send")
        @Expose
        private String bonusWalletSend;
        @SerializedName("referral_bonus")
        @Expose
        private String referralBonus;
        @SerializedName("generation_bonus")
        @Expose
        private String generationBonus;
        @SerializedName("global_pool_bonus")
        @Expose
        private String globalPoolBonus;
        @SerializedName("leadership_bonus")
        @Expose
        private String leadershipBonus;
        @SerializedName("ambassador_bonus")
        @Expose
        private String ambassadorBonus;
        @SerializedName("appraisal_bonus")
        @Expose
        private String appraisalBonus;
        @SerializedName("chairman_bonus")
        @Expose
        private String chairmanBonus;

        public UserData getUserData() {
            return userData;
        }

        public void setUserData(UserData userData) {
            this.userData = userData;
        }

        public String getTotalEarning() {
            return totalEarning;
        }

        public void setTotalEarning(String totalEarning) {
            this.totalEarning = totalEarning;
        }

        public String getBonusWalletBalance() {
            return bonusWalletBalance;
        }

        public void setBonusWalletBalance(String bonusWalletBalance) {
            this.bonusWalletBalance = bonusWalletBalance;
        }

        public String getBonusWalletReceived() {
            return bonusWalletReceived;
        }

        public void setBonusWalletReceived(String bonusWalletReceived) {
            this.bonusWalletReceived = bonusWalletReceived;
        }

        public String getBonusWalletSend() {
            return bonusWalletSend;
        }

        public void setBonusWalletSend(String bonusWalletSend) {
            this.bonusWalletSend = bonusWalletSend;
        }

        public String getReferralBonus() {
            return referralBonus;
        }

        public void setReferralBonus(String referralBonus) {
            this.referralBonus = referralBonus;
        }

        public String getGenerationBonus() {
            return generationBonus;
        }

        public void setGenerationBonus(String generationBonus) {
            this.generationBonus = generationBonus;
        }

        public String getGlobalPoolBonus() {
            return globalPoolBonus;
        }

        public void setGlobalPoolBonus(String globalPoolBonus) {
            this.globalPoolBonus = globalPoolBonus;
        }

        public String getLeadershipBonus() {
            return leadershipBonus;
        }

        public void setLeadershipBonus(String leadershipBonus) {
            this.leadershipBonus = leadershipBonus;
        }

        public String getAmbassadorBonus() {
            return ambassadorBonus;
        }

        public void setAmbassadorBonus(String ambassadorBonus) {
            this.ambassadorBonus = ambassadorBonus;
        }

        public String getAppraisalBonus() {
            return appraisalBonus;
        }

        public void setAppraisalBonus(String appraisalBonus) {
            this.appraisalBonus = appraisalBonus;
        }

        public String getChairmanBonus() {
            return chairmanBonus;
        }

        public void setChairmanBonus(String chairmanBonus) {
            this.chairmanBonus = chairmanBonus;
        }

        public class UserData {

            @SerializedName("username")
            @Expose
            private String username;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
    }
