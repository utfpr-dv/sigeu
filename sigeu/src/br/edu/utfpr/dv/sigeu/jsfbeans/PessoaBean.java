package br.edu.utfpr.dv.sigeu.jsfbeans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.adamiworks.utils.StringUtils;

import org.omnifaces.cdi.ViewScoped;

import br.edu.utfpr.dv.sigeu.entities.Pessoa;
import br.edu.utfpr.dv.sigeu.service.PessoaService;

@Named
@ViewScoped
public class PessoaBean extends JavaBean {

    @EJB
    private PessoaService pessoaService;

    @Inject
    private LoginBean loginBean;

    private static final long serialVersionUID = 7309144685247380621L;
    private int editarId = 0;
    private String password;
    private boolean passwordModified = false;
    //
    private Pessoa pessoa = new Pessoa();
    //

    @PostConstruct
    public void init() {
	// Padrão
	pessoa.setIdCampus(loginBean.getCampus());
	pessoa.setAdmin(false);
	pessoa.setExterno(true);
	pessoa.setPessoaFisica(true);
	pessoa.setCnpjCpf("0000000000000");
	pessoa.setAtivo(true);
	password = "abracadabra1234";

	HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		.getRequest();

	try {
	    this.editarId = Integer.valueOf(req.getParameter("editarId"));
	} catch (Exception e) {
	    //
	}

	if (this.editarId > 0) {
	    try {
		this.pessoa = pessoaService.encontrePorId(this.editarId);

		if (this.pessoa == null) {
		    this.pessoa = new Pessoa();
		    // this.pessoa.setCampus(new Campus());
		    // this.pessoa.getIdCampus().setIdInstituicao(new
		    // Instituicao());
		    this.addInfoMessage("Carregar", "Pessoa " + this.editarId + " inexistente.");
		}
	    } catch (Exception e) {
		e.printStackTrace();
		addErrorMessage("Carregar", "Erro ao carregar dados");
	    }
	}
    }

    public void passwordChanged() {
	this.passwordModified = true;
	// System.out.println("Password changed!");
    }

    /**
     * Cria uma nova pessoa se o ID for nulo ou 0 ou altera uma pessoa já gravada no
     * banco de dados se ela já existir
     */
    public void gravar() {
	if (editarId == 0) {

	    String hash = StringUtils.generateMD5Hash(this.password);

	    pessoa.setSenhaMd5(hash);

	    // this.addErrorMessage("Gravar", "Inclusão de pessoas é feito através de
	    // Login.");
	    try {
		pessoaService.criar(pessoa);
		String label = pessoa.getIdPessoa() + "-" + pessoa.getNomeCompleto();
		addInfoMessage("Gravar", "Registro de Pessoa [" + label + "] criado com sucesso!");

	    } catch (Exception e) {
		e.printStackTrace();
		addErrorMessage("Gravar", "Erro na gravação!");
	    } finally {

	    }
	} else {
	    if (passwordModified) {
		String hash = StringUtils.generateMD5Hash(this.password);
		pessoa.setSenhaMd5(hash);
	    }

	    try {
		pessoaService.alterar(pessoa);
		String label = pessoa.getIdPessoa() + "-" + pessoa.getNomeCompleto();
		addInfoMessage("Gravar", "Pessoa [" + label + "] alterada com sucesso!");

	    } catch (Exception e) {
		e.printStackTrace();
		addErrorMessage("Gravar", "Erro na gravação!");
	    } finally {

	    }
	}
    }

    public Integer getEditarId() {
	return editarId;
    }

    public void setEditarId(Integer editarId) {
	this.editarId = editarId;
    }

    public Pessoa getPessoa() {
	return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
	this.pessoa = pessoa;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isPasswordModified() {
	return passwordModified;
    }

    public void setPasswordModified(boolean passwordModified) {
	this.passwordModified = passwordModified;
    }
}
