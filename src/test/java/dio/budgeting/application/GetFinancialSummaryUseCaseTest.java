package dio.budgeting.application;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetFinancialSummaryUseCaseTest {
    private final TransactionRepository repository = mock(TransactionRepository.class);
    private final GetFinancialSummaryUseCase useCase = new GetFinancialSummaryUseCase(repository);

    @Test
    void calculaResumoEmReaisComCategorias() {
        when(repository.findAll()).thenReturn(List.of(
                new Transaction("Mercado", 10000, Category.GROCERIES),
                new Transaction("Farmacia", 5000, Category.PHARMA),
                new Transaction("Mercado", 1000, Category.GROCERIES)));

        var resumo = useCase.execute();

        assertEquals(new BigDecimal("160.00"), resumo.totalGasto());
        assertEquals(3, resumo.quantidadeTransacoes());
        assertEquals(new BigDecimal("53.33"), resumo.mediaPorTransacao());
        assertEquals(new BigDecimal("110.00"), resumo.totalPorCategoria().get("GROCERIES"));
        assertEquals(new BigDecimal("50.00"), resumo.totalPorCategoria().get("PHARMA"));
    }

    @Test
    void retornaZerosQuandoNaoHaTransacoes() {
        when(repository.findAll()).thenReturn(List.of());

        var resumo = useCase.execute();

        assertEquals(new BigDecimal("0.00"), resumo.totalGasto());
        assertEquals(0, resumo.quantidadeTransacoes());
        assertEquals(new BigDecimal("0.00"), resumo.mediaPorTransacao());
        assertEquals(0, resumo.totalPorCategoria().size());
    }
}
