package cdi.tp_android_convertisseur;

import android.app.DialogFragment;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
                          implements SelectionDevises.SelectionDevisesListenner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_apropos:
                Intent intent = new Intent(getApplicationContext(), AProposActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_langage:
                Intent changerLangue = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(changerLangue);
                return true;
            case R.id.action_date:
                Intent changerDate = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(changerDate);
                return true;
            case R.id.action_display:
                Intent changerAffichage = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                startActivity(changerAffichage);
                return true;
            case R.id.action_quitter:
                onClickQuitter(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // méthode appelée sur un click des TextView de séléction des devises
    public void onClickSelectDevise(View edt){
        //DialogFragment dialog = new SelectionDevises();
        DialogFragment dialog = SelectionDevises.newInstance(edt.getId());
        dialog.show(getFragmentManager(), "SelectionDevises");

    }

    // méthode appelée par la fermeture de la boite de dialogue
    @Override
    public void onSelectDevise(String nomDevise, int idTextView) {
        //toast("devise sélectionnée : " + nomDevise);
        ((TextView)findViewById(idTextView)).setText(nomDevise);
    }

    // Méthode appelée sur l'évènement OnClick du bouton "buttonConv"
    public void onClickConvertir(View v){
        // Si champs non valides, affichage du message d'anomalie
        String message = validation();
        if (!message.equals("ok")){
            toast(message);
        } else {
            // Création de l'Intent permettant d'afficher l'activité Resultat
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            // récupération des valeurs
            String source = ((TextView)findViewById(R.id.editTextDevDep)).getText().toString();
            String cible = ((TextView)findViewById(R.id.editTextDevArr)).getText().toString();
            double montant = Double.parseDouble(((EditText)findViewById(R.id.editTextMont)).getText().toString());

            // On affecte à l'Intent les données à passer
            intent.putExtra("devDep", source);
            intent.putExtra("devArr", cible);
            intent.putExtra("montant", montant);

            // Démarrage de l'activité Resultat
            startActivity(intent);
        }
    }

    // Méthode appelée sur l'évènement OnClick du bouton "buttonQuit"
    public void onClickQuitter(View v){
        toast(getString(R.string.app_fin));
        finish();
    }

    /**
     * Validation des champs de saisie
     * @return : message d'erreur, "ok" si pas d'erreur
     */
    public String validation(){
        String message = getString(R.string.message_ok);

        // Devise départ, il faut récupérer la valeur sélectionnée du spinner spiDevDep

        // Création d'une référence sur le spinner de l'activité
        TextView spiDevDep = (TextView)findViewById(R.id.editTextDevDep);

        // Récupération de la devise sous forme de chaine de caractères
        String strDevDep = spiDevDep.getText().toString();

        // Contrôle que la chaine n'est pas vide
        if (strDevDep.equals("")){
            message = getString(R.string.message_err_dev_dep);

            // Devise d'arrivée, la même chose en une ligne
        } else if (((TextView)findViewById(R.id.editTextDevArr)).getText().toString().equals("")) {
            message = getString(R.string.message_err_dev_arr);

            // Vérification du montant non-nul
        } else if (((EditText)findViewById(R.id.editTextMont)).getText().toString().equals("")) {
            message = getString(R.string.message_err_mont);

            // Vérification montant numérique
        } else {

            try {
                Double.valueOf(((EditText)findViewById(R.id.editTextMont)).getText().toString());
            } catch (NumberFormatException e) {

                message = getString(R.string.message_err_mont);
            }
        }

        // Pas d'anomalies détectées, validate() renvoie vrai
        return message;
    }


    public void toast(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

}
