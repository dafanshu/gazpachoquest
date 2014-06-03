package net.sf.gazpachoquest.questionnaires.components.question.type;

import net.sf.gazpachoquest.dto.answers.Answer;
import net.sf.gazpachoquest.dto.answers.TextAnswer;
import net.sf.gazpachoquest.questionnaires.components.question.AbstractQuestionComponent;
import net.sf.gazpachoquest.questionnaires.events.AnswerSavedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class ShortFreeTextQuestion extends AbstractQuestionComponent implements TextChangeListener {

    private static final long serialVersionUID = 4485291720274483361L;

    private static Logger logger = LoggerFactory.getLogger(ShortFreeTextQuestion.class);

    private TextField answerField;

    public ShortFreeTextQuestion() {
        super();
    }

    @Override
    public void init() {
        Label questionTitle = new Label(questionDTO.getLanguageSettings().getTitle());
        content.addComponent(questionTitle);

        answerField = new TextField();
        answerField.addTextChangeListener(this);
        if (questionDTO.getAnswer() != null) {
            answerField.setValue(((TextAnswer) questionDTO.getAnswer()).getValue());
        }
        answerField.setMaxLength(50);

        content.addComponent(answerField);
    }

    @Override
    public void textChange(TextChangeEvent event) {
        String text = event.getText();
        logger.debug("Text Change Event fired in ShortFreeTextQuestion with text = {}", text);
        String questionCode = questionDTO.getCode();
        Answer answer = TextAnswer.fromValue(text);
        answerSavedEvent.fire(AnswerSavedEvent.with().questionCode(questionCode).answer(answer).build());

    }
}
