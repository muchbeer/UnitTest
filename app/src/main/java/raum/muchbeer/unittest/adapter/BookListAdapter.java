package raum.muchbeer.unittest.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import raum.muchbeer.unittest.R;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.util.DateUtil;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private static final String TAG = BookListAdapter.class.getSimpleName();

    private List<Note> books = new ArrayList<>();
    private OnBookListener onBookListener;

    public BookListAdapter(OnBookListener onBookListener) {
        this.onBookListener = onBookListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_note_list_item
                ,parent
                ,false);
        return new BookViewHolder(view, onBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        try{
            String month = books.get(position).getTimestamp().substring(0, 2);
            month = DateUtil.getMonthFromNumber(month);
            String year = books.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timestamp.setText(timestamp);
            holder.title.setText(books.get(position).getTitle());
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public Note getBook(int position){
        if(books.size() > 0){
            return books.get(position);
        }
        return null;
    }

    public void removeNote(Note note){
        books.remove(note);
        notifyDataSetChanged();
    }

    public void setBooks(List<Note> notes){
        this.books = notes;
        notifyDataSetChanged();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       public TextView timestamp, title;
        OnBookListener mOnNoteListener;

        public BookViewHolder(@NonNull View itemView, OnBookListener onNoteListener) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.note_timestamp);
            title = itemView.findViewById(R.id.note_title);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onBookClick(getBook(getAdapterPosition()));
        }
    }

    public interface OnBookListener{
        void onBookClick(Note note);
    }
}
