package tymoteuszborkowsk.pl.systemapp.dropbox;

import android.os.Build;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxWriteMode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class DropboxService {

    private static final String CLIENT_IDENTIFIER = "user";
    private static final String TOKEN = "_an-X2jNMzIAAAAAAAAIFbW8RdeIRbefCIfuePrPcjKpEazCXzfMCGCzZKEzC6hF";
    private static final String FILEPATH = "/Coordinates/";
    private static final String TXT_EXTENSION = ".txt";

    private final DbxClientV1 client;

    public DropboxService(){
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(CLIENT_IDENTIFIER);
        client = new DbxClientV1(dbxRequestConfig, TOKEN);
    }


    public void upload(byte[] data) throws IOException {
        String currentDate = getCurrentTime();
        String model = Build.MANUFACTURER + " " + Build.MODEL;
        String fullFilePath = FILEPATH + model + ": " + currentDate + TXT_EXTENSION;

        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            client.uploadFile(fullFilePath, DbxWriteMode.force(), data.length, inputStream);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }


    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH) + 1;
        int cYear = calendar.get(Calendar.YEAR);

        return cDay+"."+cMonth+"."+cYear;
    }

}
