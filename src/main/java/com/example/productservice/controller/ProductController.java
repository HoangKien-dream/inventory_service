package com.example.productservice.controller;

import com.example.productservice.config.MessageConfig;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.RepositoryProduct;
import com.example.productservice.service.ProductService;
import com.example.productservice.specification.ProductSpecification;
import com.example.productservice.specification.SearchCriteria;
import com.example.productservice.specification.SearchCriteriaOperator;
import com.example.productservice.util.DateTimeHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "api/v1/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @PreAuthorize("hasAuthority('list_product')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int category,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "1") int status) {

        Specification<Product> specification = Specification.where(null);
        if (keyword != null && keyword.length() > 0) {
            SearchCriteria searchCriteria
                    = new SearchCriteria("name", SearchCriteriaOperator.EQUALS, keyword);
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (status != 0) {
            SearchCriteria searchCriteria
                    = new SearchCriteria("status", SearchCriteriaOperator.EQUALS, status);
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (category != 0){
            SearchCriteria searchCriteria
                    = new SearchCriteria("categoryId",SearchCriteriaOperator.EQUALS,category);
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }

        if (startDate != null & startDate.length()>1){
            SearchCriteria searchCriteria
                    = new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS, DateTimeHelper.convertStringToLocalDate(startDate));
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (endDate != null & endDate.length()>1){
            SearchCriteria searchCriteria
                    = new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS, DateTimeHelper.convertStringToLocalDate(endDate));
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        Page<Product> result = this.productService.findAll(page, size, specification);
        return ResponseEntity.ok().body(result);
    }

//    @Autowired
//    private RabbitTemplate template;
//    private static List<Product> products ;
//    static {
//        products = new ArrayList<>();
//        products.add(new Product("MacBook Pro 14 2021 M1 Pro 10CPU 16 GPU 16GB 1TB Silver", 60000000, "https://product.hstatic.net/1000026716/product/macbook_pro_14_2021_m1_pro_10cpu_16_gpu_16gb_1tb_silver_7916fd31996e4a64ba881d76266c6134.png", "Bộ vi xử lý Apple M1 Pro giúp MacBook Pro tốc độ và mạnh mẽ hơn bao giờ hết. Thậm chí phiên bản 16GB RAM còn mang đến hiệu suất ấn tượng hơn nữa, cho bạn bộ nhớ đệm lớn, băng thông cao để thoải mái làm nhiều việc cùng lúc với hiệu quả tuyệt đối.", 30,1, 1));
//        products.add(new Product("MacBook Pro 13 M1 16GB 256GB - Silver", 36000000, "https://product.hstatic.net/1000026716/product/macbook_pro_13_m1_16gb_256gb_-_silver_78a680b55d3943f1985755150ae11f98.png", "Bên cạnh chiếc MacBook Air mới được ra mắt trong cùng sự kiện One more thing T11 2020 Apple cũng  giới thiệu dòng MacBook Pro 13 inch 2020 hoàn toàn mới.",15, 1, 1));
//        products.add(new Product("MacBook Pro 13 M1 16GB 512GB - Grey", 41000000, "https://product.hstatic.net/1000026716/product/macbook_pro_13_m1_16gb_512gb_-_grey_fec311b198eb4f60abd8db8c9b08d47f.png", "MacBook Pro 13 16GB sử dụng bộ vi xử lý Apple M1 giúp tốc độ và mạnh mẽ hơn bao giờ hết", 1,1, 1));
//        products.add(new Product("MacBook Air M1 7GPU 16GB 512GB - Gold", 35000000, "https://product.hstatic.net/1000026716/product/macbook_air_m1_7gpu_16gb_512gb_-_gold_aae4fc1f983240909c80c4b19fa4001c.png", "Trong cùng 1 năm MacBook Air 2020 mới được Apple nâng cấp ra mắt với sự xuất hiện của con Chip Apple M1 được phát triển với kiến trúc ARM mới, nhanh hơn 98% PC, pin 18 giờ.",15, 1, 1));
//        products.add(new Product("MacBook Pro 16 2021 M1 Max 32GPU 32GB 1TB Space Gray", 91200000, "https://product.hstatic.net/1000026716/product/macbook_pro_16_2021_m1_max_32gpu_32gb_1tb_space_gray_090c0d8d8b644f28992954bc2c4782ca.png", "Bộ vi xử lý Apple M1 Pro giúp MacBook Pro tốc độ và mạnh mẽ hơn bao giờ hết. Thậm chí phiên bản 16GB RAM còn mang đến hiệu suất ấn tượng hơn nữa, cho bạn bộ nhớ đệm lớn, băng thông cao để thoải mái làm nhiều việc cùng lúc với hiệu quả tuyệt đối.", 30,1, 1));
//
//        products.add(new Product("Laptop Gaming ROG Zephyrus M16 GU603ZX K8025W", 100000000, "https://product.hstatic.net/1000026716/product/m16-i9_ffcc609fd96a409cbd7dd6e910f364f4.png", "Một trong những chiến binh đến từ nhà ASUS được cấu tạo từ những thành phần cao cấp nhất, vượt trội nhất so với thời điểm hiện tại là laptop gaming Asus ROG Zephyrus M16 GU603ZX K8025W", 15,2, 1));
//        products.add(new Product("Laptop gaming Asus ROG Flow Z13 GZ301ZC LD110W", 48000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-asus-rog-zephyrus-g14-ga402rj-l8030w_dcdbae86d20e47ac8c5a150060f10fd5.jpg", "Khi nhắc đến laptop gaming chắc hẳn mọi người đều có ấn tượng về một chiếc máy tính xách tay với ngoại hình hầm hố, “đô con” và cùng vô cùng ngầu lòi. Vậy là các bạn chưa biết đến dòng ASUS ROG Flow và các bạn chưa biết đến ROG Flow Z13 GZ301ZC LD110W", 30,2, 1));
//        products.add(new Product("Laptop gaming ASUS ROG Zephyrus G14 GA402RJ L8030W", 60000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-asus-rog-zephyrus-g14-ga402rj-l8030w_dcdbae86d20e47ac8c5a150060f10fd5.jpg", "Là một trong những mẫu laptop được các game thủ yêu thích nhất hiện nay, laptop gaming ASUS ROG Zephyrus G14 GA402RJ L8030W không chỉ sở hữu hiệu năng cao, mà còn có thiết kế vô cùng đẹp mắt, ấn tượng và màn hình chân thật đến mức bất ngờ.",30 ,2, 1));
//        products.add(new Product("Laptop gaming ASUS ROG Flow X13 GV301RC LJ050W", 40000000, "https://product.hstatic.net/1000026716/product/x13_98ce02aaf8be4512baf0ad9a684ebeba.png", "Nhanh - mượt mà - linh hoạt là những từ mô tả laptop gaming 13 inch đầu tiên của ROG sở hữu thiết kế xoay gập linh hoạt nhờ bản lề 360",30, 2, 1));
//
//
//        products.add(new Product("Laptop gaming MSI Raider GE76 12UHS 480VN", 101000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-msi-raider-ge76-12uhs-480vn_e3151bfa17344b7392219e9f05c976ab.jpg", "Sử dụng bộ vi xử Intel Core i9 thế hệ 12 16 nhân 24 luồng cho mức xung nhịp cao nhất lên đến 5.2 GHz, MSI GE76 Raider dễ dàng xử lý nhanh các tác vụ nặng", 15,3, 1));
//        products.add(new Product("Laptop gaming MSI Stealth GS77 12UH 075VN", 80000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-msi-stealth-gs77-12uh-075vn_24e1b58aa3ea4cca9efe853a1ef1dbac.jpg", "Là sản phẩm laptop gaming cao cấp đến từ nhà rồng MSI, MSI Stealth GS77 12UH 075VN đem lại một vẻ ngoài gọn gàng và mỏng nhẹ",5,3, 1));
//        products.add(new Product("Laptop gaming MSI Vector GP66 12UGS 422VN", 51000000, "https://product.hstatic.net/1000026716/product/1_5072c09a14f04da19aeb5a266a5a8335.png", "MSI Vector GP66 12UGS 422VN được trang bị bộ vi xử lý Intel Core  thế hệ thứ 112 mới nhất và NVIDIA GeForce RTX 30 series. GeForce RTX 30 series mang lại sức mạnh bức phá cùng công nghệ Ray Tracing", 3,3, 1));
//        products.add(new Product("Laptop MSI Gaming GP66 Leopard 11UE 643VN", 37500000, "https://product.hstatic.net/1000026716/product/643vn_a1f624a1594148009e7a10c9582533d1.png", "MSI Gaming GP66 Leopard 11UE 643VN được trang bị bộ vi xử lý Intel Core i thế hệ thứ 11 mới nhất và NVIDIA GeForce RTX 30 series", 4,3, 1));
//
//
//        products.add(new Product("Laptop gaming Legion 5 Pro 16ITH6H 82JD00BCVN", 40000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-legion-5-pro-16ith6h-82jd00bcvn_e38ad367031a4ea383d364ee8ef63b29.jpg", "Laptop gaming Legion 5 Pro 16ITH6H 82JD00BCVN là một trong những mẫu laptop gaming mới nhất của Lenovo. Là dòng gaming Legion chuyên nghiệp mới Legion 5 Pro dùng chip xử lý Intel H45 mới nhất",5, 4, 1));
//        products.add(new Product("Laptop gaming Lenovo Legion 7 16ACHG6 82N600NSVN", 68500000, "https://product.hstatic.net/1000026716/product/gearvn-laptop-gaming-lenovo-legion-7-16achg6-82n600nsvn-1_db9f8fe11b6741cdba47d2b68c64020c.png", "Để phục vụ nhu cầu chơi game trực tuyến trên máy tính xách tay, Lenovo đã cho ra mắt sản phẩm laptop gaming Lenovo Legion 7 16ACHG6 82N600NSVN", 5,4, 1));
//        products.add(new Product("Laptop gaming Legion S7 15ACH6 82K800DPVN", 38000000, "https://product.hstatic.net/1000026716/product/khung-laptop-gaming_1e0ba2d2f7454a59957a6dfc8d151c7f.png", "Laptop gaming Lenovo Legion S7 15ACH6 82K800DPVN là sản phẩm thuộc dòng máy tính xách tay cao cấp của Lenovo. Sở hữu thiết kế ấn tượng và hiệu suất cao", 5,4, 1));
//        products.add(new Product("Laptop Lenovo IdeaPad Gaming 3 15IHU6 82K100KLVN", 22000000, "https://product.hstatic.net/1000026716/product/i5-3050ti_9a41c791aabe4169b5d3d642fa44dbe9.png", "Lenovo được biết đến là một trong những hãng chuyên phân phối đến thị trường những dòng máy tính với thiết kế mỏng nhẹ, thanh lịch từ các dòng sản phẩm laptop cho sinh viên, doanh nhân,…", 5,4, 1));
//
//
//        products.add(new Product("Laptop Gaming Dell Alienware M15 R6 70262923", 50000000, "https://product.hstatic.net/1000026716/product/laptop_gaming_dell_alienware_m15_r6_70262923_b5cb854e9a8e47ebba8025cb6a4426a7.jpg", "Alienware m15 R6 được cho là ít tham vọng hơn so với phiên bản tiền nhiệm Ryzen Edition", 3,5, 1));
//        products.add(new Product("Laptop Gaming Dell Alienware M15 70262921", 44200000, "https://product.hstatic.net/1000026716/product/khung-laptop-gaming_6edf1e5348e94c3fa924df00dec7d95f.png", "Dell Alienware M15 70262921, chiếc laptop gaming cao cấp mới nhất đã được chào sân từ Dell. Thuộc dòng Alienware siêu khủng", 3,5, 1));
//        products.add(new Product("Laptop Gaming Dell G15 5511 70266676", 23100000, "https://product.hstatic.net/1000026716/product/gearvn-laptop-gaming-dell-g15-5511-70266676-1_a1ee0b58a6ee41a58240c38cd3335a74.jpg", "Laptop gaming Dell G15 là sản phẩm nằm trong phân khúc laptop gaming trên 20 triệu và là thế hệ chơi game tiếp theo của Dell.", 3,5, 1));
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public List<Product> saveAll(){
//        return repositoryProduct.saveAll(products);
//    }
}
