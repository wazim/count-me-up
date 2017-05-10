package net.wazim.countmeup;

import net.wazim.countmeup.domain.Vote;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

public class Matchers {

    public static Matcher<HttpStatus> aHttpResponseOf(int httpStatusCode) {
        return new TypeSafeMatcher<HttpStatus>() {
            @Override
            protected boolean matchesSafely(HttpStatus httpStatus) {
                return httpStatus.value() == httpStatusCode;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.valueOf(httpStatusCode));
            }
        };
    }

    public static Matcher<Vote> aVote(String voterName, String candidateName) {
        return new TypeSafeMatcher<Vote>() {
            @Override
            protected boolean matchesSafely(Vote vote) {
                return vote.equals(new Vote(voterName, candidateName));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(new Vote(voterName, candidateName).toString());
            }
        };
    }
}
