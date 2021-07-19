package com.lasertrac.ListViewMedia;

import java.util.concurrent.Executor;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.concurrent.Task;

public abstract class LazyLoadItem<T> {

    private T value ;
    private final T placeholder ;

    private final ReadOnlyBooleanWrapper loaded = new ReadOnlyBooleanWrapper();

    private boolean loadRequested ;

    private final Executor executor ;

    public LazyLoadItem(Executor executor, T placeholder) {
        this.executor = executor ;
        this.placeholder = placeholder ;
    }

    /**
     * Executed on background thread on first request to getValue().
     * @return The value.
     * @throws Exception
     */
    protected abstract T load() throws Exception ;

    /**
     * Requests to load the value in the background, and returns the value if loaded, or the placeholder otherwise.
     * Must be executed on the FX Application Thread.
     * @return The value, if loaded, otherwise the placeholder. If not loaded, spawns a task to load the value on a background thread.
     */
    public T getValue() {
        if (! loadRequested) {
            loadRequested = true ;
            Task<T> task = new Task<T>() {
                @Override
                protected T call() throws Exception {
                    return load();
                }
            };
            task.setOnSucceeded(e -> {
                value = task.getValue();
                loaded.set(true);
            });
            // do something more sensible here..
            task.setOnFailed(e -> task.getException().printStackTrace());
            executor.execute(task);
        }
        if (isLoaded()) {
            return value ;
        } else {
            return placeholder ;
        }
    }

    /**
     * Observable property indicating whether the value has been loaded.
     * Must be called from the FX Application Thread.
     * @return
     */
    public final ReadOnlyBooleanProperty loadedProperty() {
        return this.loaded.getReadOnlyProperty();
    }


    /**
     * Whether or not the value has been loaded. Must be called from the FX Application Thread.
     * @return
     */
    public final boolean isLoaded() {
        return this.loadedProperty().get();
    }


}
