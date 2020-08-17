package com.googe.ssadm.sc.utils;

import com.googe.ssadm.sc.entity.Status;

public class ConvertStatusToColor {
    private ConvertStatusToColor(){}

    public static String convertStatusToColor(Status status){
        switch (status) {
            case OPEN: return "table-success";
            case REJECTED: return "table-warning";
            case DIAGNOSTIC: return "table-secondary";
            case AGREEMENT: return "table-info";
            case APPROVED: return "table-danger";
            case WAITING: return "table-dark";
            case REPAIRED: return "table-primary";
            case GIVEN: return "table-light";
        }
        return null;
    }
}
