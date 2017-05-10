package net.wazim.countmeup.persistence;

import net.wazim.countmeup.domain.Vote;

import java.util.Collection;
import java.util.List;

public interface VoteRepository {

    void persistVote(Vote vote);

    List<Vote> votes();
}
