package by.artemzakharov.di.injector;

import by.artemzakharov.di.ioc.provider.Provider;

import java.util.Optional;

public interface Injector {
    <T> Optional<Provider<T>> getProvider(Class<T> type);

    <T> void bind(Class<T> base, Class<? extends T> impl);

    <T> void bindSingleton(Class<T> base, Class<? extends T> impl);
}
