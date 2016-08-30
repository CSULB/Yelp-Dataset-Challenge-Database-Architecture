package edu.csulb.yelp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.csulb.yelp.database.DatabaseHelper;
import edu.csulb.yelp.database.DatabaseKeys.Attributes;
import edu.csulb.yelp.database.DatabaseKeys.CategoryAttributes;

public class AttributesModel extends DatabaseHelper implements Model {

	@Override
	public ResultSet selectAll(boolean isDesc) {
		return null;
	}

	@Override
	public int countAll() {
		return 0;
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

	public int getAttributesCount(String inClause) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String subQuery = "SELECT " + CategoryAttributes.ATT_ID + " FROM " + CategoryAttributes.TABLE_NAME
					+ " WHERE " + CategoryAttributes.CATEGORY_NAME + " IN(" + inClause + ")";
			String mainQuery = "SELECT COUNT(*) FROM " + Attributes.TABLE_NAME + " WHERE " + Attributes.ATT_ID + " IN("
					+ subQuery + ")";
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

	public ResultSet getAttributes(String inClause) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String subQuery = "SELECT " + CategoryAttributes.ATT_ID + " FROM " + CategoryAttributes.TABLE_NAME
					+ " WHERE " + CategoryAttributes.CATEGORY_NAME + " IN(" + inClause + ")";
			String mainQuery = "SELECT * FROM " + Attributes.TABLE_NAME + " WHERE " + Attributes.ATT_ID + " IN("
					+ subQuery + ")";
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
