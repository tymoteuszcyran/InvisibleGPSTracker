package tymoteuszborkowsk.pl.systemapp.services;

import android.content.Context;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import tymoteuszborkowsk.pl.systemapp.dropbox.DropboxService;

public class TextFileService {

    private File coordinatesFile;

    public void createNote(Context context, String text){
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
        }else{
            isCreated = true;
        }

        if(isCreated){
            coordinatesFile = file;
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.append(output);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void uploadNote(){
        if(coordinatesFile != null) {
            DropboxService dropboxService = new DropboxService();
            try {
                byte[] data = FileUtils.readFileToByteArray(coordinatesFile);
                dropboxService.upload(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
