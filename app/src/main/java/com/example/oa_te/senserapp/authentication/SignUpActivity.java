package com.example.oa_te.senserapp.authentication;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.example.oa_te.senserapp.models.Patient;
import com.example.oa_te.senserapp.models.UserAccounts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[1-99])" +         //at least 1 digit
                    "(!(?=.*[a-zA-Z]))" +      //any letter
                    "$");


    private EditText Email, Password, Confirm, Name, Age, Phone;
    private Spinner Sex;
    private Button Signup,close;
    private DatabaseReference checknameref;
    private FirebaseAuth mAuth;
    boolean namechecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checknameref = FirebaseDatabase.getInstance().getReference();
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Confirm = findViewById(R.id.etConfirmPassword);
        Name = findViewById(R.id.etNameSensor);
        Age = findViewById(R.id.etAge);
        Sex = findViewById(R.id.etSpinnerSex);
        Phone = findViewById(R.id.etPhone);
        Signup = findViewById(R.id.btnSignup);
        close = findViewById(R.id.btnRgistClose);
        mAuth = FirebaseAuth.getInstance();
        Age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
                if (!validateEmail() | !validatePassword() | !validatAge() | !validatePhone() | !validateName()) {
                    return;
                } else {
                    String email = Email.getText().toString().trim();
                    String password = Password.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String getsensorauth = "";
                                            String patientname = Name.getText().toString();
                                            int age = Integer.valueOf(Age.getText().toString());
                                            String sex = Sex.getSelectedItem().toString();
                                            final String phone = Phone.getText().toString();
                                            String idSensor = getsensorauth;
                                            String usertype = "ผู้ป่วย";
                                            String username = patientname;
                                            UserAccounts userAccounts = new UserAccounts(username, usertype);
                                            FirebaseDatabase.getInstance().getReference("UserAccounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userAccounts);
                                            Patient patient = new Patient(patientname, age, sex, phone, idSensor,usertype);
                                            FirebaseDatabase.getInstance().getReference("Patients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Email.setText("");
                                                        Password.setText("");
                                                        Confirm.setText("");
                                                        Name.setText("");
                                                        Age.setText("");
                                                        Phone.setText("");
                                                        Toast.makeText(SignUpActivity.this, "คุณได้ทำการสมัครสำเร็จ", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
    });



    close.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
                }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
        }
    }

    private void check(){
        final String name = Name.getText().toString().trim();
        checknameref.child("UserAccounts").orderByChild("usersname").equalTo(name).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            namechecked = false;
                            Name.setError("ชื่อของท่านมีผู้ใช้งานอื่นใช้แล้ว");
                            Name.requestFocus();

                            return;
                        } else {
                            namechecked = true;
                            Toast.makeText(SignUpActivity.this, "ชื่อของท่านสามารถใช้งานได้ กรุณากดยืนยันอีกรอบ", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
    }

    private boolean validateEmail() {
        String email = Email.getText().toString().trim();
        if (email.isEmpty()) {
            Email.setError(getString(R.string.input_error_email));
            Email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError(getString(R.string.input_error_email_invalid));
            Email.requestFocus();
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = Password.getText().toString().trim();
        String confirm = Confirm.getText().toString().trim();
        if (password.isEmpty()) {
            Password.setError(getString(R.string.input_error_password));
            Password.requestFocus();
            return false;
        }else if (NUMBER_PATTERN.matcher(password).matches()) {
                Password.setError(getString(R.string.input_error_password));
                Password.requestFocus();
                return false;
        } else if (password.length()< 6 || password.length() > 13) {
            Password.setError(getString(R.string.input_error_email_invalid));
            Password.requestFocus();
            return false;
        } else if (confirm.isEmpty()) {
            Confirm.setError(getString(R.string.input_error_confirm));
            Confirm.requestFocus();
            return false;
        } else if (!(confirm.equals(password))) {
            Confirm.setError(getString(R.string.input_error_confirm));
            Confirm.requestFocus();
            return false;
        } else {
            Password.setError(null);
            Confirm.setError(null);
            return true;
        }
    }


    private boolean validateName() {
        String name = Name.getText().toString().trim();
        if (name.isEmpty()) {
            Name.setError("กรุณากรอกชื่อของท่าน");
            Name.requestFocus();
            return false;
        }else if (namechecked != true) {
            return false;
        } else {
            Name.setError(null);
            return true;
        }
    }

    private boolean validatAge() {
        String zero ="0";
        String age = Age.getText().toString().trim();
        if (age.isEmpty()) {
            Age.setError("กรุณากรอกอายุของท่าน");
            Age.requestFocus();
            return false;
        } else if (age.length() > 2 |age.equals(zero) ) {
            Age.setError("กรุณาใส่ค่าอายุของ่ทานที่ไม่น้อยกว่า 1 และไม่มากกว่า 99");
            Age.requestFocus();
            return false;
        } else {
            Age.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = Phone.getText().toString().trim();
        if (phone.isEmpty()) {
            Phone.setError("กรุณากรอกเบอร์โทรศัพท์ของท่าน");
            Phone.requestFocus();
            return false;
        } else if (phone.length() != 10) {
            Phone.setError("กรุณากรอกเบอร์โทรศัพท์ของท่านควรมีความยาว 10 ตัวเลข");
            Phone.requestFocus();
            return false;
        } else {
            Phone.setError(null);
            return true;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        Age.setText(date);
    }
}

