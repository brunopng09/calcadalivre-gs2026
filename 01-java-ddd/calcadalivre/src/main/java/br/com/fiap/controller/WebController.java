package br.com.fiap.controller;

import br.com.fiap.bo.RegistroBarreiraBO;
import br.com.fiap.bo.TrechoBO;
import br.com.fiap.bo.UsuarioBO;
import br.com.fiap.contingencia.DadosContingencia;
import br.com.fiap.entities.RegistroBarreira;
import br.com.fiap.entities.Trecho;
import br.com.fiap.excecoes.PersistenciaException;
import br.com.fiap.excecoes.RegraNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Paginas Thymeleaf do dashboard e do formulario de barreiras.
 *
 * <p>As telas de leitura (painel e lista de trechos) caem para o snapshot de
 * contingencia quando o banco falha. O formulario e a gravacao continuam
 * dependendo do banco de verdade: gravar em cache seria fingir persistencia.</p>
 */
@Controller
public class WebController {

    private static final Logger LOG = LoggerFactory.getLogger(WebController.class);

    private final TrechoBO trechoBO = new TrechoBO();
    private final RegistroBarreiraBO registroBarreiraBO = new RegistroBarreiraBO();
    private final UsuarioBO usuarioBO = new UsuarioBO();

    @GetMapping("/")
    public String dashboard(Model model) {
        try {
            List<Trecho> trechos = trechoBO.listar();
            Map<Integer, Integer> barreirasPorTrecho = registroBarreiraBO.contarPorTrecho();
            model.addAttribute("trechos", trechos);
            model.addAttribute("barreirasPorTrecho", barreirasPorTrecho);
            model.addAttribute("totalTrechos", trechos.size());
            model.addAttribute("totalBarreiras",
                    barreirasPorTrecho.values().stream().mapToInt(Integer::intValue).sum());
            model.addAttribute("contagemBarreirasIndisponivel", false);
        } catch (PersistenciaException e) {
            LOG.warn("Falha ao consultar o banco no painel; servindo snapshot de contingência", e);
            preencherComSnapshot(model);
        }
        return "index";
    }

    @GetMapping("/trechos")
    public String listarTrechos(Model model) {
        try {
            model.addAttribute("trechos", trechoBO.listar());
            model.addAttribute("barreirasPorTrecho", registroBarreiraBO.contarPorTrecho());
            model.addAttribute("contagemBarreirasIndisponivel", false);
        } catch (PersistenciaException e) {
            LOG.warn("Falha ao consultar o banco na lista de trechos; servindo snapshot de contingência", e);
            preencherComSnapshot(model);
        }
        return "trechos/lista";
    }

    @GetMapping("/barreiras/nova")
    public String formulario(Model model) {
        if (!model.containsAttribute("registroBarreira")) {
            model.addAttribute("registroBarreira", new RegistroBarreira());
        }
        model.addAttribute("trechos", trechoBO.listar());
        model.addAttribute("tipos", registroBarreiraBO.listarTipos());
        model.addAttribute("usuarios", usuarioBO.listar());
        return "barreiras/formulario";
    }

    @PostMapping("/barreiras/salvar")
    public String salvar(@ModelAttribute RegistroBarreira registroBarreira, RedirectAttributes atributos) {
        try {
            registroBarreiraBO.inserir(registroBarreira);
            atributos.addFlashAttribute("sucesso", "Barreira registrada com sucesso.");
            return "redirect:/";
        } catch (RegraNegocioException e) {
            atributos.addFlashAttribute("erro", e.getMessage());
            atributos.addFlashAttribute("registroBarreira", registroBarreira);
            return "redirect:/barreiras/nova";
        }
    }

    /**
     * Alimenta a pagina com o ultimo snapshot conhecido e marca no model que os
     * dados nao sao uma leitura atual do banco (banner de contingencia).
     * A contagem de barreiras nao faz parte do snapshot: fica marcada como indisponivel.
     */
    private void preencherComSnapshot(Model model) {
        List<Trecho> trechos = DadosContingencia.trechos();
        model.addAttribute("trechos", trechos);
        model.addAttribute("barreirasPorTrecho", Map.of());
        model.addAttribute("totalTrechos", trechos.size());
        model.addAttribute("totalBarreiras", 0);
        model.addAttribute("contagemBarreirasIndisponivel", true);
        model.addAttribute("dadosDeCache", true);
        model.addAttribute("avisoCache", DadosContingencia.AVISO_DADOS_DE_CACHE);
    }
}
