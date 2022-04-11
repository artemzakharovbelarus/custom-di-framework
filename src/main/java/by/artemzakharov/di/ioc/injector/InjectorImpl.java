package by.artemzakharov.di.injector;

import by.artemzakharov.di.exception.BindingNotFoundException;
import by.artemzakharov.di.exception.ConstructorAmbiguityException;
import by.artemzakharov.di.exception.NoSuitableConstructorException;
import by.artemzakharov.di.ioc.Binding;
import by.artemzakharov.di.ioc.provider.PrototypeProvider;
import by.artemzakharov.di.ioc.provider.Provider;
import by.artemzakharov.di.ioc.provider.SingletonProvider;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InjectorImpl implements Injector {

    private static final int INJECT_CONSTRUCTORS_RIGHT_AMOUNT = 1;
    private static final int INJECT_CONSTRUCTORS_INVALID_AMOUNT = 0;
    private static final int CONSTRUCTOR_INDEX = 0;
    private static final int EMPTY_CONSTRUCTOR_PARAMETERS_AMOUNT = 0;

    private Map<Class<?>, Binding<?>> bindingContext;
    private Map<Binding<?>, Provider<?>> providers;

    public InjectorImpl() {
        this.providers = Collections.synchronizedMap(new HashMap<>());
        this.bindingContext = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public <S> Optional<Provider<S>> getProvider(Class<S> type) {
        Binding<S> binding = (Binding<S>) bindingContext.get(type);

        if(binding == null) {
            return Optional.empty();
        }

        Provider<S> provider = (Provider<S>) providers.get(binding);

        if (provider == null) {
            provider = performProvider(binding);
            providers.put(binding, provider);
        }

        return Optional.of(provider);
    }

    @Override
    public <S> void bind(Class<S> base, Class<? extends S> impl) {
        processBinding(base, impl, false);
    }

    @Override
    public <S> void bindSingleton(Class<S> base, Class<? extends S> impl) {
        processBinding(base, impl, true);
    }

    private <S> void processBinding(Class<S> base, Class<? extends S> impl, boolean isSourceSingleton) {
        this.bindingContext.put(base, new Binding<S>(impl, isSourceSingleton));
    }

    private <S> Provider<S> performProvider(Binding<S> binding) {
        Constructor<S> constructor = maybePerformConstructorWithInject(binding).orElseGet(() -> performStandardConstructor(binding));

        if (constructor.getParameterCount() == EMPTY_CONSTRUCTOR_PARAMETERS_AMOUNT) {
            return performProviderImpl(binding, constructor);
        } else {
            Object[] parameters = performParametersForInjectConstructor(constructor);
            return performProviderImpl(binding, constructor, parameters);
        }
    }

    private <S> Provider<S> performProviderImpl(Binding<S> binding, Constructor<S> constructor, Object... parameters) {
        return binding.isSourceSingleton() ? new SingletonProvider<>(constructor, parameters) : new PrototypeProvider<>(constructor, parameters);
    }

    private <S> Object[] performParametersForInjectConstructor(Constructor<S> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                     .map(this::getProvider)
                     .map(this::performValidatedParameter)
                     .toArray();
    }

    private <S> S performValidatedParameter(Optional<Provider<S>> provider) {
        if (provider.isPresent()) {
            return provider.get().getInstance();
        } else {
            throw new BindingNotFoundException("Binding for class was not found in binding context");
        }
    }

    private <S> Optional<Constructor<S>> maybePerformConstructorWithInject(Binding<S> binding) {
        List<Constructor<S>> constructorsWithInject = performConstructorWithCriteria(binding, c -> c.isAnnotationPresent(Inject.class));

        switch (constructorsWithInject.size()) {
            case INJECT_CONSTRUCTORS_INVALID_AMOUNT:
                return Optional.empty();
            case INJECT_CONSTRUCTORS_RIGHT_AMOUNT:
                return Optional.of(constructorsWithInject.get(CONSTRUCTOR_INDEX));
            default:
                throw new ConstructorAmbiguityException("There are {0} constructors with @Inject in {1}",
                        constructorsWithInject.size(), binding.getSourceImpl());
        }
    }

    private <S> Constructor<S> performStandardConstructor(Binding<S> binding) {
        List<Constructor<S>> standardConstructors = performConstructorWithCriteria(binding, c -> !c.isAnnotationPresent(Inject.class) &&
                                                                                            c.getParameterCount() == 0);

        if (standardConstructors.size() == INJECT_CONSTRUCTORS_RIGHT_AMOUNT) {
            return standardConstructors.get(CONSTRUCTOR_INDEX);
        } else {
            throw new NoSuitableConstructorException("There is no default constructor in {0}", binding.getSourceImpl());
        }
    }

    private <S> List<Constructor<S>> performConstructorWithCriteria(Binding<S> binding, Predicate<? super Constructor<S>> criteria) {
        return Arrays.stream((Constructor<S>[]) binding.getSourceImpl().getDeclaredConstructors())
                     .filter(criteria)
                     .collect(Collectors.toList());
    }
}
