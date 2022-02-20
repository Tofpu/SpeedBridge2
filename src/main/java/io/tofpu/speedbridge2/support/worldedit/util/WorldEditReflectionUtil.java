package io.tofpu.speedbridge2.support.worldedit.util;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.Region;
import io.tofpu.speedbridge2.support.worldedit.Vector;

import java.lang.reflect.InvocationTargetException;

public final class WorldEditReflectionUtil {
    public static Vector legacyToWrapper(final Object object) {
        final Class<?> objectClass = object.getClass();
        try {
            final double x = (double) objectClass.getDeclaredMethod("getX").invoke(object);
            final double y = (double) objectClass.getDeclaredMethod("getY").invoke(object);
            final double z = (double) objectClass.getDeclaredMethod("getZ").invoke(object);
            return new Vector(x, y, z);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Vector legacyToWrapperInt(final Object object) {
        final Class<?> objectClass = object.getClass();
        try {
            final Object xObj = objectClass.getDeclaredMethod("getX").invoke(object);

            final double x, y, z;
            if (xObj instanceof Integer) {
                x = (int) xObj;
                y = (int) objectClass.getDeclaredMethod("getY").invoke(object);
                z = (int) objectClass.getDeclaredMethod("getZ").invoke(object);
            } else {
                x = (double) xObj;
                y = (double) objectClass.getDeclaredMethod("getY").invoke(object);
                z = (double) objectClass.getDeclaredMethod("getZ").invoke(object);
            }
            return new Vector(x, y, z);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Vector getMaximumPointFromRegion(final Object object) {
        final Class<?> objectClass = object.getClass();
        if (!(object instanceof Region)) {
            throw new IllegalStateException("Object is not Region!");
        }
        try {
            return legacyToWrapperInt(objectClass.getDeclaredMethod("getMaximumPoint").invoke(object));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Vector getMinimumPointFromRegion(final Object object) {
        final Class<?> objectClass = object.getClass();
        if (!(object instanceof Region)) {
            throw new IllegalStateException("Object is not Region!");
        }
        try {
            return legacyToWrapperInt(objectClass.getDeclaredMethod("getMinimumPoint").invoke(object));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Vector getOriginFromRegion(final Object object) {
        final Class<?> objectClass = object.getClass();
        if (!(object instanceof Clipboard)) {
            throw new IllegalStateException("Object is not Clipboard!");
        }
        try {
            return legacyToWrapperInt(objectClass.getDeclaredMethod("getOrigin").invoke(object));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
