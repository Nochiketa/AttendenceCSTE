package com.example.nochiketa.attendencecste;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button mOrder, displayAllDataButton, saveButton;
    String sv;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    MYDatabaseHelper myDatabaseHelper;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOrder = (Button)findViewById(R.id.btn1);
        displayAllDataButton = (Button)findViewById(R.id.btn2);
        saveButton = (Button)findViewById(R.id.bt3);

        mItemSelected = (TextView)findViewById(R.id.TV1);
        myDatabaseHelper = new MYDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        listItems = getResources().getStringArray(R.array.student_id);
        checkItems = new boolean[listItems.length];


        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle(R.string.student_list);
                mBuilder.setMultiChoiceItems(listItems, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                        if(isChecked)
                        {
                            if(!mUserItems.contains(position))
                            {
                                mUserItems.add(position);
                                i++;
                            }
                        }
                        else if(mUserItems.contains(position))
                        {
                                mUserItems.remove(position);
                                i--;
                        }
                    }
                });

                mBuilder.setCancelable(false);

                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for(int i = 0; i<mUserItems.size(); i++)
                        {
                            item = item + listItems[mUserItems.get(i)];
                            if(i != mUserItems.size() -1)
                            {
                                item = item + ", ";
                            }
                        }

                        mItemSelected.setText("Present: \n" + item +"\n\n Total Students: " + i);
                        i=0;

                        sv = item;
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0; i<checkItems.length; i++)
                        {
                            checkItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sv.equals(""))
                {
                    String currentDateTimeString = DateFormat.getDateInstance().format(new Date());

                    long rowId = myDatabaseHelper.insertData(currentDateTimeString, sv);

                    if(rowId == -1)
                    {
                        Toast.makeText(getApplicationContext(), "Not saved", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Data is Saved", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No students selected...", Toast.LENGTH_LONG).show();
                }

                for(int i = 0; i<checkItems.length; i++)
                {
                    checkItems[i] = false;
                    mUserItems.clear();
                    //mItemSelected.setText("");
                }
            }
        });


        displayAllDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = myDatabaseHelper.displayAllData();
                if(cursor.getCount() == 0)
                {
                    showData("Sorry!", "No Data Found");
                    return;
                }
                StringBuffer stringBuffer = new StringBuffer();

                while (cursor.moveToNext())
                {
                    stringBuffer.append("Class no: "+ cursor.getString(0)+"\n");
                    stringBuffer.append("Date: " + cursor.getString(1)+"\n");
                    stringBuffer.append("Student Id: \n" + cursor.getString(2)+"\n\n");
                }
                showData("Attended Students: ", stringBuffer.toString());
            }
        });
    }


    public void showData(String title, String data)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(title);
        mBuilder.setMessage(data);
        mBuilder.setCancelable(true);
        mBuilder.setPositiveButton("Delete Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                View v = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.user_input, null);
                AlertDialog.Builder dBuilder = new AlertDialog.Builder(MainActivity.this);
                dBuilder.setView(v);
                final EditText userInput = (EditText) v.findViewById(R.id.userinput);

                dBuilder.setCancelable(true);
                dBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String delete =  userInput.getText().toString();
                        int id = myDatabaseHelper.deleteData(delete);
                        if(id > 0)
                        {
                            Toast.makeText(getApplicationContext(), "Data has been deleted...", Toast.LENGTH_LONG).show();
                        }
                        else if(id<0)
                        {
                            Toast.makeText(getApplicationContext(), "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dDialog = dBuilder.create();
                dDialog.show();
            }
        });

        AlertDialog nBuilder = mBuilder.create();
        nBuilder.show();
    }
}
