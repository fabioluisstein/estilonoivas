package loja.springboot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import loja.springboot.datatables.Datatables;
import loja.springboot.datatables.DatatablesColunas;
import loja.springboot.model.LoginInfo;
import loja.springboot.model.Perfil;
import loja.springboot.model.Usuario;
import loja.springboot.repository.LoginRepository;
import loja.springboot.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private Datatables datatables;
	@Autowired
	private LoginRepository loginrepository;


	@Override @Transactional(readOnly = false)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//salvarLogin(usuario.getEmail(),"Não Autorizado");
		Usuario usuario = repository.findByEmail(username).get();
          if(usuario.getId() == null){
			new UsernameNotFoundException("Usuario " + username + " não encontrado.");
		  }

		  salvarLogin(usuario.getEmail(),"Autorizado");	

		return new User(
		
			usuario.getEmail(),
			usuario.getSenha(),
			
			AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis()))
		);
		
	}
	
	

  
    public void salvarLogin( String usuario, String Operacao) {
	

		 String ipAddress = "122221112";
        String ip = ipAddress;


        // Salva o login
        LoginInfo loginInfo = new LoginInfo(ip, usuario, Operacao);
		
       loginrepository.save(loginInfo); 
    }



	private String[] getAtuthorities(List<Perfil> perfis) {
	
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty()
				? repository.findAll(datatables.getPageable())
				: repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);

		repository.save(usuario); 	 	
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		
		return repository.findByIdAndPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
	
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);		
	}
}