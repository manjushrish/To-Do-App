package com.mdeshpande.to_do_final;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class toDoListFragment extends Fragment {

    private static final String TAG = "ToListActivityFragment";
    List<Task> tasks = new ArrayList<Task>();
    ArrayAdapter<Task> adapter = null;
    private DBHelper dbHelper = null;

     Calendar myCalendar = Calendar.getInstance();

    public toDoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            dbHelper = new DBHelper(getActivity());
            tasks = dbHelper.selectAll();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: DBHelper threw exception : " + e);
            e.printStackTrace();
        }

        ListView list = (ListView) getActivity().findViewById(R.id.task_list);
        adapter = new taskAdapter(getActivity(), R.layout.row, tasks);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                displayInfo(view, position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onDelete(view, position);
                return true;

            }
        });

        Button saveButton = (Button) getActivity().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onSave();

            }
        });

        Button sortButton = (Button) getActivity().findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                sort();
            }
        });
        /*********************************************************************************/


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        EditText dateText = (EditText) getActivity().findViewById(R.id.task_date);


        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                DatePickerDialog mDatePicker = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();


            }
        });
    }
        void updateLabel() {

        EditText dateText = (EditText) getActivity().findViewById(R.id.task_date);
        // myCalendar = Calendar.getInstance();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dateText.setText(sdf.format(myCalendar.getTime()));
        }
        /*******************************************************************************/


    private void onSave() {
        Task task = new Task();
        EditText title = (EditText) getActivity().findViewById(R.id.task_title);
        EditText shortD = (EditText) getActivity().findViewById(R.id.shortDesc);
        EditText LongD = (EditText) getActivity().findViewById(R.id.longdesc);
        EditText Date = (EditText) getActivity().findViewById(R.id.task_date);




        final String taskTitle = title.getText().toString();
        final String taskDate = Date.getText().toString();

        if ((taskTitle.isEmpty())||(taskDate.isEmpty())) {
            Toast.makeText(getView().getContext(),getResources().getString(R.string.alert_message),
                    Toast.LENGTH_LONG).show();
        } else {
            RadioButton tag1 = (RadioButton) getActivity().findViewById(R.id.tag1);

            if(tag1.isChecked())
            {
                task.setTag(getResources().getString(R.string.personal));

            }
            else
            {
                task.setTag(getResources().getString(R.string.Official));
            }

            task.setTitle(title.getText().toString());
            task.setShortd(shortD.getText().toString());
            task.setLongd(LongD.getText().toString());
            task.setDate(Date.getText().toString());

            title.setText("");
            Date.setText("");
           shortD.setText("");
            LongD.setText("");

            //title.setHint("Title");


            long taskID = 0;
            if (dbHelper != null) {
                taskID = dbHelper.insert(task);
                task.setId(taskID);
            }
            adapter.add(task);
            adapter.notifyDataSetChanged();

            InputMethodManager inputManager = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void onDelete(View view, int position) {

        Task task = adapter.getItem(position);
        if (task != null) {
            String item = "deleting: " + task.getTitle();
            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            Log.d(TAG, " onItemClick: " + task.getTitle());
            if (dbHelper != null) dbHelper.deleteRecord(task.getId());
            adapter.remove(task);

        }
    }
    public void displayInfo(View view, int position){

        Task task = adapter.getItem(position);
        ContextThemeWrapper ctw =
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom );
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
        alertDialogBuilder.setTitle(getResources().getString(R.string.long_desc));
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_info);

        String info = task.getLongd();
        String infoMessage;
        if(info.isEmpty())
        {
            infoMessage = "No additional information available";
        }
        else
        {
            infoMessage =info;
        }
        alertDialogBuilder
                .setMessage(infoMessage).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    void sort()
    {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task  FirstTask, Task  SecondTask)
            {

                return  FirstTask.getTitle().compareTo(SecondTask.getTitle());

            }
        });
        if(tasks.isEmpty())
        {
            Toast.makeText(getView().getContext(),getResources().getString(R.string.no_task),
                    Toast.LENGTH_SHORT).show();

        }
        ListView list = (ListView) getActivity().findViewById(R.id.task_list);
        adapter = new taskAdapter(getActivity(), R.layout.row, tasks);
        list.setAdapter(adapter);

    }
}


