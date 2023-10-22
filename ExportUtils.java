package utils;

import DTO.SalesDTO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExportUtils {

    public static void salesSummary(List<SalesDTO> sales,
            OutputStream outputStream)
            throws IOException {
        String outputResult = "Product Code, product Name, Unit price, quantity, amount\n";
        for (SalesDTO sale : sales) {

            String productName = sale.getProductName();
            if (productName.contains("\"")) {
                productName = productName.replace("\"", "\"\"");
            }
            if (productName.contains(",") || productName.contains("\n") ||
                    productName.contains("'") || productName.contains("\\") ||
                    productName.contains("\"")) {
                productName = "\"" + productName + "\"";
            }

            outputResult = outputResult + sale.getProductCode() + "," + productName +
                    "," + sale.getUnitPrice() + "," + sale.getQuantity() +
                    "," + sale.getAmount() + "\n";
        }

        outputStream.write(outputResult.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
