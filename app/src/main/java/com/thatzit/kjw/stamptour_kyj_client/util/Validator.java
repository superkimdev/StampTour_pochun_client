package com.thatzit.kjw.stamptour_kyj_client.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kjw on 2016. 10. 11..
 */

public class Validator {
    public Validator() {
    }

    public boolean passwordValidate(String reg_exp, final String hex) {
        Pattern pattern = Pattern.compile(reg_exp);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean emailValidate(String reg_exp,final String hex) {
        Pattern pattern = Pattern.compile(reg_exp);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean nickValidate(String reg_exp,final String hex) {
        Pattern pattern = Pattern.compile(reg_exp);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean socialValidate(String appid,String phone){
        if((!(appid.isEmpty()))&&(!(phone.isEmpty()))){
            return true;
        }else{
            return false;
        }
    }
    public boolean passwordValidateForChange(final String input,final String input_repeat) {
        return !input.equals(input_repeat);
    }

}
