
package br.com.monitoramento.bruxismo;

import br.com.monitoramento.bruxismo.PacienteBoxActivityCursor.Factory;
import io.objectbox.EntityInfo;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;
import io.objectbox.internal.IdGetter;


// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * Properties for entity "PacienteBoxActivity". Can be used for QueryBuilder and for referencing DB names.
 */
public final class PacienteBoxActivity_ implements EntityInfo<PacienteBoxActivity> {

    // Leading underscores for static constants to avoid naming conflicts with property names

    public static final String __ENTITY_NAME = "PacienteBoxActivity";

    public static final int __ENTITY_ID = 1;

    public static final Class<PacienteBoxActivity> __ENTITY_CLASS = PacienteBoxActivity.class;

    public static final String __DB_NAME = "PacienteBoxActivity";

    public static final CursorFactory<PacienteBoxActivity> __CURSOR_FACTORY = new Factory();

    @Internal
    static final PacienteBoxActivityIdGetter __ID_GETTER = new PacienteBoxActivityIdGetter();

    public final static PacienteBoxActivity_ __INSTANCE = new PacienteBoxActivity_();

    public final static io.objectbox.Property<PacienteBoxActivity> id =
        new io.objectbox.Property<>(__INSTANCE, 0, 1, long.class, "id", true, "id");

    public final static io.objectbox.Property<PacienteBoxActivity> nome =
        new io.objectbox.Property<>(__INSTANCE, 1, 2, String.class, "nome");

    public final static io.objectbox.Property<PacienteBoxActivity> idade =
        new io.objectbox.Property<>(__INSTANCE, 2, 3, String.class, "idade");

    public final static io.objectbox.Property<PacienteBoxActivity> peso =
        new io.objectbox.Property<>(__INSTANCE, 3, 4, String.class, "peso");

    public final static io.objectbox.Property<PacienteBoxActivity> genero =
        new io.objectbox.Property<>(__INSTANCE, 4, 5, String.class, "genero");

    public final static io.objectbox.Property<PacienteBoxActivity> email =
        new io.objectbox.Property<>(__INSTANCE, 5, 6, String.class, "email");

    @SuppressWarnings("unchecked")
    public final static io.objectbox.Property<PacienteBoxActivity>[] __ALL_PROPERTIES = new io.objectbox.Property[]{
        id,
        nome,
        idade,
        peso,
        genero,
        email
    };

    public final static io.objectbox.Property<PacienteBoxActivity> __ID_PROPERTY = id;

    @Override
    public String getEntityName() {
        return __ENTITY_NAME;
    }

    @Override
    public int getEntityId() {
        return __ENTITY_ID;
    }

    @Override
    public Class<PacienteBoxActivity> getEntityClass() {
        return __ENTITY_CLASS;
    }

    @Override
    public String getDbName() {
        return __DB_NAME;
    }

    @Override
    public io.objectbox.Property<PacienteBoxActivity>[] getAllProperties() {
        return __ALL_PROPERTIES;
    }

    @Override
    public io.objectbox.Property<PacienteBoxActivity> getIdProperty() {
        return __ID_PROPERTY;
    }

    @Override
    public IdGetter<PacienteBoxActivity> getIdGetter() {
        return __ID_GETTER;
    }

    @Override
    public CursorFactory<PacienteBoxActivity> getCursorFactory() {
        return __CURSOR_FACTORY;
    }

    @Internal
    static final class PacienteBoxActivityIdGetter implements IdGetter<PacienteBoxActivity> {
        @Override
        public long getId(PacienteBoxActivity object) {
            return object.id;
        }
    }

}