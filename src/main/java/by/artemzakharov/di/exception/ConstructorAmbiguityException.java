package by.artemzakharov.di.exception;

import java.text.MessageFormat;

public class ConstructorAmbiguityException extends RuntimeException {

    public ConstructorAmbiguityException(String templateMessage, Object... params) {
        super(MessageFormat.format(templateMessage, params));
    }
}
