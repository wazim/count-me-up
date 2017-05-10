package net.wazim.countmeup.interactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.persistence.InMemoryVoteRepository;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class VoterInteractions {

    private final String voterName;
    private final TestRestTemplate httpClient;
    private final InMemoryVoteRepository voteRepository;
    private final ObjectMapper objectMapper;
    private HttpStatus voteSubmissionResponse;

    public VoterInteractions(String voterName, TestRestTemplate httpClient, InMemoryVoteRepository voteRepository) {
        this.voterName = voterName;
        this.httpClient = httpClient;
        this.voteRepository = voteRepository;
        this.objectMapper = new ObjectMapper();
    }

    public GivensBuilder hasNotSubmittedAnyVotes() {
        return givens -> {
            givens.add("Existing Votes", emptyList());
            return givens;
        };
    }

    public GivensBuilder hasSubmitted(Vote... votes) {
        return givens -> {
            asList(votes).parallelStream().forEach(voteRepository::persistVote);
            givens.add("Existing Votes", asList(votes).stream().map(Vote::toString).collect(Collectors.joining("\n")));
            return givens;
        };
    }

    public GivensBuilder hasSubmitted(List<Vote> votes) {
        return givens -> {
            voteRepository.persistAllVotes(votes);
            return givens;
        };
    }

    public ActionUnderTest submitsAVoteForCandidate(String candidateName) {
        return (givens, capturedInputAndOutputs) -> {
            Vote vote = new Vote(voterName, candidateName);
            capturedInputAndOutputs.add(String.format("Request from Voter %s to CountMeUp", voterName), objectMapper.writeValueAsString(vote));
            voteSubmissionResponse = httpClient.postForObject("/votes", vote, HttpStatus.class);
            capturedInputAndOutputs.add(String.format("Response from CountMeUp to Voter %s", voterName), voteSubmissionResponse);
            return capturedInputAndOutputs;
        };
    }

    public StateExtractor<HttpStatus> receives() {
        return inputAndOutputs -> voteSubmissionResponse;
    }
}
