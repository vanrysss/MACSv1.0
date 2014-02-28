package com.VanLesh.macsv10.macs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by samvanryssegem on 2/27/14.
 */
public class CalculationListFragment extends ListFragment{

    private ArrayList<Calculation> mCalculations;
    private static  final String TAG = "CalculationListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.calculations_title);
        mCalculations = CalculationLab.get(getActivity()).getCalculations();

        CalculationAdapter adapter = new CalculationAdapter(mCalculations);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Calculation c = ((CalculationAdapter)getListAdapter()).getItem(position);

        //start a CalculationActivity
        Intent i = new Intent(getActivity(), CalculationPagerActivity.class);
        i.putExtra(CalculationFragment.EXTRA_CALCULATION_ID, c.getId());
        startActivity(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //if we dont have a view inflate one
        if(convertView == null){
            convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_calculation,null);
        }

        //configure view for this calculation
        Calculation c = getItem(position);

        TextView titleTextView =
                (TextView)convertView.findViewById(R.id.calculation_list_item_titleTextView);
        titleTextView.setText(c.getTitle());
        TextView dateTextView =
                (TextView)convertView.findViewById(R.id.calculation_list_item_dateTextView);
        dateTextView.SetText(c.getDate().toString());
        CheckBox emailCheckBox =
                (CheckBox)convertView.findViewById(R.id.calculation_list_item_emailCheckBox);
        emailCheckBox.setChecked(c.doemail());

        return convertView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((CalculationAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_calc_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_calc:
                Calculation calc = new Calculation();
                CalculationLab.get(getActivity()).addCalculation(calc);
                Intent i = new Intent(getActivity(), CalculationPagerActivity.class);
                i.putExtra(CalculationFragment.EXTRA_CALCULATION_ID, calc.getId());
                startActivityForResult(i,0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CalculationAdapter extends ArrayAdapter<Calculation> {

        public CalculationAdapter(ArrayList<Calculation> calculations){
            super(getActivity(), 0 ,calculations);
        }
    }

}
