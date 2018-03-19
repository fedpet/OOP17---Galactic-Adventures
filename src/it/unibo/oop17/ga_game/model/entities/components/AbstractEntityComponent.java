package it.unibo.oop17.ga_game.model.entities.components;

import java.util.Optional;

import it.unibo.oop17.ga_game.model.entities.Entity;
import it.unibo.oop17.ga_game.model.entities.EventfullEntity;
import it.unibo.oop17.ga_game.model.entities.events.EntityEvent;
import it.unibo.oop17.ga_game.model.entities.events.EntityEventListener;

public abstract class AbstractEntityComponent implements EntityComponent, EntityEventListener {
    private Optional<EventfullEntity> owner = Optional.empty();

    @Override
    public final Optional<? extends Entity> getOwner() {
        return owner;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void attach(final EventfullEntity owner) throws IllegalStateException {
        this.owner.ifPresent(e -> {
            throw new IllegalStateException("Component already attached to an entity");
        });
        this.owner = Optional.of(owner);
        owner.register(this);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void detach() throws IllegalStateException {
        owner.ifPresent(entity -> {
            entity.unregister(this);
        });
        owner = Optional.empty();
    }

    @Override
    public void update(final double dt) {
        //
    }

    /**
     * Convenience method to avoid the optional.
     * 
     * @return The @EventfullEntity
     * 
     * @throws IllegalStateException
     *             is the component is not attached to an Entity
     */
    protected final EventfullEntity getEntity() {
        return owner.orElseThrow(IllegalStateException::new);
    }

    /**
     * Generates an @EntityEvent.
     * 
     * @param event
     *            The event
     */
    protected void post(final EntityEvent event) {
        getEntity().post(event);
    }

    protected final void selfDetach() {
        getEntity().remove(this);
    }
}
