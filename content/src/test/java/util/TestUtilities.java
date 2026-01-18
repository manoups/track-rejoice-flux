package util;

import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserProfile;

public class TestUtilities {
    protected final UserProfile viewer = new UserProfile(new UserId("viewer"), null, null);
    protected final UserProfile user2 = new UserProfile(new UserId("user2"), null, null);
    protected final UserProfile Alice = new UserProfile(new UserId("Alice"), null, null);
}
