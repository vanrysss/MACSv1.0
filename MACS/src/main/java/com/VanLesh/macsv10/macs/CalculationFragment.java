package com.VanLesh.macsv10.macs;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
/**
 * Created by samvanryssegem on 2/24/14.
 */
public class CalculationFragment extends Fragment{
    private Calculation mCalculation;
    private EditText mTitleField;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mCalculation = new Calculation();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_calculation, parent, false);

        mTitleField = (EditText)v.findViewById(R.id.calculation_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int start, int before, int count) {
                mCalculation.setTitle(c.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //blank
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //blank
            }
        });
        return v;
    }
}
