package xyz.jpenilla.squaremap.addon.worldguard.config;

import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;
import xyz.jpenilla.squaremap.addon.common.Util;
import xyz.jpenilla.squaremap.addon.common.config.ColorSerializer;
import xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard;

@ConfigSerializable
public class StyleSettings {
    @ConfigSerializable
    public static class Stroke {
        public Color color = null;
        public Integer weight = null;
        public Double opacity = null;
    }

    @ConfigSerializable
    public static class Fill {
        public Color color = null;
        public Double opacity = null;
    }

    public Stroke stroke = null;
    public Fill fill = null;
    public String clickTooltip = null;

    public void validate() {
        Objects.requireNonNull(this.stroke, "stroke is required for the default style");
        Objects.requireNonNull(this.fill, "fill is required for the default style");
        Objects.requireNonNull(this.stroke.color, "stroke.color is required for the default style");
        Objects.requireNonNull(this.stroke.opacity, "stroke.opacity is required for the default style");
        Objects.requireNonNull(this.stroke.weight, "stroke.weight is required for the default style");
        Objects.requireNonNull(this.fill.color, "fill.color is required for the default style");
        Objects.requireNonNull(this.fill.opacity, "fill.opacity is required for the default style");
    }

    public StyleSettings defaulted(final StyleSettings fallback) {
        final StyleSettings settings = new StyleSettings();
        if (this.stroke != null) {
            final Stroke s = new Stroke();
            s.opacity = this.stroke.opacity != null ? this.stroke.opacity : fallback.stroke.opacity;
            s.color = this.stroke.color != null ? this.stroke.color : fallback.stroke.color;
            s.weight = this.stroke.weight != null ? this.stroke.weight : fallback.stroke.weight;
            settings.stroke = s;
        } else {
            settings.stroke = fallback.stroke;
        }
        if (this.fill != null) {
            final Fill f = new Fill();
            f.opacity = this.fill.opacity != null ? this.fill.opacity : fallback.fill.opacity;
            f.color = this.fill.color != null ? this.fill.color : fallback.fill.color;
            settings.fill = f;
        } else {
            settings.fill = fallback.fill;
        }
        settings.clickTooltip = this.clickTooltip != null ? this.clickTooltip : fallback.clickTooltip;
        return settings;
    }

    public static StyleSettings defaultSettings() {
        final StyleSettings settings = new StyleSettings();
        final Stroke s = new Stroke();
        s.color = Color.GREEN;
        s.weight = 1;
        s.opacity = 1.0D;
        final Fill f = new Fill();
        f.color = Color.GREEN;
        f.opacity = 0.2D;
        settings.stroke = s;
        settings.fill = f;
        settings.clickTooltip = "<span style=\"font-size:120%;\">{regionname}</span><br />" +
            "Owner <span style=\"font-weight:bold;\">{playerowners}</span><br />" +
            "Flags<br /><span style=\"font-weight:bold;\">{flags}</span>";
        return settings;
    }

    public static StyleSettings fromFlags(final SquaremapWorldGuard plugin, final ProtectedRegion region) {
        try {
            final StyleSettings settings = new StyleSettings();
            final Stroke s = new Stroke();
            s.color = color(region, plugin.strokeColorFlag);
            s.weight = region.getFlag(plugin.strokeWeightFlag);
            s.opacity = region.getFlag(plugin.strokeOpacityFlag);
            final Fill f = new Fill();
            f.color = color(region, plugin.fillColorFlag);
            f.opacity = region.getFlag(plugin.fillOpacityFlag);
            settings.stroke = s;
            settings.fill = f;
            settings.clickTooltip = region.getFlag(plugin.clickTooltipFlag);
            return settings;
        } catch (final Exception ex) {
            throw Util.rethrow(ex);
        }
    }

    private static @Nullable Color color(final ProtectedRegion region, final StringFlag flag) throws SerializationException {
        final @Nullable String data = region.getFlag(flag);
        if (data == null) {
            return null;
        }
        //noinspection DataFlowIssue
        return ColorSerializer.INSTANCE.deserialize((Type) null, data);
    }
}
