package edu.sjsu.todoapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.sjsu.todoapp.R;
import edu.sjsu.todoapp.fragments.TodoListFragment;

public class MainActivity extends AppCompatActivity {

    private final String TODO_FRAGMENT_TAG = "TodoListFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = TodoListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,fragment,TODO_FRAGMENT_TAG).commit();
        }

    }
}
