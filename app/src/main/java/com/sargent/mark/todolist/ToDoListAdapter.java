package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
import com.sargent.mark.todolist.data.ToDoItem;

import java.util.ArrayList;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;

    private ItemClickListener listener;

    private String TAG = "todolistadapter";


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(R.layout.item, parent, false);


        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    //added new extra method onCheckClick
    public interface ItemClickListener {

        void onItemClick(int pos, String description,String category, String duedate, long id);

        void onCheckClick(int pos, boolean status, long id);
    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {

        this.cursor = cursor;

        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){

        if (cursor != null) cursor.close();

        cursor = newCursor;

        if (newCursor != null) {

            //  RecyclerView to refresh
            this.notifyDataSetChanged();

        }
    }

    //added new textview category and checkbox

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descr;

        TextView due;

        TextView cate;

        TextView s;

        CheckBox stat;

        String category;

        String status;

        String duedate;

        String description;

        long id;

        ItemHolder(View view) {

            super(view);

            cate =(TextView) view.findViewById(R.id.category);

            stat= (CheckBox) view.findViewById(R.id.status);

            s= (TextView) view.findViewById(R.id.s);

            descr = (TextView) view.findViewById(R.id.description);

            due = (TextView) view.findViewById(R.id.dueDate);


            view.setOnClickListener(this);
        }



//

        public void bind(ItemHolder holder, int pos) {

            cursor.moveToPosition(pos);

            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));

            Log.d(TAG, "deleting id: " + id);

            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));

            status= cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_STATUS));

            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));

            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));

            descr.setText(description);

            due.setText(duedate);

            //setting data for category
            cate.setText(category);

            if(status.equals("0"))
            {
                s.setText("Undone");
            }
            else if (status.equals("1"))

            {
                s.setText("done");

                stat.setChecked(true);
            }



            stat.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (((CheckBox) v).isChecked()) {


                        Log.d(TAG,"checked");

                        final int pos = getAdapterPosition();

                        listener.onCheckClick(pos,true,id);
                    }
                    else
                    {
                        Log.d(TAG,"unchecked");

                        final int pos = getAdapterPosition();

                        listener.onCheckClick(pos,false,id);

                    }
                    //case 2

                }
            });

            holder.itemView.setTag(id);


        }






        @Override
        public void onClick(View v) {

            Log.d(TAG,"hhhh");

            final int pos = getAdapterPosition();


            listener.onItemClick(pos, description,category, duedate, id);
        }
    }

}
