package xyz.robertsen.androidoblig;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gitsieg on 04.04.18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RecentSearchHolder>{

    private static String TAG = HistoryAdapter.class.getSimpleName();
    private ArrayList<RecentSearchItem> recentSearchItems;
    private LayoutInflater inflater;
    private Context context;

    public HistoryAdapter(Context c, ArrayList<RecentSearchItem> recentSearchItems) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.recentSearchItems = recentSearchItems;
    }


    @Override
    public HistoryAdapter.RecentSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = inflater.inflate(R.layout.history_recycler_recentsearchitem, parent, false);
        return new RecentSearchHolder(viewItem, this);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.RecentSearchHolder holder, int position) {
        RecentSearchItem item = recentSearchItems.get(position);
        holder.twSearchString.setText(item.getSearchString());
    }

    @Override
    public int getItemCount() {
        return recentSearchItems == null ? 0: recentSearchItems.size();
    }

    public class RecentSearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final String TAG = RecentSearchHolder.class.getSimpleName();
        private TextView twSearchString;
        final HistoryAdapter adapter;

        public RecentSearchHolder(View itemView, HistoryAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            twSearchString = itemView.findViewById(R.id.txt_search_string);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            RecentSearchItem recentSearchItem = recentSearchItems.get(getAdapterPosition());
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("search_string", recentSearchItem.getSearchString());
            context.startActivity(intent);
        }
    }


}
