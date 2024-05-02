package equipo.flashcards.linguashake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // nombre de la base de datos
    public static final String databaseName ="LinguaShake.db";

    // constructor para crear la base, se le pasa el 1 para las versiones
    public DatabaseHelper(@Nullable Context context) {

        super(context, "LinguaShake.db", null, 1);
        // para ver la ruta de la bd
        Log.d("DatabaseHelper", "Database created or opened at path: " + context.getDatabasePath(databaseName));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // crea la tabla cuando la base de datos es creada por primera vez
        db.execSQL("create Table allusers(email TEXT primary key, password TEXT)");
        // la tabla se llama allusers y tiene el email que es la llave primaria y el password

        // tabla para las tarjetas, tiene id, frase y la traduccion que es la respuesta
        db.execSQL("create Table flashcards(id INTEGER PRIMARY KEY AUTOINCREMENT, frase TEXT, respuesta TEXT, idioma TEXT)");

        // descomentareen la linea 35 para que se inserten los datos de las tarjetas, si no no les va a aparecer contenido
        insertarDatosDeEjemplo(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // si la version cambia, elimina la base para asi poder actualizar la estructura
    db.execSQL("drop Table if exists allusers");
        db.execSQL("drop Table if exists flashcards");
        onCreate(db);
    }

    // para insertar un usuario
    public Boolean insertData (String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        // instancia de la bd
        ContentValues contentValues= new ContentValues();
        // se pasan los valores
        contentValues.put("email",email);
        contentValues.put("password", password);
        // si el result es -1 es que la guardad fall[o
        long result = MyDatabase.insert("allusers", null, contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    // para ver si un email existe
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        // consulta para ver si ya esta este email
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});
        // si el cursor tiene algún resultado, significa que el email existe
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    // para ver si el email y la contra coinciden
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        //consulta SQL para buscar el email y contraseña en la tabla
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email, password});
        // si hay resultado si coinciden
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    public List<TarjetaObj> obtenerProximaTarjeta(String idioma) {
        List<TarjetaObj> tarjetas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM flashcards WHERE idioma = ?",  new String[]{idioma});

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int fraseIndex = cursor.getColumnIndex("frase");
                int respuestaIndex = cursor.getColumnIndex("respuesta");
                int idiomaIndex = cursor.getColumnIndex("idioma");

                // primeras 5 tarjetas y agregarlos a la lista de tarjetas
                do {
                    int id = cursor.getInt(idIndex);
                    String frase = cursor.getString(fraseIndex);
                    String respuesta = cursor.getString(respuestaIndex);
                    String idiomax = cursor.getString(idiomaIndex);
                    TarjetaObj tarjeta = new TarjetaObj(id, frase, respuesta, idiomax);
                    tarjetas.add(tarjeta);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error al obtener las próximas tarjetas: " + e.getMessage());
        } finally {
            // cerrar el cursor y la base de datos
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tarjetas;
    }



    private void insertarDatosDeEjemplo(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("frase", "May I have the bill please?");
        contentValues.put("respuesta", "¿Me trae la cuenta, por favor?");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "Where is the restroom?");
        contentValues.put("respuesta", "¿Dónde está el baño?");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "How much does it cost?");
        contentValues.put("respuesta", "¿Cuánto cuesta?");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "What time is it?");
        contentValues.put("respuesta", "¿Qué hora es?");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "I would like a coffee, please.");
        contentValues.put("respuesta", "Quisiera un café, por favor.");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "Hello");
        contentValues.put("respuesta", "Hola.");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "Mano.");
        contentValues.put("respuesta", "Hand.");
        contentValues.put("idioma", "ingles");
        db.insert("flashcards", null, contentValues);

    }

    // para insertar un flashcard
    public boolean insertDataFlashcard(String tema, String frase, String respuesta){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("frase", frase);
        contentValues.put("respuesta", respuesta);
        contentValues.put("idioma", tema);  // Guardar el tema como idioma
        long result = db.insert("flashcards", null, contentValues);
        // si el result es -1 es que la guardad fall[o
        if(result==-1){
            return false;
        } else {
            return true;
        }

    }

    public List<String> obtenerTemasIdiomas() {
        List<String> temasIdiomas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT DISTINCT idioma FROM flashcards", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String idioma = cursor.getString(cursor.getColumnIndex("idioma"));
                    temasIdiomas.add(idioma);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error al obtener temas/idiomas: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return temasIdiomas;
    }



}
