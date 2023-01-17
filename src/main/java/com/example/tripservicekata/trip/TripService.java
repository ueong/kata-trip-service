package com.example.tripservicekata.trip;

import com.example.tripservicekata.exception.UserNotLoggedInException;
import com.example.tripservicekata.user.User;
import com.example.tripservicekata.user.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TripService {

	private UserSession userSession;
	private Trips trips;

	public TripService(UserSession userSession) {
		this(userSession, new Trips());
	}

	public TripService(UserSession userSession, Trips trips) {
		this.userSession = userSession;
		this.trips = trips;
	}

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		validateInput(user);
		if(isFriend(user, getLoggedUser())) {
			return this.trips.findTripsByUser(user);
		}
		return new ArrayList<>();
	}

	private boolean isFriend(User user, User loggedUser) {
		return user.getFriends().stream().anyMatch(friend -> friend.equals(loggedUser));
	}

	private User getLoggedUser() {
		User loggedUser = this.userSession.getLoggedUser();
		if (loggedUser == null) throw new UserNotLoggedInException();
		return loggedUser;
	}

	private void validateInput(User user) {
		if (user == null) throw new IllegalArgumentException();
	}

}
