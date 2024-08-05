package microservice.sale_service.Service;

import at.backend.drugstore.microservice.common_models.DTO.Sale.*;
import at.backend.drugstore.microservice.common_models.Utils.Result;
import microservice.sale_service.Model.PhysicalSale;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SaleService {
    CompletableFuture<Result<CreateSaleDTO>> createSale(SaleProductsDTO saleProductsDTO);
    CompletableFuture<Result<ProcessSaleDTO>> paySale(PaySaleDTO paySaleDTO);
    CompletableFuture<Result<SaleDTO>> getSaleById(Long saleId);
    CompletableFuture<List<SaleDTO>> getTodaySales();
    CompletableFuture<SalesSummaryDTO> getTodaySummarySales();

    }
