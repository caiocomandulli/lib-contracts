package com.comandulli.lib.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.comandulli.lib.sqlite.contract.Contract;
import com.comandulli.lib.sqlite.contract.Order;
import com.comandulli.lib.sqlite.contract.Query.Selection;
import com.comandulli.lib.sqlite.exception.DatabaseErrorException;
import com.comandulli.lib.sqlite.exception.NoContractException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

/**
 * Contracts is a contract based SQLite library for Android.
 *
 * When establishing your database, set all available contracts that
 * it will handle, each contract {@see com.comandulli.lib.sqlite.contract.Contract} has the specific behaviour of how
 * to handle a type of object, how it is selected from the database and
 * how it is inserted.
 *
 * With all contracts set you only need to insert, select, update or delete
 * by passing the target object or type.
 *
 * Selections are handled by the Selection {@see com.comandulli.lib.sqlite.contract.Selection} type,
 * it builds a SELECT statement using java language.
 *
 * Ordering is handled by the Order {@see com.comandulli.lib.sqlite.contract.Order} type,
 * it build a ORDER BY statement using java language.
 *
 * @author <a href="mailto:caioa.comandulli@gmail.com">Caio Comandulli</a>
 * @since 1.0
 */
public class ContractDatabase extends SQLiteOpenHelper {

    /**
     * Database version.
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * Database name.
     */
    public static final String DATABASE_NAME = "contract.db";
    /**
     * Mapping of all available contracts to its type.
     */
    private final Hashtable<Class<?>, Contract<?>> availableContracts = new Hashtable<>();

    /**
     * Instantiate a contract database.
     *
     * @param context the android context
     */
    public ContractDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Add a contract to the available contracts.
     *
     * @param type     contract type.
     * @param contract contract instance.
     */
    public void addContract(Class<?> type, Contract<?> contract) {
        availableContracts.put(type, contract);
    }

    /**
     * onCreate implementation from SQLiteOpenHelper {@see android.database.sqlite.SQLiteOpenHelper}
     *
     * @param db {@see android.database.sqlite.SQLiteDatabase}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Collection<Contract<?>> contracts = availableContracts.values();
        for (Contract<?> contract : contracts) {
            db.execSQL(contract.createTableStatement());
        }
    }

    /**
     * onUpgrade implementation from SQLiteOpenHelper {@see android.database.sqlite.SQLiteOpenHelper}
     *
     * @param db         {@see android.database.sqlite.SQLiteDatabase}
     * @param oldVersion {@see android.database.sqlite.SQLiteOpenHelper}
     * @param newVersion {@see android.database.sqlite.SQLiteOpenHelper}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Collection<Contract<?>> contracts = availableContracts.values();
        String sql = "";
        for (Contract<?> contract : contracts) {
            sql += contract.dropTableStatement() + ";";
        }
        db.execSQL(sql);
        onCreate(db);
    }

    /**
     * Insert an object in this database.
     *
     * @param object object to be inserted.
     * @return the assigned id in the database.
     * @throws NoContractException    {@see com.comandulli.lib.sqlite.exception.NoContractException} when the object has no contract assigned to its type.
     * @throws DatabaseErrorException {@see com.comandulli.lib.sqlite.exception.DatabaseErrorException} when the insert generated a SQLite error.
     * @throws
     */
    public long insert(Object object) {
        Contract<?> contract = availableContracts.get(object.getClass());
        if (contract == null) {
            throw new NoContractException();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = db.insert(contract.getTableName(), null, contract.getInsertContent(object));
        if (rowId == -1) {
            throw new DatabaseErrorException();
        }
        return rowId;
    }

    /**
     * Select all objects of type.
     *
     * @param type type to be selected.
     * @return objects found.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public List<?> select(Class<?> type) {
        return this.select(type, null, null, null);
    }

    /**
     * Select all objects of type that match the selection.
     *
     * @param type      type to be selected.
     * @param selection the selection to be applied.
     * @return objects found.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public List<?> select(Class<?> type, Selection selection) {
        return this.select(type, selection, null, null);
    }

    /**
     * Select all objects of type that match the selection, ordered.
     *
     * @param type      type to be selected.
     * @param selection the selection to be applied.
     * @param order     ordering to be applied.
     * @return objects found.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public List<?> select(Class<?> type, Selection selection, Order order) {
        return this.select(type, selection, order, null);
    }

    /**
     * Select all objects of type that match the selection, ordered and limited.
     *
     * @param type      type to be selected.
     * @param selection the selection to be applied.
     * @param order     ordering to be applied
     * @param limit     limit of entries to be returned.
     * @return objects found.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public List<?> select(Class<?> type, Selection selection, Order order, Integer limit) {
        Contract<?> contract = availableContracts.get(type);
        if (contract == null) {
            throw new NoContractException();
        }

        String selectionColumns = null;
        String[] selectionValues = null;
        String orderBy = null;
        String limitBy = null;
        if (selection != null) {
            selectionColumns = selection.getQuery();
            selectionValues = selection.getValues();
        }
        if (order != null) {
            orderBy = order.toString();
        }
        if (limit != null) {
            limitBy = limit.toString();
        }

        SQLiteDatabase db = this.getReadableDatabase();
        List<Object> results = new ArrayList<>();
        Cursor cursor = db.query(contract.getTableName(), null, selectionColumns, selectionValues, null, null, orderBy, limitBy);
        if (cursor.moveToFirst()) {
            do {
                Object row = contract.buildContractObject(cursor);
                results.add(row);
            } while (cursor.moveToNext());
        }
        return results;
    }

    /**
     * Updates entires in the database.
     *
     * @param value     value to be assigned.
     * @param selection selection to be applied.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the object has no contract assigned to its type.
     */
    public void update(Object value, Selection selection) {
        Contract<?> contract = availableContracts.get(value.getClass());
        if (contract == null) {
            throw new NoContractException();
        }
        String selectionColumns = null;
        String[] selectionValues = null;
        if (selection != null) {
            selectionColumns = selection.getQuery();
            selectionValues = selection.getValues();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(contract.getTableName(), contract.getUpdateContent(value), selectionColumns, selectionValues);
    }

    /**
     * Deletes entries in the database.
     *
     * @param type      type of the entries to be deleted.
     * @param selection selection to be applied.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public void delete(Class<?> type, Selection selection) {
        Contract<?> contract = availableContracts.get(type);
        if (contract == null) {
            throw new NoContractException();
        }
        String selectionColumns = null;
        String[] selectionValues = null;
        if (selection != null) {
            selectionColumns = selection.getQuery();
            selectionValues = selection.getValues();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(contract.getTableName(), selectionColumns, selectionValues);
    }

    /**
     * Clear completely a table in the database.
     *
     * @param type type of the table.
     * @throws NoContractException {@see com.comandulli.lib.sqlite.exception.NoContractException} when the type has no contract assigned.
     */
    public void clearTable(Class<?> type) {
        this.delete(type, null);
    }

    /**
     * Returns the available contracts map.
     *
     * @return the available contracts.
     */
    public Hashtable<Class<?>, Contract<?>> getAvailableContracts() {
        return availableContracts;
    }

    /**
     * Executes a raw SQL.
     *
     * @param sql the SQL query.
     */
    public void executeSQL(String sql) {
        this.getWritableDatabase().execSQL(sql);
    }

}
