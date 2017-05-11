package net.wazim.countmeup.persistence;

import net.wazim.countmeup.domain.Vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryVoteRepository implements VoteRepository {

    private final Map<String, List<Vote>> votes = new ConcurrentHashMap<>();

    @Override
    public void persistVote(Vote vote) {
        List<Vote> candidateVotes = votes.getOrDefault(vote.candidateName(), new ArrayList<>());
        candidateVotes.add(vote);
        votes.put(vote.candidateName(), candidateVotes);
    }

    @Override
    public Map<String, List<Vote>> votes() {
        return votes;
    }

    public void resetVotes() {
        votes.clear();
    }
}
