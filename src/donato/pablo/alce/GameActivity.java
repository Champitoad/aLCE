package donato.pablo.alce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.DialogInterface;

public class GameActivity extends Activity implements EditDialog.EditDialogListener
{
    /* Constantes */

    private final int SUJET = 0, ADJECTIF = 1, VERBE = 2, ADVERBE = 3, COD = 4, CC = 5;
    private final String[] wordsTypes = {
        "Sujet",
        "Adjectif",
        "Verbe",
        "Adverbe",
        "Complément d'objet",
        "Complément circonstanciel"
    };
    private final String[] wordsExamples = {
        "le boulanger",
        "philanthrope",
        "mange",
        "sciemment",
        "une cuillère",
        "dans le confessionnal"
    };

    /* Variables */

    private SentencesManager sentencesManager = new SentencesManager(this);

    private String sentence = "";
    private String[] words = new String[6];
    private String currentWord;
    private int currentWordType = SUJET;

    private Button nextButton;
    private TextView wordTypeTextView;
    private TextView genderNumberTextView;
    private EditText wordEditText;
    private LinearLayout genderNumberLayout;
    private Spinner genderSpinner;
    private Spinner numberSpinner;

    private AlertDialog exitDialog;

    // Appelé au lancement de l'activité
    @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.game);

            getActionBar().setDisplayHomeAsUpEnabled(true);

            exitDialog = new AlertDialog.Builder(this).create();
            exitDialog.setMessage("Voulez-vous vraiment interrompre la partie et perdre votre phrase ?");
            DialogInterface.OnClickListener exitOnClickListener =
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE :
                                finish();
                                break;
                        }
                    }
                };
            exitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Oui", exitOnClickListener);
            exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Non", exitOnClickListener);

            nextButton = (Button)findViewById(R.id.nextButton);
            wordTypeTextView = (TextView)findViewById(R.id.wordTypeTextView);
            genderNumberTextView = (TextView)findViewById(R.id.genderNumberTextView);
            wordEditText = (EditText)findViewById(R.id.wordEditText);
            genderNumberLayout = (LinearLayout)findViewById(R.id.genderNumberLayout);
            genderSpinner = (Spinner)findViewById(R.id.genderSpinner);
            numberSpinner = (Spinner)findViewById(R.id.numberSpinner);

            refreshView(currentWordType);
        }

    // Pour revenir à l'écran d'accueil lorsqu'on clique sur le bouton système Précédent
    public void onBackPressed()
    {
        exitDialog.show();
    }

    // Pour revenir à l'écran d'accueil lorsqu'on clique en haut à gauche
    @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId()) {
                case android.R.id.home:
                    exitDialog.show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    // Rafraîchit la vue pour s'adapter au bon type de mot
    public void refreshView(int currentWordType)
    {
        currentWord = words[currentWordType];
        wordEditText.setHint("ex : " + wordsExamples[currentWordType]);
        wordEditText.setText(currentWord);

        wordTypeTextView.setText(wordsTypes[currentWordType]);

        if (currentWordType == SUJET) {
            genderNumberLayout.setVisibility(View.VISIBLE);
            genderNumberTextView.setVisibility(View.GONE);
        }
        else {
            genderNumberLayout.setVisibility(View.GONE);
            genderNumberTextView.setVisibility(View.VISIBLE);

            TextView gender = (TextView)genderSpinner.getSelectedView();
            TextView number = (TextView)numberSpinner.getSelectedView();
            if (currentWordType == ADJECTIF) {
                genderNumberTextView.setText(gender.getText().toString()+" "+number.getText().toString());
            }
            else if (currentWordType == VERBE) {
                genderNumberTextView.setText(number.getText().toString());
            }
            else {
                genderNumberTextView.setVisibility(View.GONE);
            }

            if (currentWordType == CC) {
                nextButton.setText("Terminer");
            }
        }
    }

    // Callback du bouton "Suivant"
    public void next(View view)
    {
        currentWord = wordEditText.getText().toString();
        words[currentWordType] = currentWord;
        if (currentWordType != CC) {
            currentWordType++;
            refreshView(currentWordType);
        }
        else {
            for (int i = 0 ; i < 5 ; i++) {
                sentence += words[i] + " ";
            }
            sentence += words[5] + ".";
            char[] sentenceCharArray = sentence.toCharArray();
            sentenceCharArray[0] = Character.toUpperCase(sentenceCharArray[0]);
            sentence = new String(sentenceCharArray);

            AlertDialog sentenceDialog = new AlertDialog.Builder(this).create();
            sentenceDialog.setTitle("Votre phrase !");
            sentenceDialog.setMessage(sentence);

            DialogInterface.OnClickListener sentenceOnClickListener =
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE :
                                sentencesManager.add(sentence, 0);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEUTRAL :
                                EditDialog editDialog = EditDialog.newInstance(sentence, 0);
                                editDialog.show(getFragmentManager(), "EditDialog");
                                break;
                            case DialogInterface.BUTTON_NEGATIVE :
                                finish();
                                break;
                        }
                    }
                };

            sentenceDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                public void onDismiss(DialogInterface dialog)
                {
                    sentence = "";
                    refreshView(currentWordType);
                }
            });

            sentenceDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Enregistrer", sentenceOnClickListener);
            sentenceDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Modifier", sentenceOnClickListener);
            sentenceDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quitter", sentenceOnClickListener);

            sentenceDialog.show();
        }
    }

    /* Callbacks de l'EditDialog */

    @Override
        public void onDialogPositiveClick(DialogFragment dialog)
        {
            finish();
        }

    @Override
        public void onDialogNegativeClick(DialogFragment dialog) {}
}
