package net.sf.gazpachoquest.repository;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import net.sf.gazpachoquest.domain.core.QuestionGroup;
import net.sf.gazpachoquest.repository.QuestionGroupRepository;
import net.sf.gazpachoquest.test.dbunit.support.ColumnDetectorXmlDataSetLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/jpa-test-context.xml",
		"classpath:/datasource-test-context.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("QuestionGroupRepository-dataset.xml")
@DatabaseTearDown("QuestionGroupRepository-dataset.xml")
@DbUnitConfiguration(dataSetLoader = ColumnDetectorXmlDataSetLoader.class)
public class QuestionGroupRepositoryTest {

	@Autowired
	private QuestionGroupRepository questionGroupRepository;

	@Test
	public void findOneByPositionInQuestionnairTest() {
		Integer position = 2;
		Integer questionnairDefinitionId = 7;
		QuestionGroup questionGroup = questionGroupRepository
				.findOneByPositionInQuestionnairDefinition(
						questionnairDefinitionId, position);
		assertThat(questionGroup)
				.isEqualTo(QuestionGroup.with().id(11).build());
	}

	@Test
	public void findPositionInQuestionnairDefinitionTest() {
		int questionGroupId = 10;
		Integer pos = questionGroupRepository
				.findPositionInQuestionnairDefinition(questionGroupId);
		assertThat(pos).isEqualTo(1);
	}

	@Test
	public void findByQuestionnairDefinitionIdTest() {
		int questionnairDefinitionId = 7;
		List<QuestionGroup> questiongroups = questionGroupRepository
				.findByQuestionnairDefinitionId(questionnairDefinitionId);
		assertThat(questiongroups).hasSize(3);

	}

}
