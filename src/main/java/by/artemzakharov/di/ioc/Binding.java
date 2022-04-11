package by.artemzakharov.di.ioc;

public final class Binding<S> {

    private final Class<? extends S> sourceImpl;
    private final boolean isSourceSingleton;

    public Binding(Class<? extends S> sourceImpl, boolean isSourceSingleton) {
        this.sourceImpl = sourceImpl;
        this.isSourceSingleton = isSourceSingleton;

        validate();
    }

    public Class<? extends S> getSourceImpl() {
        return sourceImpl;
    }

    public boolean isSourceSingleton() {
        return isSourceSingleton;
    }

    private void validate() {
        if(sourceImpl == null) {
            throw new RuntimeException("Binding<> sourceImpl can not be null in {0}");
        }
    }
}
