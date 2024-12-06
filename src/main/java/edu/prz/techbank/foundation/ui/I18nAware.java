package edu.prz.techbank.foundation.ui;

public interface I18nAware {

  String getTranslation(String key, Object... params);

  default String i18n(String key, Object... params) {
    return getTranslation(getClass().getName() + "." + key, params);
  }

  default String i18n(Class<?> clazz, String key, Object... params) {
    return getTranslation(clazz.getName() + "." + key, params);
  }

  default String i18ng(String key, Object... params) {
    return getTranslation(key, params);
  }

  default String i18n(Enum<?> item) {
    return i18n(item.getClass(), item.name());
  }
}
