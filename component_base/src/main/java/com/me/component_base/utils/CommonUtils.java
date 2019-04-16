package com.me.component_base.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yang
 */
public class CommonUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{8,13}$");
    public static boolean isPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            Matcher matcher = PHONE_PATTERN.matcher(phone);
            return matcher.matches();
        }
        return false;
    }

    /*
     * 必须包含字母，数字，大/小写字母，特殊符号
     */
//    private static final Pattern PWD_PATTERN =
//            Pattern.compile("^(?=.*?[0-9])(?=.*?[a-zA-Z])[a-zA-Z0-9$@$!%*#?&.]{8,16}$");

    /**
     * 必须包含字母，数字，大/小写字母
     */
    private static final Pattern PWD_PATTERN =
            Pattern.compile("^(?=.*?[0-9])(?=.*?[a-zA-Z])[a-zA-Z0-9]{6,20}$");
    public static boolean isPwdValid(String pwd) {
        if (!TextUtils.isEmpty(pwd)) {
            Matcher matcher = PWD_PATTERN.matcher(pwd);
            return matcher.matches();
        }
        return false;
    }

    /**
     * 判断邮箱是否合法
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) {
            return false;
        }
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        //复杂匹配
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    /**
     * 判断时间格式是否合法
     */
    private static final Pattern TIME_PATTERN = Pattern.compile("([0-1]?[0-9]|2[0-3]):([0-5]?[0-9])");
    public static boolean isTimeValid(String time) {
        if (time == null || "".equals(time)) {
            return false;
        }

        Matcher m = TIME_PATTERN.matcher(time);
        return m.matches();
    }

}
