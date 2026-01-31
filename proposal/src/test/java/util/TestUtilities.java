package util;

import com.breece.coreapi.user.api.CreateUser;
import com.breece.coreapi.user.api.UserId;
import com.breece.coreapi.user.api.model.UserDetails;
import com.breece.coreapi.user.api.model.UserProfile;

public class TestUtilities {
    protected final UserProfile viewer = new UserProfile(new UserId("viewer"), new UserDetails("Viewer", "v@a"), null);
    protected final UserProfile user2 = new UserProfile(new UserId("user2"), new UserDetails("User2", "v@a"), null);
    protected final UserProfile Alice = new UserProfile(new UserId("Alice"), new UserDetails("Alice", "v@a"), null);

    protected CreateUser createUserFromProfile(UserProfile profile) {
        return CreateUser.builder()
                .userId(profile.userId())
                .details(UserDetails.builder().name(profile.details().name()).email(profile.details().email()).build())
                .build();
    }
}
