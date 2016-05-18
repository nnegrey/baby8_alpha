package edu.calpoly.nnegrey.baby8alpha;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import java.util.ArrayList;

/**
 * Created by noahnegrey on 5/15/16.
 */
public class PatternView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textViewPatternName;
    private RecyclerView commandLayout;
    private CommandListAdapter commandAdapter;

    private Pattern pattern;

    public PatternView(View itemView, int colorSelection) {
        super(itemView);

        textViewPatternName = (TextView) itemView.findViewById(R.id.patternViewTextViewPatternName);
        commandLayout = (RecyclerView) itemView.findViewById(R.id.patternViewReyclerViewCommands);
        itemView.setOnClickListener(this);
        commandLayout.setOnClickListener(this);
    }

    public void bind(Pattern p) {
        setPattern(p);
    }

    public void setPattern(Pattern p) {
        pattern = p;
        textViewPatternName.setText(pattern.getPatternName());
        commandAdapter = new CommandListAdapter(pattern.getCommands());
        commandLayout.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
        commandLayout.setAdapter(commandAdapter);

    }

    public Pattern getCommand() {
        return pattern;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), pattern.toString(), Toast.LENGTH_SHORT).show();
    }
}
