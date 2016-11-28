package com.comandulli.lib.sqlite.contract;

import android.content.ContentValues;
import android.database.Cursor;

import com.comandulli.lib.sqlite.exception.IncompatibleContractException;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a contract for a java type.
 * <p>
 * This implementation must define how your target object
 * is constructed and how its attributes interact with columns.
 *
 * @param <T> Java type of this contract.
 * @author <a href="mailto:caioa.comandulli@gmail.com">Caio Comandulli</a>
 * @since 1.0
 */
public abstract class Contract<T> {
    /**
     * The table related to this contract.
     */
    private final String tableName;
    /**
     * All columns defined in this contract.
     */
    protected final List<Column<T, ?>> columns = new ArrayList<>();

    /**
     * Instantiates a contract.
     *
     * @param table name of the table for this contract.
     */
    public Contract(String table) {
        this.tableName = table;
    }

    /**
     * Constructor of this contracts target object.
     *
     * @return the object constructed.
     */
    public abstract T constructor();

    /**
     * Build the create table statement for this contract.
     *
     * @return create table in SQL language.
     */
    public String createTableStatement() {
        String statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
        for (int i = 0; i < columns.size(); i++) {
            Column<T, ?> column = columns.get(i);
            if (i != 0) {
                statement += ",";
            }
            statement += column.getStructure();
        }
        statement += ")";
        return statement;
    }

    /**
     * Build the drop table statement for this contract.
     *
     * @return drop table in SQL language.
     */
    public String dropTableStatement() {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    /**
     * This table's name.
     *
     * @return the name.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Generate a ContentValues containing the target object's
     * attributes to be used in a insert statement.
     *
     * @param obj target object.
     * @return the ContentValues mapping all attributes.
     */
    @SuppressWarnings("unchecked")
    public ContentValues getInsertContent(Object obj) {
        try {
            ContentValues values = new ContentValues();
            for (Column<T, ?> column : columns) {
                if (column.isAutoincrement()) {
                    continue;
                }
                String name = column.getName();
                Object value = column.fetchValue((T) obj);
                column.putIntoContentValues(values, name, value);
            }
            return values;
        } catch (ClassCastException e) {
            throw new IncompatibleContractException();
        }
    }

    /**
     * Generate a ContentValues containing the target object's
     * attributes to be used in a update statement.
     *
     * @param obj target object.
     * @return ContentValues mapping all attributes.
     */
    @SuppressWarnings("unchecked")
    public ContentValues getUpdateContent(Object obj) {
        try {
            ContentValues values = new ContentValues();
            for (Column<T, ?> column : columns) {
                String name = column.getName();
                Object value = column.fetchValue((T) obj);
                column.putIntoContentValues(values, name, value);
            }
            return values;
        } catch (ClassCastException e) {
            throw new IncompatibleContractException();
        }
    }

    /**
     * Builds an object of the type of this contract using a cursor.
     *
     * @param cursor the cursor
     * @return the built object.
     */
    public Object buildContractObject(Cursor cursor) {
        T object = constructor();
        for (Column<T, ?> column : columns) {
            column.fetchFromCursor(cursor, object);
        }
        return object;
    }

}
