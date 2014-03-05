package com.VanLesh.macsv10.macs;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 *
 * Created by samvanryssegem on 2/24/14.
 */
public class CalculationFragment extends Fragment{
    private Calculation mCalculation;
    private EditText mTitleField;
    private CheckBox mMailCheckBox;
    private Button mDateButton;

    private static final int REQUEST_DATE = 0;
    public static String EXTRA_CALCULATION_ID = "com.VanLesh.android.calculationintent.calculation_id";
    private static final String DIALOG_DATE = "date";

    public static CalculationFragment newInstance(UUID calculationId){
                Bundle args = new Bundle();
                args.putSerializable(EXTRA_CALCULATION_ID, calculationId);

                CalculationFragment fragment = new CalculationFragment();
                fragment.setArguments(args);

                return fragment;
        }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID calculationId = (UUID)getArguments().getSerializable(EXTRA_CALCULATION_ID);

        mCalculation = CalculationLab.get(getActivity()).getCalculation(calculationId);
        setHasOptionsMenu(true);
    }


    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_calculation, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(NavUtils.getParentActivityName(getActivity())!= null)
                 getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTitleField = (EditText)v.findViewById(R.id.calculation_title);
        mTitleField.setText(mCalculation.getTitle());
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

        mDateButton = (Button)v.findViewById(R.id.calculation_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCalculation.getDate());
                dialog.setTargetFragment(CalculationFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        updateDate();

        mMailCheckBox = (CheckBox)v.findViewById(R.id.calculation_list_item_emailCheckBox);
        mMailCheckBox.setChecked(mCalculation.isDoemail());
        mMailCheckBox.setOnCheckedChangeListener( new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton button, boolean b) {
                        mCalculation.setDoemail(b);
                }
        });

        return v;

    }

    private void updateDate(){
        mDateButton.setText(mCalculation.getDate().toString());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCalculation.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_calc_list, menu);
    }

    @Override
    public void onPause(){
        super.onPause();
        CalculationLab.get(getActivity()).saveCalculations();
    }


}
