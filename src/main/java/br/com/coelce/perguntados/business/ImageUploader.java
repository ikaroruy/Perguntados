/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.coelce.perguntados.business;

/**
 *
 * @author dunkelheit
 */
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author dunkelheit
 */
public class ImageUploader implements Receiver, Upload.StartedListener, Upload.ProgressListener,
        Upload.FailedListener, Upload.SucceededListener,
        Upload.FinishedListener {

    private File file;
    private final Embedded image;
    private final ProgressBar progressBar;

    public ImageUploader(Embedded image, ProgressBar progressBar) {
        this.image = image;
        this.progressBar = progressBar;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null;
        try {
            // Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos;
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        if (progressBar == null) {
            return;
        }
        progressBar.setValue(0f);
        progressBar.setVisible(true);
        UI.getCurrent().setPollInterval(500);
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (progressBar == null) {
            return;
        }
        progressBar.setValue(readBytes / (float) contentLength);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (image == null) {
            return;
        }
        image.setVisible(true);
        image.setSource(new FileResource(file));
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        if (progressBar == null) {
            return;
        }
        progressBar.setVisible(false);
    }

}