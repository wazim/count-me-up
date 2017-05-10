package net.wazim.countmeup.happypath;

import com.googlecode.yatspec.junit.Notes;
import net.wazim.countmeup.CountMeUpTestBase;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.interactions.PresenterInteractions;
import net.wazim.countmeup.interactions.VoterInteractions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.lessThan;

@Ignore("Ignore until fixed...")
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
        given(someVoters.hasSubmitted(totalVotesOf(8000000, forCandidate("A"))));
        given(someVoters.hasSubmitted(totalVotesOf(2000000, forCandidate("B"))));
        given(someVoters.hasSubmitted(totalVotesOf(6000000, forCandidate("C"))));
        given(someVoters.hasSubmitted(totalVotesOf(4000000, forCandidate("D"))));

        when(aPresenter.requestsOverallResults());

//        then(aPresenter.receives(), votingResults(
//                withResultsForCandidate("A", scoring(8000000)),
//                withResultsForCandidate("B", scoring(2000000)),
//                withResultsForCandidate("C", scoring(6000000)),
//                withResultsForCandidate("D", scoring(4000000))
//        ));
        then(aPresenter.receivedResultsInNumberOfMilliseconds(), lessThan(1000L));
    }

    private List<Vote> totalVotesOf(int numberOfVotes, String candidate) {
        return IntStream.range(0, numberOfVotes)
                .parallel()
                .mapToObj(value -> new Vote("Voter" + value, candidate))
                .collect(Collectors.toList());
    }

}
