package com.example.ezchores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import de.mkammerer.argon2.Argon2;
//import de.mkammerer.argon2.Argon2Factory;

 class Auth_utils {

//    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id ,
//            16 ,
//            32);

    // take a String (password / email / blabla) and hash it for use on Database
//    public String getHash(String data) {
//
//        char[] data_arr = data.toCharArray();
//        String hash = argon2.hash(3,
//                64 * 1024, //64mb
//                1, // num of thread to use
//                data_arr);
//
//        return hash;
//
//    }

    // check if the hash match or not
//    public boolean checkHash(String hash , String data){
//        return argon2.verify(hash , data.toCharArray());
//    }

    // function to check is the password is valid or not
    public boolean password_validity(String password) {
        String regex_pattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d@$!%*?&]{8,32}$";
        Pattern p = Pattern.compile(regex_pattern);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

//    function to check if the mail is valid
    public boolean email_validity(String mail){
        String regex_pattern = "^(\\w)+[\\w-\\.]*@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern p = Pattern.compile(regex_pattern);
        if (mail == null){
            return false;
        }
        Matcher m = p.matcher(mail);
        return m.matches();
    }


     //    function to check if the name is valid
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
