package net.wazim.countmeup.happypath;

import com.googlecode.yatspec.junit.Notes;
import net.wazim.countmeup.CountMeUpTestBase;
import net.wazim.countmeup.domain.CandidateTotalScore;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.domain.VotingResults;
import net.wazim.countmeup.interactions.PresenterInteractions;
import net.wazim.countmeup.interactions.VoterInteractions;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.lessThan;

public class VoteResultsTest extends CountMeUpTestBase {

    private VoterInteractions someVoters;
    private PresenterInteractions aPresenter;

    @Before
    public void setUpVoterInteractions() {
        this.someVoters = new VoterInteractions("A", testRestTemplate, voteRepository);
        this.aPresenter = new PresenterInteractions(testRestTemplate);
    }

    @Test
    @Notes("Scenario: Count Me Up returns the voting results")
    public void countMeUpReturnsTheVotingResults() throws Exception {
        given(someVoters.hasSubmitted(totalVotesOf(8000000), forCandidate("A")));
        given(someVoters.hasSubmitted(totalVotesOf(2000000), forCandidate("B")));
        given(someVoters.hasSubmitted(totalVotesOf(6000000), forCandidate("C")));
        given(someVoters.hasSubmitted(totalVotesOf(4000000), forCandidate("D")));

        when(aPresenter.requestsOverallResults());

        then(aPresenter.receives(), votingResults(
                withResultsForCandidate("A", totalVotesOf(8000000)),
                withResultsForCandidate("B", totalVotesOf(2000000)),
                withResultsForCandidate("C", totalVotesOf(6000000)),
                withResultsForCandidate("D", totalVotesOf(4000000))
        ));
        then(aPresenter.receivedResultsInNumberOfMilliseconds(), lessThan(1000L));
    }

    private Matcher<VotingResults> votingResults(CandidateTotalScore... candidateTotalScores) {
        return new TypeSafeMatcher<VotingResults>() {
            @Override
            protected boolean matchesSafely(VotingResults votingResults) {
                return votingResults.candidateTotalScores().containsAll(Arrays.asList(candidateTotalScores));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(Arrays.asList(candidateTotalScores).stream()
                        .map(CandidateTotalScore::toString)
                        .collect(Collectors.joining(", ")));
            }
        };
    }

    private int totalVotesOf(int numberOfVotes) {
        return numberOfVotes;
    }

    private CandidateTotalScore withResultsForCandidate(String candidateName, int totalNumberOfVotes) {
        return new CandidateTotalScore(candidateName, totalNumberOfVotes);
    }


}
