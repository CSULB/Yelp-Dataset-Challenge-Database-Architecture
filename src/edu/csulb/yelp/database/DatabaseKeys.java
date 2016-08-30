package edu.csulb.yelp.database;

public class DatabaseKeys {

	public static class CheckinTable {
		public static final String TABLE_NAME = "CHECKIN";

		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String TYPE = "TYPE";
		public static final String CHECKIN_DATE = "CHECKIN_DATE";
		public static final String COUNT = "COUNT";
	}

	public static class ReviewsTable {
		public static final String TABLE_NAME = "REVIEWS";

		public static final String REVIEW_ID = "REVIEW_ID";
		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String USER_ID = "USER_ID";
		public static final String STARS = "STARS";
		public static final String POSTED_ON = "POSTED_ON";
		public static final String TYPE = "TYPE";
		public static final String VOTES_FUNNY = "VOTES_FUNNY";
		public static final String VOTES_USEFUL = "VOTES_USEFUL";
		public static final String VOTES_COOL = "VOTES_COOL";
		public static final String TEXT = "TEXT";
	}

	public static class UsersTable {
		public static final String TABLE_NAME = "USERS";

		public static final String USER_ID = "USER_ID";
		public static final String YELPING_SINCE = "YELPING_SINCE";
		public static final String NAME = "NAME";
		public static final String FANS = "FANS";
		public static final String TYPE = "TYPE";
		public static final String AVERAGE_STARS = "AVERAGE_STARS";
		public static final String VOTES_FUNNY = "VOTES_FUNNY";
		public static final String VOTES_USEFUL = "VOTES_USEFUL";
		public static final String VOTES_COOL = "VOTES_COOL";
		public static final String C_PROFILE = "C_PROFILE";
		public static final String C_FUNNY = "TEXC_FUNNYT";
		public static final String C_PLAIN = "C_PLAIN";
		public static final String C_WRITER = "C_WRITER";
		public static final String C_NOTE = "C_NOTE";
		public static final String C_PHOTOS = "C_PHOTOS";
		public static final String C_HOT = "C_HOT";
		public static final String C_COOL = "C_COOL";
		public static final String C_MORE = "C_MORE";
	}

	public static class FriendsTable {
		public static final String TABLE_NAME = "FRIENDS";

		public static final String USER_ID = "USER_ID";
		public static final String FRIEND_ID = "FRIEND_ID";
	}

	public static class EliteUsersTable {
		public static final String TABLE_NAME = "ELITE_USERS";

		public static final String USER_ID = "USER_ID";
		public static final String YEAR = "YEAR";
	}

	public static class BusinessTable {
		public static final String TABLE_NAME = "BUSINESS";

		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String NAME = "NAME";
		public static final String OPEN = "OPEN";
		public static final String REVIEW_COUNT = "REVIEW_COUNT";
		public static final String STARS = "STARS";
		public static final String LONGITUDE = "LONGITUDE";
		public static final String LATITUDE = "LATITUDE";
		public static final String TYPE = "TYPE";
		public static final String CITY = "CITY";
		public static final String FULL_ADDRESS = "FULL_ADDRESS";
		public static final String STATE = "STATE";
	}

	public static class BusinessHoursTable {
		public static final String TABLE_NAME = "BUSINESS_HOURS";

		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String DAY_OF_WEEK = "DAY_OF_WEEK";
		public static final String OPEN = "OPEN";
		public static final String CLOSE = "CLOSE";
	}

	public static class BusinessNeighborhoodTable {
		public static final String TABLE_NAME = "BUSINESS_NEIGHBORHOODS";

		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String NEIGHBORHOOD = "NEIGHBORHOOD";
	}

	public static class BusinessCategories {
		public static final String TABLE_NAME = "BUSINESS_CATEGORIES";

		public static final String BUSINESS_ID = "BUSINESS_ID";
		public static final String CATEGORY_ID = "CATEGORY_ID";
	}

	public static class CategoriesTable {
		public static final String TABLE_NAME = "CATEGORIES";

		public static final String CAT_ID = "CAT_ID";
		public static final String CAT_NAME = "CAT_NAME";
		public static final String PARENT = "PARENT";
	}

	public static class CategoryAttributes {
		public static final String TABLE_NAME = "CATEGORY_ATTRIBUTES";

		public static final String ATT_ID = "ATT_ID";
		public static final String CATEGORY_NAME = "CATEGORY_NAME";
	}

	public static class Attributes {
		public static final String TABLE_NAME = "ATTRIBUTES";

		public static final String ATT_ID = "ATT_ID";
		public static final String ATT_NAME = "ATT_NAME";
	}
}
