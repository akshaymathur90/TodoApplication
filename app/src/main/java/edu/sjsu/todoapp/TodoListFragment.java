package edu.sjsu.todoapp;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.todoapp.database.TodoItemDbHelper;
import edu.sjsu.todoapp.database.TodoItemDbUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends Fragment implements TodoItemsAdapter.DeleteItemListener{

    public RecyclerView mRecyclerView;
    public FloatingActionButton mAddButton;
    public LinearLayoutManager mLinearLayoutManager;
    SQLiteDatabase db;
    TodoItemsAdapter mTodoItemsAdapter;

    private static final int REQUEST_ADD = 0;
    private static final int REQUEST_EDIT = 1;
    private static final String TAG = "TodoListFragment";

    public TodoListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TodoItemDbHelper dbHelper = new TodoItemDbHelper(getActivity());
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.todo_items_list);
        mAddButton = (FloatingActionButton) v.findViewById(R.id.add_new_item);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        List<ToDoItem> itemList= TodoItemDbUtils.fetchAllItems(db);
        mTodoItemsAdapter = new TodoItemsAdapter(getActivity(),itemList,getActivity().getSupportFragmentManager());
        mRecyclerView.setAdapter(mTodoItemsAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = AddorEditItemFragment.newInstance(0,null);
                dialogFragment.setTargetFragment(TodoListFragment.this,REQUEST_ADD);
                dialogFragment.show(getFragmentManager(),"AddOrEditItem");
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==REQUEST_ADD){
                ToDoItem newItem = (ToDoItem) data.getExtras().getSerializable("newitem");
                TodoItemDbUtils.addNewItem(newItem,db);
                Toast.makeText(getActivity(),"Item Added",Toast.LENGTH_LONG).show();
            }
            else if(requestCode==REQUEST_EDIT){
                ToDoItem newItem = (ToDoItem) data.getExtras().getSerializable("edititem");
                TodoItemDbUtils.updateItem(db,newItem);
                Toast.makeText(getActivity(),"Item Edited",Toast.LENGTH_LONG).show();
            }
            List<ToDoItem> updatedList = TodoItemDbUtils.fetchAllItems(db);
            mTodoItemsAdapter.updateDataSet(updatedList);
        }

    }

    @Override
    public void onItemSelected(final ToDoItem item) {
        Log.d(TAG,"Item selected Listener called");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete \'"+ item.getItemName()+"\'");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TodoItemDbUtils.deleteItem(db,item.getID());
                List<ToDoItem> updatedList = TodoItemDbUtils.fetchAllItems(db);
                Log.d(TAG,updatedList.toString()  + " CHECK");
                mTodoItemsAdapter.updateDataSet(updatedList);
                Toast.makeText(getActivity(),"Item Deleted",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }
}
