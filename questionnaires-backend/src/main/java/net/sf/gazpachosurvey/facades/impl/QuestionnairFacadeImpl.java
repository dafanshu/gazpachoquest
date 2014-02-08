package net.sf.gazpachosurvey.facades.impl;

import java.util.Map;
import java.util.Set;

import net.sf.gazpachosurvey.domain.core.Question;
import net.sf.gazpachosurvey.domain.core.QuestionGroup;
import net.sf.gazpachosurvey.domain.core.Questionnair;
import net.sf.gazpachosurvey.domain.core.QuestionnairDefinition;
import net.sf.gazpachosurvey.domain.support.QuestionnairElement;
import net.sf.gazpachosurvey.dto.PageDTO;
import net.sf.gazpachosurvey.dto.QuestionDTO;
import net.sf.gazpachosurvey.dto.QuestionnairDTO;
import net.sf.gazpachosurvey.dto.QuestionnairDefinitionLanguageSettingsDTO;
import net.sf.gazpachosurvey.dto.answers.Answer;
import net.sf.gazpachosurvey.dto.answers.TextAnswer;
import net.sf.gazpachosurvey.facades.QuestionnairFacade;
import net.sf.gazpachosurvey.questionnair.resolver.QuestionnairElementResolver;
import net.sf.gazpachosurvey.questionnair.resolver.ResolverSelector;
import net.sf.gazpachosurvey.services.QuestionnairAnswersService;
import net.sf.gazpachosurvey.services.QuestionnairDefinitionService;
import net.sf.gazpachosurvey.services.QuestionnairService;
import net.sf.gazpachosurvey.types.BrowsingAction;
import net.sf.gazpachosurvey.types.Language;
import net.sf.gazpachosurvey.types.RenderingMode;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class QuestionnairFacadeImpl implements QuestionnairFacade {

    @Autowired
    private ResolverSelector resolverSelector;

    @Autowired
    private QuestionnairService questionnairService;

    @Autowired
    private QuestionnairDefinitionService questionnairDefinitionService;

    @Autowired
    private QuestionnairAnswersService questionnairAnswersService;

    @Autowired
    private Mapper mapper;

    public QuestionnairFacadeImpl() {
        super();
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionnairDTO findOne(Integer questionnairId) {
        QuestionnairDTO questionnairDTO = new QuestionnairDTO();

        Questionnair questionnair = questionnairService.findOne(questionnairId);
        QuestionnairDefinition definition = questionnair.getQuestionnairDefinition();
        Set<Language> translations = questionnairDefinitionService.translationsSupported(definition.getId());
        for (Language language : translations) {
            questionnairDTO.addSupportedLanguage(language);
        }
        questionnairDTO.addSupportedLanguage(definition.getLanguage());
        questionnairDTO.setLanguage(definition.getLanguage());
        QuestionnairDefinitionLanguageSettingsDTO languageSettings = mapper.map(definition.getLanguageSettings(),
                QuestionnairDefinitionLanguageSettingsDTO.class);
        questionnairDTO.setLanguageSettings(languageSettings);
        questionnairDTO.setId(questionnairId);
        return questionnairDTO;
    }

    @Transactional
    @Override
    public PageDTO resolvePage(Integer questionnairId, RenderingMode mode, BrowsingAction action) {
        Questionnair questionnair = questionnairService.findOne(questionnairId);
        QuestionnairElementResolver resolver = resolverSelector.selectBy(mode);
        QuestionnairElement questionnairElement = resolver.resolveFor(questionnair, action);
        PageDTO page = new PageDTO();

        if (questionnairElement instanceof QuestionGroup) {
            QuestionGroup questionGroup = (QuestionGroup) questionnairElement;

            for (Question question : questionGroup.getQuestions()) {
                QuestionDTO questionDTO = mapper.map(question, QuestionDTO.class);
                page.addQuestion(questionDTO);
            }
        } else {
            Question question = (Question) questionnairElement;
            QuestionDTO questionDTO = mapper.map(question, QuestionDTO.class);
            page.addQuestion(questionDTO);
        }

        Map<String, Object> answers = questionnairAnswersService.findByQuestionnair(questionnair);
        for (QuestionDTO question : page.getQuestions()) {
            populateAnswers(question, answers);
        }
        return page;
    }

    private void populateAnswers(QuestionDTO question, Map<String, Object> answers) {
        if (question.getType().hasSubquestions()) {
            for (QuestionDTO subquestion : question.getSubquestions()) {
                populateAnswers(subquestion, answers);
            }
        } else {
            Answer answer = null;
            if (question.getType().getAnswerType().isAssignableFrom(String.class)) {
                Object value = answers.get(question.getCode());
                answer = TextAnswer.fromValue((String) value);
            }
            question.setAnswer(answer);
        }

    }
}
