package com.breece.app.util;

import com.breece.coreapi.authentication.AuthenticationUtils;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.test.TestFixture;

public abstract class TestUtil {

    protected String createAuthorizationHeader(String user) {
        return testFixture().getFluxzero().apply(
                fc -> AuthenticationUtils.createAuthorizationHeader(new UserId(user)));
    }

    protected abstract TestFixture testFixture();
}
