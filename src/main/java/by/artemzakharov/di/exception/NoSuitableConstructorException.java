package by.artemzakharov.di.exception;

import java.text.MessageFormat;

public class NoSuitableConstructorException extends RuntimeException {

    public NoSuitableConstructorException(String templateMessage, Object... params) {
        super(MessageFormat.format(templateMessage, params));
    }
}
