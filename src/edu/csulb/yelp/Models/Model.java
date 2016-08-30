package edu.csulb.yelp.Models;

import java.sql.ResultSet;

public interface Model {

	ResultSet selectAll(boolean isDesc);

	int countAll();

	void deleteAll();

	void delete(String value);

	void update(Object... values);
}
