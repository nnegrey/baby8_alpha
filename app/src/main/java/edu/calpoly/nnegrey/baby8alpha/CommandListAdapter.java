package edu.calpoly.nnegrey.baby8alpha;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by noahnegrey on 5/15/16.
 */
public class CommandListAdapter extends RecyclerView.Adapter<CommandView> {

    private List<Command> commandList;

    public CommandListAdapter(ArrayList<Command> commands) {
        this.commandList = commands;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.command_view;
    }

    @Override
    public CommandView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommandView(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(CommandView holder, int position) {
        holder.bind(commandList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return commandList.size();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(commandList, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(commandList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
