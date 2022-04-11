package by.artemzakharov.di.ioc.provider;

import by.artemzakharov.di.exception.ProviderException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PrototypeProvider<S> extends BaseProvider<S> implements Provider<S> {

    public PrototypeProvider(Constructor<S> constructor, Object... sourceParameters) {
        super(constructor, sourceParameters);
    }

    @Override
    public S getInstance() {
        try {
            return constructor.newInstance(sourceParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ProviderException("Exception during creating instance of {0} via Reflection API", constructor.getDeclaringClass());
        }
    }
}
