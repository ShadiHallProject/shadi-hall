package org.by9steps.shadihall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.dbhelper.BookingManager;
import org.by9steps.shadihall.model.Booking;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.SINGLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookCalendarFragment extends Fragment {

    List<Date> dateList = new ArrayList<>();
    List<Date> BookingList = new ArrayList<>();
    public List<Booking> itemList;
    CalendarPickerView calendar;
    Date today;
    Calendar LastYear, nextYear;
    TextView textView;
    Button refresh;
    public BookingManager mDatabase;
    String pattern="yyyy-MM-dd";


    public BookCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_calendar, container, false);

        mDatabase = new BookingManager(getContext());

        LastYear = Calendar.getInstance();
        LastYear.add(Calendar.YEAR, -1);
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        today = new Date();

        calendar = (CalendarPickerView)view.findViewById(R.id.calendar);
//        calendar.init(LastYear.getTime(), nextYear.getTime()).withSelectedDate(today);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.YEAR, 2019);
        today = c.getTime();
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.YEAR, 2020);
        nextYear.setTime(c.getTime());
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(new Date()).inMode(SINGLE);

        FetchFromDb();

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
//        dateList.add(today);
//        dateList.add(c.getTime());
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //     String selecteddate= DateFormat.getInstance().format(DateFormat.FULL).format(String.valueOf(date));
                Boolean found = false;
                Calendar calselected = Calendar.getInstance();
                calselected.setTime(date);
                for (int i = 0; i < dateList.size(); i++) {
                    if (dateList.get(i).equals(calselected.getTime())) {
                        found = true;
                    }
                }
                if (found) {
                    String selectedDate = calselected.get(Calendar.DAY_OF_MONTH) + "-" + calselected.get(Calendar.MONTH) + "-" + calselected.get(Calendar.YEAR);
                    Toast.makeText(getContext(), "Click is\n" + selectedDate, Toast.LENGTH_LONG).show();

                    DateFormat df = new SimpleDateFormat(pattern);
                    selectedDate= df.format(calendar.getSelectedDate());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.menu_container, BookingDetailFragment.newInstance(selectedDate))
                            .addToBackStack(null)
                            .commit();

                    Toast.makeText(getContext(), selectedDate+"\nEvent Already Booked \n" + calselected.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
                } else {
                    //new Activity
                    String selectedDate = calselected.get(Calendar.DAY_OF_MONTH) + "-" + calselected.get(Calendar.MONTH) + "-" + calselected.get(Calendar.YEAR);
                    Toast.makeText(getContext(), "Click is\n" + selectedDate, Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.menu_container, new BookingFormFragment())
                            .addToBackStack(null)
                            .commit();
//                    Intent intent = new Intent(BookCalender.this, BookingForm.class);
//                    intent.putExtra("message", selectedDate);
//                    startActivity(intent);
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        return  view;
    }

    public void FetchFromDb() {
        mDatabase.open();
        itemList = new ArrayList<>();
        dateList = new ArrayList<>();
        itemList = mDatabase.getBooking();
        mDatabase.close();
        for (Booking book : itemList) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = sdf.parse(book.getEventDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (between(cal.getTime(), today, nextYear.getTime()) == true) {
                dateList.add(cal.getTime());
            }
        }
        // mDatabase.close();
        calendar.highlightDates(dateList);

    }

    public static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
