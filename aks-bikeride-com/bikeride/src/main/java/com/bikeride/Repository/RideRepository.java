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
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class RideRepository {

    private final JdbcTemplate jdbcTemplate;
    private final String CREATE_RIDE="INSERT INTO ride(ride_name,ride_type,ride_location,ride_date,ride_time,userId,user_name,mobileNumber,itenary) values(?,?,?,?,?,?,?,?,?)";
    private final String CREATE_USER_RIDE = "INSERT INTO user_ride(ride_id,users_id,ride_name,ride_type,ride_add_user_name,mobileNumber) values(?,?,?,?,?,?)";


    public RideRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    public int createRide(RideRequest rideRequest) {
        try {
            // Insert into Ride table
            String sql = CREATE_RIDE;
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
                ps.setString(8, rideRequest.getMobileNumber());
                ps.setString(9, rideRequest.getItenary());
                return ps;
            }, keyHolder);

            // Check if the insert into Ride table was successful
            if (rowsAffected > 0) {
                // Get the generated ride_id
                Long rideId = keyHolder.getKey().longValue();

                // Insert into User_Ride table
                jdbcTemplate.update(CREATE_USER_RIDE, rideId, rideRequest.getUserId(),
                        rideRequest.getRideName(), rideRequest.getRideType(),
                        rideRequest.getUsername(), rideRequest.getMobileNumber());

                return 1;
            } else {
                return 0; // or throw an exception
            }
        } catch (DataAccessException ex) {
            // Exception handling - transaction will automatically rollback
            throw new RuntimeException("Error occurred while creating ride", ex);
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
        String selectSql = "SELECT * FROM ride WHERE ride_type = ? AND id = ?";
        String insertSql = "INSERT INTO user_ride(ride_id, users_id, ride_name, ride_type) VALUES (?, ?, ?, ?)";

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
        String checkRideSql = "SELECT * FROM ride WHERE id = ?";
        String insertUserRideSql = "INSERT INTO user_ride(ride_id, users_id, ride_name, ride_type,ride_add_user_name,mobileNumber) VALUES (?, ?, ?, ?,?,?)";
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
                    rideEvent.getRideType(),
                    addUserToRide.getRideAddUserName(),
                    addUserToRide.getMobileNumber());
            return  rowsAffected;
        }
        return 0;
    }

    public int updateRide(RideRequest rideRequest){
        String checkRideSql = "SELECT * FROM ride WHERE id = ?";
        String updateRideSql = "update ride set itenary = ? where id = ?";
        RideEvent rideEvent = jdbcTemplate.queryForObject(checkRideSql, new Object[]{rideRequest.getRideId()},
                (ResultSet rs, int rowNum) -> {
                    RideEvent event = new RideEvent();
                    event.setId(rs.getLong("id"));
                    event.setRideName(rs.getString("ride_name"));
                    event.setRideType(rs.getString("ride_type"));
                    return event;
                });
        log.info("checkRideSql :{}",rideEvent);
        if (rideEvent != null) {
            // Insert the user into the User_Ride table
            int rowsAffected = jdbcTemplate.update(updateRideSql,
                    rideRequest.getItenary(),
                    rideEvent.getId());
            return  rowsAffected;
        }
        return 0;
    }

    public int removeUser(AddUserToRide addUserToRide){
        String deleteUserRideSql = "DELETE FROM user_ride WHERE ride_id = ? AND ride_add_user_name = ?";

            // If the user is part of the ride, remove them
            int rowsAffected = jdbcTemplate.update(deleteUserRideSql, addUserToRide.getRideId(), addUserToRide.getRideAddUserName());
            return rowsAffected;

    }

    public RideEvent rideDetails(RideRequest rideRequest){
        log.info("RideRepository rideDetails: {}", rideRequest);
        String rideUserSql = "SELECT * FROM user_ride WHERE ride_id = ?";
        String rideSql = "SELECT * FROM ride WHERE id = ?";

        RideEvent rideEvent = new RideEvent();

        try {
            jdbcTemplate.query(rideSql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, rideRequest.getRideId());
                }
            }, new ResultSetExtractor<RideEvent>() {
                @Override
                public RideEvent extractData(ResultSet rs) throws SQLException {
                    log.info("extractData result ride detail:{}", rs);

                    List<User> userList = new ArrayList<>();
                    while (rs.next()) {
                        rideEvent.setRideName(rs.getString("ride_name"));
                        rideEvent.setId(rs.getLong("id"));
                        rideEvent.setUsername(rs.getString("user_name"));
                        rideEvent.setUserId(rs.getString("userId"));
                        rideEvent.setLocation(rs.getString("ride_location"));
                        rideEvent.setDateTime(rs.getString("ride_date") + " " + rs.getString("ride_time"));
                        rideEvent.setMobileNumber(rs.getString("mobileNumber"));
                        rideEvent.setItenary(rs.getString("itenary"));
                    }
                    return rideEvent;
                }
            });
            log.info("ride details: {}",rideEvent);
            jdbcTemplate.query(rideUserSql, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, rideRequest.getRideId());
                }
            }, new ResultSetExtractor<RideEvent>() {
                @Override
                public RideEvent extractData(ResultSet rs) throws SQLException {
                    log.info("extractData result ride detail:{}", rs);

                    List<User> userList = new ArrayList<>();
                    while (rs.next()) {
                        User us = new User();
                        rideEvent.setId(rs.getLong("id"));
                        rideEvent.setRideName(rs.getString("ride_name"));
                        rideEvent.setRideType(rs.getString("ride_type"));
                        rideEvent.setUserId(rs.getString("users_id"));
                        us.setUsername(rs.getString("ride_add_user_name"));
                        userList.add(us);
                    }
                    rideEvent.setUser(userList);
                    return rideEvent;
                }
            });
        } catch (Exception e) {
            log.error("Error while fetching ride user details", e);
        }

        return rideEvent;
    }

    public int deleteRide(RideRequest rideRequest){
        String deleteRideSql = "DELETE FROM ride WHERE id = ?";
        String deleteUserRideSql="DELETE FROM user_ride WHERE ride_id = ?";
        // If the user is part of the ride, remove them
        int rowsAffected = jdbcTemplate.update(deleteRideSql, rideRequest.getRideId());
         rowsAffected += jdbcTemplate.update(deleteUserRideSql, rideRequest.getRideId());
        return rowsAffected;

    }
}
