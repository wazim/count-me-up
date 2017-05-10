package net.wazim.countmeup.persistence;

import net.wazim.countmeup.domain.Vote;

import java.util.ArrayList;
import java.util.List;

public class InMemoryVoteRepository implements VoteRepository {

    private final List<Vote> votes = new ArrayList<>();

    @Override
    public void persistVote(Vote vote) {
        votes.add(vote);
    }

    @Override
    public List<Vote> votes() {
        return votes;
    }

    public void persistAllVotes(List<Vote> votes) {
        this.votes.addAll(votes);
    }

    public void resetVotes() {
        this.votes.clear();
    }
}
