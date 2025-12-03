package com.breece.trackrejoice;

import com.breece.trackrejoice.user.api.GetUsers;
import io.fluxzero.sdk.test.TestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(classes = App.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class AppTest {

	@Autowired TestFixture testFixture;

	@Test
	void createUser() {
		testFixture.whenCommand("/com/breece/trackrejoice/user/create-user.json")
				.expectEvents("/com/breece/trackrejoice/user/create-user.json");
	}

	@Test
	void getUsers() {
		testFixture.givenCommands("/com/breece/trackrejoice/user/create-user.json")
				.whenQuery(new GetUsers())
				.expectResult(r -> r.size() == 1);
	}
}
