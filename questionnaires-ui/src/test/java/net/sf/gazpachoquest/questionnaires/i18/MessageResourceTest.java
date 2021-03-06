package net.sf.gazpachoquest.questionnaires.i18;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Locale;

import javax.inject.Inject;

import net.sf.gazpachoquest.questionnaires.i18.impl.MessageBundleImpl;
import net.sf.gazpachoquest.questionnaires.i18.impl.MessageResourceProducer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @see https://community.jboss.org/message/729492a
 * @see http://java.dzone.com/articles/injecting-string-resource
 * @author antoniomaria
 * 
 */
@RunWith(Arquillian.class)
public class MessageResourceTest {

    @Inject
    @MessageBundle
    private MessageResource messageResource;

    @Deployment
    public static Archive<?> createTestArchive() {
        String beansDescriptor = Descriptors.create(BeansDescriptor.class).exportAsString();

        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "myarchive.jar").addClasses(MessageBundle.class)
                .addClasses(MessageBundleImpl.class).addClasses(MessageResource.class)
                .addClasses(MessageResourceProducer.class)

                .addAsResource("resources/messages.properties")
                .addAsManifestResource(new StringAsset(beansDescriptor), "beans.xml");

        return archive;
    }

    @Test
    public void getStringTest() {
        Locale locale = Locale.ENGLISH;
        String resource = messageResource.getString(locale, "login.language.select.label");
        assertThat(resource).contains("Select your language");
        locale = new Locale("ES");
        resource = messageResource.getString(locale, "login.language.select.label");
        assertThat(resource).contains("Seleccione");

    }
}
