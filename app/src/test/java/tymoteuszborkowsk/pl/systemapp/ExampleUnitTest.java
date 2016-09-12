package tymoteuszborkowsk.pl.systemapp;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import tymoteuszborkowsk.pl.systemapp.dropbox.DropboxService;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isFileUploaded() throws IOException {
        DropboxService dropboxService = new DropboxService();
        File file = new File("C:\\Users\\Tymek\\Desktop\\dupa.txt");
        byte[] bytes = FileUtils.readFileToByteArray(file);

        dropboxService.upload(bytes);
    }
}