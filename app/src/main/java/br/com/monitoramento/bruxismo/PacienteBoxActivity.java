package br.com.monitoramento.bruxismo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class PacienteBoxActivity {

    @Id
    private long id;

    private String nome;
    private int idade;
    private int peso;
    private String genero;
    private String email;

    // properties
    public void setID(int id) {
        this.id = id;
    }
    public long getID() {
        return this.id;
    }
    public void setPacienteNome(String pacienteNome) {
        this.nome = pacienteNome;
    }
    public String getPacienteNome() {
        return this.nome;
    }

    public void setPacienteIdade(int pacienteIdade) {
        this.idade = pacienteIdade;
    }
    public int getPacienteIdade() {
        return this.idade;
    }

    public void setPacientePeso(int pacientePeso) {
        this.peso = pacientePeso;
    }
    public int getPacientePeso() {
        return this.peso;
    }

    public void setPacienteGenero(String pacienteGenero) {
        this.genero = pacienteGenero;
    }
    public String getPacienteGenero() {
        return this.genero;
    }

    public void setPacienteemail(String pacienteEmail) {
        this.email = pacienteEmail;
    }
    public String getPacienteEmail() {
        return this.email;
    }
}
