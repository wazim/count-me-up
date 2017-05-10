package net.wazim.countmeup.happypath;

import com.googlecode.yatspec.junit.Notes;
import net.wazim.countmeup.CountMeUpTestBase;
import net.wazim.countmeup.interactions.VoterInteractions;
import org.junit.Before;
import org.junit.Test;

import static net.wazim.countmeup.Matchers.aHttpResponseOf;
import static net.wazim.countmeup.Matchers.aVote;

public class VoteAcceptingTest extends CountMeUpTestBase {

    private VoterInteractions voterA;

    @Before
    public void setUpVoterInteractions() {
        this.voterA = new VoterInteractions("A", testRestTemplate, voteRepository);
    }

    @Test
    @Notes("Scenario: Count Me Up accepts a vote")
    public void countMeUpAcceptsAVote() throws Exception {
        given(voterA.hasNotSubmittedAnyVotes());

        when(voterA.submitsAVoteForCandidate("A"));

        then(countMeUp.hasRegistered(), aVote(fromVoter("A"), forCandidate("A")));
        then(voterA.receives(), aHttpResponseOf(201));
    }

}
