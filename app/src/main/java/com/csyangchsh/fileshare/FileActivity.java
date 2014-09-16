package com.csyangchsh.fileshare;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.csyangchsh.fileshare.util.DefaultFileFilter;
import com.csyangchsh.fileshare.util.MiniFileServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author csyangchsh
 * 2014-08-28
 */
public class FileActivity extends ListActivity {

    //SD card
    private File sd;
    private FileListAdapter adapter;
    // current dir
    private File current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        //Check SD card
        if (Environment.getExternalStorageState().equals((Environment.MEDIA_MOUNTED))){
            sd = Environment.getExternalStorageDirectory();
        } else {
            Toast.makeText(getApplicationContext(), R.string.sd_toast, Toast.LENGTH_LONG).show();
        }
        //init list
        setListAdapter(initAdapter());
        current = sd;
    }

    private FileListAdapter initAdapter() {
        if(this.adapter==null) {
            adapter=new FileListAdapter(this, buildFileList(sd), this.getListView());
        }
        return adapter;
    }

    private ArrayList<File> buildFileList(File dir) {
        ArrayList<File> list = new ArrayList<File>();
        list.addAll(Arrays.asList(dir.listFiles(new DefaultFileFilter())));
        return list;
    }

    private void updateList(File dir) {
        adapter.clear();
        adapter.addAll(buildFileList(dir));
        adapter.notifyDataSetChanged();
        current = dir;
        setTitle(current.getName());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File item = (File) l.getItemAtPosition(position);
        if (item.isDirectory()) {
            updateList(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    public void actionClick(MenuItem menu) {
        if (menu.getItemId() == R.id.menu_back) {
           String currentPath = current.getAbsolutePath();
           if (currentPath.equals(sd.getAbsolutePath())) {
               return;
           }
           File parent = new File(current.getParent());
           updateList(parent);
        } else if (menu.getItemId() == R.id.menu_share) {

           //MiniFileServer server = new MiniFileServer(13131, current);
           //new Thread(server,"Server").start();
           //Toast.makeText(getApplicationContext(), FSApplication.getIPAddress(), Toast.LENGTH_LONG).show();
        }
    }
}
