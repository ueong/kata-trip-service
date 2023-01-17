package com.example.tripservicekata.trip;

import com.example.tripservicekata.exception.UserNotLoggedInException;
import com.example.tripservicekata.user.User;
import com.example.tripservicekata.user.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class TripServiceTest {
    @Test
    public void whenUserSessionReturnsNullThenThrowUserNotLoggedInException() {
        // given
        UserSession nullUserSession = new NullUserSession();
        TripService service = new TripService(nullUserSession);
        User user = new User();

        // when, then
        Assertions.assertThrows(UserNotLoggedInException.class, () -> {
            service.getTripsByUser(user);
        });
    }

    @Test
    public void whenInputUserHasNoFriendThenReturnEmptyTripList() {
        UserSession noFriendUserSession = new NoFriendUserSession();
        TripService service = new TripService(noFriendUserSession);
        User user = new User();

        // when
        List<Trip> result = service.getTripsByUser(user);

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void whenInputUserHasAFriendButItsNotLoggedUserThenReturnEmptyTripList() {
        User loggedUser = new User();
        UserSession userSession = new FakeUserSession(loggedUser);
        TripService service = new TripService(userSession);

        User user = new User();
        User usersFriend = new User();
        user.addFriend(usersFriend);

        // when
        List<Trip> result = service.getTripsByUser(user);

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void whenInputUserIsFriendOfLoggedUserThenReturnTripList() {
        // given
        User loggedUser = new User();
        UserSession userSession = new FakeUserSession(loggedUser);
        User user = new User();
        user.addFriend(loggedUser);
        Trips trips = new FakeTrips(Arrays.asList(new Trip(), new Trip()));
        TripService service = new TripService(userSession, trips);

        // when
        List<Trip> result = service.getTripsByUser(user);

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void whenInputUserIsNullThenThrowIllegalArgumentException() {
        // given
        User loggedUser = new User();
        UserSession userSession = new FakeUserSession(loggedUser);
        TripService service = new TripService(userSession);

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.getTripsByUser(null);
        });
    }

    public static class NullUserSession extends UserSession {
        @Override
        public User getLoggedUser() {
            return null;
        }
    }

    public static class NoFriendUserSession extends UserSession {
        @Override
        public User getLoggedUser() {
            return new User();
        }
    }

    public static class FakeUserSession extends UserSession {
        private final User loggedUser;

        public FakeUserSession(final User loggedUser) {
            this.loggedUser = loggedUser;
        }

        @Override
        public User getLoggedUser() {
            return this.loggedUser;
        }
    }

    public static class FakeTrips extends Trips {
        private final List<Trip> trips;
        public FakeTrips(List<Trip> trips) {
            this.trips = trips;
        }

        @Override
        public List<Trip> findTripsByUser(User user) {
            return this.trips;
        }
    }
}
