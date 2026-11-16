package br.com.fiap.controller;

import br.com.fiap.bo.RegistroBarreiraBO;
import br.com.fiap.bo.TrechoBO;
import br.com.fiap.bo.UsuarioBO;
import br.com.fiap.entities.RegistroBarreira;
import br.com.fiap.entities.Trecho;
import br.com.fiap.excecoes.RegraNegocioException;
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
 */
@Controller
public class WebController {

    private final TrechoBO trechoBO = new TrechoBO();
    private final RegistroBarreiraBO registroBarreiraBO = new RegistroBarreiraBO();
    private final UsuarioBO usuarioBO = new UsuarioBO();

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Trecho> trechos = trechoBO.listar();
        Map<Integer, Integer> barreirasPorTrecho = registroBarreiraBO.contarPorTrecho();
        int totalBarreiras = barreirasPorTrecho.values().stream().mapToInt(Integer::intValue).sum();
        model.addAttribute("trechos", trechos);
        model.addAttribute("barreirasPorTrecho", barreirasPorTrecho);
        model.addAttribute("totalTrechos", trechos.size());
        model.addAttribute("totalBarreiras", totalBarreiras);
        return "index";
    }

    @GetMapping("/trechos")
    public String listarTrechos(Model model) {
        model.addAttribute("trechos", trechoBO.listar());
        model.addAttribute("barreirasPorTrecho", registroBarreiraBO.contarPorTrecho());
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
}
