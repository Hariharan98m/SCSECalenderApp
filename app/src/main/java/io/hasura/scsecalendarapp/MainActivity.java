package io.hasura.scsecalendarapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static io.hasura.scsecalendarapp.R.id.calendarView;
import static java.util.GregorianCalendar.AD;


public class MainActivity extends AppCompatActivity {

    CalendarView cv;
    ListView lv;
    CoordinatorLayout coordinatorLayout;

    public ProgressDialog progressDialog;
    YearClass[] years=new YearClass[2100];

    // Array of strings...
    ArrayList<String> mobileArray = new ArrayList<String>(20);

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog= new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.list_view_text, mobileArray);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);

        final CompactCalendarView calendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        calendarView.setUseThreeLetterAbbreviation(true);

        setTitle(calendarView.getFirstDayOfCurrentMonth().toString().substring(4,7) + " - "+calendarView.getFirstDayOfCurrentMonth().toString().substring(30));

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);
                Log.d("TAG", "Day was clicked: " + dateClicked + " with events " + events);
                if(events.size()>0)
                    Toast.makeText(MainActivity.this, events.get(0).getData().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("TAG", "Month was scrolled to: " + firstDayOfNewMonth);
                mobileArray.clear();
                setTitle(firstDayOfNewMonth.toString().substring(4,7) + " - "+firstDayOfNewMonth.toString().substring(30));
                for(Event e:calendarView.getEventsForMonth(firstDayOfNewMonth)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(e.getTimeInMillis());

                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    mobileArray.add(mDay + " "+e.getData().toString());
                }
                    adapter.notifyDataSetChanged();

            }
        });

        showProgressDialog(true);
        new AuthApiManager(getBaseContext()).getApiInterface().insert()
                .enqueue(new CustomResponseListener<ReqResponse>() {
                    @Override
                    public void onSuccessfulResponse(ReqResponse response) {
                        showProgressDialog(false);
                        for(ReqResponse.Sheet i:response.sheet){
                            Date date1= null;
                            try {
                                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(i.getDate().substring(0,10));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Log.i("time", "" + date1.getTime());

                            Event ev1 = new Event(Color.GREEN, date1.getTime()+100000000, i.getEvent());
                            calendarView.addEvent(ev1);

                            Log.i("YEAR date:",""+ date1.getYear()%2000);

                            Log.d("event: ", i.getEvent());
                        }


                        for(Event e:calendarView.getEventsForMonth(calendarView.getFirstDayOfCurrentMonth())) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(e.getTimeInMillis());

                            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                            mobileArray.add(mDay + " "+e.getData().toString());
                        }

                        adapter.notifyDataSetChanged();
                        showSnackBar();
                    }

                    @Override
                    public void onFailureResponse(String errorResponse) {
                        Log.d("song insert failed","error");
                    }
                });

    }

    void showSnackBar(){
        Snackbar snackbar= Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>Data Synced from Source</i></font>"),Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLACK);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        snackbar.show();
    }

    public void showProgressDialog(Boolean shouldShow) {
        if (shouldShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }



}
