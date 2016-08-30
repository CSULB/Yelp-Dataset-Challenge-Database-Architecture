package edu.csulb.yelp.Models;

import java.sql.ResultSet;
import java.sql.Statement;

import edu.csulb.yelp.database.DatabaseHelper;
import edu.csulb.yelp.database.DatabaseKeys.ReviewsTable;
import edu.csulb.yelp.database.DatabaseKeys.UsersTable;

public class ReviewsModel extends DatabaseHelper implements Model {

	@Override
	public ResultSet selectAll(boolean isDesc) {
		return null;
	}

	public ResultSet selectBusinessReviews(String businessId) {
		Statement st = null;
		try {
			st = sysConn.createStatement();
			String query = "SELECT * FROM " + ReviewsTable.TABLE_NAME + " r, " + UsersTable.TABLE_NAME
					+ " u WHERE r.USER_ID = u.USER_ID AND r.BUSINESS_ID='" + businessId + "' ORDER BY "
					+ ReviewsTable.POSTED_ON + " DESC";
			System.out.println(query);
			return st.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int countAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object... values) {
		// TODO Auto-generated method stub

	}

}
