package com.example.android.tipcalcu;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android.tipcalcu.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView lblCost,lblQService,lblTotalcost;
    EditText txtCosts;
    RadioButton radioButton1,radioButton2,radioButton3;
    Button btnCalculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblCost=(TextView )findViewById(R.id.lblCost);
          txtCosts=(EditText)findViewById(R.id.txtCosts);
         lblQService=(TextView)findViewById(R.id.lblQService);
         lblTotalcost=(TextView )findViewById(R.id.lblTotalcost);
         radioButton1=(RadioButton)findViewById(R.id.radioButton1);
         radioButton2=(RadioButton)findViewById(R.id.radioButton2);
         radioButton3 =(RadioButton)findViewById(R.id.radioButton3);
         btnCalculate=(Button)findViewById(R.id.btnCalculate);}

    public void calculate(android.view.View parameter){
        int CostValue=Integer.valueOf(txtCosts.getText().toString());
        if(radioButton1.isChecked())
        {
            double rv= CostValue*1.15; lblTotalcost.setText("The total cost is:"+ String.valueOf(rv));}
         else if(radioButton2.isChecked())
         {
             double rv= CostValue*1.25; lblTotalcost.setText("The total cost is:"+ String.valueOf(rv));}
         else if(radioButton3.isChecked())
         {
             double rv= CostValue*1.5; lblTotalcost.setText("The total cost is:"+ String.valueOf(rv));}
    }
}

