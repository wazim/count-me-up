package net.wazim.countmeup.service;

import net.wazim.countmeup.domain.CandidateTotalScore;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.domain.VotingResults;
import net.wazim.countmeup.persistence.VoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VotingServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Test
    public void persistsAVoteIfCurrentVoteCountIsLessThan3() {
        when(voteRepository.votes()).thenReturn(asList(new Vote("A", "B"), new Vote("A", "B")));

        VotingService votingService = new VotingService(voteRepository);
        Vote vote = new Vote("A", "B");

        HttpStatus httpStatus = votingService.handleVoteSubmission(vote);

        assertThat(httpStatus.value(), is(201));
        verify(voteRepository, times(1)).persistVote(vote);
    }

    @Test
    public void doesNotPersistVoteIfCurrentVoteCountIs3() {
        when(voteRepository.votes()).thenReturn(asList(new Vote("A", "B"), new Vote("A", "B"), new Vote("A", "B")));
        VotingService votingService = new VotingService(voteRepository);

        HttpStatus httpStatus = votingService.handleVoteSubmission(new Vote("A", "B"));

        assertThat(httpStatus.value(), is(403));
        verify(voteRepository, never()).persistVote(any());
    }

    @Test
    public void doesNotPersistVoteIfCurrentVoteCountIsGreaterThan3() {
        when(voteRepository.votes()).thenReturn(asList(new Vote("A", "B"), new Vote("A", "B"), new Vote("A", "B"), new Vote("A", "B")));
        VotingService votingService = new VotingService(voteRepository);

        HttpStatus httpStatus = votingService.handleVoteSubmission(new Vote("A", "B"));

        assertThat(httpStatus.value(), is(403));
        verify(voteRepository, never()).persistVote(any());
    }

    @Test
    public void retrievesVotingResults() {
        when(voteRepository.votes()).thenReturn(asList(
                new Vote("A", "B"),
                new Vote("A", "B"),
                new Vote("A", "B"),
                new Vote("A", "B"),
                new Vote("B", "A")
        ));
        VotingService votingService = new VotingService(voteRepository);

        VotingResults results = votingService.overallResultsForPoll();

        assertThat(results.candidateTotalScores(), hasItems(new CandidateTotalScore("B", 4), new CandidateTotalScore("A", 1)));
    }

}