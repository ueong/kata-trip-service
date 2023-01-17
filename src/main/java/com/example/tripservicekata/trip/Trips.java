package com.example.tripservicekata.trip;

import com.example.tripservicekata.user.User;

import java.util.List;

public class Trips {
    public List<Trip> findTripsByUser(User user) {
        return TripDAO.findTripsByUser(user);
    }
}
