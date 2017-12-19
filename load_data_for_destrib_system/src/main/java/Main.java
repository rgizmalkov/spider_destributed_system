import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        int data_size = 1000;
        for (int i = 0; i <data_size; i++) {
            try {
                Unirest.post("http://localhost:8080/master/write")
                        .header("accept", "application/json")
                        .header("content-type", "application/json")
                               .body(
                                String.format("{\"uid\":\"%s\",\"json\":\"{~~~~~VALUE[%s]~~~~~\"}",
                                        UUID.randomUUID().toString(), i

                                ))
                        .asJson();
                if(i%100 == 0){
                    Thread.sleep(500);
                }
            } catch (UnirestException e) {
                /*NuN*/
            }
        }
    }
}
