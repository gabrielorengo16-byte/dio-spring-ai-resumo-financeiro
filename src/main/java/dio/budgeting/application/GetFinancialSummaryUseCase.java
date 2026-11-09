package dio.budgeting.application;

import dio.budgeting.application.output.FinancialSummaryOutput;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GetFinancialSummaryUseCase {
    private final TransactionRepository transactionRepository;

    public GetFinancialSummaryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public FinancialSummaryOutput execute() {
        var transactions = transactionRepository.findAll();
        long totalCentavos = 0;
        Map<String, Long> centavosPorCategoria = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            totalCentavos = Math.addExact(totalCentavos, transaction.getAmount());
            centavosPorCategoria.merge(transaction.getCategory().name(), transaction.getAmount(), Math::addExact);
        }

        Map<String, BigDecimal> totalPorCategoria = new LinkedHashMap<>();
        centavosPorCategoria.forEach((categoria, centavos) ->
                totalPorCategoria.put(categoria, emReais(centavos)));

        long quantidade = transactions.size();
        BigDecimal totalGasto = emReais(totalCentavos);
        BigDecimal media = quantidade == 0
                ? BigDecimal.ZERO.setScale(2)
                : totalGasto.divide(BigDecimal.valueOf(quantidade), 2, RoundingMode.HALF_UP);

        return new FinancialSummaryOutput(totalGasto, quantidade, media, totalPorCategoria);
    }

    private static BigDecimal emReais(long centavos) {
        return BigDecimal.valueOf(centavos, 2);
    }
}
