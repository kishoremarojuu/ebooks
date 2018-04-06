package com.ebooks.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.ebooks.dao.ProductDao;
import com.ebooks.model.Product;

@Controller
public class AdminController {
	
	private Path path;
	@Autowired
	private ProductDao productDao; 
	@RequestMapping("/admin")
	public String adminPage() {
		return "admin";
	}

	@RequestMapping("/admin/productInventory")
	public String productInventory(Model model) {
		List<Product> products = productDao.getAllProducts();
		for(Product product:products){
			System.out.println(product);
		}
		try{
			
		}finally{
			
		}
		model.addAttribute("products", products);

		return "productInventory";
	}

	@RequestMapping("/admin/productInventory/addProduct")
	public String addProduct(Model model) {
		Product product = new Product();
		product.setProductCategory("instrument");
		product.setProductCondition("new");
		product.setProductStatus("active");

		model.addAttribute("product", product);

		return "addProduct";
	}

	@RequestMapping(value = "/admin/productInventory/addProduct", method = RequestMethod.POST)
	public String addProductPost(
			@Valid @ModelAttribute("product") Product product,
			BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "addProduct";
		}
		productDao.addProduct(product);

		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext()
				.getRealPath("/");
		path = Paths.get(rootDirectory + "\\resources\\images\\"
				+ product.getProductId() + ".png");

		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(path.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Product image saving failed", e);
			}
		}

		return "redirect:/admin/productInventory";
	}

	@RequestMapping("/admin/productInventory/deleteProduct/{id}")
	public String deleteProduct(@PathVariable String id, Model model,
			HttpServletRequest request) {

		String rootDirectory = request.getSession().getServletContext()
				.getRealPath("/");
		path = Paths.get(rootDirectory + "\\resources\\images\\" + id + ".png");
		// //eBook/src/main/webapp/WEB-INF/views/images

		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productDao.deleteProduct(id);

		return "redirect:/admin/productInventory";
	}

	@RequestMapping("/admin/productInventory/editProduct/{id}")
	public String editProduct(@PathVariable("id") String id, Model model) {
		Product product = productDao.getProductById(id);

		model.addAttribute(product);

		return "editProduct";
	}

	@RequestMapping(value = "/admin/productInventory/editProduct", method = RequestMethod.POST)
	public String editProduct(
			@Valid @ModelAttribute("product") Product product, Model model,
			BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return "editProduct";
		}
		MultipartFile productImage = product.getProductImage();
		String rootDirectory = request.getSession().getServletContext()
				.getRealPath("/");
		path = Paths.get(rootDirectory + "\\resources\\images\\"
				+ product.getProductId() + ".png");

		if (productImage != null && !productImage.isEmpty()) {
			try {
				productImage.transferTo(new File(path.toString()));
			} catch (Exception e) {
				throw new RuntimeException("Product image saving failed", e);
			}
		}

		productDao.editProduct(product);

		return "redirect:/admin/productInventory";
	}

}
