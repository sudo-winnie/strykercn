package com.zalexdev.stryker.utils;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Checks if a directory exists on the device
 */
public class CheckDir extends AsyncTask<Void, String, Boolean> {


    public String path;

    public CheckDir(String path1) {
        path = path1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @SuppressLint("WrongThread")
    @Override
    protected Boolean doInBackground(Void... command) {
        String line;
        boolean result = false;
        Logger logger = new Logger();

        try {

            Process process = Runtime.getRuntime().exec("su -mm");
            OutputStream stdin = process.getOutputStream();
            InputStream stderr = process.getErrorStream();
            InputStream stdout = process.getInputStream();
            stdin.write(("[ -d " + path + " ] && echo true || echo false" + '\n').getBytes());
            logger.writeLine("Checking dir "+path,1);
            stdin.write(("exit\n").getBytes());
            stdin.flush();
            stdin.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while ((line = br.readLine()) != null) {
                logger.writeLine(line,2);
                if (line.contains("true")) {
                    result = true;
                }
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(stderr));
            while ((line = br.readLine()) != null) {
                logger.writeLine(line,3);
                onProgressUpdate(line);
            }
            br.close();
            process.waitFor();
            process.destroy();
        } catch (IOException e) {
            Log.d("Debug: ", "An IOException was caught: " + e.getMessage());
        } catch (InterruptedException ex) {
            Log.d("Debug: ", "An InterruptedException was caught: " + ex.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }


}
