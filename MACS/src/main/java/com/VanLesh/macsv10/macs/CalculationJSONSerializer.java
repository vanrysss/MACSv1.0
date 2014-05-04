package com.VanLesh.macsv10.macs;

import android.content.Context;

import com.VanLesh.macsv10.macs.Models.Calculation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by samvanryssegem on 2/28/14.
 */
class CalculationJSONSerializer {

    private final Context mContext;
    private final String mFilename;

    public CalculationJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Calculation> loadCalculations() throws IOException, JSONException {
        ArrayList<Calculation> calculations = new ArrayList<Calculation>();
        BufferedReader reader = null;

        try {
            //open and read into a string builder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // line breaks don't matter
                jsonString.append(line);
            }
            // parse using a tokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of calculations
            for (int i = 0; i < array.length(); i++) {
                calculations.add(new Calculation(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //ignore when starting fresh
        } finally {
            if (reader != null) {
                reader.close();
            }

        }
        return calculations;
    }

    public void saveCalculations(ArrayList<Calculation> calculations)
                                                                                        throws JSONException, IOException {

        //build JSON array
        JSONArray array = new JSONArray();
        for (Calculation c : calculations) {
            array.put(c.toJSON());
        }
        //write to HDD
        Writer writer = null;

        try {
            OutputStream out = mContext
                                                                                                .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
