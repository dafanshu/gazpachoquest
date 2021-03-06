package net.sf.gazpachoquest.questionnaires.components.question;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.resource.NotSupportedException;

import net.sf.gazpachoquest.api.QuestionnaireResource;
import net.sf.gazpachoquest.dto.QuestionDTO;
import net.sf.gazpachoquest.questionnaires.components.question.type.CheckboxListQuestion;
import net.sf.gazpachoquest.questionnaires.components.question.type.ListRadioQuestion;
import net.sf.gazpachoquest.questionnaires.components.question.type.NumericQuestion;
import net.sf.gazpachoquest.questionnaires.components.question.type.ShortFreeTextQuestion;
import net.sf.gazpachoquest.questionnaires.resource.GazpachoResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionFactory implements Serializable {

    private static final long serialVersionUID = -6134111942730296445L;

    private static final Logger logger = LoggerFactory.getLogger(QuestionFactory.class);

    @Inject
    private Instance<AbstractQuestionComponent> question;

    @GazpachoResource
    @Inject
    private QuestionnaireResource questionnaireResource;

    @PostConstruct
    public void init() {
        logger.debug("Question Factory instance created");
    }

    public QuestionComponent build(Integer questionnaireId, QuestionDTO questionDTO) throws NotSupportedException {
        AbstractQuestionComponent questionComponent = null;
        switch (questionDTO.getType()) {
        case S:
            questionComponent = question.select(ShortFreeTextQuestion.class).get();
            break;
        case F:
            throw new NotSupportedException("Question Type " + questionDTO.getType() + " not supported");
        case L:
            questionComponent = question.select(ListRadioQuestion.class).get();
            break;
        case M:
            questionComponent = question.select(CheckboxListQuestion.class).get();
            break;
        case N:
            questionComponent = question.select(NumericQuestion.class).get();
            break;
        case T:
            throw new NotSupportedException("Question Type " + questionDTO.getType() + " not supported");
        default:
            throw new NotSupportedException("Question Type " + questionDTO.getType() + " not supported");
        }
        questionComponent.setQuestionDTO(questionDTO);
        questionComponent.init();
        return questionComponent;
    }

}
