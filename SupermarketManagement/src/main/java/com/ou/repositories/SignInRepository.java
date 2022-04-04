package com.ou.repositories;

import com.ou.pojos.Staff;
import com.ou.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInRepository {
    
    public Staff getAccount(String username, String password) throws SQLException{
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM staff,person  WHERE staff.sta_id = person.pers_id"
                    + " AND staff.sta_username = ? "
                    + " AND staff.sta_password =  ? "
                    + " AND person.pers_is_active = true";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(rs.next()){
                Staff staff = new Staff();
                
                staff.setStaUsername(rs.getString("sta_username"));
                staff.setStaPassword(rs.getString("sta_password"));
                staff.setStaIsAdmin(rs.getBoolean("sta_is_admin"));
                return  staff;
            }
            else
                return null;
        }
    }

}
