package com.breece.sighting.ui;


import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserProfile;

public class TestUtilities {
    final UserProfile viewer = new UserProfile(new UserId("viewer"), null, null);
    final UserProfile user2 = new UserProfile(new UserId("user2"), null, null);
    final UserProfile Alice = new UserProfile(new UserId("Alice"), null, null);
    final UserProfile Bob = new UserProfile(new UserId("Bob"), null, null);
}
