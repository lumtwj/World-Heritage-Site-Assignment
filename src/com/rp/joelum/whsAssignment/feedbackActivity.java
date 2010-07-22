package com.rp.joelum.whsAssignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class feedbackActivity extends Activity implements TextWatcher {
	Spinner rating;
	TextView dispWHSName;
	EditText feedback;
	EditText email;
	EditText name;
	Button submit;
	Button cancel;
	String getName;
	double getLat;
	double getLng;
	ArrayAdapter<?> adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        
        rating = (Spinner)findViewById(R.id.rating);
        cancel = (Button)findViewById(R.id.cancelBtn);
        submit = (Button)findViewById(R.id.submitBtn);
        dispWHSName = (TextView)findViewById(R.id.whsNameLbl);
        feedback = (EditText)findViewById(R.id.feedbackTxt);
        email = (EditText)findViewById(R.id.emailTxt);
        name = (EditText)findViewById(R.id.nameTxt);
        
        feedback.addTextChangedListener(this);
        email.addTextChangedListener(this);
        name.addTextChangedListener(this);
        
        //Disable submit button by default and enable it when the requirement are met
        submit.setEnabled(false);
        
        getDataFromPrevAct();
        
        //Display the WHSite name in the current activity
        dispWHSName.setText("World Heritage Site:\n" + getName + "\n");
        
        adapter = ArrayAdapter.createFromResource(this, R.array.rating, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(adapter);
        
        cancel.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		//Return to the previous activity
        		toNewActivity();
        	}
        });
        
        submit.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
                try {                	
                	//Sending of feedback to the email address provided
                	GmailSender sender = new GmailSender("your@gmail.com", "yourgmailpassword");
                    sender.sendMail("Feedback on WHS Site",   
                    		"Hi " + name.getText().toString() + ",\n\n" + 
                            "We have received your feedback for the following WHS Site: \n" + getName +
                            "\n\nHere is your feedback: \n" + feedback.getText().toString() +
                            "\n\nYou rated it " + adapter.getItem(rating.getSelectedItemPosition()) + " star(s)" +
                            "\n\nRegards, \nWHS Team",   
                            "your@gmail.com",   
                            email.getText().toString()
                    );
                } catch (Exception e) {   
                	//Log for error in sending mail
                	Log.d("Send Mail", e.getMessage(), e);
                }
                
                AlertDialog.Builder alertbox = new AlertDialog.Builder(feedbackActivity.this);

                alertbox.setMessage("Feedback successful..!");

                alertbox.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                		toNewActivity();
                    }
                 });

                alertbox.show();
        	}
        });
    }
    
    public void getDataFromPrevAct() {
    	//Retrieve stored data from previous activity
    	getName = getIntent().getStringExtra("whsName");
    	getLat = getIntent().getDoubleExtra("latitude", 0);
    	getLng = getIntent().getDoubleExtra("longitude", 0);
    }
    
    public void toNewActivity() {
    	//Store data to be transferred to the next activity (mapActivity.class) and start the new activity
    	Intent intentNewAct = new Intent(getBaseContext(), whsAssignment.class);
    	
    	startActivity(intentNewAct);
    }

	@Override
	public void afterTextChanged(Editable s) {
		//Validate the EditText by ensuring the necessary fields are entered and email are in correct format
		String feedbackCheck = feedback.getText().toString();
		String emailCheck = email.getText().toString();
		String nameCheck = email.getText().toString();
		if(feedbackCheck.equals("") || emailCheck.equals("") || nameCheck.equals("")){
			submit.setEnabled(false);
			
		} 
		else {
			if(nameCheck.matches("(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*")) {
				submit.setEnabled(true);
			}
			else {
				//do nothing
			}

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}
