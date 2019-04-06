package com.danielthedev.config;

import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

public abstract class Config extends ConfigFile {

    private final Class<?> validator_class;

    public Config(Plugin plugin, String name, boolean fromJar, Class<?> validator_class) {
        super(plugin, name, fromJar);
        this.validator_class = validator_class;
    }

    public Config loadConfiguration() throws ConfigParseException {
        super.load();
        String variable_name;
        Object object;
        try {
            for (Field field : this.validator_class.getDeclaredFields()) {
                if(this.isMarked(field)) {
                    if(this.getMark(field).type() == ConfigValueType.IGNORE) continue;
                }
                variable_name = this.ToConfigField(field.getName());

                object = this.getConfig().get(variable_name);
                if(object != null) {
                    if(this.isMarked(field)) {

                        if(this.getMark(field).type() == ConfigValueType.VALIDATE) {
                            try {
                                try {
                                    Method method;
                                    try {
                                        method = this.validator_class.getDeclaredMethod(field.getName(), this.getMark(field).parameter());
                                    } catch (NoSuchMethodException e) {
                                        method = this.validator_class.getDeclaredMethod(field.getName(), toPrimitive(this.getMark(field).parameter()));
                                    }
                                    method.setAccessible(true);
                                    if(!Modifier.isStatic(method.getModifiers())) {
                                        throw new IllegalAccessException("method " + method.toGenericString() + " must be static");
                                    }
                                    ConfigValueValidatorResult result = (ConfigValueValidatorResult) method.invoke(null, object);

                                    if (!result.isSuccces()) {
                                        throw new ConfigParseException(String.format("could not load variable '%s', the validation is false (%s)", this.ToConfigField(field.getName()), result.getError_message()));
                                    }
                                } catch (ClassCastException e) {
                                    throw new ConfigParseException(String.format("could not load variable '%s', method %s(%s) must return %s", this.ToConfigField(field.getName()), field.getName(), this.getMark(field).parameter().getName(), ConfigValueValidatorResult.class.getName()));
                                }
                            } catch (InvocationTargetException e) {
                                throw new ConfigParseException(String.format("could not load variable '%s', error while invoking method 'static %s %s(%s)' in %s.class", this.ToConfigField(field.getName()), ConfigValueValidatorResult.class.getName(), field.getName(), this.getMark(field).parameter().getName(), this.validator_class.getSimpleName()));
                            } catch (NoSuchMethodException e) {
                                throw new ConfigParseException(String.format("could not load variable '%s', method 'static %s %s(%s)' in %s.class cannot be found", this.ToConfigField(field.getName()), ConfigValueValidatorResult.class.getName(), field.getName(), this.getMark(field).parameter().getName(), this.validator_class.getSimpleName()));
                            }
                        }
                    }
                    if (field.getType().isEnum()) {
                        try {
                            field.set(null, Enum.valueOf((Class<Enum>) field.getType(), Objects.toString(object).toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            throw new ConfigParseException(String.format("could not load variable '%s', Enum value not found (%s)", this.ToConfigField(field.getName()), Objects.toString(object).toUpperCase()));
                        }
                    } else {

                        if (object.getClass() == field.getType() || toWrapper(field.getType()) == object.getClass()) {
                            field.set(null, object);
                        } else {
                            throw new ConfigParseException(String.format("could not load variable '%s', type cast (%s) -> (%s)", this.ToConfigField(field.getName()), object.getClass().getSimpleName(), field.getType().getSimpleName()));
                        }
                    }
                } else {
                    throw new ConfigParseException(String.format("could not load variable '%s', the field '%s' (%s) is missing in %s", this.ToConfigField(field.getName()), variable_name, field.getType().getSimpleName(), super.getFileName()));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    private Class<?> toPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive())
            return clazz;

        if (clazz == Integer.class)
            return Integer.TYPE;
        if (clazz == Long.class)
            return Long.TYPE;
        if (clazz == Boolean.class)
            return Boolean.TYPE;
        if (clazz == Byte.class)
            return Byte.TYPE;
        if (clazz == Character.class)
            return Character.TYPE;
        if (clazz == Float.class)
            return Float.TYPE;
        if (clazz == Double.class)
            return Double.TYPE;
        if (clazz == Short.class)
            return Short.TYPE;
        if (clazz == Void.class)
            return Void.TYPE;

        return clazz;
    }

    private Class<?> toWrapper(Class<?> clazz) {
        if (!clazz.isPrimitive())
            return clazz;

        if (clazz == Integer.TYPE)
            return Integer.class;
        if (clazz == Long.TYPE)
            return Long.class;
        if (clazz == Boolean.TYPE)
            return Boolean.class;
        if (clazz == Byte.TYPE)
            return Byte.class;
        if (clazz == Character.TYPE)
            return Character.class;
        if (clazz == Float.TYPE)
            return Float.class;
        if (clazz == Double.TYPE)
            return Double.class;
        if (clazz == Short.TYPE)
            return Short.class;
        if (clazz == Void.TYPE)
            return Void.class;

        return clazz;
    }

    private ConfigValueIndicator getMark(Field field) {
        return field.getAnnotation(ConfigValueIndicator.class);
    }

    private boolean isMarked(Field field) {
        return field.isAnnotationPresent(ConfigValueIndicator.class);
    }

    private String ToConfigField(String field) {
        return field.replace("_", "-").replace("$", ".");
    }

    private String toClassField(String field) {
        return field.replace("-", "_").replace(".", "$");
    }
}
