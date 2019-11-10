package com.example.realwhatsappclone.utills;

import android.content.res.Resources;

import com.example.realwhatsappclone.R;
import com.google.firebase.annotations.PublicApi;
import com.google.firebase.database.DatabaseError;

public class DataBaseErrorToException {
    public static final int DATA_STALE = -1;
    public static final int OPERATION_FAILED = -2;
    public static final int PERMISSION_DENIED = -3;
    public static final int DISCONNECTED = -4;
    public static final int EXPIRED_TOKEN = -6;
    public static final int INVALID_TOKEN = -7;
    public static final int MAX_RETRIES = -8;
    public static final int OVERRIDDEN_BY_SET = -9;
    public static final int UNAVAILABLE = -10;
    public static final int USER_CODE_EXCEPTION = -11;
    public static final int NETWORK_ERROR = -24;
    public static final int WRITE_CANCELED = -25;

    public static String convertToException(Resources recources,DatabaseError error)
    {
        switch (error.getCode()) {
            case NETWORK_ERROR:
            case DISCONNECTED:
            case UNAVAILABLE:
            {
                return recources.getString(R.string.there_is_no_internet_connetion);
                }
            case INVALID_TOKEN:
            case EXPIRED_TOKEN: {
                return recources.getString(R.string.invalid_token);
            }
            default:
                return recources.getString(R.string.something_went_wrong);
        }
            }
}
