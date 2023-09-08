package xyz.jpenilla.squaremap.addon.common;

import io.papermc.paper.text.PaperComponents;
import java.lang.reflect.Method;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

@DefaultQualifier(NonNull.class)
public final class Util {
    private Util() {
    }

    @SuppressWarnings("unchecked")
    public static <X extends Throwable> RuntimeException rethrow(final Throwable t) throws X {
        throw (X) t;
    }

    public static String asHtml(final Component component) {
        try {
            return SquaremapProvider.get().htmlComponentSerializer().serialize(component);
        } catch (final Throwable thr) {
            // fallback for pre-1.2
            try {
                final Class<?> s = Class.forName("xyz.jpenilla.squaremap.common.util.HtmlComponentSerializer");
                final Method make = s.getDeclaredMethod("withFlattener", ComponentFlattener.class);
                final Object serializer = make.invoke(null, PaperComponents.flattener());
                final Method serialize = s.getDeclaredMethod("serialize", ComponentLike.class);
                return (String) serialize.invoke(serializer, component);
            } catch (final ReflectiveOperationException ex) {
                final RuntimeException err = new RuntimeException("Failed to serialize component to HTML");
                err.addSuppressed(thr);
                err.addSuppressed(ex);
                throw err;
            }
        }
    }
}
