package org.qtum.wallet.utils;


import java.math.BigDecimal;

public class StringUtils {

    public static String convertBalanceToString(BigDecimal bigDecimal){
        String value = String.valueOf(bigDecimal.doubleValue());
        if (value.endsWith(".0"))
            return value.substring(0, value.length()-2);
        else
            return value;
    }

}
