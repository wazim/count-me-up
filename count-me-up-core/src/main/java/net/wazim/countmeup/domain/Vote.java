package net.wazim.countmeup.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class Vote {
    @JsonProperty
    private String voterName;

    @JsonProperty
    private String candidateName;

    public Vote(String voterName, String candidateName) {
        this.voterName = voterName;
        this.candidateName = candidateName;
    }

    public Vote() {
        //for serialization
    }

    public String voterName() {
        return voterName;
    }

    public String candidateName() {
        return candidateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote that = (Vote) o;

        return Objects.equals(this.voterName, that.voterName) &&
                Objects.equals(this.candidateName, that.candidateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voterName, candidateName);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("voterName = " + voterName)
                .add("candidateName = " + candidateName).toString();
    }
}
