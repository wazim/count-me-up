package net.wazim.countmeup.service;

import net.wazim.countmeup.domain.CandidateTotalScore;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.domain.VotingResults;
import net.wazim.countmeup.persistence.VoteRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class VotingService {

    private final VoteRepository voteRepository;

    public VotingService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public HttpStatus handleVoteSubmission(Vote vote) {
        if (voteLimitHasBeenExceededBy(vote.voterName())) {
            return HttpStatus.FORBIDDEN;
        }
        voteRepository.persistVote(vote);
        return HttpStatus.CREATED;
    }

    public VotingResults overallResultsForPoll() {
        Map<String, List<Vote>> groupedVotes = voteRepository.votes()
                .parallelStream()
                .collect(groupingBy(Vote::candidateName));

        List<CandidateTotalScore> candidateTotalScores = groupedVotes.entrySet()
                .stream()
                .map(entry -> new CandidateTotalScore(entry.getKey(), entry.getValue().size()))
                .collect(toList());

        return new VotingResults(candidateTotalScores);
    }

    private boolean voteLimitHasBeenExceededBy(String voterName) {
        return voteRepository.votes().stream()
                .filter(persistedVote -> persistedVote.voterName().equals(voterName))
                .count() >= 3;
    }

}
