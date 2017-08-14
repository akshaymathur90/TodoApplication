package edu.sjsu.todoapp.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Integer,Integer> mPriorityColorMap;
    private final String TAG = "TodoItemsAdapter";

    public TodoItemsAdapter(Context context, List<ToDoItem> list, FragmentManager fragmentManager){
        mToDoItemList = list;
        mContext = context;
        mFragmentManager = fragmentManager;

        mPriorityColorMap = new HashMap<>();
        mPriorityColorMap.put(3,R.color.red);
        mPriorityColorMap.put(2,R.color.orange);
        mPriorityColorMap.put(1,R.color.yellow);

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
        holder.itemDue.setText(item.getDateDue());
        Log.d(TAG,"Item priority--> "+item.getPriority());
        Log.d(TAG,"Item color--> "+mPriorityColorMap.get(item.getPriority()));
        holder.itemPriority.setBackgroundColor(ContextCompat.getColor(mContext,mPriorityColorMap.get(item.getPriority())));
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TodoItemAdapter","item clicked");
                DialogFragment dialogFragment = AddorEditItemFragment.newInstance(1,item);
                dialogFragment.setTargetFragment(mFragmentManager.findFragmentByTag(TODO_FRAGMENT_TAG),REQUEST_EDIT);
                dialogFragment.show(mFragmentManager,"AddOrEditItem");
            }
        });
        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG,"item long clicked");
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
        TextView itemDue;
        LinearLayout itemPriority;
        LinearLayout itemLayout;
        TodoItemsViewHolder(View view){
            super(view);
            itemTitle = (TextView) view.findViewById(R.id.todo_item_title);
            itemDue = (TextView) view.findViewById(R.id.todo_list_item_due);
            itemPriority = (LinearLayout) view.findViewById(R.id.priority_image_view);
            itemLayout = (LinearLayout) view.findViewById(R.id.todo_item_layout);
        }
    }
}
