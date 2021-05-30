package com.nam.restaurantmap;

import android.provider.BaseColumns;

public class LocationContract {
    private LocationContract(){}
    public static class LocationTable implements BaseColumns {
        public static final String TABLE_NAME="location_table";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_LOCATION="location";
    }
}
