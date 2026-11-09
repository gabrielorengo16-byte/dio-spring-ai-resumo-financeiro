package dio.budgeting.application.output;

import java.math.BigDecimal;
import java.util.Map;

public record FinancialSummaryOutput(
        BigDecimal totalGasto,
        long quantidadeTransacoes,
        BigDecimal mediaPorTransacao,
        Map<String, BigDecimal> totalPorCategoria) {
}
