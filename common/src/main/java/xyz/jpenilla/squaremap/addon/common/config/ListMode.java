package xyz.jpenilla.squaremap.addon.common.config;

import java.util.Collection;

public enum ListMode {
    WHITELIST {
        @Override
        public <E> boolean allowed(final Collection<E> collection, final E e) {
            return collection.contains(e);
        }
    },
    BLACKLIST {
        @Override
        public <E> boolean allowed(final Collection<E> collection, final E e) {
            return !collection.contains(e);
        }
    };

    public abstract <E> boolean allowed(Collection<E> collection, E e);
}
