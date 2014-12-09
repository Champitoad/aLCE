package donato.pablo.alce;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.LayoutInflater;
import java.util.ArrayList;

public class EditDialog extends DialogFragment
{
	// Attributs

	private String sentenceToEdit;
	private int sentenceIndex;
	private EditText sentenceEditText;

	// Fonctions permettant d'implémenter les callbacks de l'EditDialog dans les classes l'utilisant

	public interface EditDialogListener
	{
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	EditDialogListener mListener;

	@Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (EditDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditDialogListener");
        }
    }

	// Fonction lancée à la création de l'EditDialog

	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final SentencesManager sentencesManager = new SentencesManager(getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		sentenceToEdit = getArguments().getString("sentenceToEdit");
		sentenceIndex = getArguments().getInt("sentenceIndex");

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.edit_dialog, null);

		sentenceEditText = (EditText)v.findViewById(R.id.sentenceEditText);
		sentenceEditText.setText(sentenceToEdit);

		builder.setView(v);

		DialogInterface.OnClickListener onClickListener =
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE :
							ArrayList<String> sentenceToEditArrayList = new ArrayList<String>();
							sentenceToEditArrayList.add(sentenceToEdit);
							sentencesManager.remove(sentenceToEditArrayList);
							sentencesManager.add(getEditedSentence(), sentenceIndex);
							mListener.onDialogPositiveClick(EditDialog.this);
							break;
					}
				}
			};
		builder.setPositiveButton("Enregistrer", onClickListener);
		builder.setNegativeButton("Annuler", onClickListener);

		return builder.create();
	}

	// Fonction permettant de créer une nouvelle EditDialog en passant la phrase à modifiée en paramètre, et éventuellement son index

	static EditDialog newInstance(String sentence, int index)
	{
		EditDialog ed = new EditDialog();
		Bundle args = new Bundle();

		args.putString("sentenceToEdit", sentence);
		args.putInt("sentenceIndex", index);

		ed.setArguments(args);
		return ed;
	}

	// Fonction permettant de récupérer la phrase éditée dans l'EditText

	public String getEditedSentence()
	{
		return sentenceEditText.getText().toString();
	}
}
