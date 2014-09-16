package com.csyangchsh.fileshare;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.csyangchsh.fileshare.util.TempFileFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author csyangchsh
 * 2014-08-28
 */
public class FSApplication extends Application{

    public static TempFileFactory fileFactory;
    private static ConnectivityManager connManager;
    private static WifiManager wifiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        fileFactory = new TempFileFactory();
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        fileFactory.destroy();
    }

    public static boolean isWifi() {
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    public static String getIPAddress() {
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String ip = Formatter.formatIpAddress(ipAddress);
        return ip;
    }

}
