package com.bikeride.Repository;

import com.bikeride.Model.User;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Queue;

import com.bikeride.Model.Request.Userrequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String INSERT_USER="INSERT INTO USER(name,username,password,email,mobile) values(?,?,?,?,?)";

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addUser(User user) {
        return 0;
    }

    public User getUser(Userrequest userrequest) {
        String sql = "SELECT * FROM users WHERE username = ? and password=?";
        return jdbcTemplate.query(sql, new Object[]{userrequest.getUsername(),userrequest.getPassword()},
                (ResultSet rs) -> {
            if (rs.next()) {
                User user = new User();
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
        return jdbcTemplate.query(sql, new Object[]{userrequest.getName(),userrequest.getUsername(),
                        userrequest.getPassword(),userrequest.getEmail(),userrequest.getMobileNum()},
                (ResultSet rs) -> {
                    if (rs!=null && rs.next()) {
                        return 1;
                    } else {
                        return 0; // or throw an exception
                    }
                });
    }

}


