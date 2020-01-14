package com.example.oa_te.senserapp.authentication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oa_te.senserapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[1-99])" +         //at least 1 digit
                    "(!(?=.*[a-zA-Z]))" +      //any letter
                    "$");

    EditText userEmail;
    EditText userPass;
    Button userLogin;

    private Button UserMenu,BtnLine,BtnCall,BtnFace;
    FirebaseAuth firebaseAuth;
    TextView textMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        userEmail = findViewById(R.id.et_email);
        userPass = findViewById(R.id.et_pass);
        userLogin = findViewById(R.id.loginbtn);
        UserMenu = findViewById(R.id.managebtn);

        BtnLine = findViewById(R.id.linebtn);
        BtnFace = findViewById(R.id.facebookbtn);
        //textMenu = findViewById(R.id.textMenu);
        BtnLine.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                Uri lineIntent = Uri.parse("https://line.me/R/ti/g/ynhEFh6VOo");
                Intent IntentLine = new Intent(Intent.ACTION_VIEW,lineIntent);
                IntentLine.setPackage("jp.never.line.android");
                try {
                    startActivity(IntentLine);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://line.me/R/ti/g/ynhEFh6VOo")));

                }
            }
        });

        BtnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri fbIntent = Uri.parse("https://www.facebook.com/krupeung.saowakhon");
                Intent IntentFb = new Intent(Intent.ACTION_VIEW,fbIntent);
                startActivity(IntentFb);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        userLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validateEmail() | !validatePassword()) {
                            return;
                        } else {
                            firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                                    userPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                            startActivity(new Intent(LoginActivity.this, Permission.class));
                                            finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
        });
       /* textMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        UserMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regisauth();

            }
        });
    }
    private void repassword() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.repassword_sendmail, null);
        dialogBuilder.setView(dialogView);

        final Button agree = dialogView.findViewById(R.id.agree_repassword);
        final Button close = dialogView.findViewById(R.id.close_repassword);
        final EditText email = dialogView.findViewById(R.id.email_repassword);

        final AlertDialog c = dialogBuilder.create();
        c.show();
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getemail = email.getText().toString().trim();
                if (getemail.isEmpty()) {
                    email.setError("กรุณากรอกอีเมลล์");
                    view.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(getemail).matches()) {
                    email.setError("กรุณากรอกอีเมลล์ให้ถูกต้อง");
                    view.requestFocus();
                    return;
                } else {
                    email.setError(null);
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "ระบบทำการส่งคำขอเปลี่ยนรหัสผ่านไปยังอีเมลล์ของท่านเรียบร้อย", Toast.LENGTH_LONG).show();
                                        c.dismiss();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "ระบบทำรายการไม่สำเร็จ กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
            }
        });
    }

    private void regisauth() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.regis_auth, null);
        dialogBuilder.setView(dialogView);

        final Button forget = dialogView.findViewById(R.id.goforget);
        final Button register = dialogView.findViewById(R.id.goregister);
        final Button close = dialogView.findViewById(R.id.close_regauth);
        final AlertDialog c = dialogBuilder.create();
        c.show();

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repassword();
                c.dismiss();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.dismiss();
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            c.dismiss();
            }
        });

    }
//    private void SendVerify(final FirebaseAuth firebaseAuth) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.verify_dialog, null);
//        final Button Verify  = dialogView.findViewById(R.id.agree_verify);
//        final Button Close  = dialogView.findViewById(R.id.close_verify);
//        dialogBuilder.setView(dialogView);
//        final AlertDialog c = dialogBuilder.create();
//        c.show();
//        Verify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.getCurrentUser().sendEmailVerification();
//                Toast.makeText(LoginActivity.this, "ระบบทำการส่ง verify เรียบร้อย", Toast.LENGTH_SHORT).show();
//                c.dismiss();
//            }
//        });
//
//        Close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                c.dismiss();
//            }
//        });
//
//    }

    private boolean validateEmail() {
        String email = userEmail.getText().toString().trim();
        if (email.isEmpty()) {
            userEmail.setError(getString(R.string.input_error_email));
            userEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError(getString(R.string.input_error_email_invalid));
            userEmail.requestFocus();
            return false;
        } else {
            userEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = userPass.getText().toString().trim();
        if (password.isEmpty()) {
            userPass.setError(getString(R.string.input_error_password));
            userPass.requestFocus();
            return false;
        }else if (NUMBER_PATTERN.matcher(password).matches()) {
            userPass.setError(getString(R.string.input_error_password));
            userPass.requestFocus();
            return false;
        } else if (password.length()< 6 || password.length() > 13) {
            userPass.setError(getString(R.string.input_error_email_invalid));
            userPass.requestFocus();
            return false;
        } else {
            userPass.setError(null);
            return true;
        }
    }
}