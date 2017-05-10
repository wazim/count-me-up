package net.wazim.countmeup;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import net.wazim.countmeup.config.ApplicationConfig;
import net.wazim.countmeup.interactions.CountMeUpInteractions;
import net.wazim.countmeup.persistence.InMemoryVoteRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestContextManager;

import javax.annotation.Resource;

import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpecRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,
        classes = {ApplicationConfig.class}
)
public abstract class CountMeUpTestBase extends TestState implements WithCustomResultListeners {

    @Resource
    protected TestRestTemplate testRestTemplate;
    @Resource
    protected InMemoryVoteRepository voteRepository;

    protected CountMeUpInteractions countMeUp;

    @Before
    public void setUp() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        countMeUp = new CountMeUpInteractions(voteRepository);
    }

    @After
    public void resetVotes() {
        voteRepository.resetVotes();
    }

    protected String fromVoter(String voterName) {
        return voterName;
    }

    protected String forCandidate(String candidateName) {
        return candidateName;
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return singletonList(new HtmlResultRenderer()
                .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()));
    }
}

