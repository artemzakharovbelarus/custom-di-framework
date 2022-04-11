package by.artemzakharov.di.ioc.provider;

import by.artemzakharov.di.exception.ProviderException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SingletonProvider<S> extends BaseProvider<S> implements Provider<S> {

    private S source;

    public SingletonProvider(Constructor<S> constructor, Object... sourceParameters) {
        super(constructor, sourceParameters);
    }

    @Override
    public synchronized S getInstance() {
        if (source == null) {
            initSource();
        }
        return source;
    }

    private void initSource() {
        try {
            source = constructor.newInstance(sourceParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProviderException("Exception during creating instance of {0} via Reflection API", constructor.getDeclaringClass());
        }
    }
}
