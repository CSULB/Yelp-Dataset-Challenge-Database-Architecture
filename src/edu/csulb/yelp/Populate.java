package edu.csulb.yelp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import oracle.jdbc.pool.OracleDataSource;

public class Populate {

	public static final String CONNECTION_URL = "jdbc:oracle:thin:@localhost:1521:csulb";
	public static final String ORACLE_USERNAME = "csulb";
	public static final String ORACLE_PASSWORD = "csulb";
	public static final String[] mainCategories = { "Active Life", "Arts & Entertainment", "Automotive", "Car Rental",
			"Cafes", "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores", "Department Stores",
			"Education", "Event Planning & Services", "Flowers & Gifts", "Food", "Health & Medical", "Home Services",
			"Home & Garden", "Hospitals", "Hotels & Travel", "Hardware Stores", "Grocery", "Medical Centers",
			"Nurseries & Gardening", "Nightlife", "Restaurants", "Shopping", "Transportation" };
	private static List<String> mainCategoryArray;

	public static void main(String[] args) {

		mainCategoryArray = Arrays.asList(mainCategories);

		DatabaseHelper db = new DatabaseHelper();
		mainCategories(db.open(), mainCategories);
		business(db.open(), "D:\\Study\\DBA\\Assignment 3\\yelp_business.json");
		reviews(db.open(), "D:\\Study\\DBA\\Assignment 3\\yelp_review.json");
		checkins(db.open(), "D:\\Study\\DBA\\Assignment 3\\yelp_checkin.json");
		users(db.open(), "D:\\Study\\DBA\\Assignment 3\\yelp_user.json");
		db.close();
	}

	private static class DatabaseHelper {

		private Connection sysConn;

		public Connection open() {
			try {
				// Create a OracleDataSource instance and set properties
				OracleDataSource ods = new OracleDataSource();
				ods.setURL(CONNECTION_URL);
				ods.setUser(ORACLE_USERNAME);
				ods.setPassword(ORACLE_PASSWORD);
				return sysConn = ods.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public void close() {
			try {
				if (sysConn != null && !sysConn.isClosed()) {
					sysConn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void mainCategories(Connection conn, String[] mainCats) {

		PreparedStatement ps = null;
		try {
			String truncateBusinessTable = "TRUNCATE TABLE CATEGORIES";
			Statement stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessTable);
			stmnt.close();

			String insertIntoCategories = "INSERT INTO CATEGORIES (CAT_ID, CAT_NAME, PARENT) VALUES (?, ?, ?)";
			ps = conn.prepareStatement(insertIntoCategories);

			for (int i = 0; i < mainCategories.length; i++) {
				ps.setInt(1, i + 1);
				ps.setString(2, mainCategories[i]);
				ps.setString(3, "0");
				ps.addBatch();
			}

			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void checkins(Connection conn, String path) {

		PreparedStatement ps = null;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			conn.setAutoCommit(false);

			String truncateCheckin = "TRUNCATE TABLE CHECKIN";
			Statement stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateCheckin);
			stmnt.close();

			String insertIntoCheckin = "INSERT INTO CHECKIN (BUSINESS_ID, TYPE, CHECKIN_DATE, COUNT) VALUES (?, ?, ?, ?)";
			ps = conn.prepareStatement(insertIntoCheckin);

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				JSONObject checkin = new JSONObject(sCurrentLine);
				String businessId = checkin.getString("business_id");
				String type = checkin.getString("type");

				if (checkin.has("checkin_info")) {
					JSONObject checkinDates = checkin.getJSONObject("checkin_info");
					Iterator<?> keys = checkinDates.keys();
					while (keys.hasNext()) {
						ps.setString(1, businessId);
						ps.setString(2, type);
						String key = (String) keys.next();
						ps.setString(3, (String) key);
						ps.setInt(4, checkinDates.getInt(key));
						ps.addBatch();
					}
				} else {
					ps.setString(1, businessId);
					ps.setString(2, type);
					ps.setString(3, "");
					ps.setInt(4, 0);
					ps.addBatch();
				}
			}

			ps.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void reviews(Connection conn, String path) {

		Statement stmnt = null;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			conn.setAutoCommit(false);

			String truncateReviews = "TRUNCATE TABLE REVIEWS";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateReviews);

			String sCurrentLine;
			String insertIntoReviews = "INSERT INTO REVIEWS (REVIEW_ID, BUSINESS_ID, USER_ID, STARS, POSTED_ON, TYPE, TEXT, VOTES_FUNNY, VOTES_USEFUL, VOTES_COOL) VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?)";

			while ((sCurrentLine = br.readLine()) != null) {

				JSONObject review = new JSONObject(sCurrentLine);
				JSONObject votes = review.getJSONObject("votes");

				PreparedStatement ps = conn.prepareStatement(insertIntoReviews);
				ps.setString(1, review.getString("review_id"));
				ps.setString(2, review.getString("business_id"));
				ps.setString(3, review.getString("user_id"));
				ps.setInt(4, review.getInt("stars"));
				ps.setString(5, review.getString("date"));
				ps.setString(6, review.getString("type"));
				ps.setString(7, review.getString("text"));
				ps.setLong(8, votes.getLong("funny"));
				ps.setLong(9, votes.getLong("useful"));
				ps.setLong(10, votes.getLong("cool"));
				ps.executeUpdate();
				ps.close();
			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmnt != null && !stmnt.isClosed()) {
					stmnt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("Cause: " + e.getCause() + " Error code: " + e.getErrorCode() + " Message: "
						+ e.getMessage() + " LMessage: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public static void users(Connection conn, String path) {
		PreparedStatement usersPS = null;
		PreparedStatement elitePS = null;
		PreparedStatement friendsPS = null;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			{
				conn.setAutoCommit(false);

				String truncateUsers = "TRUNCATE TABLE USERS";
				Statement stmnt = conn.createStatement();
				stmnt.executeUpdate(truncateUsers);
				stmnt.close();

				String truncateFriends = "TRUNCATE TABLE FRIENDS";
				stmnt = conn.createStatement();
				stmnt.executeUpdate(truncateFriends);
				stmnt.close();

				String truncateEliteUsers = "TRUNCATE TABLE ELITE_USERS";
				stmnt = conn.createStatement();
				stmnt.executeUpdate(truncateEliteUsers);
				stmnt.close();

				String insertIntoUsers = "INSERT INTO USERS (USER_ID, YELPING_SINCE, NAME, FANS, TYPE, AVERAGE_STARS, VOTES_FUNNY, VOTES_USEFUL, VOTES_COOL, C_PROFILE, C_FUNNY, C_PLAIN, C_WRITER, C_NOTE, C_PHOTOS, C_HOT, C_COOL, C_MORE) VALUES (?, TO_DATE(?, 'YYYY-MM'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				usersPS = conn.prepareStatement(insertIntoUsers);

				String insertIntoFriends = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
				friendsPS = conn.prepareStatement(insertIntoFriends);

				String insertIntoElite = "INSERT INTO ELITE_USERS (USER_ID, YEAR) VALUES (?, TO_DATE(?, 'YYYY'))";
				elitePS = conn.prepareStatement(insertIntoElite);

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					JSONObject user = new JSONObject(sCurrentLine);
					String userId = user.getString("user_id");

					usersPS.setString(1, userId);
					usersPS.setString(2, user.getString("yelping_since"));
					usersPS.setString(3, user.getString("name"));
					usersPS.setInt(4, user.getInt("fans"));
					usersPS.setString(5, user.getString("type"));
					usersPS.setBigDecimal(6, user.getBigDecimal("average_stars"));

					if (user.has("votes")) {
						JSONObject votes = user.getJSONObject("votes");
						if (votes.has("funny")) {
							usersPS.setInt(7, votes.getInt("funny"));
						} else {
							usersPS.setInt(7, 0);
						}
						if (votes.has("useful")) {
							usersPS.setInt(8, votes.getInt("useful"));
						} else {
							usersPS.setInt(8, 0);
						}
						if (votes.has("cool")) {
							usersPS.setInt(9, votes.getInt("cool"));
						} else {
							usersPS.setInt(9, 0);
						}
					} else {
						usersPS.setInt(7, 0);
						usersPS.setInt(8, 0);
						usersPS.setInt(9, 0);
					}

					if (user.has("votes")) {
						JSONObject compliements = user.getJSONObject("votes");
						if (compliements.has("profile")) {
							usersPS.setInt(10, compliements.getInt("profile"));
						} else {
							usersPS.setInt(10, 0);
						}
						if (compliements.has("funny")) {
							usersPS.setInt(11, compliements.getInt("funny"));
						} else {
							usersPS.setInt(11, 0);
						}
						if (compliements.has("plain")) {
							usersPS.setInt(12, compliements.getInt("plain"));
						} else {
							usersPS.setInt(12, 0);
						}
						if (compliements.has("writer")) {
							usersPS.setInt(13, compliements.getInt("writer"));
						} else {
							usersPS.setInt(13, 0);
						}
						if (compliements.has("note")) {
							usersPS.setInt(14, compliements.getInt("note"));
						} else {
							usersPS.setInt(14, 0);
						}
						if (compliements.has("photos")) {
							usersPS.setInt(15, compliements.getInt("photos"));
						} else {
							usersPS.setInt(15, 0);
						}
						if (compliements.has("hot")) {
							usersPS.setInt(16, compliements.getInt("hot"));
						} else {
							usersPS.setInt(16, 0);
						}
						if (compliements.has("cool")) {
							usersPS.setInt(17, compliements.getInt("cool"));
						} else {
							usersPS.setInt(17, 0);
						}
						if (compliements.has("more")) {
							usersPS.setInt(18, compliements.getInt("more"));
						} else {
							usersPS.setInt(18, 0);
						}
					} else {
						usersPS.setInt(10, 0);
						usersPS.setInt(11, 0);
						usersPS.setInt(12, 0);
						usersPS.setInt(13, 0);
						usersPS.setInt(14, 0);
						usersPS.setInt(15, 0);
						usersPS.setInt(16, 0);
						usersPS.setInt(17, 0);
						usersPS.setInt(18, 0);
					}

					if (user.has("friends")) {
						JSONArray friends = user.getJSONArray("friends");
						for (int j = 0; j < friends.length(); j++) {
							friendsPS.setString(1, userId);
							friendsPS.setString(2, friends.getString(j));
							friendsPS.addBatch();
						}
					}

					if (user.has("elite")) {
						JSONArray eliteYears = user.getJSONArray("elite");
						for (int j = 0; j < eliteYears.length(); j++) {
							elitePS.setString(1, userId);
							elitePS.setInt(2, eliteYears.getInt(j));
							elitePS.addBatch();
						}
					}

					usersPS.addBatch();
				}

				elitePS.executeBatch();
				friendsPS.executeBatch();
				usersPS.executeBatch();
				conn.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (usersPS != null && !usersPS.isClosed()) {
					usersPS.close();
				}
				if (elitePS != null && !elitePS.isClosed()) {
					elitePS.close();
				}
				if (friendsPS != null && !friendsPS.isClosed()) {
					friendsPS.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void business(Connection conn, String path) {

		PreparedStatement businessPS = null;
		PreparedStatement businessHoursPS = null;
		PreparedStatement businessNeighborhoodsPS = null;
		PreparedStatement businessCategoriesPS = null;

		PreparedStatement categoriesPS = null;
		PreparedStatement attributesPS = null;
		PreparedStatement categoryAttributesPS = null;
		PreparedStatement businessAttributesPS = null;

		Map<String, String> attributesMap = new TreeMap<>();
		Set<String> categoryAttributeMapping = new TreeSet<>();
		Set<String> subcategoryMapping = new TreeSet<>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			conn.setAutoCommit(false);

			String truncateBusinessTable = "TRUNCATE TABLE BUSINESS";
			Statement stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessTable);
			stmnt.close();

			String truncateBusinessHoursTable = "TRUNCATE TABLE BUSINESS_HOURS";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessHoursTable);
			stmnt.close();

			String truncateBusinessHeighborhoodTable = "TRUNCATE TABLE BUSINESS_NEIGHBORHOODS";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessHeighborhoodTable);
			stmnt.close();

			String truncateBusinessCategories = "TRUNCATE TABLE BUSINESS_CATEGORIES";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessCategories);
			stmnt.close();

			String truncateCategoryAttributes = "TRUNCATE TABLE CATEGORY_ATTRIBUTES";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateCategoryAttributes);
			stmnt.close();

			String truncateAttributes = "TRUNCATE TABLE ATTRIBUTES";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateAttributes);
			stmnt.close();

			String truncateBusinessAttributes = "TRUNCATE TABLE BUSINESS_ATTRIBUTES";
			stmnt = conn.createStatement();
			stmnt.executeUpdate(truncateBusinessAttributes);
			stmnt.close();

			String insertIntoBusiness = "INSERT INTO BUSINESS (BUSINESS_ID, NAME, OPEN, REVIEW_COUNT, STARS, LONGITUDE, LATITUDE, TYPE, CITY, FULL_ADDRESS, STATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			businessPS = conn.prepareStatement(insertIntoBusiness);

			String insertIntoBusinessHours = "INSERT INTO BUSINESS_HOURS (BUSINESS_ID, DAY_OF_WEEK, OPEN, CLOSE) VALUES (?, ?, TO_TIMESTAMP(?, 'HH24:MI'), TO_TIMESTAMP(?, 'HH24:MI'))";
			businessHoursPS = conn.prepareStatement(insertIntoBusinessHours);

			String insertIntoNeighborhood = "INSERT INTO BUSINESS_NEIGHBORHOODS (BUSINESS_ID, NEIGHBORHOOD) VALUES (?, ?)";
			businessNeighborhoodsPS = conn.prepareStatement(insertIntoNeighborhood);

			String insertIntoBusinessCategories = "INSERT INTO BUSINESS_CATEGORIES (BUSINESS_ID, CATEGORY_NAME) VALUES (?, ?)";
			businessCategoriesPS = conn.prepareStatement(insertIntoBusinessCategories);

			String insertIntoCategoriesAttributes = "INSERT INTO CATEGORY_ATTRIBUTES (CATEGORY_NAME, ATT_ID) VALUES (?, ?)";
			categoryAttributesPS = conn.prepareStatement(insertIntoCategoriesAttributes);

			String insertIntoAttributes = "INSERT INTO ATTRIBUTES (ATT_ID, ATT_NAME) VALUES (?, ?)";
			attributesPS = conn.prepareStatement(insertIntoAttributes);

			String insertIntoBusinessAttributes = "INSERT INTO BUSINESS_ATTRIBUTES (BUSINESS_ID, ATT_ID) VALUES (?, ?)";
			businessAttributesPS = conn.prepareStatement(insertIntoBusinessAttributes);

			String insertIntoCategories = "INSERT INTO CATEGORIES (CAT_ID, CAT_NAME, PARENT) VALUES (?, ?, ?)";
			categoriesPS = conn.prepareStatement(insertIntoCategories);

			long attributeId = 1;
			int catId = mainCategories.length;
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				JSONObject business = new JSONObject(sCurrentLine);

				String businessId = business.getString("business_id");
				String fullAddress = business.getString("full_address");
				businessPS.setString(1, businessId);
				businessPS.setString(2, business.getString("name"));
				businessPS.setBoolean(3, business.getBoolean("open"));
				businessPS.setInt(4, business.getInt("review_count"));
				businessPS.setBigDecimal(5, business.getBigDecimal("stars"));
				businessPS.setString(6, String.valueOf(business.getBigDecimal("longitude")));
				businessPS.setString(7, String.valueOf(business.getBigDecimal("latitude")));
				businessPS.setString(8, business.getString("type"));
				businessPS.setString(9, business.getString("city") + "##"
						+ fullAddress.substring(fullAddress.length() - 5, fullAddress.length()));
				businessPS.setString(10, fullAddress);
				businessPS.setString(11, business.getString("state"));

				if (business.has("neighborhoods")) {
					JSONArray neighborhoods = business.getJSONArray("neighborhoods");
					for (int j = 0; j < neighborhoods.length(); j++) {
						businessNeighborhoodsPS.setString(1, businessId);
						businessNeighborhoodsPS.setString(2, neighborhoods.getString(j));
						businessNeighborhoodsPS.addBatch();
					}
				}

				if (business.has("hours")) {
					JSONObject workingHours = business.getJSONObject("hours");
					Iterator<?> keys = workingHours.keys();

					while (keys.hasNext()) {
						businessHoursPS.setString(1, businessId);
						String key = (String) keys.next();

						switch (key) {
						case "Sunday":
							businessHoursPS.setInt(2, 1);
							businessHoursPS.setString(3, workingHours.getJSONObject("Sunday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Sunday").getString("close"));
							break;
						case "Monday":
							businessHoursPS.setInt(2, 2);
							businessHoursPS.setString(3, workingHours.getJSONObject("Monday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Monday").getString("close"));
							break;
						case "Tuesday":
							businessHoursPS.setInt(2, 3);
							businessHoursPS.setString(3, workingHours.getJSONObject("Tuesday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Tuesday").getString("close"));
							break;
						case "Wednesday":
							businessHoursPS.setInt(2, 4);
							businessHoursPS.setString(3, workingHours.getJSONObject("Wednesday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Wednesday").getString("close"));
							break;
						case "Thursday":
							businessHoursPS.setInt(2, 5);
							businessHoursPS.setString(3, workingHours.getJSONObject("Thursday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Thursday").getString("close"));
							break;
						case "Friday":
							businessHoursPS.setInt(2, 6);
							businessHoursPS.setString(3, workingHours.getJSONObject("Friday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Friday").getString("close"));
							break;
						case "Saturday":
							businessHoursPS.setInt(2, 7);
							businessHoursPS.setString(3, workingHours.getJSONObject("Saturday").getString("open"));
							businessHoursPS.setString(4, workingHours.getJSONObject("Saturday").getString("close"));
							break;
						default:
							break;
						}

						businessHoursPS.addBatch();
					}
				}

				Set<Long> attributIds = new TreeSet<>();
				JSONObject attributes = business.getJSONObject("attributes");
				Iterator<?> keys = attributes.keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					if (attributes.get(key) instanceof JSONObject) {

						JSONObject innerAttributes = attributes.getJSONObject(key);

						// Level 2 attributes
						Iterator<?> innerKeys = innerAttributes.keys();
						while (innerKeys.hasNext()) {
							String innerKey = (String) innerKeys.next();
							innerKey = innerKey + "_" + innerAttributes.get(innerKey);
							String mapKey = key + "_" + innerKey;
							if (!attributesMap.containsKey(mapKey)) {
								attributesMap.put(mapKey, String.valueOf(attributeId));
								attributIds.add(attributeId);

								// if (innerAttributes.getBoolean(innerKey)) {
								businessAttributesPS.setString(1, businessId);
								businessAttributesPS.setString(2, String.valueOf(attributeId));
								businessAttributesPS.addBatch();
								// }
								attributeId++;
							}
						}
					} else {
						key = key + "_" + attributes.get(key);
						if (!attributesMap.containsKey(key)) {
							attributesMap.put(key, String.valueOf(attributeId));
							attributIds.add(attributeId);

							// if (attributes.getBoolean(key)) {
							businessAttributesPS.setString(1, businessId);
							businessAttributesPS.setString(2, String.valueOf(attributeId));
							businessAttributesPS.addBatch();
							// }
							attributeId++;
						}
					}
				}

				JSONArray categories = business.getJSONArray("categories");
				Set<String> mainCategoriesForThis = new TreeSet<>();
				for (int j = 0; j < categories.length(); j++) {
					businessCategoriesPS.setString(1, businessId);
					businessCategoriesPS.setString(2, categories.getString(j));

					for (int i = 0; i < mainCategories.length; i++) {
						// If this category is one of the main category
						if (categories.getString(j).equalsIgnoreCase(mainCategories[i])) {
							mainCategoriesForThis.add(mainCategories[i]);
						}

						businessCategoriesPS.addBatch();
					}

					// Associate each attribute with main categories
					for (Long id : attributIds) {
						categoryAttributeMapping.add(categories.getString(j) + "##" + id);
					}
				}

				for (int j = 0; j < categories.length(); j++) {
					if (!mainCategoriesForThis.contains(categories.getString(j))) {
						for (String string : mainCategoriesForThis) {
							subcategoryMapping
									.add(categories.getString(j) + "##" + (mainCategoryArray.indexOf(string) + 1));
						}
					}
				}

				businessPS.addBatch();
			}

			System.out.println(subcategoryMapping);
			for (String s : subcategoryMapping) {
				String[] catSub = s.split("##");
				categoriesPS.setString(1, String.valueOf(++catId));
				categoriesPS.setString(2, catSub[0]);
				categoriesPS.setString(3, catSub[1]);
				System.out.println("catId: " + catId + " catSub[0]: " + catSub[0] + " catSub[1]: " + catSub[1]);
				categoriesPS.addBatch();
			}

			for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
				attributesPS.setString(1, entry.getValue());
				attributesPS.setString(2, entry.getKey());
				attributesPS.addBatch();
			}

			System.out.println(categoryAttributeMapping);

			for (String s : categoryAttributeMapping) {
				String[] catAtt = s.split("##");
				categoryAttributesPS.setString(1, catAtt[0]);
				categoryAttributesPS.setString(2, catAtt[1]);
				categoryAttributesPS.addBatch();
			}

			categoriesPS.executeBatch();
			attributesPS.executeBatch();
			businessAttributesPS.executeBatch();
			categoryAttributesPS.executeBatch();
			businessPS.executeBatch();
			businessCategoriesPS.executeBatch();
			businessHoursPS.executeBatch();
			businessNeighborhoodsPS.executeBatch();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (businessPS != null && !businessPS.isClosed()) {
					businessPS.close();
				}
				if (categoriesPS != null && !categoriesPS.isClosed()) {
					categoriesPS.close();
				}
				if (businessHoursPS != null && !businessHoursPS.isClosed()) {
					businessHoursPS.close();
				}
				if (businessNeighborhoodsPS != null && !businessNeighborhoodsPS.isClosed()) {
					businessNeighborhoodsPS.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
