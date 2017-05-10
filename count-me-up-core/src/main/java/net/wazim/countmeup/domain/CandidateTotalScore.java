package net.wazim.countmeup.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CandidateTotalScore {

    @JsonProperty
    private String candidateName;
    @JsonProperty
    private int totalVotes;

    public CandidateTotalScore(String candidateName, int totalVotes) {
        this.candidateName = candidateName;
        this.totalVotes = totalVotes;
    }

    public CandidateTotalScore() {
        //for serialization
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateTotalScore that = (CandidateTotalScore) o;

        return Objects.equals(this.candidateName, that.candidateName) &&
                Objects.equals(this.totalVotes, that.totalVotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateName, totalVotes);
    }

    @Override
    public String toString() {
        return "CandidateTotalScore{" +
                "candidateName='" + candidateName + '\'' +
                ", totalVotes=" + totalVotes +
                '}';
    }
}
