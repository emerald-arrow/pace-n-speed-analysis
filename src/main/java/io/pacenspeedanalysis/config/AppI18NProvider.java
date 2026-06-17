package io.pacenspeedanalysis.config;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@SpringComponent
public class AppI18NProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "bundle";

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(new Locale("pl"), Locale.ENGLISH);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
        String value = bundle.getString(key);
        return params.length == 0 ? value : MessageFormat.format(value, params);
    }
}
