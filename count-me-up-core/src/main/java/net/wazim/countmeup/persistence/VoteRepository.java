package net.wazim.countmeup.persistence;

import net.wazim.countmeup.domain.Vote;

import java.util.List;
import java.util.Map;

public interface VoteRepository {

    void persistVote(Vote vote);

    Map<String, List<Vote>> votes();
}
