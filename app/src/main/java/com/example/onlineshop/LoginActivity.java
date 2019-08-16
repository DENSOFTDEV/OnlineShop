package com.example.onlineshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshop.Model.Users;
import com.example.onlineshop.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private TextView Adminlink, NotAdminlink;

    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        LoginButton = (Button) findViewById(R.id.login_btn);
        InputNumber =(EditText) findViewById(R.id.login_phone_number_input);
        InputPassword =(EditText) findViewById(R.id.login_password_input);
        Adminlink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminlink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        Adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                Adminlink.setVisibility(View.INVISIBLE);
                NotAdminlink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";

            }
        });
        NotAdminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                Adminlink.setVisibility(View.VISIBLE);
                NotAdminlink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void loginUser() {

        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

          if (TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Please write your  phone number", Toast.LENGTH_SHORT).show();
          }
            else if (TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please write your  password", Toast.LENGTH_SHORT).show();
            } else {
              loadingBar.setTitle("Login Account");
              loadingBar.setMessage("Please wait, while we are checking the credentials ");
              loadingBar.setCanceledOnTouchOutside(false);
              loadingBar.show();

              AllowAccessToAccount(phone,password);
          }

    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if (chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){

                           if (parentDbName.equals("Admins")){

                               Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                               startActivity(intent);

                           }else if (parentDbName.equals("Users")){

                               Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                               Prevalent.currentonlineuser =usersData;
                               startActivity(intent);

                           }
                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Account with this "+phone+" number do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "You need to create a new account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
