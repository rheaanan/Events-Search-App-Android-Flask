package com.example.ticketing;

public class EventDetails {
    private String Date;
    private String EventName;
    private String EventId;
    private String Category;
    private String Venue;

    @Override
    public String toString() {
        return  "{" +
                "Date='" + Date + '\'' +
                ", EventName='" + EventName + '\'' +
                ", EventId='" + EventId + '\'' +
                ", Category='" + Category + '\'' +
                ", Venue='" + Venue + '\'' +
                '}';
    }

    public EventDetails(String date, String eventName, String eventId, String category, String venue) {
        Date = date;
        EventName = eventName;
        EventId = eventId;
        Category = category;
        Venue = venue;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }


}

