package net.wazim.countmeup.interactions;

import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.persistence.VoteRepository;

import java.util.List;

public class CountMeUpInteractions {

    private final VoteRepository voteRepository;

    public CountMeUpInteractions(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public StateExtractor<Vote> hasRegistered() {
        return inputAndOutputs -> {
            List<Vote> allVotes = voteRepository
                    .votes();
            return allVotes.get(allVotes.size() - 1);
        };
    }

    public StateExtractor<Long> hasNumberOfVotesRegisteredFromVoter(String voterName) {
        return inputAndOutputs -> voteRepository.votes().stream().filter(vote -> vote.voterName().equals(voterName)).count();
    }

}
