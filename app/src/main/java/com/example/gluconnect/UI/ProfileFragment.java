package com.example.gluconnect.UI;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.gluconnect.R;
import com.google.android.material.textfield.TextInputLayout;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View myView;
    EditText username, email, hospital, password;
    Button editProfile, saveProfile,logoutBtn;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    String picturePath;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getContext().getSharedPreferences("MyPreferences", 0);


        imageView = myView.findViewById(R.id.profile_photo_imageview);
        username = myView.findViewById(R.id.profile_username_et);
        email = myView.findViewById(R.id.email_et);
        hospital = myView.findViewById(R.id.hospital_et);
//        password = myView.findViewById(R.id.password_et);
        editProfile = myView.findViewById(R.id.edit_profile_btn);
        saveProfile = myView.findViewById(R.id.save_btn);
        logoutBtn = myView.findViewById(R.id.logout_btn);
        progressBar = myView.findViewById(R.id.progressBar);

        setValues();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile.setVisibility(View.GONE);
                saveProfile.setVisibility(View.VISIBLE);
                username.setEnabled(true);
                username.requestFocus();
                username.setSelection(username.getText().length());
                email.setEnabled(true);
                hospital.setEnabled(true);
//                password.setEnabled(true);
            }
        });
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile.setVisibility(View.GONE);
                editProfile.setVisibility(View.VISIBLE);
                username.setEnabled(false);
                email.setEnabled(false);
                hospital.setEnabled(false);
//                password.setEnabled(false);
                updateDetails();

            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        LoginFragment loginFragment = new LoginFragment();
                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.register_sv, loginFragment);
                        trans.addToBackStack(null);
                        trans.commit();
                    }
                }, 3000);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });

        return myView;
    }

    public void setValues() {
        username.setText(sharedPreferences.getString("Username", "User"));
        email.setText(sharedPreferences.getString("Email", "Email"));
        hospital.setText(sharedPreferences.getString("Hospital", "Hospital"));
//        password.setText(sharedPreferences.getString("Password", "Password"));
    }

    public void updateDetails() {

    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Change your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }


}
