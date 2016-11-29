package com.comandulli.lib.sqlite.contract;

/**
 * When passing this object to a select method in ContractDatabse
 * you define how the selection will be ordered.
 * <p>
 * If it will be Ascending or Descending according to the value in a column.
 * <p>
 * Defines a ORDER BY statement using java language.
 *
 * @author <a href="mailto:caioa.comandulli@gmail.com">Caio Comandulli</a>
 * @since 1.0
 */
public class Order {
    /**
     * The target column.
     */
    private final String column;
    /**
     * The order type.
     */
    private final OrderType type;

    /**
     * The type of ordering
     * if it is Ascending or Descending.
     */
    public enum OrderType {
        Ascending("ASC"), Descending("DESC");
        /**
         * Text representation in the SQL language.
         */
        private final String text;

        /**
         * Instantiate a type of ordering.
         *
         * @param text in SQL language.
         */
        Type(String text) {
            this.text = text;
        }

        /**
         * Translates this statement to SQL language.
         *
         * @return
         */
        @Override
        public String toString() {
            return " " + this.text;
        }
    }

    /**
     * Instantiates a Order by statement.
     *
     * @param column target column
     * @param type   order type
     */
    public Order(String column, OrderType type) {
        this.column = column;
        this.type = type;
    }

    /**
     * Translates this Order type into a SQL statement.
     *
     * @return
     */
    @Override
    public String toString() {
        return column + type;
    }
}
