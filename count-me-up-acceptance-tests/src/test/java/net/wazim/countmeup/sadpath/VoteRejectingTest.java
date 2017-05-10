package net.wazim.countmeup.sadpath;

import com.googlecode.yatspec.junit.Notes;
import net.wazim.countmeup.CountMeUpTestBase;
import net.wazim.countmeup.interactions.VoterInteractions;
import net.wazim.countmeup.domain.Vote;
import org.junit.Before;
import org.junit.Test;

import static net.wazim.countmeup.Matchers.aHttpResponseOf;
import static org.hamcrest.Matchers.equalTo;

public class VoteRejectingTest extends CountMeUpTestBase {

    public static final String VOTER_NAME = "B";
    private VoterInteractions voterB;

    @Before
    public void setUpVoterInteractions() {
        this.voterB = new VoterInteractions(VOTER_NAME, testRestTemplate, voteRepository);
    }

    @Test
    @Notes("Scenario: Count Me Up only accepts 3 votes per user")
    public void countMeUpOnlyAccepts3VotesPerUser() throws Exception {
        given(voterB.hasSubmitted(
                aVote(forCandidate("A")),
                aVote(forCandidate("A")),
                aVote(forCandidate("A"))));

        when(voterB.submitsAVoteForCandidate("A"));

        then(voterB.receives(), aHttpResponseOf(403));
        then(countMeUp.hasNumberOfVotesRegisteredFromVoter(VOTER_NAME), equalTo(3L));
    }

    @Test
    @Notes("Scenario: Count Me Up only accepts 3 votes per user regardless of candidate")
    public void countMeUpOnlyAccepts3VotesPerUserRegardlessOfCandidate() throws Exception {
        given(voterB.hasSubmitted(
                aVote(forCandidate("A")),
                aVote(forCandidate("A")),
                aVote(forCandidate("D"))));

        when(voterB.submitsAVoteForCandidate("D"));

        then(voterB.receives(), aHttpResponseOf(403));
        then(countMeUp.hasNumberOfVotesRegisteredFromVoter(VOTER_NAME), equalTo(3L));
    }

    private Vote aVote(String candidateName) {
        return new Vote(VOTER_NAME, candidateName);
    }


}
