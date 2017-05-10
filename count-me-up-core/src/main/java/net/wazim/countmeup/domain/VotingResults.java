package net.wazim.countmeup.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VotingResults {

    @JsonProperty
    private List<CandidateTotalScore> candidateTotalScores;

    public VotingResults(List<CandidateTotalScore> candidateTotalScores) {
        this.candidateTotalScores = candidateTotalScores;
    }

    public VotingResults() {
        //for serialization
    }

    public List<CandidateTotalScore> candidateTotalScores() {
        return candidateTotalScores;
    }
}
