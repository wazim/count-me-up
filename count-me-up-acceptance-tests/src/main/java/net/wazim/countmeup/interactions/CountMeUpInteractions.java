package net.wazim.countmeup.interactions;

import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.persistence.VoteRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CountMeUpInteractions {

    private final VoteRepository voteRepository;

    public CountMeUpInteractions(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public StateExtractor<Vote> hasRegistered() {
        return inputAndOutputs -> {
            List<Vote> allVotes = voteRepository.votes().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            return allVotes.get(allVotes.size() - 1);
        };
    }

    public StateExtractor<Long> hasNumberOfVotesRegisteredFromVoter(String voterName) {
        return inputAndOutputs -> {
            List<Vote> allVotes = voteRepository.votes().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            return allVotes.stream().filter(vote -> vote.voterName().equals(voterName)).count();
        };
    }

}
