package io.hasura.scsecalendarapp;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by HARIHARAN on 17-10-2017.
 */

public class ReqResponse {

    @SerializedName("Sheet1")
    List<Sheet> sheet;

    public class Sheet{
        @SerializedName("date")
        String date;

        @SerializedName("event")
        String event;

        Date date1;

        public String getDate() {
            return date;
        }

        public String getEvent() {
            return event;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }
    }

}
