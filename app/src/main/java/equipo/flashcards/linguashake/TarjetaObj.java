package equipo.flashcards.linguashake;

public class TarjetaObj {
    private int id;
    private String frase;
    private String respuesta;
    private String idioma;

    public TarjetaObj(int id, String frase, String respuesta, String idioma) {
        this.id = id;
        this.frase = frase;
        this.respuesta = respuesta;
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public int getId() {
        return id;
    }

    public String getFrase() {
        return frase;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
