# Contracts - SQLite Library for Android

Contracts is a library that uses a contract based approach to make
SQLite database interactions more simple.

Each type that will be persisted has a defined contract,
the contract describes how this type of object is created,
updated and queried from the database.

## Usage

The first step is to instantiate a new Contract Database.

```java
ContractDatabase contractDatabase = new ContractDatabase(this);
```

Right now your database is just a blank slate and knows no contract or how to handle any type of object.

### Defining your contract

A `Contract` defines a representation of a type within the database,
and the interactions that the object has with it, such as its constructor,
setting and getting attributes.

As for our example we will define a simple `Object`:

```java
public class NamedExample {
    private int id;
    private String name;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;    
    }
    
    public String getName() {
        return name;
    }
}
```

The getters and setters will be how the contract can define an object's attributes.
We extend the `Contract` type and pass our `NamedExample` as its generic type.

```java
public class NamedExampleContract extends Contract<NamedExample> {
    public NamedExampleContract() {
		super("MyTable");
		columns.add(new Column<NamedExample, Integer>("ColumnId", DataType.INTEGER, true, false) {
			@Override
			public Integer fetchValue(NamedExample obj) {
				return obj.getId();
			}

			@Override
			public void insertValue(NamedExample obj, Integer value) {
				obj.setId(value);
			}
		});
		columns.add(new Column<NamedExample, String>("ColumnName", DataType.STRING) {
			@Override
			public String fetchValue(NamedExample obj) {
				return obj.getName();
			}

			@Override
			public void insertValue(NamedExample obj, String value) {
				obj.setName(value);
			}
		});
	}

	@Override
	public NamedExample constructor() {
		return new NamedExample();
	}
}
```

You can notice that the name of the table is passed through the `Contract` constructor and right after we start defining columns.

The columns variable is the list of `Column` defined in this contract. We define for each attribute in our base object a `Column`,
and within the column implementation how the attribute is set in an object and how you get it from an object.

```java
columns.add(new Column<NamedExample, String>("ColumnName", DataType.STRING) {
    @Override
    public String fetchValue(NamedExample obj) {
        return obj.getName();
    }

    @Override
    public void insertValue(NamedExample obj, String value) {
        obj.setName(value);
    }
});
```

The first parameter is the column name within the database, the second is the [DataType](#available-data-types).
Through other constructors you can set if it is `nullable`, `primary key` and `auto increment` as well.

Finally we define the constructor of the object.

```java
@Override
public NamedExample constructor() {
    return new NamedExample();
}
```

### Available Data Types

Available types:

`INTEGER` maps to a java Integer represented by `INTEGER`

`STRING` maps to a java String represented by `TEXT`

`BOOLEAN` maps to a java Boolean represented by `INTEGER`

`DOUBLE` maps to a java Double represented by `REAL`

`FLOAT` maps to a java Float represented by `REAL`

`LONG` maps to a java Long represented by `INTEGER`

`SHORT` maps to a java Short represented by `INTEGER`

In SQLite:

`INTEGER` The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.

`REAL` The value is a floating point value, stored as an 8-byte IEEE floating point number.

`TEXT` The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).

### Using queries

The `Query` type provides a fast way to write SQLite queries without using SQLite language.
It allows you to do select statements without any SQLite knowledge.

As an example, a simple Id selection:

````java
Selection selection = new Query().column("ColumnId").equalsTo(obj.getId()).end();
contractDatabase.delete(NamedExample.class, selection);
````

First we instantiate a query, we pass the target column by calling `column(String)`,
we state that the column value must be equals to our value by calling `equalsTo(Object)`
and finally we end the query.

Then we pass the resulting Selection to our delete method, that will delete only
objects that match our query.

We can write more complex queries by chaining operators.

````java
Selection selection = new Query().column("ColumnId").equalsTo(obj.getId()).and().column("ColumnName").equalsTo(obj.getName()).end();
contractDatabase.select(NamedExample.class, selection);
````

Here we only query objects that have both the same Id and same Name as our object.

Many other operations are available as greater, smaller, not equals, between, like and more.

To control even more our query we can use the `Order` type and pass a `limit` integer to our select.

````java
Selection selection = new Query().column("ColumnId").greaterThan(5).end();
Order order = new Order("ColumnName", OrderType.Descending);
contractDatabase.select(NamedExample.class, selection, order, 10);
````

Here we state that our results must be limited to 10 entries and ordered from highest to lowest (`OrderType.Descending`) value at the name column. 
We execute the query by calling `select(Class<?>, Selection, Order, Integer)`.

### Executing raw SQL

Finally you can as well execute a SQL of your own through the method `executeSQL(String)`.

```java
contractDatabase.executeSQL(sqlQuery);
```
 
## Install Library

__Step 1.__ Get this code and compile it

__Step 2.__ Define a dependency within your project. For that, access to Properties > Android > Library and click on add and select the library

##  License

MIT License. See the file LICENSE.md with the full license text.

## Compatibility

This Library is valid for Android systems from version Android 4.4 (android:minSdkVersion="19" android:targetSdkVersion="19").
