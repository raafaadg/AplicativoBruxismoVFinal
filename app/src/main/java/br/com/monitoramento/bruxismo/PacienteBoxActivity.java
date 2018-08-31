package br.com.monitoramento.bruxismo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class PacienteBoxActivity {

    @Id
    long id;

    private String nome;
    private String idade;
    private String peso;
    private String genero;
    private String email;

    public PacienteBoxActivity(long id, String nome, String idade, String peso, String genero,
                               String email){
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        this.genero = genero;
        this.email = email;
    }
    public PacienteBoxActivity(){}

    // properties
    public void setID(int id) {
        this.id = id;
    }
    public long getID() {
        return this.id;
    }

    public void setNome(String pacienteNome) {
        this.nome = pacienteNome;
    }
    public String getNome() {
        return this.nome;
    }

    public void setIdade(String pacienteIdade) {
        this.idade = pacienteIdade;
    }
    public String getIdade() {
        return this.idade;
    }

    public void setPeso(String pacientePeso) {
        this.peso = pacientePeso;
    }
    public String getPeso() {
        return this.peso;
    }

    public void setGenero(String pacienteGenero) {
        this.genero = pacienteGenero;
    }
    public String getGenero() {
        return this.genero;
    }

    public void setEmail(String pacienteEmail) {
        this.email = pacienteEmail;
    }
    public String getEmail() {
        return this.email;
    }
}
