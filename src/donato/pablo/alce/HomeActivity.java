package donato.pablo.alce;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class HomeActivity extends Activity
{
	// Appelé au lancement de l'application

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	}

	// Callback du bouton "Lancer une partie"

	public void launchGame(View view)
	{
		startActivity(new Intent(this, GameActivity.class));
	}

	// Callback du bouton "Phrases enregistrées"

	public void loadSavedSentences(View view)
	{
		startActivity(new Intent(this, SentencesActivity.class));
	}
}
