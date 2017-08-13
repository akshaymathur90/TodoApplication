package edu.sjsu.todoapp.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.todoapp.R;
import edu.sjsu.todoapp.fragments.AddorEditItemFragment;
import edu.sjsu.todoapp.model.ToDoItem;

/**
 * Created by akshaymathur on 8/11/17.
 */

public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.TodoItemsViewHolder> {

    List<ToDoItem> mToDoItemList;
    Context mContext;
    FragmentManager mFragmentManager;
    private static final int REQUEST_EDIT = 1;
    private final String TODO_FRAGMENT_TAG = "TodoListFragment";
    DeleteItemListener mDeleteItemListener;

    public TodoItemsAdapter(Context context, List<ToDoItem> list, FragmentManager fragmentManager){
        mToDoItemList = list;
        mContext = context;
        mFragmentManager = fragmentManager;

        try {
            mDeleteItemListener = (DeleteItemListener) mFragmentManager.findFragmentByTag(TODO_FRAGMENT_TAG);
        } catch (ClassCastException e) {
            throw new ClassCastException(mFragmentManager.findFragmentByTag(TODO_FRAGMENT_TAG).toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public void updateDataSet(List<ToDoItem> list){
        mToDoItemList = list;
        notifyDataSetChanged();
    }

    public interface DeleteItemListener{
        void onItemSelected(ToDoItem item);
    }

    @Override
    public TodoItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.todo_list_item,parent,false);

        return new TodoItemsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TodoItemsViewHolder holder, int position) {
        //ToDoItem toDoItem = mToDoItemList.get(position);
        final ToDoItem item = mToDoItemList.get(position);
        holder.itemTitle.setText(item.getItemName());
        holder.itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TodoItemAdapter","item clicked");
                DialogFragment dialogFragment = AddorEditItemFragment.newInstance(1,item);
                dialogFragment.setTargetFragment(mFragmentManager.findFragmentByTag(TODO_FRAGMENT_TAG),REQUEST_EDIT);
                dialogFragment.show(mFragmentManager,"AddOrEditItem");
            }
        });
        holder.itemTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("TodoItemAdapter","item long clicked");
                mDeleteItemListener.onItemSelected(item);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mToDoItemList.size();
    }

    public class TodoItemsViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitle;
        TodoItemsViewHolder(View view){
            super(view);
            itemTitle = (TextView) view.findViewById(R.id.todo_item_title);
        }
    }
}
