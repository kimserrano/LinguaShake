package equipo.flashcards.linguashake;

public class Pregunta {
    private int id;
    private String textoPregunta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private int respuesta;
    private int dificultad;
    private String idioma;

    public Pregunta(int id, String textoPregunta, String opcion1, String opcion2, String opcion3, int respuesta,int dificultad, String idioma) {
        this.id = id;
        this.textoPregunta = textoPregunta;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.respuesta = respuesta;
        this.dificultad = dificultad;
        this.idioma = idioma;
    }

    public int getId() {
        return id;
    }

    public int getDificultad() {
        return dificultad;
    }
    public String getTextoPregunta() {
        return textoPregunta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public int getRespuesta() {
        return respuesta;
    }
    public String getIdioma() {
        return idioma;
    }
}