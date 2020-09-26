package com.vynkpay.models;

public class WithdrawalRequestModel {
    String id ;
    String user_id ;
    String type;
    String amount_wt ;
    String amount;
    String admin_chrg;
    String admin_chrg_amount;
    String tds ;
    String tds_amount ;
    String other_fee_amount ;
    String shopping ;
    String shopping_amount;
    String invoice_number;
    String description;
    String name_in_bank;
    String bank_name;
    String branch_address;
    String account_number;
    String ifsc_code;
    String mode;
    String bit_address;
    String created_date;
    String isactive;
    boolean clicked;

    public WithdrawalRequestModel(String id, String user_id, String type, String amount_wt, String amount, String admin_chrg, String admin_chrg_amount, String tds, String tds_amount, String other_fee_amount, String shopping, String shopping_amount, String invoice_number, String description, String name_in_bank, String bank_name, String branch_address, String account_number, String ifsc_code, String mode, String bit_address, String created_date, String isactive, boolean clicked) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.amount_wt = amount_wt;
        this.amount = amount;
        this.admin_chrg = admin_chrg;
        this.admin_chrg_amount = admin_chrg_amount;
        this.tds = tds;
        this.tds_amount = tds_amount;
        this.other_fee_amount = other_fee_amount;
        this.shopping = shopping;
        this.shopping_amount = shopping_amount;
        this.invoice_number = invoice_number;
        this.description = description;
        this.name_in_bank = name_in_bank;
        this.bank_name = bank_name;
        this.branch_address = branch_address;
        this.account_number = account_number;
        this.ifsc_code = ifsc_code;
        this.mode = mode;
        this.bit_address = bit_address;
        this.created_date = created_date;
        this.isactive = isactive;
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount_wt() {
        return amount_wt;
    }

    public void setAmount_wt(String amount_wt) {
        this.amount_wt = amount_wt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdmin_chrg() {
        return admin_chrg;
    }

    public void setAdmin_chrg(String admin_chrg) {
        this.admin_chrg = admin_chrg;
    }

    public String getAdmin_chrg_amount() {
        return admin_chrg_amount;
    }

    public void setAdmin_chrg_amount(String admin_chrg_amount) {
        this.admin_chrg_amount = admin_chrg_amount;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getTds_amount() {
        return tds_amount;
    }

    public void setTds_amount(String tds_amount) {
        this.tds_amount = tds_amount;
    }

    public String getOther_fee_amount() {
        return other_fee_amount;
    }

    public void setOther_fee_amount(String other_fee_amount) {
        this.other_fee_amount = other_fee_amount;
    }

    public String getShopping() {
        return shopping;
    }

    public void setShopping(String shopping) {
        this.shopping = shopping;
    }

    public String getShopping_amount() {
        return shopping_amount;
    }

    public void setShopping_amount(String shopping_amount) {
        this.shopping_amount = shopping_amount;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_in_bank() {
        return name_in_bank;
    }

    public void setName_in_bank(String name_in_bank) {
        this.name_in_bank = name_in_bank;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBit_address() {
        return bit_address;
    }

    public void setBit_address(String bit_address) {
        this.bit_address = bit_address;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }
}
