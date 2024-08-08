package com.bikeride.Repository;

import com.bikeride.Model.User;
import java.sql.ResultSet;

import com.bikeride.Model.Userrequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

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
                return user;
            } else {
                return null; // or throw an exception
            }
        });
    }


}


