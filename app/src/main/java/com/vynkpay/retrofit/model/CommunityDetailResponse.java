package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommunityDetailResponse {

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

        @SerializedName("team_size")
        @Expose
        private String teamSize;
        @SerializedName("team_size_active")
        @Expose
        private String teamSizeActive;
        @SerializedName("team_size_inactive")
        @Expose
        private String teamSizeInactive;
        @SerializedName("team_total_income")
        @Expose
        private String teamTotalIncome;
        @SerializedName("my_earning")
        @Expose
        private String myEarning;
        @SerializedName("direct_active")
        @Expose
        private String directActive;
        @SerializedName("bonus_achivement")
        @Expose
        private String bonusAchivement;
        @SerializedName("global_pool_status")
        @Expose
        private String globalPoolStatus;
        @SerializedName("leadership_status")
        @Expose
        private String leadershipStatus;
        @SerializedName("ambassador_status")
        @Expose
        private String ambassadorStatus;
        @SerializedName("appraisal_status")
        @Expose
        private String appraisalStatus;
        @SerializedName("chairman_status")
        @Expose
        private String chairmanStatus;
        @SerializedName("club_level")
        @Expose
        private String clubLevel;

        public String getTeamSize() {
            return teamSize;
        }

        public void setTeamSize(String teamSize) {
            this.teamSize = teamSize;
        }

        public String getTeamSizeActive() {
            return teamSizeActive;
        }

        public void setTeamSizeActive(String teamSizeActive) {
            this.teamSizeActive = teamSizeActive;
        }

        public String getTeamSizeInactive() {
            return teamSizeInactive;
        }

        public void setTeamSizeInactive(String teamSizeInactive) {
            this.teamSizeInactive = teamSizeInactive;
        }

        public String getTeamTotalIncome() {
            return teamTotalIncome;
        }

        public void setTeamTotalIncome(String teamTotalIncome) {
            this.teamTotalIncome = teamTotalIncome;
        }

        public String getMyEarning() {
            return myEarning;
        }

        public void setMyEarning(String myEarning) {
            this.myEarning = myEarning;
        }

        public String getDirectActive() {
            return directActive;
        }

        public void setDirectActive(String directActive) {
            this.directActive = directActive;
        }

        public String getBonusAchivement() {
            return bonusAchivement;
        }

        public void setBonusAchivement(String bonusAchivement) {
            this.bonusAchivement = bonusAchivement;
        }

        public String getGlobalPoolStatus() {
            return globalPoolStatus;
        }

        public void setGlobalPoolStatus(String globalPoolStatus) {
            this.globalPoolStatus = globalPoolStatus;
        }

        public String getLeadershipStatus() {
            return leadershipStatus;
        }

        public void setLeadershipStatus(String leadershipStatus) {
            this.leadershipStatus = leadershipStatus;
        }

        public String getAmbassadorStatus() {
            return ambassadorStatus;
        }

        public void setAmbassadorStatus(String ambassadorStatus) {
            this.ambassadorStatus = ambassadorStatus;
        }

        public String getAppraisalStatus() {
            return appraisalStatus;
        }

        public void setAppraisalStatus(String appraisalStatus) {
            this.appraisalStatus = appraisalStatus;
        }

        public String getChairmanStatus() {
            return chairmanStatus;
        }

        public void setChairmanStatus(String chairmanStatus) {
            this.chairmanStatus = chairmanStatus;
        }

        public String getClubLevel() {
            return clubLevel;
        }

        public void setClubLevel(String clubLevel) {
            this.clubLevel = clubLevel;
        }
    }
    }
