package com.csyangchsh.fileshare;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author csyangchsh
 * 2014-08-28
 */
public class FileService extends Service {
    public FileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
