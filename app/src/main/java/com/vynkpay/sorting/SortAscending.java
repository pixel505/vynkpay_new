package com.vynkpay.sorting;

import com.vynkpay.activity.recharge.mobile.model.PlanModel;
import java.util.Comparator;

public class SortAscending implements Comparator<PlanModel> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(PlanModel a, PlanModel b) {
        return Integer.parseInt(a.getRecharge_amount()) - Integer.parseInt(b.getRecharge_amount());
    }

}