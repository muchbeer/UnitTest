package raum.muchbeer.unittest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import raum.muchbeer.unittest.adapter.BookListAdapter;
import raum.muchbeer.unittest.model.Note;
import raum.muchbeer.unittest.ui.DataStateStatus;
import raum.muchbeer.unittest.util.VerticalSpacingItemDecorator;
import raum.muchbeer.unittest.viewmodel.BookListViewModel;
import raum.muchbeer.unittest.viewmodel.ViewModelProviderFactory;

public class BookListActivity extends DaggerAppCompatActivity implements
        BookListAdapter.OnBookListener,
        View.OnClickListener{

    private static final String TAG = BookListAdapter.class.getSimpleName();

    // ui components
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CoordinatorLayout parent;

    // vars
    private BookListViewModel viewModel;
    private BookListAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        parent = findViewById(R.id.parent);

        fab.setOnClickListener(this);

        viewModel =new  ViewModelProvider(this, providerFactory).get(BookListViewModel.class);

        initRecyclerView();
    }

    private void subscribeObservers(){

        Log.d(TAG, "subscribeObservers: called.");
        viewModel.observeNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes != null){
                    adapter.setBooks(notes);
                }
            }
        });
        viewModel.getNotes(); }


    @Override
    protected void onStart() {
        super.onStart();
        subscribeObservers();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpacingItemDecorator(10));
        adapter = new BookListAdapter(this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:{
                Intent intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBookClick(Note note) {
        Intent intent = new Intent(this, BookActivity.class);
        intent.putExtra(getString(R.string.intent_note), note);
        startActivity(intent);
    }

    private void showSnackBar(String message){
        if(!TextUtils.isEmpty(message)) {
            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    Note note = adapter.getBook(viewHolder.getAdapterPosition());
                    adapter.removeNote(note);

                    try {
                        final LiveData<DataStateStatus<Integer>> deleteAction = viewModel.deleteNote(note);
                        deleteAction.observe(BookListActivity.this, new Observer<DataStateStatus<Integer>>() {
                            @Override
                            public void onChanged(DataStateStatus<Integer> integerResource) {
                                if(integerResource != null){
                                    showSnackBar(integerResource.message);
                                }
                                deleteAction.removeObserver(this);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar(e.getMessage());
                    }
                }
            };
}
