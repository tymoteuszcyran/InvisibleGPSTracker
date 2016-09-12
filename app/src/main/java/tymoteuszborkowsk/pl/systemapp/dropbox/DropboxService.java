package tymoteuszborkowsk.pl.systemapp.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxWriteMode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class DropboxService {

    private static final String CLIENT_IDENTIFIER = "tborkovski";
    private static final String PASSWORD = "charmander1907";
    private static final String TOKEN = "_an-X2jNMzIAAAAAAAAIAr_lbMBOO1CGXvafegKPZU-eKkxbdMTQ1NOAiCIJAn1B";
    private static final String FILEPATH = "/Coordinates/";
    private static final String TXT_EXTENSION = ".txt";

    private final DbxClientV1 client;

    public DropboxService(){
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(CLIENT_IDENTIFIER);
        client = new DbxClientV1(dbxRequestConfig, TOKEN);
    }


    public void upload(byte[] data) throws IOException {
        String currentTime = getCurrentTime();
        String fullFilePath = FILEPATH + currentTime + TXT_EXTENSION;

        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            client.uploadFile(fullFilePath, DbxWriteMode.add(), data.length, inputStream);
        } catch (DbxException e) {
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

        return cDay+"."+cMonth+"."+cYear+" "+cHour+":"+cMinute+":"+cSecond;
    }

}
