package edu.calpoly.nnegrey.baby8alpha;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CommandView extends RecyclerView.ViewHolder {

	private ImageView imageViewDirection;
	private ImageView imageViewVelocity;
	private TextView textViewVelocity;
	private ImageView imageViewDuration;
	private TextView textViewDuration;
	private ImageView imageViewHeadDegreeIcon;
	private ImageView imageViewHeadDegree;
	private TextView textViewHeadDegree;

	private Command command;


	public CommandView(View itemView) {
		super(itemView);

		imageViewDirection = (ImageView) itemView.findViewById(R.id.commandViewImageViewDirection);
		imageViewVelocity = (ImageView) itemView.findViewById(R.id.commandViewImageViewVelocity);
		textViewVelocity = (TextView) itemView.findViewById(R.id.commandViewTextViewVelocity);
		imageViewDuration = (ImageView) itemView.findViewById(R.id.commandViewImageViewDuration);
		textViewDuration = (TextView) itemView.findViewById(R.id.commandViewTextViewDuration);
		imageViewHeadDegreeIcon = (ImageView) itemView.findViewById(R.id.commandViewImageViewHeadDegreeIcon);
		imageViewHeadDegree = (ImageView) itemView.findViewById(R.id.commandViewImageViewHeadDegree);
		textViewHeadDegree = (TextView) itemView.findViewById(R.id.commandViewTextViewHeadDegree);
	}

	public void bind(Command command) {
		setCommand(command);
	}

	public void setCommand(Command c) {
		command = c;
		if (command.getType() == 0) {
			imageViewDirection.setVisibility(View.VISIBLE);
			imageViewVelocity.setVisibility(View.VISIBLE);
			imageViewDuration.setVisibility(View.VISIBLE);
			imageViewHeadDegreeIcon.setVisibility(View.VISIBLE);
			imageViewHeadDegree.setVisibility(View.VISIBLE);
			imageViewDirection.setRotation(command.getDirection());
			textViewVelocity.setText(String.valueOf(command.getVelocity()));
			textViewDuration.setText(String.valueOf(command.getDuration()));
			if (command.getHeadDegree() < 180) {
				imageViewHeadDegree.setRotation(90);
			} else {
				imageViewHeadDegree.setRotation(270);
			}
			textViewHeadDegree.setText(String.valueOf(command.getHeadDegree()));
		}
		else {
			imageViewDirection.setVisibility(View.GONE);
			imageViewVelocity.setVisibility(View.GONE);
			imageViewDuration.setVisibility(View.GONE);
			imageViewHeadDegreeIcon.setVisibility(View.GONE);
			imageViewHeadDegree.setVisibility(View.GONE);
			textViewVelocity.setText(command.getEffect());
			textViewDuration.setText("");
			textViewHeadDegree.setText("");
		}
	}
}
