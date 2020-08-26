package com.neige_i.mareu.view.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.neige_i.mareu.data.MeetingRepository;
import com.neige_i.mareu.data.model.Meeting;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddViewModel extends ViewModel {

    private final MeetingRepository meetingRepository;

    private final MutableLiveData<String> time = new MutableLiveData<>("");
    private final MutableLiveData<String> date = new MutableLiveData<>("");
    private final MutableLiveData<String> topicError = new MutableLiveData<>();
    private final MutableLiveData<String> timeError = new MutableLiveData<>();
    private final MutableLiveData<String> dateError = new MutableLiveData<>();
    private final MutableLiveData<String> placeError = new MutableLiveData<>();
//    private final MutableLiveData<Boolean[]> inputError = new MutableLiveData<>(new Boolean[]{false, false, false, false});
    private final MutableLiveData<Boolean> showSnack = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> endActivity = new MutableLiveData<>(false);

    private final String ERROR_MESSAGE = " "; // Only show the end icon without the message beneath the field
    private final Calendar calendar = Calendar.getInstance();
    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute = calendar.get(Calendar.MINUTE);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private int month = calendar.get(Calendar.MONTH);
    private int year = calendar.get(Calendar.YEAR);
    private String topic = "";
    private String place = "";

    public AddViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void onAddMeeting() {
        boolean error = false;
        if (topic.isEmpty()) {
            topicError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (time.getValue().isEmpty()) {
            timeError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (date.getValue().isEmpty()) {
            dateError.setValue(ERROR_MESSAGE);
            error = true;
        }
        if (place.isEmpty()) {
            placeError.setValue(ERROR_MESSAGE);
            error = true;
        }

        if (error) {
            showSnack.setValue(true);
        } else {
            calendar.set(year, month, day, hour, minute);
            meetingRepository.addMeeting(new Meeting(topic, place, calendar, Arrays.asList("moi", "toi")));
            endActivity.setValue(true);
        }
    }

    public void setTopic(String topic) {
        this.topic = topic;
        topicError.setValue(topic.isEmpty() ? ERROR_MESSAGE : null);
    }

    public LiveData<String> getTime() {
        return time;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        time.setValue(String.format(Locale.FRANCE, "%02d:%02d", hour, minute));
        timeError.setValue(null); // Remove error
    }

    public LiveData<String> getDate() {
        return date;
    }

    public void setDate(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        date.setValue(String.format(Locale.FRANCE, "%02d/%02d/%d", day, month, year));
        dateError.setValue(null); // Remove error
    }

    public void setPlace(String place) {
        this.place = place;
        placeError.setValue(null); // Remove error
    }

    public LiveData<String> getTopicError() {
        return topicError;
    }

    public LiveData<String> getTimeError() {
        return timeError;
    }

    public LiveData<String> getDateError() {
        return dateError;
    }

    public LiveData<String> getPlaceError() {
        return placeError;
    }

    public LiveData<Boolean> getShowSnack() {
        return showSnack;
    }

    public void cancelShowSnack() {
        showSnack.setValue(false);
    }

    public LiveData<Boolean> getEndActivity() {
        return endActivity;
    }

    public void cancelEndActivity() {
        endActivity.setValue(false);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
