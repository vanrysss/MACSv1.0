package com.VanLesh.macsv10.macs.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.VanLesh.macsv10.macs.CalculationLab;
import com.VanLesh.macsv10.macs.Models.Calculation;
import com.VanLesh.macsv10.macs.Models.GPSTracker;
import com.VanLesh.macsv10.macs.Models.Pdf;
import com.VanLesh.macsv10.macs.Models.Soil;
import com.VanLesh.macsv10.macs.Models.Vehicle;
import com.VanLesh.macsv10.macs.R;
import com.VanLesh.macsv10.macs.SoilLab;
import com.VanLesh.macsv10.macs.ToastExpander;
import com.VanLesh.macsv10.macs.VehicleLab;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by samvanryssegem on 2/24/14.
 * <p/>
 * Fragment which defines the main Form's behavior. This also handles behavior for the Vehicle and
 * Soil popups. In short if this is a new Calculation all of the fields should be empty, otherwise
 * we load. The Calculate button @OnClick actually puts everything into a Calculation object and then
 * performs the operation. This prevents a lot of nastiness associated with running a calculation
 * more than once.
 * <p/>
 * A lot of boilerplate was removed by using ButterKnife. If you're not familiar with the library
 * check it out here... https://github.com/JakeWharton/butterknife
 */

public class CalculationFragment extends Fragment {

    private static final int REQUEST_DATE = 0;
    public static final String EXTRA_CALCULATION_ID = "calculationintent.calculation_id";
    private static final String DIALOG_DATE = "date";

    private Calculation mCalculation;
    Callbacks mCallbacks;

    //InjectionView definitions needed by Butterknife
    @InjectView(R.id.calculation_title)
    EditText calculationTitle;
    @InjectView(R.id.calculation_engineer)
    EditText calculationEngineer;
    @InjectView(R.id.calculation_jobsite)
    EditText calculationJobsite;
    @InjectView(R.id.calculation_beta)
    EditText calculationBeta;
    @InjectView(R.id.calculation_theta)
    EditText calculationTheta;
    @InjectView(R.id.calculation_anchor_height)
    EditText calculationAnchorHeight;
    @InjectView(R.id.calculation_anchor_setback)
    EditText calculationAnchorSetback;
    @InjectView(R.id.calculation_blade_depth)
    EditText calculationBladeDepth;

    @InjectView(R.id.dragging_value)
    TextView dragValue;
    @InjectView(R.id.moment_value)
    TextView momentValue;

    @InjectView(R.id.toggle_units)
    ToggleButton unitsToggle;

    @InjectView(R.id.perform_calculation)
    Button performCalculation;
    @InjectView(R.id.calculation_date)
    Button calculationDate;
    @InjectView(R.id.report_button)
    Button mReportButton;

    @Optional
    @InjectView(R.id.question_db)
    ImageButton DbQuestion;
    @Optional
    @InjectView(R.id.question_la)
    ImageButton LaQuestion;
    @Optional
    @InjectView(R.id.question_ha)
    ImageButton HaQuestion;


    @Optional
    @InjectView(R.id.Ha_unit)
    TextView HaUnit;
    @Optional
    @InjectView(R.id.La_unit)
    TextView LaUnit;
    @Optional
    @InjectView(R.id.Db_unit)
    TextView DbUnit;


    @InjectView(R.id.vehicle_add)
    ImageButton addVehicle;
    @InjectView(R.id.vehicle_delete)
    ImageButton deleteVehicle;
    @InjectView(R.id.soil_add)
    ImageButton addSoil;
    @InjectView(R.id.soil_delete)
    ImageButton deleteSoil;

    @InjectView(R.id.vehicle_spinner)
    Spinner mVehicleSpin;
    @InjectView(R.id.current_latitude)
    TextView latitudeField;
    @InjectView(R.id.current_longitude)
    TextView longitudeField;


    void populateCalculation() {
        try {
            //noinspection ConstantConditions
            mCalculation.setTitle(calculationTitle.getText().toString());
            mCalculation.setEngineerName(calculationEngineer.getText().toString());
            mCalculation.setJobSite(calculationJobsite.getText().toString());
            mCalculation.setBeta(Integer.parseInt(calculationBeta.getText().toString()));
            mCalculation.setTheta(Integer.parseInt(calculationTheta.getText().toString()));
            mCalculation.setHa(Double.parseDouble(calculationAnchorHeight.getText().toString()));
            mCalculation.setLa(Double.parseDouble(calculationAnchorSetback.getText().toString()));
            mCalculation.setD_b(Double.parseDouble(calculationBladeDepth.getText().toString()));
        } catch (NumberFormatException e) {
            Toast thistoast = Toast.makeText(getActivity(), "A required field was left unpopulated", Toast.LENGTH_LONG);
            thistoast.show();
        }
        Log.i("Title", calculationTitle.toString());
    }

    double latitudeValue;
    double longitudeValue;

    GPSTracker gps;

    String answerUnits;
    boolean isimperial = false;

    private ArrayList<Vehicle> mVehicles;
    private ArrayList<Soil> mSoils;


    public interface Callbacks {
        void onCalculationUpdated(Calculation calculation);

    }

    public static CalculationFragment newInstance(UUID calculationId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CALCULATION_ID, calculationId);
        CalculationFragment fragment = new CalculationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID calculationId = (UUID) getArguments().getSerializable(EXTRA_CALCULATION_ID);
        mCalculation = CalculationLab.get(getActivity()).getCalculation(calculationId);

        mSoils = SoilLab.get(getActivity()).getSoils();
        staticSoils(mSoils);

        mVehicles = VehicleLab.get(getActivity()).getVehicles();
        staticVehicles(mVehicles);

        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            latitudeValue = gps.getLatitude();
            longitudeValue = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    void updateDate() {
        calculationDate.setText(mCalculation.getDate().toString());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //inflate our view based on the layout defined
        final View v = inflater.inflate(R.layout.fragment_calculation, parent, false);
        // inject all of those views we created earlier
        //this single line eliminates
        ButterKnife.inject(this, v);
        final Dialog soildialog = new Dialog(getActivity());
        final Dialog vehicledialog = new Dialog(getActivity());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        answerUnits = getResources().getString(R.string.metric_answer);
        latitudeField.setText(String.valueOf(latitudeValue));
        longitudeField.setText(String.valueOf(longitudeValue));
        calculationTitle.setText(mCalculation.getTitle());
        calculationEngineer.setText(mCalculation.getEngineerName());
        calculationJobsite.setText(mCalculation.getJobSite());

        if (mCalculation.getBeta() != 0)
            calculationBeta.setText(Integer.toString(mCalculation.getBeta()));

        if (mCalculation.getTheta() != 0)
            calculationTheta.setText(Integer.toString(mCalculation.getTheta()));

        if (mCalculation.getHa() != 0)
            calculationAnchorHeight.setText(Double.toString(decimalFormater(mCalculation.getHa())));

        if (mCalculation.getLa() != 0)
            calculationAnchorSetback.setText(Double.toString(decimalFormater(mCalculation.getLa())));

        if (mCalculation.getD_b() != 0)
            calculationBladeDepth.setText(Double.toString(decimalFormater(mCalculation.getD_b())));

        ToastMaker(R.string.ha_popup, HaQuestion, v);
        ToastMaker(R.string.la_popup, LaQuestion, v);
        ToastMaker(R.string.db_popup, DbQuestion, v);

        updateDate();
        calculationDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCalculation.getDate());
                dialog.setTargetFragment(CalculationFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        if (mCalculation.isimperial) {
            unitsToggle.setChecked(mCalculation.isimperial);
            HaUnit.setText(getResources().getString(R.string.imperial_distance));
            LaUnit.setText(getResources().getString(R.string.imperial_distance));
            DbUnit.setText(getResources().getString(R.string.imperial_distance));

            answerUnits = getResources().getString(R.string.imperial_answer);
            isimperial = true;
            mCalculation.isimperial = true;
            mCalculation.getVehicle().isimperial = true;
            mCalculation.getSoil().isimperial = true;
        } else {
            HaUnit.setText(getResources().getString(R.string.metric_distance));
            LaUnit.setText(getResources().getString(R.string.metric_distance));
            DbUnit.setText(getResources().getString(R.string.metric_distance));

            answerUnits = getResources().getString(R.string.metric_answer);
            mCalculation.isimperial = false;
            mCalculation.getVehicle().isimperial = false;
            mCalculation.getSoil().isimperial = false;
        }

        unitsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Calculation.resetCalculation(mCalculation);
                //Unit textfield declarations for main view
                //for the soil dialog
                // if it's checked lets set all of the textview to their imperial counterparts
                if (isChecked) {
                    HaUnit.setText(getResources().getString(R.string.imperial_distance));
                    LaUnit.setText(getResources().getString(R.string.imperial_distance));
                    DbUnit.setText(getResources().getString(R.string.imperial_distance));

                    answerUnits = getResources().getString(R.string.imperial_answer);
                    isimperial = true;
                    mCalculation.isimperial = true;
                    mCalculation.getVehicle().isimperial = true;
                    mCalculation.getSoil().isimperial = true;

                    // default behavior for the program is metric units
                } else {
                    HaUnit.setText(getResources().getString(R.string.metric_distance));
                    LaUnit.setText(getResources().getString(R.string.metric_distance));
                    DbUnit.setText(getResources().getString(R.string.metric_distance));

                    answerUnits = getResources().getString(R.string.metric_answer);
                    mCalculation.isimperial = false;
                    mCalculation.getVehicle().isimperial = false;
                    mCalculation.getSoil().isimperial = false;

                }
                // invalidating the view forces it to re-render. This will display our changes
                //without the user having to perform an action.
                v.invalidate();
            }
        });

        final ArrayList<String> vehicleoutputlist = new ArrayList<String>();
        String SingleVehicle;
        for (Vehicle vehicle : mVehicles) {

            SingleVehicle = vehicle.getVehicleType();
            if (!vehicleoutputlist.contains(SingleVehicle))
                vehicleoutputlist.add(SingleVehicle);

        }
        Collections.sort(vehicleoutputlist);

        final ArrayAdapter<String> vadapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, vehicleoutputlist);
        vadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mVehicleSpin.setAdapter(vadapter);

        //If we're loading a Calculation display the correct Vehicle in the Spinner
        if (mCalculation.getVehicle() != null) {
            String selectedVehicle = mCalculation.getVehicle().getVehicleType();
            int spinnerPosition = vadapter.getPosition(selectedVehicle);
            mVehicleSpin.setSelection(spinnerPosition);
        }


        mVehicleSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                String vcheck = (String) mVehicleSpin.getSelectedItem();
                Iterator<Vehicle> iterv = mVehicles.iterator();
                while (iterv.hasNext()) {
                    Vehicle currentv = iterv.next();
                    if (currentv.getVehicleType().matches(vcheck)) {
                        mCalculation.setVehicle(currentv);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        deleteVehicle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Vehicle checkvehicle : mVehicles) {
                    if (checkvehicle.equals(mVehicleSpin.getSelectedItem()))
                        mVehicles.remove(checkvehicle);
                }
                vadapter.remove((String) mVehicleSpin.getSelectedItem());
                vadapter.notifyDataSetChanged();
            }


        });

        addVehicle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicledialog.setContentView(R.layout.fragment_vehicle_picker);
                vehicledialog.setTitle("Enter Vehicle Data");
                final EditText mVehicleclass;
                final EditText mVehicletype;
                final EditText mVehicleHeight;
                final EditText mCOGDistance;
                final EditText mWeight;
                final EditText mTrackL;
                final EditText mTrackW;
                final EditText mBladeW;
                TextView VehicleWeight = (TextView) vehicledialog.findViewById(R.id.VehicleWeight_unit);
                TextView VehicleTrackLength = (TextView) vehicledialog.findViewById(R.id.VehicleTrackLength_unit);
                TextView VehicleTrackWidth = (TextView) vehicledialog.findViewById(R.id.VehicleTrackWidth_unit);
                TextView VehicleCg = (TextView) vehicledialog.findViewById(R.id.VehicleCg_unit);
                TextView VehicleBladeWidth = (TextView) vehicledialog.findViewById(R.id.VehicleBladeWidth_unit);
                TextView VehicleHeight = (TextView) vehicledialog.findViewById(R.id.VehicleHeight_unit);


                if (unitsToggle.isChecked()) {
                    VehicleHeight.setText(getResources().getString(R.string.imperial_distance));
                    VehicleWeight.setText(getResources().getString(R.string.imperial_weight));
                    VehicleCg.setText(getResources().getString(R.string.imperial_distance));
                    VehicleBladeWidth.setText(getResources().getString(R.string.imperial_distance));
                    VehicleTrackLength.setText(getResources().getString(R.string.imperial_distance));
                    VehicleTrackWidth.setText(getResources().getString(R.string.imperial_distance));


                } else {
                    VehicleHeight.setText(getResources().getString(R.string.metric_distance));
                    VehicleCg.setText(getResources().getString(R.string.metric_distance));
                    VehicleBladeWidth.setText(getResources().getString(R.string.metric_distance));
                    VehicleTrackLength.setText(getResources().getString(R.string.metric_distance));
                    VehicleTrackWidth.setText(getResources().getString(R.string.metric_distance));
                    VehicleWeight.setText(getResources().getString(R.string.metric_weight));


                }

                //this unfortunately long block defines all of our EditText fields
                // each EditText is mapped to it's resource Id, we check if the field needs to be filled
                // then assign the Calculation's appropriate vehicle parameter if something is entered into
                //the field
                mVehicleclass = (EditText) vehicledialog.findViewById(R.id.input_vehicle_class);
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

                mVehicletype = (EditText) vehicledialog.findViewById(R.id.input_vehicle_type);
                mVehicletype.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        mCalculation.getVehicle().setType(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }

                });

                mVehicleHeight = (EditText) vehicledialog.findViewById(R.id.input_vehicle_height);
                mVehicleHeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sVehicleHeight = mVehicleHeight.getText().toString();
                        if (!sVehicleHeight.matches(""))
                            mCalculation.getVehicle().setHg(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }

                });

                mCOGDistance = (EditText) vehicledialog.findViewById(R.id.Cg);
                mCOGDistance.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sCOGDistance = mCOGDistance.getText().toString();
                        if (!sCOGDistance.matches(""))
                            mCalculation.getVehicle().setCg(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }

                });

                mWeight = (EditText) vehicledialog.findViewById(R.id.Wv);
                mWeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sWeight = mWeight.getText().toString();
                        if (!sWeight.matches(""))
                            mCalculation.getVehicle().setWv(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mTrackL = (EditText) vehicledialog.findViewById(R.id.Tl);
                mTrackL.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sTrackLength = mTrackL.getText().toString();
                        if (!sTrackLength.matches(""))
                            mCalculation.getVehicle().setTrackL(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mTrackW = (EditText) vehicledialog.findViewById(R.id.Tw);
                mTrackW.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sTrackWidth = mTrackW.getText().toString();
                        if (!sTrackWidth.matches(""))
                            mCalculation.getVehicle().setTrackW(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mBladeW = (EditText) vehicledialog.findViewById(R.id.Wb);
                mBladeW.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sBladeWidth = mBladeW.getText().toString();
                        if (!sBladeWidth.matches("")) {
                            mCalculation.getVehicle().setBladeW(Double.parseDouble(charSequence.toString()));
                        }
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }


                    @Override
                    public void afterTextChanged(Editable editable) {
                    }

                });


                Button mCancelButton = (Button) vehicledialog.findViewById(R.id.vehicle_cancel);
                mCancelButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        mCalculation.setVehicle(null);
                        vehicledialog.dismiss();

                    }

                });

                Button mSaveButton = (Button) vehicledialog.findViewById(R.id.vehicle_save);
                mSaveButton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        mCalculation.getVehicle().convertToMetric();
                        mVehicles.add(mCalculation.getVehicle());
                        vehicleoutputlist.add(mCalculation.getVehicle().getVehicleType());
                        VehicleLab.get(getActivity()).saveVehicles();
                        vehicledialog.dismiss();

                    }
                });


                // this code block defines the "?" buttons
                //We assign them the correct ID and provide the relevant string and ID for the toast
                ImageButton HgQuestion = (ImageButton) vehicledialog.findViewById(R.id.question_hg);
                ImageButton WvQuestion = (ImageButton) vehicledialog.findViewById(R.id.question_wv);
                ImageButton CgQuestion = (ImageButton) vehicledialog.findViewById(R.id.question_cg);

                ToastMaker(R.string.hg_popup, HgQuestion, v);
                ToastMaker(R.string.wv_popup, WvQuestion, v);
                ToastMaker(R.string.cg_popup, CgQuestion, v);

                vehicledialog.show();
            }
        });

        final ArrayList<String> SoilOutputList = new ArrayList<String>();
        String SingleSoil;
        for (Soil soil : mSoils) {
            SingleSoil = soil.getName();
            if (!SoilOutputList.contains(SingleSoil))
                SoilOutputList.add(SingleSoil);
        }
        Collections.sort(SoilOutputList);

        final Spinner mSoilSpin = (Spinner) v.findViewById(R.id.soil_spinner);
        final ArrayAdapter<String> sadapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, SoilOutputList);
        sadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSoilSpin.setAdapter(sadapter);

        //If we're loading a Calculation display the correct Soil in Spinner
        if (mCalculation.getSoil() != null) {
            String selectedSoil = mCalculation.getSoil().getName();
            int spinnerPosition = vadapter.getPosition(selectedSoil);
            mSoilSpin.setSelection(spinnerPosition);
        }
        mSoilSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                String check = (String) mSoilSpin.getSelectedItem();

                Iterator<Soil> iter = mSoils.iterator();
                while (iter.hasNext()) {
                    Soil currents = iter.next();
                    if (currents.getName().matches(check))
                        mCalculation.setSoil(currents);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        deleteSoil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Soil checksoil : mSoils) {
                    if (checksoil.equals(mSoilSpin.getSelectedItem()))
                        mSoils.remove(checksoil);
                }
                sadapter.remove((String) mSoilSpin.getSelectedItem());
                sadapter.notifyDataSetChanged();
            }

        });

        //OnClick we launcha  dialog which allows the user to enter a new custom Soil
        addSoil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //grab the appropriate layout
                soildialog.setContentView(R.layout.fragment_soil_picker);
                soildialog.setTitle("Enter Soil Data");

                final EditText mSoilType;
                final EditText mSoilUnitWeight;
                final EditText mSoilFrictionAngle;
                final EditText mSoilCohesionFactor;

                //textviews to display units, we don't include the degrees because it never changes
                TextView SoilWtUnit = (TextView) soildialog.findViewById(R.id.soil_weight_unit_type);
                TextView SoilCUnit = (TextView) soildialog.findViewById(R.id.soil_cohesion_unit_type);

                //if units are set to imperial display etc etc
                if (unitsToggle.isChecked()) {
                    SoilWtUnit.setText(getResources().getString(R.string.imperial_soilunitwt));
                    SoilCUnit.setText(getResources().getString(R.string.imperial_soilc));

                } else {
                    SoilWtUnit.setText(getResources().getString(R.string.metric_soilunitwt));
                    SoilCUnit.setText(getResources().getString(R.string.metric_soilc));

                }


                mSoilType = (EditText) soildialog.findViewById(R.id.soil_name);
                mSoilType.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                        mCalculation.getSoil().setName(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mSoilUnitWeight = (EditText) soildialog.findViewById(R.id.soil_unit_weight);
                mSoilUnitWeight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sSoilUnitWeight = mSoilType.getText().toString();
                        if (!sSoilUnitWeight.matches(""))
                            mCalculation.getSoil().setunitW(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mSoilCohesionFactor = (EditText) soildialog.findViewById(R.id.soil_cohesion);
                mSoilCohesionFactor.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sSoilCohesionFactor = mSoilCohesionFactor.getText().toString();
                        if (!sSoilCohesionFactor.matches(""))
                            mCalculation.getSoil().setC(Double.parseDouble(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mSoilFrictionAngle = (EditText) soildialog.findViewById(R.id.soil_friction_angle);
                mSoilFrictionAngle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        String sSoilFrictionAngle = mSoilFrictionAngle.getText().toString();
                        if (!sSoilFrictionAngle.matches(""))
                            mCalculation.getSoil().setfrictA(Integer.parseInt(charSequence.toString()));
                        mCallbacks.onCalculationUpdated(mCalculation);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                //if they decide to cancel for whatever reason jsut remove everything
                Button mCancelButton = (Button) soildialog.findViewById(R.id.soil_cancel);
                mCancelButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        mCalculation.setSoil(null);
                        soildialog.dismiss();

                    }

                });
                //saving adds the soil to our calculation, we also convert it to metric in case it's
                //imperial.Finally we add it to our array of Soils and the Spinner so it can be selected
                Button mSaveButton = (Button) soildialog.findViewById(R.id.soil_save);
                mSaveButton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        mCalculation.getSoil().convertToMetric();
                        mSoils.add(mCalculation.getSoil());
                        SoilOutputList.add(mCalculation.getSoil().getName());
                        SoilLab.get(getActivity()).saveSoils();

                        soildialog.dismiss();

                    }
                });

                // Toasts corresponding to Questions in this popup
                ImageButton PhiQuestion = (ImageButton) soildialog.findViewById(R.id.question_fricta);
                ImageButton CohesionQuestion = (ImageButton) soildialog.findViewById(R.id.question_cohesion);
                ImageButton UnitWeightQuestion = (ImageButton) soildialog.findViewById(R.id.question_ws);

                ToastMaker(R.string.fricta_popup, PhiQuestion, v);
                ToastMaker(R.string.cohesion_popup, CohesionQuestion, v);
                ToastMaker(R.string.ws_popup, UnitWeightQuestion, v);

                soildialog.show();


            }

        });

        //Nothing is actually added to the calculation object until the user decides to perform the
        //operation itself.
        performCalculation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //output from a calc is in double form, but we don't need that much accuracy
                DecimalFormat df = new DecimalFormat("#");
                //method call which fetches all of the input data and places it in our
                //calculation object
                populateCalculation();
                //convert from imperial if needed
                mCalculation.imperialconversion();
                double anchcap = mCalculation.AnchorCapacity(isimperial);
                double moment = mCalculation.MomentCalc(isimperial);
                dragValue.setText(df.format(anchcap) + "     " + answerUnits);
                momentValue.setText(df.format(moment) + "     " + answerUnits);
                dragValue.setTextColor(Color.BLACK);
                momentValue.setTextColor(Color.BLACK);

                if (anchcap <= moment) {
                    dragValue.setTextColor(Color.RED);
                    dragValue.setHighlightColor(Color.YELLOW);
                } else {
                    momentValue.setTextColor(Color.RED);
                    momentValue.setHighlightColor(Color.YELLOW);
                }

                CalculationLab.get(getActivity()).saveCalculations();
                VehicleLab.get(getActivity()).saveVehicles();
                SoilLab.get(getActivity()).saveSoils();

            }

        });
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Pdf.maker(mCalculation);
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                     new String[]{});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MACS Report");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This email sent by the MACS App");
                // Here my file name is shortcuts.pdf which i have stored in /res/raw folder
                Uri emailUri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Report.pdf");
                emailIntent.putExtra(Intent.EXTRA_STREAM, emailUri);
                emailIntent.setType("application/pdf");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            }
        });

        setRetainInstance(true);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCalculation.setDate(date);
            mCallbacks.onCalculationUpdated(mCalculation);
            updateDate();
        }


    }

    // A Simple toast function, displays text by resource Id depending on what imagebutton was clicked
    void ToastMaker(final int display, ImageButton button, View view) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast thistoast = Toast.makeText(getActivity(), getResources().getString(display), Toast.LENGTH_LONG);
                //calls the class which allows us to display toasts for longer than 3.5 seconds
                ToastExpander.showFor(thistoast, 5000);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void staticVehicles(ArrayList<Vehicle> v) {

        Vehicle ex1 = new Vehicle();
        ex1.setVehicleClass("Bulldozer");
        ex1.setType("Caterpillar D8");
        ex1.setWv(36287.39);
        ex1.setBladeW(3.93);
        ex1.setCg(3.45);
        ex1.setTrackL(3.2);
        ex1.setTrackW(0.55);

        Vehicle ex2 = new Vehicle();
        ex2.setVehicleClass("Bulldozer");
        ex2.setType("Caterpillar D7");
        ex2.setWv(18143.7);
        ex2.setBladeW(3.9);
        ex2.setCg(3.14);
        ex2.setTrackL(2.87);
        ex2.setTrackW(0.55);

        Vehicle ex3 = new Vehicle();
        ex3.setVehicleClass("Bulldozer");
        ex3.setType("Caterpillar D6");
        ex3.setWv(24494.0);
        ex3.setBladeW(3.35);
        ex3.setCg(2.65);
        ex3.setTrackL(2.62);
        ex3.setTrackW(0.6);

        Vehicle ex4 = new Vehicle();
        ex4.setVehicleClass("Bulldozer");
        ex4.setType("CAT 320- Drawbar Attachment w/ Embedded ");
        ex4.setWv(21318.8);
        ex4.setBladeW(0.91);
        ex4.setCg(6.7);
        ex4.setTrackL(3.26);
        ex4.setTrackW(0.56);

        Vehicle ex5 = new Vehicle();
        ex5.setVehicleClass("Bulldozer");
        ex5.setType("CAT 320- Elbow Attachment w/ Embedded ");
        ex5.setWv(21318.8);
        ex5.setBladeW(0.91);
        ex5.setCg(6.7);
        ex5.setTrackL(3.26);
        ex5.setTrackW(0.56);

        Vehicle ex6 = new Vehicle();
        ex6.setVehicleClass("Bulldozer");
        ex6.setType("CAT 330- Drawbar Attachment w/ Embedded ");
        ex6.setWv(35108.0);
        ex6.setBladeW(0.91);
        ex6.setCg(6.7);
        ex6.setTrackL(4.05);
        ex6.setTrackW(0.86);

        Vehicle ex7 = new Vehicle();
        ex7.setVehicleClass("Bulldozer");
        ex7.setType("CAT 330- Elbow Attachment w/ Embedded ");
        ex7.setWv(35108.0);
        ex7.setBladeW(0.91);
        ex7.setCg(6.7);
        ex7.setTrackL(4.05);
        ex7.setTrackW(0.86);

        if (!v.contains(ex1))
            v.add(ex1);
        if (!v.contains(ex2))
            v.add(ex2);
        if (!v.contains(ex3))
            v.add(ex3);
        if (!v.contains(ex4))
            v.add(ex4);
        if (!v.contains(ex5))
            v.add(ex5);
        if (!v.contains(ex6))
            v.add(ex6);
        if (!v.contains(ex7))
            v.add(ex7);


    }

    private void staticSoils(ArrayList<Soil> s) {

        Soil a = new Soil();
        a.setC(0);
        a.setfrictA(25);
        a.setunitW(1522);
        a.setName("Uncompacted Loose Silt/Sand/Gravel");

        Soil b = new Soil();
        b.setC(0);
        b.setfrictA(30);
        b.setunitW(1762);
        b.setName("Uncompacted Lightly Compacted Silt/Sand/Gravel");

        Soil c = new Soil();
        c.setC(0);
        c.setfrictA(35);
        c.setunitW(2082);
        c.setName("Dense Compacted Silt/Sand/Gravel");

        Soil d = new Soil();
        d.setC(23.9);
        d.setfrictA(0);
        d.setunitW(1522);
        d.setName("Soft Clay");

        Soil e = new Soil();
        e.setC(47.88);
        e.setfrictA(0);
        e.setunitW(1522);
        e.setName("Firm Clay");

        Soil f = new Soil();
        f.setC(95.76);
        f.setfrictA(0);
        f.setunitW(1522);
        f.setName("Stiff Clay");


        Soil g = new Soil();
        g.setC(143.64);
        g.setfrictA(0);
        g.setunitW(1522);
        g.setName("Very Stiff Clay");

        Soil h = new Soil();
        h.setC(191.52);
        h.setfrictA(0);
        h.setunitW(1522);
        h.setName("Hard Clay");

        if (!s.contains(a))
            s.add(a);
        if (!s.contains(b))
            s.add(b);
        if (!s.contains(c))
            s.add(c);
        if (!s.contains(d))
            s.add(d);
        if (!s.contains(e))
            s.add(e);
        if (!s.contains(f))
            s.add(f);
        if (!s.contains(g))
            s.add(g);
        if (!s.contains(h))
            s.add(h);

    }

    Double decimalFormater(double parameter){
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(parameter));
    }

}
