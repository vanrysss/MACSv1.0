//TODO: Emailing(report), correctly use Soil and Vehicles
package com.VanLesh.macsv10.macs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import java.text.DecimalFormat;
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
    EditText mBladeW;

    EditText mSoilType;
    EditText mSoilUnitWeight;
    EditText mSoilFrictionAngle;
    EditText mSoilCohesionFactor;

    EditText mSlopeAngle;
    EditText mAnchorAngle;
    EditText mAnchorHeight;
    EditText mAnchorSetback;
    EditText mBladeEmbedment;

    TextView mAnchorCapacity;
    TextView mRollOver;
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
        mVehicleclass.setText(mCalculation.getVehicle().getVehicleClass());
        mVehicleclass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setVehicleClass(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicletype = (EditText)v.findViewById(R.id.input_vehicle_type);
        mVehicletype.setText(mCalculation.getVehicle().getVehicleType());
        mVehicletype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setVehicleClass(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mVehicleHeight = (EditText)v.findViewById(R.id.input_vehicle_height);
        if (mCalculation.getVehicle().getHg() != 0)
            mVehicleHeight.setText(Double.toString(mCalculation.getVehicle().getHg()));

        mVehicleHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.mVehicle.setHg(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCOGDistance = (EditText)v.findViewById(R.id.Cg);
        if(mCalculation.getVehicle().getCg() != 0)
             mCOGDistance.setText(Double.toString(mCalculation.getVehicle().getCg()));

        mCOGDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setCg(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mWeight = (EditText)v.findViewById(R.id.Wv);
        if(mCalculation.getVehicle().getWv() != 0)
            mWeight.setText(Double.toString(mCalculation.getVehicle().getWv()));
        mWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setWv(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTrackL = (EditText)v.findViewById(R.id.Tl);
        if(mCalculation.getVehicle().getTrackL() !=0)
            mTrackL.setText(Double.toString(mCalculation.getVehicle().getTrackL()));
        mTrackL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setTrackL(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTrackW = (EditText)v.findViewById(R.id.Tw);
        if(mCalculation.getVehicle().getTrackW() != 0)
            mTrackW.setText(Double.toString(mCalculation.getVehicle().getTrackW()));
        mTrackW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setTrackW(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBladeW =(EditText)v.findViewById(R.id.Wb);
        if (mCalculation.getVehicle().getBladeW() !=0)
            mBladeW.setText(Double.toString(mCalculation.getVehicle().getBladeW()));
        mBladeW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getVehicle().setBladeW(Double.parseDouble(charSequence.toString()));
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
        if (mCalculation.getSoil().getunitW() != 0)
            mSoilUnitWeight.setText(Double.toString(mCalculation.getSoil().getunitW()));
        mSoilUnitWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setunitW(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilCohesionFactor = (EditText)v.findViewById(R.id.Soilcohesion);
        if (mCalculation.getSoil().getC() !=0 )
             mSoilCohesionFactor.setText(Double.toString(mCalculation.getSoil().getC()));
        mSoilCohesionFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.getSoil().setC(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSoilFrictionAngle = (EditText)v.findViewById(R.id.Soilfricta);
        if (mCalculation.getSoil().getfrictA() != 0)
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

        mSlopeAngle = (EditText)v.findViewById(R.id.Beta);
        if (mCalculation.getBeta() != 0)
            mSlopeAngle.setText(Integer.toString(mCalculation.getBeta()));
        mSlopeAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setBeta(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorAngle = (EditText)v.findViewById(R.id.Theta);
        if (mCalculation.getTheta() != 0)
            mAnchorAngle.setText(Integer.toString(mCalculation.getTheta()));

        mAnchorAngle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setTheta(Integer.parseInt(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorHeight = (EditText)v.findViewById(R.id.Ha);
        if (mCalculation.getHa() !=0)
            mAnchorHeight.setText(Double.toString(mCalculation.getHa()));

        mAnchorHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setHa(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAnchorSetback = (EditText)v.findViewById(R.id.La);
        if (mCalculation.getLa() != 0)
            mAnchorSetback.setText(Double.toString(mCalculation.getLa()));

        mAnchorSetback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setLa(Double.parseDouble(charSequence.toString()));
                mCallbacks.onCalculationUpdated(mCalculation);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBladeEmbedment = (EditText)v.findViewById(R.id.Db);
        if (mCalculation.getD_b() != 0)
            mBladeEmbedment.setText(Double.toString(mCalculation.getD_b()));

        mBladeEmbedment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCalculation.setD_b(Double.parseDouble(charSequence.toString()));
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
        mAnchorCapacity = (TextView)v.findViewById(R.id.slide_answer);
        mRollOver = (TextView)v.findViewById(R.id.roll_answer);
        mCalculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                double A1 = mCalculation.Alpha1();
                double A2 = mCalculation.Alpha2();
                DecimalFormat df = new DecimalFormat("#.##");

                double Pp =mCalculation.Pp(mCalculation.getVehicle(),mCalculation.getSoil());
                double AnchCap = mCalculation.anchor_capacity(A1,A2,Pp);
                double tipover = mCalculation.tip_over_moment(mCalculation.getVehicle(), Pp);
                mAnchorCapacity.setText(df.format(AnchCap));
                mRollOver.setText(df.format(tipover));

                if (AnchCap >= tipover) {
                    mAnchorCapacity.setTextColor(Color.RED);
                    mAnchorCapacity.setHighlightColor(Color.YELLOW);
                    }
                else {
                    mRollOver.setTextColor(Color.RED);
                    mRollOver.setHighlightColor(Color.YELLOW);
                }

            }

        });


        setRetainInstance(true);
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
