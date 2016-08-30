package edu.csulb.yelp.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.csulb.yelp.database.DatabaseHelper;

public class BusinessModel extends DatabaseHelper implements Model {

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

	public ResultSet customSelect(String cInClause, String aInClause) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String attributesSubQuery = "SELECT ATT_ID FROM ATTRIBUTES WHERE ATT_NAME IN( " + aInClause + ")";
			String mainQuery = "SELECT DISTINCT(b.BUSINESS_ID),  b.NAME,  b.CITY,  b.STATE,  b.STARS FROM BUSINESS b,  BUSINESS_CATEGORIES bc,  BUSINESS_ATTRIBUTES ba WHERE b.BUSINESS_ID = bc.BUSINESS_ID AND b.BUSINESS_ID = ba.BUSINESS_ID AND ba.ATT_ID = ALL ("
					+ attributesSubQuery + ")";
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

	public ResultSet customTimeSelect(int selectedDay, String start, String end, String attributesCSV) {

		Statement st = null;
		try {
			st = sysConn.createStatement();
			String attributesSubQuery = "SELECT ATT_ID FROM ATTRIBUTES WHERE ATT_NAME IN( " + attributesCSV + ")";
			String timeConstraint = " AND bh.DAY_OF_WEEK = " + selectedDay + " AND (TO_TIMESTAMP(" + start
					+ ", 'HH24:MI') BETWEEN bh.OPEN AND bh.CLOSE) AND (TO_TIMESTAMP(" + end
					+ ", 'HH24:MI') BETWEEN bh.OPEN AND bh.CLOSE)";
			String mainQuery = "SELECT DISTINCT(b.BUSINESS_ID),  b.NAME,  b.CITY,  b.STATE,  b.STARS FROM BUSINESS b,  BUSINESS_CATEGORIES bc,  BUSINESS_ATTRIBUTES ba, BUSINESS_HOURS bh WHERE b.BUSINESS_ID = bc.BUSINESS_ID AND b.BUSINESS_ID = ba.BUSINESS_ID AND b.BUSINESS_ID = bh.BUSINESS_ID AND ba.ATT_ID = ALL ("
					+ attributesSubQuery + ")" + timeConstraint;
			System.out.println(mainQuery);
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
