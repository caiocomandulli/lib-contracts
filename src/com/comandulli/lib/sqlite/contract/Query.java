package com.comandulli.lib.sqlite.contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Starts a query for a select statement.
 */
public class Query {
    /**
     * List of all values referenced.
     */
    private final List<String> values = new ArrayList<>();
    /**
     * This query in SQL language.
     */
    private String query = "";

    /**
     * Set a column context for this query.
     *
     * @param columnName name of the column.
     * @return ColumnQuery to be used to further build the query.
     */
    public ColumnQuery column(String columnName) {
        query += columnName;
        return new ColumnQuery();
    }

    /**
     * A reference of this query within the context of a column.
     */
    public class ColumnQuery {
        /**
         * A reference of this query within the context of a column.
         */
        private ColumnQuery() {
        }

        /**
         * Adds a negative statement leading to a column context.
         *
         * @return the context
         */
        public ColumnQuery not() {
            query += " NOT";
            return this;
        }

        /**
         * Adds a equals to value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery equalsTo(Object value) {
            values.add(value.toString());
            query += " = ?";
            return new ValueQuery();
        }

        /**
         * Adds a not equals to value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery notEquals(Object value) {
            values.add(value.toString());
            query += " NOT ?";
            return new ValueQuery();
        }

        /**
         * Adds a equals or greater than value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery equalsOrGreaterThan(Object value) {
            values.add(value.toString());
            query += " >= ?";
            return new ValueQuery();
        }

        /**
         * Adds a equals or smaller than value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery equalsOrSmallerThan(Object value) {
            values.add(value.toString());
            query += " <= ?";
            return new ValueQuery();
        }

        /**
         * Adds a grater than value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery greaterThan(Object value) {
            values.add(value.toString());
            query += " > ?";
            return new ValueQuery();
        }

        /**
         * Adds a smaller than value statement leading to a value context.
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery smallerThan(Object value) {
            values.add(value.toString());
            query += " < ?";
            return new ValueQuery();
        }

        /**
         * Adds a values in statement leading to a value context.
         *
         * @param in values
         * @return the context
         */
        public ValueQuery in(Object[] in) {
            String inStatement = "(";
            for (int i = 0; i < in.length; i++) {
                if (i != 0) {
                    inStatement += ",";
                }
                inStatement += in[i].toString();
            }
            inStatement += ")";
            values.add(inStatement);
            query += " IN ?";
            return new ValueQuery();
        }

        /**
         * Adds a between min and max statement leading to a value context.
         *
         * @param min minimum value
         * @param max maximum value
         * @return the context
         */
        public ValueQuery between(Object min, Object max) {
            values.add(min.toString());
            values.add(max.toString());
            query += " BETWEEN ? AND ?";
            return new ValueQuery();
        }

        /**
         * Adds a like statement leading to a value context.
         *
         * @param likeStatement like statement
         * @return context
         */
        public ValueQuery like(String likeStatement) {
            query += " LIKE " + likeStatement;
            return new ValueQuery();
        }

        /**
         * Adds a glob statement leading to a value context
         *
         * @param globStatement glob statement
         * @return the context
         */
        public ValueQuery glob(String globStatement) {
            query += " GLOB " + globStatement;
            return new ValueQuery();
        }

        /**
         * Adds a column is null statement leading to a value context
         *
         * @return the context
         */
        public ValueQuery isNull() {
            query += " IS NULL";
            return new ValueQuery();
        }

        /**
         * Adds a column is not null statement leading to a value context
         *
         * @return the context
         */
        public ValueQuery isNotNull() {
            query += " IS NOT NULL";
            return new ValueQuery();
        }

        /**
         * Adds a value exists statement leading to a value context
         *
         * @param value the value
         * @return the context
         */
        public ValueQuery exists(Object value) {
            values.add(value.toString());
            query += " EXISTS (?)";
            return new ValueQuery();
        }
    }

    /**
     * A reference of this query within the context of a value.
     */
    public class ValueQuery {
        /**
         * A reference of this query within the context of a value.
         */
        private ValueQuery() {
        }

        /**
         * Adds a logic AND statement leading to a logic context.
         *
         * @return the context
         */
        public LogicQuery and() {
            query += " AND ";
            return new LogicQuery();
        }

        /**
         * Adds a logic OR statement leading to a logic context.
         *
         * @return
         */
        public LogicQuery or() {
            query += " OR ";
            return new LogicQuery();
        }

        /**
         * Ends this query.
         *
         * @return the final selection for this query.
         */
        public Selection end() {
            String[] array = new String[values.size()];
            return new Selection(query, values.toArray(array));
        }
    }

    /**
     * A reference of this query within the context of a logic operator.
     */
    public class LogicQuery {
        /**
         * A reference of this query within the context of a logic operator.
         */
        private LogicQuery() {
        }

        /**
         * Adds a column to this query initiating a column context.
         *
         * @param columnName the name of the column
         * @return the context
         */
        public ColumnQuery column(String columnName) {
            query += columnName;
            return new ColumnQuery();
        }
    }

    /**
     * Final representation of this query that will be used by the database.
     */
    public class Selection {
        /**
         * Query with no values assigned.
         */
        private final String queryString;
        /**
         * Values to be assigned in order.
         */
        private final String[] valuesArray;

        /**
         * Instantiates a selection with a SQL query and the values to be assigned.
         *
         * @param query
         * @param values
         */
        public Selection(String query, String[] values) {
            this.queryString = query;
            this.valuesArray = values;
        }

        /**
         * The query in SQL with no values assigned.
         *
         * @return the SQL value
         */
        public String getQuery() {
            return queryString;
        }

        /**
         * Values assigned to this query in order.
         *
         * @return the array of values
         */
        public String[] getValues() {
            return valuesArray;
        }

        /**
         * The SQL query with all values.
         *
         * @return the SQL query
         */
        @Override
        public String toString() {
            String stringRepresentation = queryString;
            for (String value : valuesArray) {
                int questionMark = stringRepresentation.indexOf('?');
                stringRepresentation = stringRepresentation.substring(0, questionMark) + value + stringRepresentation.substring(questionMark + 1);
            }
            return stringRepresentation;
        }
    }

}
