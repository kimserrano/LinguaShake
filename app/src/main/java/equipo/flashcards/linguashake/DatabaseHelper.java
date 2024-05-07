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
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // nombre de la base de datos
    public static final String databaseName ="LinguaShake.db";

    // constructor para crear la base, se le pasa el 1 para las versiones
    public DatabaseHelper(@Nullable Context context) {

        super(context, "LinguaShake.db", null, 3);
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
        db.execSQL("create Table questions(id INTEGER PRIMARY KEY AUTOINCREMENT, pregunta TEXT,opcion1 TEXT,opcion2 TEXT,opcion3 TEXT, respuesta INTEGER,dificultad INTEGER, idioma TEXT)");

        // descomentareen la linea 35 para que se inserten los datos de las tarjetas, si no no les va a aparecer contenido
        insertarDatosDeEjemplo(db);
        insertarDatosDeEjemploPreguntas(db);
        insertarDatosDeEjemploPreguntasChino(db);
        insertarDatosDeEjemploPreguntasDificilesChino(db);
        insertarDatosDeEjemploPreguntasDificiles(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // si la version cambia, elimina la base para asi poder actualizar la estructura
        db.execSQL("drop Table if exists allusers");
        db.execSQL("drop Table if exists flashcards");
        db.execSQL("drop Table if exists questions");
        onCreate(db);
    }
    // Método para obtener 5 preguntas aleatorias sin repetición
    public List<Pregunta> obtenerPreguntasAleatorias(String idioma, int dificultad) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Pregunta> preguntas = new ArrayList<>();

        // Obtener el número total de preguntas en la base de datos para el idioma y la dificultad especificados
        int totalPreguntas = obtenerTotalPreguntas(db, idioma, dificultad);

        if (totalPreguntas < 5) {
            return preguntas; // No hay suficientes preguntas en la base de datos para el idioma y la dificultad especificados
        }

        // Crear una lista con los índices de todas las preguntas en la base de datos para el idioma y la dificultad especificados
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i <= totalPreguntas; i++) {
            indices.add(i);
        }

        // Mezclar los índices para obtener un orden aleatorio
        Collections.shuffle(indices);

        // Seleccionar las primeras 5 preguntas aleatorias
        for (int i = 0; i < 5; i++) {
            int indice = indices.get(i);
            Pregunta pregunta = obtenerPreguntaPorIndice(db, indice, idioma, dificultad);
            preguntas.add(pregunta);
        }

        return preguntas;
    }
    public int obtenerTotalPreguntas(SQLiteDatabase db, String idioma, int dificultad) {
        int totalPreguntas = 0;
        Cursor cursor = null;
        try {
            String[] selectionArgs = {idioma, String.valueOf(dificultad)};
            cursor = db.rawQuery("SELECT COUNT(*) FROM questions WHERE idioma = ? AND dificultad = ?", selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                totalPreguntas = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error al obtener el total de preguntas: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalPreguntas;
    }

    public Pregunta obtenerPreguntaPorIndice(SQLiteDatabase db, int indice, String idioma, int dificultad) {
        Pregunta pregunta = null;
        Cursor cursor = null;
        try {
            String[] selectionArgs = {idioma, String.valueOf(dificultad), String.valueOf(indice)};
            cursor = db.rawQuery("SELECT * FROM questions WHERE idioma = ? AND dificultad = ? ORDER BY RANDOM() LIMIT 1 OFFSET ?", selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int textoPreguntaIndex = cursor.getColumnIndex("pregunta");
                int opcion1Index = cursor.getColumnIndex("opcion1");
                int opcion2Index = cursor.getColumnIndex("opcion2");
                int opcion3Index = cursor.getColumnIndex("opcion3");
                int respuestaIndex = cursor.getColumnIndex("respuesta");
                int dificultadIndex = cursor.getColumnIndex("dificultad");
                int idiomaIndex = cursor.getColumnIndex("idioma");

                int id = cursor.getInt(idIndex);
                String textoPregunta = cursor.getString(textoPreguntaIndex);
                String opcion1 = cursor.getString(opcion1Index);
                String opcion2 = cursor.getString(opcion2Index);
                String opcion3 = cursor.getString(opcion3Index);
                int respuesta = cursor.getInt(respuestaIndex);
                int dificul = cursor.getInt(dificultadIndex);
                String idi = cursor.getString(idiomaIndex);
                pregunta = new Pregunta(id, textoPregunta, opcion1, opcion2, opcion3, respuesta,dificul, idi);
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseHelper", "Error al obtener la pregunta por índice: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return pregunta;
    }
    private Pregunta obtenerPreguntaPorIndice( int indice) {
        Cursor cursor =null;
        Pregunta pregunta = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT * FROM questions WHERE id = ?", new String[]{String.valueOf(indice)});

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int textoPreguntaIndex = cursor.getColumnIndex("pregunta");
                int opcion1Index = cursor.getColumnIndex("opcion1");
                int opcion2Index = cursor.getColumnIndex("opcion2");
                int opcion3Index = cursor.getColumnIndex("opcion3");
                int respuestaIndex = cursor.getColumnIndex("respuesta");
                int dificultadIndex = cursor.getColumnIndex("dificultad");
                int idiomaIndex = cursor.getColumnIndex("idioma");

                int id = cursor.getInt(idIndex);
                String textoPregunta = cursor.getString(textoPreguntaIndex);
                String opcion1 = cursor.getString(opcion1Index);
                String opcion2 = cursor.getString(opcion2Index);
                String opcion3 = cursor.getString(opcion3Index);
                int respuesta = cursor.getInt(respuestaIndex);
                int dificultad = cursor.getInt(dificultadIndex);
                String idioma = cursor.getString(idiomaIndex);
                pregunta = new Pregunta(id, textoPregunta, opcion1, opcion2, opcion3, respuesta,dificultad, idioma);
            }
        }catch (Exception e){

        }
        cursor.close();
        return pregunta;
    }
    // Método para obtener el número total de preguntas en la base de datos
    private int obtenerTotalPreguntas(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM questions";
        Cursor cursor = db.rawQuery(query, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    private void insertarDatosDeEjemploPreguntas(SQLiteDatabase db) {
        // Pregunta 1
        insertarPregunta(db, "What color is the sky?", "Green", "Blue", "Red", 2,1, "ingles");
        // Pregunta 2
        insertarPregunta(db, "What animal says \"moo\"?", "Dog", "Cow", "Cat", 2, 1, "ingles");
        // Pregunta 3
        insertarPregunta(db, "Which of these is a fruit?", "Carrot", "Banana", "Broccoli", 2, 1, "ingles");
        // Pregunta 4
        insertarPregunta(db, "What is the name of the big, yellow celestial body that shines in the sky during the day?", "Moon", "Star", "Sun", 3, 1, "ingles");
        // Pregunta 5
        insertarPregunta(db, "What do we use to brush our teeth?", "Spoon", "Toothbrush", "Fork", 2, 1, "ingles");
        // Pregunta 6
        insertarPregunta(db, "Which animal has a long trunk?", "Elephant", "Giraffe", "Lion", 1, 1, "ingles");
        // Pregunta 7
        insertarPregunta(db, "What is the opposite of cold?", "Hot", "Warm", "Cool", 1, 1, "ingles");
        // Pregunta 8
        insertarPregunta(db, "Which of these is a type of transport that flies in the sky?", "Car", "Boat", "Plane", 3, 1, "ingles");
        // Pregunta 9
        insertarPregunta(db, "What do we use to write on paper?", "Pencil", "Spoon", "Plate", 1, 1, "ingles");
        // Pregunta 10
        insertarPregunta(db, "What do we wear on our feet?", "Hat", "Socks", "Shoes", 3, 1, "ingles");
    }
    private void insertarDatosDeEjemploPreguntasDificiles(SQLiteDatabase db) {
        // Pregunta 1
        insertarPregunta(db, "What is the chemical symbol for gold?", "Au", "Ag", "Fe", 1, 2, "ingles");
        // Pregunta 2
        insertarPregunta(db, "Who wrote the novel '1984'?", "George Orwell", "J.K. Rowling", "Ernest Hemingway", 1, 2, "ingles");
        // Pregunta 3
        insertarPregunta(db, "What is the capital of Australia?", "Sydney", "Melbourne", "Canberra", 3, 2, "ingles");
        // Pregunta 4
        insertarPregunta(db, "Which planet is known as the Red Planet?", "Earth", "Venus", "Mars", 3, 2, "ingles");
        // Pregunta 5
        insertarPregunta(db, "Who painted the 'Mona Lisa'?", "Pablo Picasso", "Leonardo da Vinci", "Vincent van Gogh", 2, 2, "ingles");
        // Pregunta 6
        insertarPregunta(db, "What is the longest river in the world?", "Amazon", "Nile", "Mississippi", 1, 2, "ingles");
        // Pregunta 7
        insertarPregunta(db, "What is the chemical symbol for water?", "Wa", "H2O", "W", 2, 2, "ingles");
        // Pregunta 8
        insertarPregunta(db, "Who wrote the play 'Hamlet'?", "William Shakespeare", "Anton Chekhov", "Fyodor Dostoevsky", 1, 2, "ingles");
        // Pregunta 9
        insertarPregunta(db, "What is the tallest mountain in the world?", "K2", "Mount Everest", "Kangchenjunga", 2, 2, "ingles");
        // Pregunta 10
        insertarPregunta(db, "Who discovered penicillin?", "Alexander Fleming", "Marie Curie", "Albert Einstein", 1, 2, "ingles");
    }
    private void insertarDatosDeEjemploPreguntasDificilesChino(SQLiteDatabase db) {
        // Pregunta 1
        insertarPregunta(db, "黄金的化学符号是什么？", "Au", "Ag", "Fe", 1, 2, "chino");
        // Pregunta 2
        insertarPregunta(db, "谁写了小说《1984年》？", "乔治·奥威尔", "J·K·罗琳", "厄内斯特·海明威", 1, 2, "chino");
        // Pregunta 3
        insertarPregunta(db, "澳大利亚的首都是哪个城市？", "悉尼", "墨尔本", "堪培拉", 3, 2, "chino");
        // Pregunta 4
        insertarPregunta(db, "哪颗行星被称为红色星球？", "地球", "金星", "火星", 3, 2, "chino");
        // Pregunta 5
        insertarPregunta(db, "谁画了《蒙娜丽莎》？", "巴勃罗·毕加索", "莱昂纳多·达·芬奇", "文森特·梵高", 2, 2, "chino");
        // Pregunta 6
        insertarPregunta(db, "世界上最长的河流是哪条？", "亚马逊河", "尼罗河", "密西西比河", 1, 2, "chino");
        // Pregunta 7
        insertarPregunta(db, "水的化学符号是什么？", "Wa", "H2O", "W", 2, 2, "chino");
        // Pregunta 8
        insertarPregunta(db, "谁写了戏剧《哈姆雷特》？", "威廉·莎士比亚", "安东·契诃夫", "费奥多尔·陀思妥耶夫斯基", 1, 2, "chino");
        // Pregunta 9
        insertarPregunta(db, "世界上最高的山是哪座？", "K2", "珠穆朗玛峰", "康奇朱姆峰", 2, 2, "chino");
        // Pregunta 10
        insertarPregunta(db, "谁发现了青霉素？", "亚历山大·弗莱明", "玛丽·居里", "阿尔伯特·爱因斯坦", 1, 2, "chino");
    }
    private void insertarDatosDeEjemploPreguntasChino(SQLiteDatabase db) {
        // Pregunta 1
        insertarPregunta(db, "天空是什么颜色的？", "绿色", "蓝色", "红色", 2,1,  "chino");

        // Pregunta 2
        insertarPregunta(db, "猫会说什么？", "汪汪", "喵喵", "叫叫", 2, 1, "chino");

        // Pregunta 3
        insertarPregunta(db, "下面哪个是水果？", "胡萝卜", "香蕉", "西兰花", 2, 1, "chino");

        // Pregunta 4
        insertarPregunta(db, "在白天天空中闪耀的大黄色天体叫什么？", "月亮", "星星", "太阳", 3, 1, "chino");

        // Pregunta 5
        insertarPregunta(db, "我们用什么刷牙？", "勺子", "牙刷", "叉子", 2, 1, "chino");

        // Pregunta 6
        insertarPregunta(db, "哪种动物有长长的象鼻？", "大象", "长颈鹿", "狮子", 1, 1, "chino");

        // Pregunta 7
        insertarPregunta(db, "冷的反义词是什么？", "热", "温暖", "凉爽", 1, 1, "chino");

        // Pregunta 8
        insertarPregunta(db, "下面哪个是在天空中飞行的交通工具？", "汽车", "船", "飞机", 3, 1, "chino");

        // Pregunta 9
        insertarPregunta(db, "我们用什么在纸上写字？", "铅笔", "勺子", "盘子", 1,1,  "chino");

        // Pregunta 10
        insertarPregunta(db, "我们穿什么在脚上？", "帽子", "袜子", "鞋子", 3, 1, "chino");
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
    private void insertarPregunta(SQLiteDatabase db, String pregunta, String opcion1, String opcion2, String opcion3, int respuesta,int dificultad, String idioma) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pregunta", pregunta);
        contentValues.put("opcion1", opcion1);
        contentValues.put("opcion2", opcion2);
        contentValues.put("opcion3", opcion3);
        contentValues.put("respuesta", respuesta);
        contentValues.put("dificultad", respuesta);
        contentValues.put("idioma", idioma);
        db.insert("questions", null, contentValues);
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
                    int idiomaIndex = cursor.getColumnIndex("idioma");
                    String idioma = cursor.getString(idiomaIndex);
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
