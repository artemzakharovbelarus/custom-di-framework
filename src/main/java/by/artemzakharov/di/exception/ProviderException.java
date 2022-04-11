package by.artemzakharov.di.exception;

import java.text.MessageFormat;

public class ProviderException extends RuntimeException {

    public ProviderException(String templateMessage, Object... params) {
        super(MessageFormat.format(templateMessage, params));
    }
}
