package net.wazim.countmeup.config;

import net.wazim.countmeup.persistence.InMemoryVoteRepository;
import net.wazim.countmeup.persistence.VoteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig {

    @Bean
    public VoteRepository voteRepository() {
        return new InMemoryVoteRepository();
    }
}
