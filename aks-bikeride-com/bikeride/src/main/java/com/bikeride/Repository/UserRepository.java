package com.bikeride.Repository;

import com.bikeride.Model.User;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Queue;

import com.bikeride.Model.Request.Userrequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String INSERT_USER="INSERT INTO users(name,username,password,email,mobileNumber) values(?,?,?,?,?)";
    private final String UPDATE_PASSWORD="UPDATE users set password = ? where username = ?";

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addUser(User user) {
        return 0;
    }

    public User getUser(Userrequest userrequest) {
        String sql = "SELECT * FROM users WHERE username = ? and password=?";
        User user = new User();
         jdbcTemplate.query(sql, new Object[]{userrequest.getUsername(),userrequest.getPassword()},
                (ResultSet rs) -> {
            if (rs.next()) {
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("email"));
                return user;
            } else {
                return null; // or throw an exception
            }
        });
         if(user.getId()==null) {
             String emailsql = "SELECT * FROM users WHERE email = ? and password=?";
             jdbcTemplate.query(emailsql, new Object[]{userrequest.getUsername(), userrequest.getPassword()},
                     (ResultSet rs) -> {
                         if (rs.next()) {
                             user.setId(rs.getLong("id"));
                             user.setName(rs.getString("name"));
                             user.setEmail(rs.getString("email"));
                             user.setUsername(rs.getString("email"));
                             return user;
                         } else {
                             return null; // or throw an exception
                         }
                     });
         }
        return user;
    }

    public User checkForUserName(String name){
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{name},
                (ResultSet rs) -> {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        return user;
                    } else {
                        return null; // or throw an exception
                    }
                });
    }

    public int insertUser(Userrequest userrequest){
        String sql =INSERT_USER;
        int rowsAffected = jdbcTemplate.update(sql, userrequest.getName(), userrequest.getUsername(),
                userrequest.getPassword(), userrequest.getEmail(), userrequest.getMobileNum());

        if (rowsAffected > 0) {
            return 1; // Successfully inserted
        } else {
            return 0; // Insert failed
        }
    }

    public int updatePassword(Userrequest userrequest){
        log.info("UserRepository");
        return jdbcTemplate.update(UPDATE_PASSWORD, userrequest.getPassword(),userrequest.getUsername());
    }

}


