package edu.calpoly.nnegrey.baby8alpha;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatternListAdapter extends RecyclerView.Adapter<PatternView> {

	private List<Pattern> patterns;

    public PatternListAdapter(ArrayList<Pattern> p) {
        this.patterns = p;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.pattern_view;
    }

    @Override
    public PatternView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PatternView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), patterns.size() % 2);
    }

    @Override
    public void onBindViewHolder(PatternView holder, int position) {
        holder.bind(patterns.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(patterns, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(patterns, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
