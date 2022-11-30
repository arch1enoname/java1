import com.vk.api.sdk.exceptions.ApiMessagesDenySendException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Timetable {

    private Group group;
    private String date;
    private List<String> listOfLessons = new ArrayList<>();
    private List<String> listOfHour = new ArrayList<>();
    private List<String> listOfRoom = new ArrayList<>();
    private Calendar calendar = new GregorianCalendar();

    public Timetable(){}

    public Timetable(Group group) {
        this.group = new Group(group.getLink());
    }

    public void getTimetableForDay() throws IOException {
        Document document = Jsoup.connect(group.getLink()).get();
        Elements days = document.select(".day");
        String stringDay = days.select(".day-header").get(calendar.get(Calendar.DAY_OF_WEEK)-1).text();
        date = stringDay;
        Elements lessons = document.select(".day-current");
        for (Element elem:
                lessons.select("div.lesson-name")) {
            listOfLessons.add(elem.text());
        }

        for (Element elem:
                lessons.select("div.lesson-hour")) {
            listOfHour.add(elem.text());
        }

        for (Element elem:
                lessons.select("div.lesson-room")) {
            listOfRoom.add(elem.text());
        }


    }

    public void clearLessons() {
        listOfLessons.clear();
    }


    public Group getGroup() {
        return new Group(group.getLink());
    }

    public void setGroup(Group group) throws IOException{
        clearLessons();
        this.group = group;
        getTimetableForDay();
    }

    public String getDate() {
        return date;
    }

    public List<String> getListOfLessons() throws ApiMessagesDenySendException {
        return new ArrayList<>(listOfLessons);
    }

    public List<String> getListOfHour() throws ApiMessagesDenySendException{
        return new ArrayList<>(listOfHour);
    }

    public List<String> getListOfRoom() {
        return new ArrayList<>(listOfRoom);
    }
}
