package by.artemzakharov.di.ioc.provider;

import java.lang.reflect.Constructor;

public abstract class BaseProvider<S> implements Provider<S> {

    protected final Constructor<S> constructor;
    protected final Object[] sourceParameters;

    protected BaseProvider(Constructor<S> constructor, Object... sourceParameters) {
        this.constructor = constructor;
        this.sourceParameters = sourceParameters;
    }
}
