//TODO: Emailing(report), correctly use Soil and Vehicles
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 *
 * Created by samvanryssegem on 2/24/14.
 */
public class CalculationFragment extends Fragment{
    Calculation mCalculation;
    EditText mTitleField;
    EditText mEngineerName;
    EditText mJobsite;
    EditText mVehicleclass;
    EditText mVehicletype;
    EditText mVehicleHeight;
    EditText mCOGDistance;
    EditText mWeight;
    EditText mTrackL;
    EditText mTrackW;
    EditText mSoilType;
    EditText mSoilUnitWeight;
    EditText mSoilFrictionAngle;
    EditText mSoilCohesionFactor;

    Button mDateButton;
    Callbacks mCallbacks;
    Button mCalculateButton;


    private static final int REQUEST_DATE = 0;
    public static String EXTRA_CALCULATION_ID = "calculationintent.calculation_id";

    private static final String DIALOG_DATE = "date";

    public interface Callbacks{
        void onCalculationUpdated(Calculation calculation);

    }

    public static CalculationFragment newInstance(UUID calculationId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CALCULATION_ID, calculationId);
        CalculationFragment fragment = new CalculationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks =(Callbacks)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID calculationId = (UUID)getArguments().getSerializable(EXTRA_CALCULATION_ID);
        mCalculation = CalculationLab.get(getActivity()).getCalculation(calculationId);

        setHasOptionsMenu(true);
    }

    public void updateDate(){
        mDateButton.setText(mCalculation.getDate().toString());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_calculation, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                 getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTitleField = (EditText)v.findViewById(R.id.calculation_title);
        mTitleField.setText(mCalculation.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence c, int start, int before, int count) {
                //blank
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setTitle(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
                }

            public void afterTextChanged(Editable editable) {
                //blank
            }
        });

        mEngineerName = (EditText)v.findViewById(R.id.calculation_engineer);
        mEngineerName.setText(mCalculation.getEngineerName());
        mEngineerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setEngineerName(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mJobsite = (EditText)v.findViewById(R.id.calculation_jobsite);
        mJobsite.setText(mCalculation.getJobSite());
        mJobsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setJobSite(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicleclass = (EditText)v.findViewById(R.id.input_vehicle_class);
        mVehicleclass.setText(mCalculation.getVehicle().getVehicleclass());
        mVehicleclass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setVehicleclass(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicleHeight = (EditText)v.findViewById(R.id.input_vehicle_height);
        mVehicleHeight.setText(Integer.toString(mCalculation.getVehicle().getHg()));
        mVehicleHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.mVehicle.setHg(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCOGDistance = (EditText)v.findViewById(R.id.Cg);
        mCOGDistance.setText(Integer.toString(mCalculation.getVehicle().getCg()));
        mCOGDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setCg(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mWeight = (EditText)v.findViewById(R.id.Wv);
        mWeight.setText(Integer.toString(mCalculation.getVehicle().getWv()));
        mWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setWv(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTrackL = (EditText)v.findViewById(R.id.Tl);
        mTrackL.setText(Integer.toString(mCalculation.getVehicle().getTrackL()));
        mTrackL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setTrackL(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTrackW = (EditText)v.findViewById(R.id.Tw);
        mTrackW.setText(Integer.toString(mCalculation.getVehicle().getTrackW()));
        mTrackW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setTrackW(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilType = (EditText)v.findViewById(R.id.SoilName);
        mSoilType.setText(mCalculation.getSoil().getName());
        mSoilType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setName(charSequence.toString());
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilUnitWeight = (EditText)v.findViewById(R.id.Soilunitwt);
        mSoilUnitWeight.setText(Integer.toString(mCalculation.getSoil().getunitW()));
        mSoilUnitWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setunitW(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilCohesionFactor = (EditText)v.findViewById(R.id.Soilcohesion);
        mSoilCohesionFactor.setText(Integer.toString(mCalculation.getSoil().getC()));
        mSoilCohesionFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setC(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilFrictionAngle = (EditText)v.findViewById(R.id.Soilfricta);
        mSoilFrictionAngle.setText(Integer.toString(mCalculation.getSoil().getfrictA()));
        mSoilFrictionAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setfrictA(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mDateButton = (Button)v.findViewById(R.id.calculation_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCalculation.getDate());
                dialog.setTargetFragment(CalculationFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mCalculateButton = (Button)v.findViewById(R.id.calc_button);
        mCalculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });
        return v;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCalculation.setDate(date);
            mCallbacks.onCalculationUpdated(mCalculation);
            updateDate();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        CalculationLab.get(getActivity()).saveCalculations();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
