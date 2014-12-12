package donato.pablo.alce;

import android.content.Context;
import android.widget.Toast;
import java.util.ArrayList;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class SentencesManager
{
	/* Variables */

	private ArrayList<String> sentencesArrayList = new ArrayList<String>();
	private Context context;

	SentencesManager(Context activity)
	{
		context = activity;
	}

	// Fonction permettant de rafraîchir la liste des phrases
	public void refreshList()
	{
		sentencesArrayList.clear();
		try { 
			InputStream inputStream = context.openFileInput("sentences.txt");
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(isr);

			String savedSentence;

			while ((savedSentence = bufferedReader.readLine()) != null) {
				sentencesArrayList.add(savedSentence);
			}

			inputStream.close();
		} catch(Exception e) {
            File f = new File("sentences.txt");
            if (f.exists()) 
                Toast.makeText(context, "Erreur lors de la lecture : " + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	// Fonction permettant de récupérer la liste des phrases
	public ArrayList<String> getSentences()
	{
		refreshList();
		return sentencesArrayList;
	}

	// Fonction permettant de sauvegarder une phrase dans un fichier
	public void add(String sentence, int index)
	{
		refreshList();
		sentencesArrayList.add(index, sentence);

		FileOutputStream fOut = null; 
		OutputStreamWriter osw = null; 
		try { 
			fOut = context.openFileOutput("sentences.txt", Context.MODE_PRIVATE);       
			osw = new OutputStreamWriter(fOut);
			for (String savedSentence : sentencesArrayList) {
				osw.write(savedSentence + "\n");
			} 
			osw.flush(); 
			Toast.makeText(context, "Phrase enregistrée", Toast.LENGTH_SHORT).show(); 
		} catch(Exception e) {       
			Toast.makeText(context, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show(); 
		} finally { 
			try { 
				osw.close(); 
				fOut.close(); 
			} catch (IOException e) { 
				Toast.makeText(context, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show(); 
			} 
		}
	}

	// Fonction permettant de supprimer une ou plusieurs phrases
	public void remove(ArrayList<String> removedSentences)
	{
		refreshList();

		FileOutputStream fOut = null; 
		OutputStreamWriter osw = null; 
		try { 
			fOut = context.openFileOutput("sentences.txt", Context.MODE_PRIVATE);       
			osw = new OutputStreamWriter(fOut);
			for (String sentence : removedSentences) {
				sentencesArrayList.remove(sentence);
			}
			for (String sentence : sentencesArrayList) {
				osw.write(sentence + "\n");
			}
			osw.flush(); 
		} catch (Exception e) {       
			Toast.makeText(context, "Erreur lors de la suppression : " + e.toString(), Toast.LENGTH_SHORT).show(); 
		} finally { 
			try { 
				osw.close(); 
				fOut.close(); 
			} catch (IOException e) { 
				Toast.makeText(context, "Erreur lors de la suppression : " + e.toString(), Toast.LENGTH_SHORT).show(); 
			} 
		}
	}
}
