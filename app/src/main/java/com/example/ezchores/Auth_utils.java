package com.example.ezchores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


 class Auth_utils {

    /*
     function to check is the password is valid
     requirement : at least :
     - one digit
     - one uppercase
     - one lowercase
     - one of !@#$%^&*
     */

    public boolean password_validity(String password) {
        String regex_pattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,32}$";
        Pattern p = Pattern.compile(regex_pattern);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    //function to check if the mail is valid
    public boolean email_validity(String mail){
        String regex_pattern = "^([\\w])+[\\w-\\.]*@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern p = Pattern.compile(regex_pattern);
        if (mail == null){
            return false;
        }
        Matcher m = p.matcher(mail);
        return m.matches();
    }


     /*
         function to check if the name is valid
         requirement : surName + space + lastName
      */
     public boolean full_name_validity(String full_name){
         String regex_pattern = "^([\\w\\W]){2,}\\s([\\w\\W]){2,}$";
         Pattern p = Pattern.compile(regex_pattern);
         if (full_name == null){
             return false;
         }
         Matcher m = p.matcher(full_name);
         return m.matches();
     }



}
