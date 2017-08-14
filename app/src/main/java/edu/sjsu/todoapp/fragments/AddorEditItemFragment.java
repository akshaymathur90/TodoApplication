package edu.sjsu.todoapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.sjsu.todoapp.R;
import edu.sjsu.todoapp.model.ToDoItem;
import edu.sjsu.todoapp.database.TodoItemDbHelper;

/**
 * Created by akshaymathur on 8/11/17.
 */

public class AddorEditItemFragment extends DialogFragment {

    String mDialogTitle = "Add Item";
    ToDoItem mToDoItem = null;

    public TextView mTitleTextView;
    public TextInputEditText mItemTitle,mItemAssignedTo,mItemDueDate, mItemPriority;
    public AppCompatMultiAutoCompleteTextView mItemDesciption;
    public AppCompatButton mCancelButton, mOkButton;
    public TextInputLayout mTitleLayout;
    public static final String TODO_ITEM_KEY = "TODO ITEM";
    public static final String TODO_MODE_KEY = "MODE";
    public static final String TAG = "AddorEditItem";
    private static final int REQUEST_DATE = 0;
    SQLiteDatabase db;
    int mode;
    Map<String,Integer> priorityMap;

    /*public static AddorEditItemFragment newAddInstance(){
        return new AddorEditItemFragment();
    }*/

    public static AddorEditItemFragment newInstance(int mode, ToDoItem toDoItem){
        AddorEditItemFragment addorEditItemFragment = new AddorEditItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TODO_ITEM_KEY, toDoItem);
        bundle.putInt(TODO_MODE_KEY, mode);
        addorEditItemFragment.setArguments(bundle);

        return addorEditItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mDialogTitle = "Edit Item";
            mToDoItem = (ToDoItem) bundle.getSerializable(TODO_ITEM_KEY);
            mode = bundle.getInt(TODO_MODE_KEY);
        }
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        priorityMap =new HashMap<>();
        priorityMap.put("Low",1);
        priorityMap.put("Medium",2);
        priorityMap.put("High",3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_add_or_edit_item,container,false);
        mTitleTextView = (TextView) v.findViewById(R.id.dialog_title);
        mItemTitle = (TextInputEditText) v.findViewById(R.id.todo_item_title_text);
        mItemDueDate = (TextInputEditText) v.findViewById(R.id.todo_item_due_date_text);
        mItemAssignedTo = (TextInputEditText) v.findViewById(R.id.todo_item_assigned_to_text);
        mItemDesciption = (AppCompatMultiAutoCompleteTextView) v.findViewById(R.id.todo_item_description_view);
        mCancelButton = (AppCompatButton) v.findViewById(R.id.todo_item_cancel_button);
        mOkButton = (AppCompatButton) v.findViewById(R.id.todo_item_ok_button);
        mTitleLayout = (TextInputLayout) v.findViewById(R.id.todo_item_title_layout);
        mItemPriority = (TextInputEditText) v.findViewById(R.id.todo_item_priority_text);
        mTitleTextView.setText(mDialogTitle);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isTitleEmpty()){
                    mTitleLayout.setError("Title cannot be empty");
                    return;
                }else {

                    if (mode == 1) {
                        editItem();
                    } else {
                        addItem();
                    }
                    dismiss();
                }

            }
        });

        if(mode==1){
            mItemTitle.setText(mToDoItem.getItemName());
            mItemDueDate.setText(mToDoItem.getDateDue());
            mItemAssignedTo.setText(mToDoItem.getAssignedTo());
            mItemDesciption.setText(mToDoItem.getItemDescription());
            mItemPriority.setText(getPriorityString(mToDoItem.getPriority()));

        }
        else{
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            String date = dateFormat.format(calendar.getTime());
            Log.d(TAG,"Today's date is--> "+date);
            mItemDueDate.setText(date);
            mItemPriority.setText(getPriorityString(1));
        }

        mItemDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddorEditItemFragment.this,REQUEST_DATE);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        mItemPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), mItemDueDate);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_priority_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_item_high:
                                mItemPriority.setText(R.string.text_high);
                                break;
                            case R.id.menu_item_medium:
                                mItemPriority.setText(R.string.text_medium);
                                break;
                            case R.id.menu_item_low:
                                mItemPriority.setText(R.string.text_low);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
        return v;

    }
    public boolean isTitleEmpty(){
        String title = mItemTitle.getText().toString();
        if(title!=null && !title.equals("")){
            return false;
        }
        return true;
    }

    public int getPriorityValue(String priority){
        return priorityMap.get(priority);
    }
    public String getPriorityString(int priority){
        switch (priority){
            case 1: return "Low";
            case 2: return "Medium";
            case 3: return "High";
            default: return null;
        }
    }
    private void addItem() {

        ToDoItem newItem = new ToDoItem();
        newItem.setItemName(mItemTitle.getText().toString());
        newItem.setItemDescription(mItemDesciption.getText().toString());
        newItem.setDateEdited(null);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String date = dateFormat.format(calendar.getTime());
        newItem.setDateCreated(date);
        newItem.setAssignedTo(mItemAssignedTo.getText().toString());
        newItem.setDateDue(mItemDueDate.getText().toString());
        newItem.setPriority(getPriorityValue(mItemPriority.getText().toString()));


        if(getTargetFragment()!=null){
            Intent intent = new Intent();
            intent.putExtra("newitem",newItem);
            getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);
        }
    }

    private void editItem() {
        ToDoItem newItem = new ToDoItem();
        newItem.setItemName(mItemTitle.getText().toString());
        newItem.setItemDescription(mItemDesciption.getText().toString());
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        String date = dateFormat.format(calendar.getTime());
        newItem.setDateEdited(date);
        newItem.setDateCreated(mToDoItem.getDateCreated());
        newItem.setAssignedTo(mItemAssignedTo.getText().toString());
        newItem.setDateDue(mItemDueDate.getText().toString());
        newItem.setID(mToDoItem.getID());
        newItem.setPriority(getPriorityValue(mItemPriority.getText().toString()));


        if(getTargetFragment()!=null){
            Intent intent = new Intent();
            intent.putExtra("edititem",newItem);
            getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            int day = data.getExtras().getInt("day");
            int month = data.getExtras().getInt("month");
            int year = data.getExtras().getInt("year");
            Log.d(TAG,"Day selected is--> "+day);
            Log.d(TAG,"Month selected is--> "+month);
            Log.d(TAG,"Year selected is--> "+year);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);
            DateFormat dateFormat = SimpleDateFormat.getDateInstance();
            String date = dateFormat.format(calendar.getTime());
            Log.d(TAG,"Date is--> "+date);
            mItemDueDate.setText(date);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
