package tymoteuszborkowsk.pl.systemapp.services;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class TextFileService {

    public void createNote(Context context, String text){
        String root = context.getFilesDir().getAbsolutePath();
        String currentTime = getCurrentTime();



        File file = new File(root + "/system.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(file.getAbsolutePath());

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(currentTime + "  " + text + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getCurrentTime(){
        Calendar calander = Calendar.getInstance();
        int cDay = calander.get(Calendar.DAY_OF_MONTH);
        int cMonth = calander.get(Calendar.MONTH) + 1;
        int cYear = calander.get(Calendar.YEAR);
        int cHour = calander.get(Calendar.HOUR);
        int cMinute = calander.get(Calendar.MINUTE);
        int cSecond = calander.get(Calendar.SECOND);

        return cDay+"."+cMonth+"."+cYear+"-"+cHour+":"+cMinute+":"+cSecond;
    }

}
