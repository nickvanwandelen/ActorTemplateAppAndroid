package com.example.nick.actortemplateapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import domain.Member;
import task.ImageHelper;

public class ShowIndividualMemberActivity extends AppCompatActivity {

    private String memberKey;
    private Member showingMember;

    private DatabaseReference reference;

    private Menu showingMenu;

    private boolean isEditing = false;

    private ImageHelper imageHelper;

    private ImageView memberPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_individual_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference();
        imageHelper = new ImageHelper();

        Intent retrievedIntent = getIntent();
        memberKey = retrievedIntent.getStringExtra("member_key");

        reference.child("members").child(memberKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showingMember = dataSnapshot.getValue(Member.class);
                if (showingMember == null) {
                    Toast.makeText(getApplicationContext(), "Error reading Firebase database: Member not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText memberEmailET = (EditText) findViewById(R.id.showMember_emailEditText);
                memberEmailET.setText(showingMember.getEmail());
                memberEmailET.setEnabled(false);

                EditText memberNameET = (EditText) findViewById(R.id.showMember_nameEditText);
                memberNameET.setText(showingMember.getName());
                memberNameET.setEnabled(false);

                EditText memberNotesET = (EditText) findViewById(R.id.showMember_notesEditText);
                memberNotesET.setText(showingMember.getNotes());
                memberNotesET.setEnabled(false);

                EditText memberPhoneET = (EditText) findViewById(R.id.showMember_phoneEditText);
                memberPhoneET.setText(showingMember.getPhone());
                memberPhoneET.setEnabled(false);

                EditText memberRoleET = (EditText) findViewById(R.id.showMember_roleEditText);
                memberRoleET.setText(showingMember.getRole());
                memberRoleET.setEnabled(false);

                memberPicture = (ImageView) findViewById(R.id.showMember_image);
                if (!showingMember.getPicture().equals("")) {
                    memberPicture.setImageBitmap(imageHelper.convertPhotoStringToImage(showingMember.getPicture()));
                }


                ((Toolbar) findViewById(R.id.toolbar)).setTitle(showingMember.getName());
                ((TextView) findViewById(R.id.showMember_ActorTextView)).setText("Member belongs to Actor: " + getIntent().getStringExtra("actor_name"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error reading Firebase database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.individual_member_menu, menu);
        showingMenu = menu;
        String loggedInUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child("users").child(loggedInUserUid).child("isAnalist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isAnalist = dataSnapshot.getValue(boolean.class);

                if (!isAnalist) {
                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.member_menu_action_edit:
                handleEditMember();
                return true;
            case R.id.member_menu_change_picture:
                handleChangePicture();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleEditMember() {
        EditText memberEmailET = (EditText) findViewById(R.id.showMember_emailEditText);
        EditText memberNameET = (EditText) findViewById(R.id.showMember_nameEditText);
        EditText memberNotesET = (EditText) findViewById(R.id.showMember_notesEditText);
        EditText memberPhoneET = (EditText) findViewById(R.id.showMember_phoneEditText);
        EditText memberRoleET = (EditText) findViewById(R.id.showMember_roleEditText);

        memberEmailET.setEnabled(!isEditing);
        memberNameET.setEnabled(!isEditing);
        memberNotesET.setEnabled(!isEditing);
        memberPhoneET.setEnabled(!isEditing);
        memberRoleET.setEnabled(!isEditing);

        showingMenu.getItem(1).setVisible(isEditing);

        if (isEditing) {
            if (memberEmailET.getText().toString().equals("") ||
                    memberEmailET.getText().toString().equals("") ||
                    memberNameET.getText().toString().equals("") ||
                    memberNotesET.getText().toString().equals("") ||
                    memberRoleET.getText().toString().equals("")) {

                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();

                memberEmailET.setEnabled(isEditing);
                memberNameET.setEnabled(isEditing);
                memberNotesET.setEnabled(isEditing);
                memberPhoneET.setEnabled(isEditing);
                memberRoleET.setEnabled(isEditing);
                showingMenu.getItem(1).setVisible(!isEditing);
                return;
            }

            reference.child("members").child(memberKey).child("email").setValue(memberEmailET.getText().toString());
            reference.child("members").child(memberKey).child("name").setValue(memberNameET.getText().toString());
            reference.child("members").child(memberKey).child("notes").setValue(memberNotesET.getText().toString());
            reference.child("members").child(memberKey).child("phone").setValue(memberPhoneET.getText().toString());
            reference.child("members").child(memberKey).child("role").setValue(memberRoleET.getText().toString());

            showingMember.setEmail(memberEmailET.getText().toString());
            showingMember.setName(memberNameET.getText().toString());
            showingMember.setNotes(memberNotesET.getText().toString());
            showingMember.setPhone(memberPhoneET.getText().toString());
            showingMember.setRole(memberRoleET.getText().toString());

            memberEmailET.setBackgroundResource(android.R.color.transparent);
            memberNameET.setBackgroundResource(android.R.color.transparent);
            memberNotesET.setBackgroundResource(android.R.color.transparent);
            memberPhoneET.setBackgroundResource(android.R.color.transparent);
            memberRoleET.setBackgroundResource(android.R.color.transparent);
        } else {
            memberEmailET.setBackgroundResource(android.R.drawable.edit_text);
            memberNameET.setBackgroundResource(android.R.drawable.edit_text);
            memberNotesET.setBackgroundResource(android.R.drawable.edit_text);
            memberPhoneET.setBackgroundResource(android.R.drawable.edit_text);
            memberRoleET.setBackgroundResource(android.R.drawable.edit_text);
        }

        isEditing = !isEditing;
    }

    private void handleChangePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent toPhoneGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        toPhoneGalleryIntent.setType("image/*");

        startActivityForResult(toPhoneGalleryIntent, 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.e("ChangePicture:", "Unkown intent, data is null");
                return;
            }

            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                memberPicture.setImageBitmap(selectedImage);
                reference.child("members").child(memberKey).child("picture").setValue(imageHelper.convertImageToPhotoString(new BitmapDrawable(getResources(), selectedImage)));
            } catch (IOException exception) {
                Log.e("ChangePicture: ", "Error while retrieving bitmap: " + exception.getStackTrace());
            }
        }
    }

    public void callMember(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + showingMember.getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 888);
        }
        else{
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == 888){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callMember(null);
            }
            else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void emailMember(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + showingMember.getEmail()));
        startActivity(intent);
    }

}
