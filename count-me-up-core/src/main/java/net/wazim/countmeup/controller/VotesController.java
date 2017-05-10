package net.wazim.countmeup.controller;

import net.wazim.countmeup.domain.Vote;
import net.wazim.countmeup.domain.VotingResults;
import net.wazim.countmeup.service.VotingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
public class VotesController {

    private final VotingService votingService;

    public VotesController(VotingService votingService) {
        this.votingService = votingService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpStatus receiveVote(@RequestBody Vote vote) {
        return votingService.handleVoteSubmission(vote);
    }

    @RequestMapping(method = RequestMethod.GET)
    public VotingResults votingResults() {
        return votingService.overallResultsForPoll();
    }

}
