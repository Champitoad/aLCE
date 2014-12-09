package donato.pablo.alce;

import android.app.ListActivity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.view.View;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import java.util.ArrayList;

public class SentencesActivity extends ListActivity implements EditDialog.EditDialogListener
{
	// Variables

	private SentencesManager sentencesManager = new SentencesManager(this);
	private ArrayList<String> sentences = new ArrayList<String>();
	private ArrayList<String> selectedSentences = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private MenuItem editMenuItem;
	private ActionMode actionMode;

	// Appelé au lancement de l'application

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.sentences);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		sentences = sentencesManager.getSentences();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, sentences);
		setListAdapter(adapter);

		// On implémente le menu contextuel permettant de supprimer les phrases

		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener()
		{
			// Lorsqu'une phrase dans la liste est sélectionnée ou désélectionnée

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
			{
				if (checked) {
					selectedSentences.add(adapter.getItem(position));
				} else {
					selectedSentences.remove(adapter.getItem(position));
				}

				if (selectedSentences.size() == 1) {
					editMenuItem.setVisible(true);
				} else {
					editMenuItem.setVisible(false);
				}
			}

			// Lorsque l'on clique sur un des éléments du menu contextuel

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item)
			{
				switch (item.getItemId()) {
					case R.id.menu_edit : // Editer la phrase
						String selectedSentence = selectedSentences.get(0);
						EditDialog editDialog = EditDialog.newInstance(selectedSentence, sentences.indexOf(selectedSentence));
						editDialog.show(getFragmentManager(), "EditDialog");
						actionMode = mode;
						return true;	
				    case R.id.menu_delete : // Supprimer la ou les phrases
						sentencesManager.remove(selectedSentences);	
				        mode.finish();
				        return true;
				    default :
				        return false;
				}
			}

			// Lorsque le menu contextuel apparaît

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu)
			{
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.sentence_menu, menu);
				editMenuItem = menu.getItem(0);
				return true;
			}

			// Lorsque le menu contextuel disparaît

			@Override
			public void onDestroyActionMode(ActionMode mode)
			{
				selectedSentences.clear();
			}

			// Ne sert à rien

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu)
			{
				return false;
			}
		});
	}
	
	// Pour revenir à l'écran d'accueil lorsqu'on clique en haut à gauche

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		    case android.R.id.home:
				finish();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		}
	}


	// Callbacks de l'EditDialog

	@Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        sentencesManager.refreshList();
        actionMode.finish();
    }

	@Override
    public void onDialogNegativeClick(DialogFragment dialog) {}
}
