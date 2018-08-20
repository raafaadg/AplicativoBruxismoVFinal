package br.com.monitoramento.bruxismo;


public class Paciente {
    // fields
    private int pacienteID;
    private String pacienteNome;
    private int pacienteIdade;
    private int pacientePeso;
    private String pacienteGenero;
    private String pacienteEmail;

    // constructors
    public Paciente() {}
    public Paciente(int pacienteID, String pacienteNome, int pacienteIdade, int pacientePeso,
                    String pacienteGenero, String pacienteEmail) {
        this.pacienteID = pacienteID;
        this.pacienteNome = pacienteNome;
        this.pacienteIdade = pacienteIdade;
        this.pacientePeso = pacientePeso;
        this.pacienteGenero = pacienteGenero;
        this.pacienteEmail = pacienteEmail;
    }
    // properties
    public void setID(int id) {
        this.pacienteID = id;
    }
    public int getID() {
        return this.pacienteID;
    }
    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }
    public String getPacienteNome() {
        return this.pacienteNome;
    }

    public void setPacienteIdade(int pacienteIdade) {
        this.pacienteIdade = pacienteIdade;
    }
    public int getPacienteIdade() {
        return this.pacienteIdade;
    }

    public void setPacientePeso(int pacientePeso) {
        this.pacientePeso = pacientePeso;
    }
    public int getPacientePeso() {
        return this.pacientePeso;
    }

    public void setPacienteGenero(String pacienteGenero) {
        this.pacienteGenero = pacienteGenero;
    }
    public String getPacienteGenero() {
        return this.pacienteGenero;
    }

    public void setPacienteemail(String pacienteEmail) {
        this.pacienteEmail = pacienteEmail;
    }
    public String getPacienteEmail() {
        return this.pacienteEmail;
    }
}
