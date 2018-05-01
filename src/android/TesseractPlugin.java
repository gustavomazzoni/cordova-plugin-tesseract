package com.gmazzoni.cordova;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import android.content.Context;

public class TesseractPlugin extends CordovaPlugin {
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/OCRFolder/";
    private static final String TAG = "TesseractPlugin";
    private String lang = "por";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        try {
            String language = args.getString(0);

            String result = null;
            Log.v(TAG, "Action: " + action);
            if (action.equals("recognizeText")) {
                String imageData = args.getString(1);
                result = recognizeText(imageData, language);
            } else {
                result = loadLanguage(language);
            }

            Log.v(TAG, "Result: " + result);
            this.echo(result, callbackContext);
            return true;
        } catch (Exception e) {
            Log.v(TAG, "Exception in Execute:" + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
    }


    private void echo(String result, CallbackContext callbackContext) {
        if (result != null && result.length() > 0) {
            callbackContext.success(result);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    public String recognizeText(String imageData, String language) {
        Log.v(TAG, "Starting process to recognize text in photo.");

        byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, language);
        baseApi.setImage(bitmap);

        String recognizedText = "";
        Log.v(TAG, "calling baseApi.getUTF8Text()");

        recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        // You now have the text in recognizedText var, you can do anything with it.
        Log.v(TAG, "Recognized Text: " + recognizedText);
        return recognizedText;
    }

    public String loadLanguage(String language) {
        Log.v(TAG, "Starting process to load OCR language file.");
        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "Error: Creation of directory " + path + " on sdcard failed");
                    return "Error: Creation of directory " + path + " on sdcard failed";
                } else {
                    Log.v(TAG, "Directory created " + path + " on sdcard");
                }
            }
        }

        if (language != null && language != "") {
            lang = language;
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            Log.v(TAG, "couldn't find tessdata, downloading");
            DownloadAndCopy job = new DownloadAndCopy();
            job.execute(lang);
            try {
            job.get(); //wait for download to complete
            } catch (Exception e) {
                Log.v(TAG, "download task interrupted");
                e.printStackTrace();
                return "Interrupted";
            }
        } else 
            Log.v(TAG, "Found existing tessdata");
        return "Ok";
    }


    private class DownloadAndCopy extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
            try {
                Log.v(TAG, "Downloading " + lang + ".traineddata");
                // tess two now supports tessdata 3.04, and switching to official repo 
                String stringURL = "https://github.com/tesseract-ocr/tessdata/raw/3.04.00/" + lang + ".traineddata";
                Log.v(TAG, "downloading from url " + stringURL);
                URL url = new URL(stringURL);

		InputStream input = new BufferedInputStream(url.openStream(), 8192);
                   		
			
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                byte[] buf = new byte[1024];
                int len;
                while ((len = input.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                input.close();
                out.close();

                Log.v(TAG, "Copied " + lang + ".traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Unable to copy " + lang + ".traineddata " + e.toString());
            }

            return "Copied " + lang + ".traineddata";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
            Log.v(TAG, "Download and copy done! Nothing else to do.");
        }
    }
}
