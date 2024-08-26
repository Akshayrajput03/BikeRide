package com.bikeride.Repository;

import com.bikeride.Model.Request.AddUserToRide;
import com.bikeride.Model.Request.JoinRideRequest;
import com.bikeride.Model.Request.RideRequest;
import com.bikeride.Model.RideEvent;
import com.bikeride.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class RideRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String CREATE_RIDE="INSERT INTO Ride(ride_name,ride_type,ride_location,ride_date,ride_time,userId,user_name) values(?,?,?,?,?,?,?)";
    private final String CREATE_USER_RIDE = "INSERT INTO User_Ride(ride_id,users_id,ride_name,ride_type) values(?,?,?,?)";


    public RideRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createRide(RideRequest rideRequest){
        String sql = CREATE_RIDE;

        // Insert into Ride table
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, rideRequest.getRideName());
            ps.setString(2, rideRequest.getRideType());
            ps.setString(3, rideRequest.getLocation());
            ps.setString(4, rideRequest.getDate());
            ps.setString(5, rideRequest.getTime());
            ps.setLong(6, rideRequest.getUserId());
            ps.setString(7, rideRequest.getUsername());
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            // Get the generated ride_id
            Long rideId = keyHolder.getKey().longValue();

            // Insert into User_Ride table
            jdbcTemplate.update(CREATE_USER_RIDE, rideId, rideRequest.getUserId(),
                    rideRequest.getRideName(), rideRequest.getRideType());

            return 1;
        } else {
            return 0; // or throw an exception
        }
    }

    public List<RideEvent> getPublicRide(){
        String rideSql = "SELECT * FROM ride WHERE ride_type = ?";
        String userSql = "SELECT u.id, u.username FROM user_ride ur JOIN users u ON ur.users_id = u.id WHERE ur.ride_id = ?";

        return jdbcTemplate.query(rideSql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, "PUBLIC"); // Set ride type to PUBLIC
            }
        }, new ResultSetExtractor<List<RideEvent>>() {
            @Override
            public List<RideEvent> extractData(ResultSet rs) throws SQLException {
                List<RideEvent> list = new ArrayList<>();
                while (rs.next()) {
                    RideEvent rideEvent = new RideEvent();
                    rideEvent.setId(rs.getLong("id"));
                    rideEvent.setRideName(rs.getString("ride_name"));
                    rideEvent.setRideType(rs.getString("ride_type"));
                    rideEvent.setUserId(rs.getString("userId"));
                    rideEvent.setUsername(rs.getString("user_name"));

                    // Fetch users for this ride
                    List<User> users = jdbcTemplate.query(userSql, new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setLong(1, rideEvent.getId());
                        }
                    }, (ResultSet userRs) -> {
                        List<User> userList = new ArrayList<>();
                        while (userRs.next()) {
                            User user = new User();
                            user.setId(userRs.getLong("id"));
                            user.setUsername(userRs.getString("username"));
                            userList.add(user);
                        }
                        return userList;
                    });

                    rideEvent.setUser(users);
                    list.add(rideEvent);
                }
                return list;
            }
        });

    }

    public List<RideEvent> getPrivateRide(String userId){
        String rideSql = "SELECT * FROM ride WHERE ride_type = ? AND userId = ?";
        String userSql = "SELECT u.id, u.username FROM user_ride ur JOIN users u ON ur.users_id = u.id WHERE ur.ride_id = ?";

        return jdbcTemplate.query(rideSql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, "PRIVATE"); // Ensure ride type is PRIVATE
                ps.setString(2, userId);
            }
        }, new ResultSetExtractor<List<RideEvent>>() {
            @Override
            public List<RideEvent> extractData(ResultSet rs) throws SQLException {
                List<RideEvent> list = new ArrayList<>();
                while (rs.next()) {
                    RideEvent rideEvent = new RideEvent();
                    rideEvent.setId(rs.getLong("id"));
                    rideEvent.setRideName(rs.getString("ride_name"));
                    rideEvent.setRideType(rs.getString("ride_type"));
                    rideEvent.setUserId(rs.getString("userId"));
                    rideEvent.setUsername(rs.getString("user_name"));

                    // Log the current ride details
                    log.info("Processing RideEvent: {}", rideEvent);

                    // Fetch users for this ride
                    List<User> users = jdbcTemplate.query(userSql, new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps) throws SQLException {
                            ps.setLong(1, rideEvent.getId());
                        }
                    }, (ResultSet userRs) -> {
                        List<User> userList = new ArrayList<>();
                        while (userRs.next()) {
                            User user = new User();
                            user.setId(userRs.getLong("id"));
                            user.setUsername(userRs.getString("username"));
                            userList.add(user);
                        }
                        return userList;
                    });

                    rideEvent.setUser(users);
                    list.add(rideEvent);
                }
                return list;
            }
        });
    }

    public int joinRide(JoinRideRequest joinRideRequest){
        List<RideEvent> list = new ArrayList<>();
        String selectSql = "SELECT * FROM Ride WHERE ride_type = ? AND id = ?";
        String insertSql = "INSERT INTO User_Ride(ride_id, users_id, ride_name, ride_type) VALUES (?, ?, ?, ?)";

        // Check if the ride exists and is of type "PRIVATE"
        jdbcTemplate.query(selectSql, new Object[]{"PRIVATE", joinRideRequest.getRideId()},
                (ResultSet rs) -> {
                    if (rs != null && rs.next()) {
                        RideEvent rideEvent = new RideEvent();
                        rideEvent.setRideName(rs.getString("ride_name"));
                        rideEvent.setId(rs.getLong("id"));
                        rideEvent.setUsername(rs.getString("user_name"));
                        rideEvent.setUserId(rs.getString("userId"));
                        list.add(rideEvent);
                    }
                });

        if (!list.isEmpty()) {
            RideEvent rideEvent = list.get(0);
            // Insert the new user into User_Ride table
            int rowsAffected = jdbcTemplate.update(insertSql,
                    rideEvent.getId(), joinRideRequest.getUserId(), rideEvent.getRideName(), rideEvent.getRideType());

            return rowsAffected; // Return the number of rows affected by the insert
        } else {
            return 0; // No matching ride found or ride is not "PRIVATE"
        }
    }

    public int addUser(AddUserToRide addUserToRide){
        String checkRideSql = "SELECT * FROM Ride WHERE id = ?";
        String insertUserRideSql = "INSERT INTO User_Ride(ride_id, users_id, ride_name, ride_type) VALUES (?, ?, ?, ?)";
        RideEvent rideEvent = jdbcTemplate.queryForObject(checkRideSql, new Object[]{addUserToRide.getRideId()},
                (ResultSet rs, int rowNum) -> {
                    RideEvent event = new RideEvent();
                    event.setId(rs.getLong("id"));
                    event.setRideName(rs.getString("ride_name"));
                    event.setRideType(rs.getString("ride_type"));
                    return event;
                });

        if (rideEvent != null) {
            // Insert the user into the User_Ride table
            int rowsAffected = jdbcTemplate.update(insertUserRideSql,
                    rideEvent.getId(),
                    addUserToRide.getUserId(),
                    rideEvent.getRideName(),
                    rideEvent.getRideType());
            return  rowsAffected;
        }
        return 0;
    }

    public int removeUser(AddUserToRide addUserToRide){
        String checkUserRideSql = "SELECT COUNT(*) FROM User_Ride WHERE ride_id = ? AND users_id = ?";
        String deleteUserRideSql = "DELETE FROM User_Ride WHERE ride_id = ? AND users_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkUserRideSql, new Object[]{addUserToRide.getRideId(), addUserToRide.getUserId()}, Integer.class);

        if (count != null && count > 0) {
            // If the user is part of the ride, remove them
            int rowsAffected = jdbcTemplate.update(deleteUserRideSql, addUserToRide.getRideId(), addUserToRide.getUserId());
            return rowsAffected;
        }
        return 0;
    }
}
