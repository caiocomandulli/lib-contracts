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

A contract defines a representation of a type within the database,
and the interactions that the object has with it, such as its constructor,
setting and getting attributes.

As for our example we will define a simple Object type:

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

The getters and setters will be how the contract can define an object.
We extend the Contract type and pass our NamedExample as its generic type.

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

You can notice that the name of the table is passed through the Contract constructor and right after we start defining columns.

The columns variable is the list of columns defined in this contract. We define for each attribute in our base object a column,
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

The first parameter is the column name within the database, the second is the DataType (see https://github.com/caiocomandulli/lib-rest#available-data-types).
Through other constructors you can set if it is nullable, primary key and auto increment as well.

Finally we define the constructor of the object.

```java
@Override
public NamedExample constructor() {
    return new NamedExample();
}
```

### Available Data Types

Available types:

INTEGER maps to a java Integer represented by "INTEGER"

STRING maps to a java String represented by "TEXT"

BOOLEAN maps to a java Boolean represented by "INTEGER"

DOUBLE maps to a java Double represented by "REAL"

FLOAT maps to a java Float represented by "REAL"

LONG maps to a java Long represented by "INTEGER"

SHORT maps to a java Short represented by "INTEGER"

In SQLite:

INTEGER. The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.

REAL. The value is a floating point value, stored as an 8-byte IEEE floating point number.

TEXT. The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).

### Using queries


### Executing raw SQL

Finally you can as well execute a SQL of your own through the method executeSQL.

```java
public void executeSQL(String sql);
```
 
## Install Library

__Step 1.__ Get this code and compile it

__Step 2.__ Define a dependency within your project. For that, access to Properties > Android > Library and click on add and select the library

##  License

MIT License. See the file LICENSE.md with the full license text.

## Compatibility

This Library is valid for Android systems from version Android 4.4 (android:minSdkVersion="19" android:targetSdkVersion="19").
