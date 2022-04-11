package by.artemzakharov.di.exception;

import java.text.MessageFormat;

public class BindingNotFoundException extends RuntimeException {

    public BindingNotFoundException(String templateMessage, Object... params) {
        super(MessageFormat.format(templateMessage, params));
    }
}
