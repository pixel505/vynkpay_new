package com.vynkpay.events;

public class ListingBeneficiaryEvent {
    String status, beneficiary_id,ben_account,ben_ifsc,mobile_number , accountHolderName;

    public ListingBeneficiaryEvent(String status, String beneficiary_id, String ben_account, String ben_ifsc, String mobile_number,String accountHolderName) {
        this.status = status;
        this.beneficiary_id = beneficiary_id;
        this.ben_account = ben_account;
        this.ben_ifsc = ben_ifsc;
        this.mobile_number = mobile_number;
        this.accountHolderName = accountHolderName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getStatus() {
        return status;
    }

    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public String getBen_account() {
        return ben_account;
    }

    public String getBen_ifsc() {
        return ben_ifsc;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}
