package ServiceTest;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.GeradorDePagamento;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;

public class GeradorDePagamentoTest {

    @InjectMocks
    private GeradorDePagamento service;

    @Mock
    private PagamentoDao pagamentoDao;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void inicalizer() {
        MockitoAnnotations.initMocks(this);
        this.service = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    public void shouldGeneratePayment() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();

        LocalDate date = LocalDate.of(2023,01,17);

        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);

        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        service.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();
        Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
        Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        Assert.assertFalse(pagamento.getPago());
        Assert.assertEquals(vencedor.getLeilao(), pagamento.getLeilao());
    }

    @Test
    public void shouldGeneratePayment3() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();

        LocalDate date = LocalDate.of(2023,01,17);

        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);

        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        service.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();
        Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
        Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        Assert.assertFalse(pagamento.getPago());
        Assert.assertEquals(vencedor.getLeilao(), pagamento.getLeilao());
    }


    private Leilao leilao() {
//        Leilao leilao = new Leilao();

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Ciclano"),
                new BigDecimal("900"));

        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);

//        lista.add(leilao);

        return leilao;
    }
}
