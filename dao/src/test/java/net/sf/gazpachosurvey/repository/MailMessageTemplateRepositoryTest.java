package net.sf.gazpachosurvey.repository;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import net.sf.gazpachosurvey.domain.core.MailMessageTemplate;
import net.sf.gazpachosurvey.domain.core.Survey;
import net.sf.gazpachosurvey.repository.qbe.SearchParameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/jpa-test-context.xml", "classpath:/datasource-test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("MailMessageTemplateRepository-dataset.xml")
public class MailMessageTemplateRepositoryTest {

    @Autowired
    private MailMessageTemplateRepository mailMessageTemplateRepository;

    @Test
    public void findByExampleTest() {
        MailMessageTemplate example = new MailMessageTemplate();
        example.setSurvey(Survey.with().id(58).build());
        List<MailMessageTemplate> results = mailMessageTemplateRepository
                .findByExample(example, new SearchParameters());
        assertThat(results).contains(MailMessageTemplate.with().id(125).build());
    }

}