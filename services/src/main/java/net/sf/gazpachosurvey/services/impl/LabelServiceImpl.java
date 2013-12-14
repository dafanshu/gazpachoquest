package net.sf.gazpachosurvey.services.impl;

import net.sf.gazpachosurvey.domain.core.Label;
import net.sf.gazpachosurvey.domain.core.embeddables.LabelLanguageSettings;
import net.sf.gazpachosurvey.domain.i18.LabelTranslation;
import net.sf.gazpachosurvey.repository.LabelRepository;
import net.sf.gazpachosurvey.repository.i18.LabelTranslationRepository;
import net.sf.gazpachosurvey.services.LabelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class LabelServiceImpl extends
        AbstractLocalizedPersistenceService<Label, LabelTranslation, LabelLanguageSettings> implements LabelService {

    @Autowired
    public LabelServiceImpl(LabelRepository repository, LabelTranslationRepository translationRepository) {
        super(repository, translationRepository);
    }

    @Override
    public Label save(Label entity) {
        Assert.state(!entity.isNew(), "Label must be already persisted. Try by adding to labelSet first.");
        Label existing = repository.save(entity);
        existing.setLanguage(entity.getLanguage());
        existing.setLanguageSettings(entity.getLanguageSettings());
        return existing;
    }

}