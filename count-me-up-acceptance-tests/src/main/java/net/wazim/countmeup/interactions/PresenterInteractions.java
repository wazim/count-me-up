package net.wazim.countmeup.interactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import net.wazim.countmeup.domain.VotingResults;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.util.StopWatch;

public class PresenterInteractions {

    private final TestRestTemplate httpClient;
    private final ObjectMapper objectMapper;
    private VotingResults votingResults;
    private long requestTimeMillis;

    public PresenterInteractions(TestRestTemplate httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public ActionUnderTest requestsOverallResults() {
        return (givens, capturedInputAndOutputs) -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            capturedInputAndOutputs.add("Request from Presenter to CountMeUp", "/votes");
            votingResults = httpClient.getForObject("/votes", VotingResults.class);
            capturedInputAndOutputs.add("Response from CountMeUp to Presenter", objectMapper.writeValueAsString(votingResults));
            stopWatch.stop();
            requestTimeMillis = stopWatch.getTotalTimeMillis();
            capturedInputAndOutputs.add("Response Time from CountMeUp to Presenter", requestTimeMillis);
            return capturedInputAndOutputs;
        };
    }

    public StateExtractor<VotingResults> receives() {
        return inputAndOutputs -> votingResults;
    }

    public StateExtractor<Long> receivedResultsInNumberOfMilliseconds() {
        return inputAndOutputs -> requestTimeMillis;
    }
}
