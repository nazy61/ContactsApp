package com.ighub.sqliteclass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ighub.sqliteclass.dbfiles.ContactModel;
import com.ighub.sqliteclass.dbfiles.DatabaseHandler;

public class MainActivity extends AppCompatActivity {
    
    private EditText etFname, etLname, etEmail, etPhone, etId;
    private Button btnLogin, btnGetDetails;
    private String fName, lName, email, phone;
    private int id;

    DatabaseHandler db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(getApplicationContext());
        
        etEmail = findViewById(R.id.etEmail);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etlname);
        etPhone = findViewById(R.id.etPhone);
        etId = findViewById(R.id.etId);
        btnLogin = findViewById(R.id.btnLogin);
        btnGetDetails = findViewById(R.id.btnGetDetails);
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
                proceed();
            }
        });

        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etId.getText().toString().trim().isEmpty()){
                    etId.setError("Please enter your ID");
                } else {
                    id = Integer.parseInt(etId.getText().toString().trim());
                    loadDetails();
                }
            }
        });
    }

    private void validateData() {
        if(etId.getText().toString().trim().isEmpty()){
            etId.setError("Please enter your ID");
        } else if(etFname.getText().toString().trim().isEmpty()){
            etFname.setError("Please enter your first name");
        } else if(etLname.getText().toString().trim().isEmpty()) {
            etLname.setError("Please enter your last name");
        } else if(etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Please enter your email address");
        } else if(!etEmail.getText().toString().trim().contains("@")) {
            etEmail.setError("Please enter a correct email address");
        } else if(etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Please enter your phone number");
        } else {
            fName = etFname.getText().toString().trim();
            lName = etLname.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            id = Integer.parseInt(etId.getText().toString().trim());
        }
    }

    private void proceed() {
        ContactModel model = new ContactModel(id, fName, lName, email, phone);
        db.addContact(model);
        Toast.makeText(this, "Saved successfully...", Toast.LENGTH_SHORT).show();
    }

    private void loadDetails() {
        ContactModel contact = db.getContact(id);

        if(contact != null) {
            etFname.setText(contact.getFname());
            etLname.setText(contact.getLname());
            etEmail.setText(contact.getEmail());
            etPhone.setText(contact.getPhone());
        } else {
            Toast.makeText(this, "Record with ID not found", Toast.LENGTH_SHORT).show();
        }
    }
}
