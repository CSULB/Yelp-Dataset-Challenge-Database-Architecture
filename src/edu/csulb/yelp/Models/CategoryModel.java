package edu.csulb.yelp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.csulb.yelp.database.DatabaseHelper;
import edu.csulb.yelp.database.DatabaseKeys.CategoriesTable;

public class CategoryModel extends DatabaseHelper implements Model {

	public ResultSet selectAll(boolean isDesc) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			if (isDesc) {
				return st.executeQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME + " ORDER BY "
						+ CategoriesTable.CAT_NAME + " DESC");
			} else {
				return st.executeQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME + " ORDER BY "
						+ CategoriesTable.CAT_NAME + " ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getMainCategories() {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String query = "SELECT * FROM " + CategoriesTable.TABLE_NAME + " WHERE " + CategoriesTable.PARENT
					+ " = '0' ORDER BY " + CategoriesTable.CAT_NAME + " ASC";
			return st.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Counts ONLY main categories.
	 */
	@Override
	public int countAll() {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + CategoriesTable.TABLE_NAME + " WHERE "
					+ CategoriesTable.PARENT + " = '0'");
			rs.next();
			int rowCount = rs.getInt(1);
			rs.close();
			return rowCount;
		} catch (Exception e) {
			return 0;
		} finally {
			try {
				if (st != null && st.isClosed()) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public void delete(String value) {
	}

	@Override
	public void update(Object... values) {
	}

	public int getSubCategoriesCount(String inClause) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String subQuery = "SELECT " + CategoriesTable.CAT_ID + " FROM " + CategoriesTable.TABLE_NAME + " WHERE "
					+ CategoriesTable.CAT_NAME + " IN(" + inClause + ")";
			String mainQuery = "SELECT COUNT(*) FROM " + CategoriesTable.TABLE_NAME + " WHERE " + CategoriesTable.PARENT
					+ " IN(" + subQuery + ")";
			ResultSet rs = st.executeQuery(mainQuery);
			rs.next();
			int rowCount = rs.getInt(1);
			rs.close();
			return rowCount;
		} catch (Exception e) {
			return 0;
		} finally {
			try {
				if (st != null && st.isClosed()) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet getSubCategories(String inClause) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String subQuery = "SELECT " + CategoriesTable.CAT_ID + " FROM " + CategoriesTable.TABLE_NAME + " WHERE "
					+ CategoriesTable.CAT_NAME + " IN(" + inClause + ")";
			String mainQuery = "SELECT " + CategoriesTable.CAT_NAME + " FROM " + CategoriesTable.TABLE_NAME + " WHERE "
					+ CategoriesTable.PARENT + " IN(" + subQuery + ")";
			return st.executeQuery(mainQuery);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (st != null && st.isClosed()) {
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
