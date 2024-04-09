package equipo.flashcards.linguashake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // nombre de la base de datos
    public static final String databaseName ="Singup.db";

    // constructor para crear la base, se le pasa el 1 para las versiones
    public DatabaseHelper(@Nullable Context context) {

        super(context, "Singup.db", null, 1);
        // para ver la ruta de la bd
        Log.d("DatabaseHelper", "Database created or opened at path: " + context.getDatabasePath(databaseName));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // crea la tabla cuando la base de datos es creada por primera vez
        db.execSQL("create Table allusers(email TEXT primary key, password TEXT)");
        // la tabla se llama allusers y tiene el email que es la llave primaria y el password

        // tabla para las tarjetas, tiene id, frase y la traduccion que es la respuesta
        db.execSQL("create Table flashcards(id INTEGER PRIMARY KEY AUTOINCREMENT, frase TEXT, respuesta TEXT)");

        // insertarDatosDeEjemplo(db);
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

    public TarjetaObj obtenerProximaTarjeta() {
        TarjetaObj tarjeta = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            // para seleccionar una tarjeta aleatoria de la tabla flashcards
            cursor = db.rawQuery("SELECT * FROM flashcards ORDER BY RANDOM() LIMIT 1", null);

            if (cursor != null && cursor.moveToFirst()) {
                // índices de las columnas
                int idIndex = cursor.getColumnIndex("id");
                int fraseIndex = cursor.getColumnIndex("frase");
                int respuestaIndex = cursor.getColumnIndex("respuesta");

                // los valores de la fila actual utilizando los índices
                int id = cursor.getInt(idIndex);
                String frase = cursor.getString(fraseIndex);
                String respuesta = cursor.getString(respuestaIndex);

                //objeto Tarjeta con los valores obtenidos
                tarjeta = new TarjetaObj(id, frase, respuesta);
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error al obtener la próxima tarjeta: " + e.getMessage());
        } finally {
            // Cerrar el cursor y la base de datos
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tarjeta;
    }


    private void insertarDatosDeEjemplo(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("frase", "May I have the bill please?");
        contentValues.put("respuesta", "¿Me trae la cuenta, por favor?");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "Where is the restroom?");
        contentValues.put("respuesta", "¿Dónde está el baño?");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "How much does it cost?");
        contentValues.put("respuesta", "¿Cuánto cuesta?");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "What time is it?");
        contentValues.put("respuesta", "¿Qué hora es?");
        db.insert("flashcards", null, contentValues);

        contentValues.put("frase", "I would like a coffee, please.");
        contentValues.put("respuesta", "Quisiera un café, por favor.");
        db.insert("flashcards", null, contentValues);

    }

}
