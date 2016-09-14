package tymoteuszborkowsk.pl.systemapp.services;

import android.content.Context;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import tymoteuszborkowsk.pl.systemapp.dropbox.DropboxService;

public class TextFileService {

    private File coordinatesFile;
    private int actualDay = 0;

    public void createNote(Context context, String text) {
        boolean isCreated = false;
        String root = context.getFilesDir().getAbsolutePath();
        String currentTime = getCurrentTime();
        String output = currentTime + "  " + text + "\n";

        File file = new File(root + "/coordinates.txt");
        if (!file.exists()) {
            try {
                isCreated = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isCreated = true;
        }

        if (isCreated) {
            coordinatesFile = file;
            FileWriter fileWriter;
            try {

                if(isSameDay(file)){
                    fileWriter = new FileWriter(file, true);
                }else{

                    fileWriter = new FileWriter(file);
                }

                fileWriter.append(output);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void uploadNote() {
        if (coordinatesFile != null) {
            DropboxService dropboxService = new DropboxService();
            try {
                byte[] data = FileUtils.readFileToByteArray(coordinatesFile);
                dropboxService.upload(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH) + 1;
        int cYear = calendar.get(Calendar.YEAR);
        int cHour = calendar.get(Calendar.HOUR);
        int cMinute = calendar.get(Calendar.MINUTE);
        int cSecond = calendar.get(Calendar.SECOND);

        actualDay = cDay;
        return cDay + "." + cMonth + "." + cYear + " " + cHour + ":" + cMinute + ":" + cSecond;
    }

    private boolean isSameDay(File file) {
        String currentLine;
        String lastLine = "";
        int dayFromFile = 0;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            while ((currentLine = bufferedReader.readLine()) != null) {
                lastLine = currentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!lastLine.isEmpty()){
            String dayStr = lastLine.substring(0, 2);
            if (dayStr.length() == 2)
                dayFromFile = Integer.parseInt(dayStr);
        }

        return dayFromFile == actualDay;
    }

}
