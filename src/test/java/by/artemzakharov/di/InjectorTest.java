package by.artemzakharov.di;

import by.artemzakharov.di.data.dao.event.EventDAO;
import by.artemzakharov.di.data.dao.event.InMemoryEventDAO;
import by.artemzakharov.di.data.dao.profile.InMemoryProfileDAO;
import by.artemzakharov.di.data.dao.profile.ProfileDAO;
import by.artemzakharov.di.data.service.EventServiceImpl;
import by.artemzakharov.di.data.service.InjectAmbiguityService;
import by.artemzakharov.di.data.service.NoSuitableConstructorService;
import by.artemzakharov.di.exception.BindingNotFoundException;
import by.artemzakharov.di.exception.ConstructorAmbiguityException;
import by.artemzakharov.di.exception.NoSuitableConstructorException;
import by.artemzakharov.di.ioc.injector.Injector;
import by.artemzakharov.di.ioc.injector.InjectorImpl;
import by.artemzakharov.di.ioc.provider.Provider;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {

    @Test
    void testExistingBinding() {
        Injector injector = new InjectorImpl();
        injector.bind(EventDAO.class, InMemoryEventDAO.class);

        Optional<Provider<EventDAO>> daoProvider = injector.getProvider(EventDAO.class);

        assertTrue(daoProvider.isPresent());
        assertNotNull(daoProvider.get().getInstance());

        assertSame(InMemoryEventDAO.class, daoProvider.get().getInstance().getClass());
    }

    @Test
    void testNonExistingBinding() {
        Injector injector = new InjectorImpl();
        assertFalse(injector.getProvider(EventDAO.class).isPresent());
    }

    @Test
    void testPrototypeBinding() {
        Injector injector = new InjectorImpl();
        injector.bind(EventDAO.class, InMemoryEventDAO.class);

        Optional<Provider<EventDAO>> daoProvider = injector.getProvider(EventDAO.class);

        assertTrue(daoProvider.isPresent());
        assertNotNull(daoProvider.get().getInstance());
        assertNotSame(daoProvider.get().getInstance(), daoProvider.get().getInstance());
    }

    @Test
    void testSingletonBinding() {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAO.class);

        Optional<Provider<EventDAO>> daoProvider = injector.getProvider(EventDAO.class);

        assertTrue(daoProvider.isPresent());
        assertNotNull(daoProvider.get().getInstance());
        assertSame(daoProvider.get().getInstance(), daoProvider.get().getInstance());
    }

    @Test
    void testInjection() {
        Injector injector = new InjectorImpl();
        injector.bindSingleton(EventDAO.class, InMemoryEventDAO.class);
        injector.bindSingleton(EventServiceImpl.class, EventServiceImpl.class);

        Optional<Provider<EventDAO>> daoProvider = injector.getProvider(EventDAO.class);
        Optional<Provider<EventServiceImpl>> serviceProvider = injector.getProvider(EventServiceImpl.class);

        assertTrue(daoProvider.isPresent());
        assertTrue(serviceProvider.isPresent());
        assertNotNull(daoProvider.get().getInstance());
        assertNotNull(serviceProvider.get().getInstance());

        EventServiceImpl service = serviceProvider.get().getInstance();

        EventDAO expectedDao = daoProvider.get().getInstance();

        try {
            Field daoField = EventServiceImpl.class.getDeclaredField("dao");
            daoField.setAccessible(true);
            EventDAO injectedDao = (EventDAO) daoField.get(service);

            assertSame(expectedDao, injectedDao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorAmbiguityException() {
        assertThrows(ConstructorAmbiguityException.class, () -> {
            Injector injector = new InjectorImpl();
            injector.bind(EventDAO.class, InMemoryEventDAO.class);
            injector.bind(ProfileDAO.class, InMemoryProfileDAO.class);
            injector.bind(InjectAmbiguityService.class, InjectAmbiguityService.class);

            Optional<Provider<InjectAmbiguityService>> serviceProvider = injector.getProvider(InjectAmbiguityService.class);

            // In case of correct implementation the following statement is unreachable
            assertTrue(serviceProvider.isPresent());
            assertNotNull(serviceProvider.get().getInstance());
        });
    }

    @Test
    void testNoSuitableConstructorException() {
        assertThrows(NoSuitableConstructorException.class, () -> {
            Injector injector = new InjectorImpl();
            injector.bind(NoSuitableConstructorService.class, NoSuitableConstructorService.class);

            Optional<Provider<NoSuitableConstructorService>> serviceProvider = injector.getProvider(NoSuitableConstructorService.class);

            // In case of a correct implementation the following statement is unreachable
            assertTrue(serviceProvider.isPresent());
            assertNotNull(serviceProvider.get().getInstance());
        });
    }

    @Test
    void testBindingNotFoundException() {
        assertThrows(BindingNotFoundException.class, () -> {
            Injector injector = new InjectorImpl();
            injector.bind(EventServiceImpl.class, EventServiceImpl.class);

            Optional<Provider<EventServiceImpl>> serviceProvider = injector.getProvider(EventServiceImpl.class);

            // In case of a correct implementation the following statement is unreachable
            assertTrue(serviceProvider.isPresent());
            assertNotNull(serviceProvider.get().getInstance());
        });
    }
}
