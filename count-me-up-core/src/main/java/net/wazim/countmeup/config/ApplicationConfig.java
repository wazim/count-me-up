package net.wazim.countmeup.config;

import net.wazim.countmeup.controller.VotesController;
import net.wazim.countmeup.persistence.VoteRepository;
import net.wazim.countmeup.service.VotingService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@SpringBootApplication
@Import(PersistenceConfig.class)
public class ApplicationConfig {

    @Resource
    private VoteRepository voteRepository;

    @Bean
    public VotesController votesController() {
        return new VotesController(votingService());
    }

    @Bean
    public VotingService votingService() {
        return new VotingService(voteRepository);
    }
}
