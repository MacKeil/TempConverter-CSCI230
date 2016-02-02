package com.example.zack.tempconverter;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TempConverter extends Activity {

    //creating variables to hold widgets...yay
    private EditText currentTemp;
    private TextView output;
    private boolean isCelsius;
    //create a shared preferences object for state saving
    private SharedPreferences savedValues;
    //create a string to hold the currentTemp
    private String currentTempString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //all that follows to the next comment was added by the IDE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_converter);

        //Now I get to do my own code: reference all the widgets I mean to use
        currentTemp = (EditText) findViewById(R.id.editText);;
        output = (TextView) findViewById(R.id.output);

        //initialize the Shared Preferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onRadioButtonClicked(View view){
        //check if a radio button has been clicked in a way I can pass to another function
        boolean checked = ((RadioButton) view).isChecked();
        //check which button got checked and set my pass off accordingly
        switch (view.getId()){
            case R.id.celsius:
                if(checked){
                    isCelsius = true;
                }
                break;
            case R.id.fahrenheit:
                if(checked){
                    isCelsius = false;
                }
                break;
        }
    }
    public void onButtonClick(View view){
        calculateDisplay(isCelsius);
    }

    public void calculateDisplay(boolean whichOne){
        //get the user's input
        String currentTempString = currentTemp.getText().toString();
        double userInput;
        double answer;
        if(currentTempString.equals("")){
            userInput = 0;
        }
        else{
            userInput = Double.parseDouble(currentTempString);
        }
        //check which calculation we're doing and then execute
        if(whichOne){
            answer = userInput * 1.8;
            answer += 32;
        }
        else{
            answer = userInput - 32;
            answer /= 1.8;
        }

        //show what we did
        output.setText(Double.toString(answer));
    }

    public void onPause(){
        //save the instance variables
        Editor editor = savedValues.edit();
        editor.putBoolean("isCelsius", isCelsius);
        editor.putString("CurrentTemp", currentTempString);
        editor.commit();

        super.onPause();
    }

    public void onResume(){
        super.onResume();

        //retreive the instance variables
        currentTempString = savedValues.getString("CurrentTemp","");
        isCelsius = savedValues.getBoolean("isCelsius", true);

        //set the editText object to the saved value
        currentTemp.setText(currentTempString);

        //calculate so it's showing what was placed last
        calculateDisplay(isCelsius);
    }
}
