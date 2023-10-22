package DTO;

public class ProductDTO {
	
    private int productCode;
    private String productName;
    private int productPrice;
    private String registerDateTime;
    private String updateDateTime;
    private String deleteDateTime;

    // getters and setters
    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(String registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    
    public String getDeleteDateTime() {
		return deleteDateTime;
	}



	public void setDeleteDateTime(String deleteDateTime) {
		this.deleteDateTime = deleteDateTime;
	}
}
