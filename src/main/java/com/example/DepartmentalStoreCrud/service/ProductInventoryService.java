package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductInventoryService {

    @Autowired
    private ProductInventoryRepository productRepo;

    @Autowired
    private BackorderRepository backorderRepo;

    @Autowired
    private OrderRepository orderRepo;

    //check that file is of excel type or not
    public final boolean checkExcelFormat(final MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType != null && contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        }
        return false;
    }

    //convert excel to list of products
    public final List<ProductInventory> convertExcelToListOfProduct(final InputStream is) throws IOException {
        List<ProductInventory> productList = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            XSSFSheet sheet = workbook.getSheet("productData");

            int rowNumber = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cells = row.iterator();

                int cid = 0;

                ProductInventory p = new ProductInventory();

                while (cells.hasNext()) {
                    Cell cell = cells.next();

                    switch (cid) {
                        case 0:
                            p.setProductName(cell.getStringCellValue());
                            break;
                        case 1:
                            p.setProductDesc(cell.getStringCellValue());
                            break;
                        case 2:
                            p.setPrice(cell.getNumericCellValue());
                            break;
                        case 3:
                            p.setProductQuantity((int) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                productList.add(p);
            }
        return productList;
    }

    public final void saveExcel(final MultipartFile file) throws IOException {
        if (checkExcelFormat(file)) {
            List<ProductInventory> products = convertExcelToListOfProduct(file.getInputStream());
            productRepo.saveAll(products);
        } else {
            throw new IllegalArgumentException("Invalid file format");
        }
    }

    public final List<ProductInventory> getAllProducts() {
        return productRepo.findAll();
    }

    public final ProductInventory getProductById(final Long productID) {
        return productRepo.findById(productID)
                .orElseThrow(() -> new NoSuchElementException("No product exists with ID: " + productID));
    }

//    public void addProductDetails(ProductInventory productInventory) {
//        productRepo.save(productInventory);
//    }

    public final void updateProductDetails(final Long productID, final ProductInventory productInventory) {
        ProductInventory existingProduct = getProductById(productID);
        if (existingProduct != null) {
            existingProduct.setProductID(productID);
            existingProduct.setProductDesc(productInventory.getProductDesc());
            existingProduct.setProductName(productInventory.getProductName());
            existingProduct.setPrice(productInventory.getPrice());
            existingProduct.setProductQuantity(productInventory.getProductQuantity());
            productRepo.save(existingProduct);
        }
    }

    public final void removeBackorders(final int newQuantity, final ProductInventory existingProduct) {
        if (newQuantity > 0) {
            List<Order> orders = orderRepo.findByProductInventory(existingProduct);
            if (!orders.isEmpty()) {
                for (Order order : orders) {
                    // Remove the backorder if quantity becomes sufficient
                    if (existingProduct.getProductQuantity() >= order.getOrderQuantity()) {
                        Backorder backorder = backorderRepo.findByOrder(order);
                        if (backorder != null) {
                                // Remove the backorder associated with the order
                                backorderRepo.delete(backorder);
                                existingProduct.setProductQuantity(existingProduct.getProductQuantity() - order.getOrderQuantity());
                        }
                }
            }
            productRepo.save(existingProduct);
        }
      }
    }

    @Scheduled(cron = "0 * * * * *")   // Runs every midnight
    public final void deleteBackordersCronJob() {
        // Get all existing products
        List<ProductInventory> products = productRepo.findAll();
        for (ProductInventory existingProduct : products) {
            int quantity = existingProduct.getProductQuantity();
            removeBackorders(quantity, existingProduct);
        }
    }

    public final void deleteProductDetails(final Long productID) {
        if (getProductById(productID) != null) {
            productRepo.deleteById(productID);
        }
    }
}
