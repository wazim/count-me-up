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

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
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
        HashMap<String, List<Vote>> votes = new HashMap<>();
        votes.put("A", asList(
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B")
        ));
        when(voteRepository.votes()).thenReturn(votes);

        VotingService votingService = new VotingService(voteRepository);
        Vote vote = new Vote("Voter A", "Candidate B");

        HttpStatus httpStatus = votingService.handleVoteSubmission(vote);

        assertThat(httpStatus.value(), is(201));
        verify(voteRepository, times(1)).persistVote(vote);
    }

    @Test
    public void doesNotPersistVoteIfCurrentVoteCountIs3() {
        HashMap<String, List<Vote>> votes = new HashMap<>();
        votes.put("A", asList(
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B")
        ));
        when(voteRepository.votes()).thenReturn(votes);
        VotingService votingService = new VotingService(voteRepository);

        HttpStatus httpStatus = votingService.handleVoteSubmission(new Vote("Voter A", "Candidate B"));

        assertThat(httpStatus.value(), is(403));
        verify(voteRepository, never()).persistVote(any());
    }

    @Test
    public void doesNotPersistVoteIfCurrentVoteCountIsGreaterThan3() {
        HashMap<String, List<Vote>> votes = new HashMap<>();
        votes.put("A", asList(
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B")
        ));
        when(voteRepository.votes()).thenReturn(votes);
        VotingService votingService = new VotingService(voteRepository);

        HttpStatus httpStatus = votingService.handleVoteSubmission(new Vote("Voter A", "Candidate B"));

        assertThat(httpStatus.value(), is(403));
        verify(voteRepository, never()).persistVote(any());
    }

    @Test
    public void retrievesVotingResults() {
        HashMap<String, List<Vote>> votes = new HashMap<>();
        votes.put("A", singletonList(
                new Vote("Voter B", "Candidate A")
        ));
        votes.put("B", asList(
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B"),
                new Vote("Voter A", "Candidate B")
        ));
        when(voteRepository.votes()).thenReturn(votes);
        VotingService votingService = new VotingService(voteRepository);

        VotingResults results = votingService.overallResultsForPoll();

        assertThat(results.candidateTotalScores(), hasItems(new CandidateTotalScore("B", 4), new CandidateTotalScore("A", 1)));
    }

}