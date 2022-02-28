package de.matthesvoss.security;

import com.sun.jna.platform.win32.Crypt32Util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ChromiumPasswordRecovery {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            String loginData = System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Login Data";
            Files.copy(new File(loginData).toPath(), new File(loginData + ".db").toPath(), StandardCopyOption.REPLACE_EXISTING);
            ResultSet rs = DriverManager.getConnection("jdbc:sqlite:" + loginData + ".db").prepareStatement("SELECT `origin_url`,`username_value`,`password_value` from `logins`").executeQuery();
            while (rs.next())
                System.out.println(rs.getString("origin_url") + ":" + rs.getString("username_value") + ":" + new String(Crypt32Util.cryptUnprotectData(rs.getBytes("password_value"))));
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
