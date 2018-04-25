package io.hasura.scsecalendarapp;

/**
 * Created by HARIHARAN on 18-03-2018.
 */

public class YearClass {

    MonthClass[] mon= new MonthClass[12];

    public MonthClass[] getMon() {
        mon= new MonthClass[12];
        return mon;
    }

    public void setMon(MonthClass[] mon) {
        this.mon = mon;
    }

    public class MonthClass{

        DayClass[] day=new DayClass[31];

        public DayClass[] getDay() {
            day= new DayClass[31];
            return day;
        }

        public void setDay(DayClass[] day) {
            this.day = day;
        }

        public class DayClass{

            String event;

            public String getEvent() {
                return event;
            }

            public void setEvent(String event) {
                this.event = event;
            }
        }
    }
}
