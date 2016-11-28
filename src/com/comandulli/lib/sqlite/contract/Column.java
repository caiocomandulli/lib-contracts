package com.comandulli.lib.sqlite.contract;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * This class defines a Column inside a contract.
 * <p>
 * In its implementation you must define how this
 * column interacts with your object.
 *
 * @param <S> The original type of the contract.
 * @param <V> The java type of this columns data.
 * @author <a href="mailto:caioa.comandulli@gmail.com">Caio Comandulli</a>
 * @since 1.0
 */
public abstract class Column<S, V> {

    /**
     * The column name in the database.
     */
    private final String name;
    /**
     * The data type of the column.
     */
    private final DataType dataType;
    /**
     * If the column is nullable.
     */
    private boolean nullable = true;
    /**
     * If the column is the primary key.
     */
    private boolean primary;
    /**
     * If the column is autoincrement.
     */
    private boolean autoincrement;

    /**
     * The SQLite datatype of the column.
     * <p>
     * Available types:
     * INTEGER maps to a java Integer represented by "INTEGER"
     * STRING maps to a java String represented by "TEXT"
     * BOOLEAN maps to a java Boolean represented by "INTEGER"
     * DOUBLE maps to a java Double represented by "REAL"
     * FLOAT maps to a java Float represented by "REAL"
     * LONG maps to a java Long represented by "INTEGER"
     * SHORT maps to a java Short represented by "INTEGER"
     * <p>
     * Types in SQLite:
     * INTEGER. The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
     * REAL. The value is a floating point value, stored as an 8-byte IEEE floating point number.
     * TEXT. The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
     */
    public enum DataType {
        INTEGER(Integer.class, "INTEGER") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Integer instancedValue = (Integer) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getInt(cursor.getColumnIndex(name));
            }
        },
        STRING(String.class, "TEXT") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                String instancedValue = (String) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getString(cursor.getColumnIndex(name));
            }
        },
        BOOLEAN(Boolean.class, "INTEGER") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Boolean instancedValue = (Boolean) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                int data = cursor.getInt(cursor.getColumnIndex(name));
                return data != 0 && data != -1;
            }
        },
        DOUBLE(Double.class, "REAL") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Double instancedValue = (Double) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getDouble(cursor.getColumnIndex(name));
            }
        },
        FLOAT(Float.class, "REAL") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Float instancedValue = (Float) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getFloat(cursor.getColumnIndex(name));
            }
        },
        LONG(Long.class, "INTEGER") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Long instancedValue = (Long) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getLong(cursor.getColumnIndex(name));
            }
        },
        SHORT(Short.class, "INTEGER") {
            @Override
            public void putIntoContentValues(ContentValues contentValues, String name, Object value) {
                Short instancedValue = (Short) value;
                contentValues.put(name, instancedValue);
            }

            @Override
            public Object fetchFromCursor(Cursor cursor, String name) {
                return cursor.getShort(cursor.getColumnIndex(name));
            }
        };

        /**
         * Java type of this Data Type.
         */
        private final Class<?> typeClass;
        /**
         * SQL name of this Data Type.
         */
        private final String dataType;

        /**
         * Instantiates a Data Type.
         *
         * @param typeClass Java type of this Data Type.
         * @param type      SQL name of this Data Type.
         */
        DataType(Class<?> typeClass, String type) {
            this.typeClass = typeClass;
            this.dataType = type;
        }

        /**
         * Get the java type class.
         *
         * @return the type.
         */
        public Class<?> getTypeClass() {
            return typeClass;
        }

        /**
         * The SQL type.
         *
         * @return the type.
         */
        public String getDataType() {
            return dataType;
        }

        /**
         * Puts the column value into a ContentValues {@see android.content.ContentValues}
         *
         * @param contentValues ContentValues object
         * @param name          column name
         * @param value         assigned value
         */
        public abstract void putIntoContentValues(ContentValues contentValues, String name, Object value);

        /**
         * Method that fetches this column's value from a cursor.
         *
         * @param cursor target cursor.
         * @param name   column name
         * @return the value
         */
        public abstract Object fetchFromCursor(Cursor cursor, String name);
    }

    /**
     * Stub method for the acquirement of a value in this column's contract.
     *
     * @param obj target object.
     * @return value acquired.
     */
    public abstract V fetchValue(S obj);

    /**
     * Stub method for the insertion of a value in this column's contract.
     *
     * @param obj   target object.
     * @param value value to be inserted
     */
    public abstract void insertValue(S obj, V value);

    /**
     * Instantiate a column with a name and type and setting if it is a primary key and autoincrement.
     *
     * @param name          the name in database
     * @param dataType      {@see com.comandulli.lib.sqlite.contract.Column.DataType}
     * @param primary       if it is a primary key
     * @param autoincrement if it is autoincrement
     */
    public Column(String name, DataType dataType, boolean primary, boolean autoincrement) {
        this(name, dataType, false);
        this.primary = primary;
        this.autoincrement = autoincrement;
    }

    /**
     * Instantiate a column with a name and type and if it is nullable.
     *
     * @param name     the name in database
     * @param dataType {@see com.comandulli.lib.sqlite.contract.Column.DataType}
     * @param nullable if it is nullable
     */
    public Column(String name, DataType dataType, boolean nullable) {
        this(name, dataType);
        this.nullable = nullable;
    }

    /**
     * Instantiate a column with a name and type.
     *
     * @param name     the name in database
     * @param dataType {@see com.comandulli.lib.sqlite.contract.Column.DataType}
     */
    public Column(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * The column name in the database.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * This data type java class.
     *
     * @return the java class.
     */
    public Class<?> getValueClass() {
        return dataType.getTypeClass();
    }

    /**
     * If this column is accepts null values.
     *
     * @return if it does accept null values.
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * If this column is automatically incremented
     *
     * @return if it is automatically incremented.
     */
    public boolean isAutoincrement() {
        return autoincrement;
    }

    /**
     * If this column is a primary key.
     *
     * @return if it is a primary key.
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * @return this column structure in SQL format.
     */
    public String getStructure() {
        return name + " " + getDataType() + (primary ? " PRIMARY KEY" : "") + (autoincrement ? " AUTOINCREMENT" : "") + (nullable ? " NOT NULL" : "");
    }

    /**
     * The data type of this column {@see com.comandulli.lib.sqlite.contract.Column.DataType}
     *
     * @return the data type of this column.
     */
    public String getDataType() {
        return dataType.getDataType();
    }

    /**
     * Puts the column value into a ContentValues {@see android.content.ContentValues}
     *
     * @param values ContentValues object
     * @param name   column name
     * @param value  assigned value
     */
    public void putIntoContentValues(ContentValues values, String name, Object value) {
        dataType.putIntoContentValues(values, name, value);
    }

    /**
     * Method that fetches this column's value from a cursor and send it to the object.
     *
     * @param cursor target cursor
     * @param object target object
     */
    @SuppressWarnings("unchecked")
    public void fetchFromCursor(Cursor cursor, S object) {
        V value = (V) dataType.fetchFromCursor(cursor, name);
        insertValue(object, value);
    }

}
