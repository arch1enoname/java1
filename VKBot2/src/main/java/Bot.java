import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiMessagesDenySendException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class Bot {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException, IOException, ApiMessagesDenySendException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        GroupActor actor = new GroupActor(204589572, "vk1.a.e-x6aEGaJVh_bYnOxNoC4vlU2pa7u8T4gN2Tmnq42pSS3kyjx4Wg9GyUtw42rzaeX7qkWyro8lCx_xuTbRNV33KdM4Y1LoRl_S8Ick83BCbZxmU_4cUSpdrKn0A_GTfEwRMo9xORK-z7VJsjQYe5uykjZ4pJNxwdhhGFP8v30T6eRx_XjsfjpkkNT9IsuKvJeCGqCqSOO2nURwdLw7ZmOg");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        Calendar calendar = new GregorianCalendar();






        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();

        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("/restart").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("/rasp").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));

        allKey.add(line1);
        keyboard.setButtons(allKey);


        List<User> userList = new ArrayList<>();
        Timetable timetable = new Timetable();
        Group group = new Group("https://rasp.sstu.ru/rasp/group/141");

        while(true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();

            if (!messages.isEmpty()) {
                messages.forEach(message -> {

                    try {

                        if(message.getText().equals("/restart") || message.getText().equals("Начать")) {
                            vk.messages().send(actor).message("Дай ссылку на свою группу").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        }

                        if (message.getText().contains("https://rasp.sstu.ru/rasp")) {
                            group.setLink(message.getText());
                            Student student = new Student(message.getFromId(), group);
                            timetable.setGroup(group);
                            vk.messages().send(actor).message("Готово").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        }

                        if(message.getText().equals("/rasp")) {
                            for (int i = 0; i < timetable.getListOfLessons().size(); i++) {
                                vk.messages().send(actor).message(timetable.getListOfHour().get(i) + timetable.getListOfLessons().get(i) + " " + timetable.getListOfRoom().get(i)).userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                            }
                        }


                    } catch (ApiException | ClientException | IOException e){
                        e.printStackTrace();
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
