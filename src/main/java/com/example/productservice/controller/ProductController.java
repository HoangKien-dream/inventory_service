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
            @RequestParam(defaultValue = "0") int startPrice,
            @RequestParam(defaultValue = "0") int endPrice,
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
        if (startPrice != 0){
            SearchCriteria searchCriteria
                    = new SearchCriteria("price",SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startPrice);
            ProductSpecification filter = new ProductSpecification(searchCriteria);
            specification = specification.and(filter);
        }
        if (endPrice != 0){
            SearchCriteria searchCriteria
                    = new SearchCriteria("price",SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endPrice);
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

    @PreAuthorize("hasAuthority('list_product')")
    @RequestMapping(method = RequestMethod.POST)
    public Product save(@RequestBody Product product){
       return productService.save(product);
    }

    @RequestMapping(path = "{id}",method =RequestMethod.GET)
    public Product findById(@PathVariable int id){
        return productService.findById(id);
    }

    @PreAuthorize("hasAuthority('updated_product')")
    @RequestMapping(path = "{id}",method = RequestMethod.PUT)
    public Product updated (@PathVariable int id, @RequestBody Product product){
        return productService.updated(id,product);
    }

    @PreAuthorize("hasAuthority('delete_product')")
    @RequestMapping(path = "{id}",method = RequestMethod.DELETE)
    public boolean isDelete(@PathVariable int id){
        return productService.isDelete(id);
    }

//    @Autowired
//    private RabbitTemplate template;
//    private static List<Product> products ;
//    static {
//        products = new ArrayList<>();
//        products.add(new Product("MacBook Pro 14 2021 M1 Pro 10CPU 16 GPU 16GB 1TB Silver", 60000000, "https://product.hstatic.net/1000026716/product/macbook_pro_14_2021_m1_pro_10cpu_16_gpu_16gb_1tb_silver_7916fd31996e4a64ba881d76266c6134.png", "B??? vi x??? l?? Apple M1 Pro gi??p MacBook Pro t???c ????? v?? m???nh m??? h??n bao gi??? h???t. Th???m ch?? phi??n b???n 16GB RAM c??n mang ?????n hi???u su???t ???n t?????ng h??n n???a, cho b???n b??? nh??? ?????m l???n, b??ng th??ng cao ????? tho???i m??i l??m nhi???u vi???c c??ng l??c v???i hi???u qu??? tuy???t ?????i.", 30,1, 1));
//        products.add(new Product("MacBook Pro 13 M1 16GB 256GB - Silver", 36000000, "https://product.hstatic.net/1000026716/product/macbook_pro_13_m1_16gb_256gb_-_silver_78a680b55d3943f1985755150ae11f98.png", "B??n c???nh chi???c MacBook Air m???i ???????c ra m???t trong c??ng s??? ki???n One more thing T11 2020 Apple c??ng  gi???i thi???u d??ng MacBook Pro 13 inch 2020 ho??n to??n m???i.",15, 1, 1));
//        products.add(new Product("MacBook Pro 13 M1 16GB 512GB - Grey", 41000000, "https://product.hstatic.net/1000026716/product/macbook_pro_13_m1_16gb_512gb_-_grey_fec311b198eb4f60abd8db8c9b08d47f.png", "MacBook Pro 13 16GB s??? d???ng b??? vi x??? l?? Apple M1 gi??p t???c ????? v?? m???nh m??? h??n bao gi??? h???t", 1,1, 1));
//        products.add(new Product("MacBook Air M1 7GPU 16GB 512GB - Gold", 35000000, "https://product.hstatic.net/1000026716/product/macbook_air_m1_7gpu_16gb_512gb_-_gold_aae4fc1f983240909c80c4b19fa4001c.png", "Trong c??ng 1 n??m MacBook Air 2020 m???i ???????c Apple n??ng c???p ra m???t v???i s??? xu???t hi???n c???a con Chip Apple M1 ???????c ph??t tri???n v???i ki???n tr??c ARM m???i, nhanh h??n 98% PC, pin 18 gi???.",15, 1, 1));
//        products.add(new Product("MacBook Pro 16 2021 M1 Max 32GPU 32GB 1TB Space Gray", 91200000, "https://product.hstatic.net/1000026716/product/macbook_pro_16_2021_m1_max_32gpu_32gb_1tb_space_gray_090c0d8d8b644f28992954bc2c4782ca.png", "B??? vi x??? l?? Apple M1 Pro gi??p MacBook Pro t???c ????? v?? m???nh m??? h??n bao gi??? h???t. Th???m ch?? phi??n b???n 16GB RAM c??n mang ?????n hi???u su???t ???n t?????ng h??n n???a, cho b???n b??? nh??? ?????m l???n, b??ng th??ng cao ????? tho???i m??i l??m nhi???u vi???c c??ng l??c v???i hi???u qu??? tuy???t ?????i.", 30,1, 1));
//
//        products.add(new Product("Laptop Gaming ROG Zephyrus M16 GU603ZX K8025W", 100000000, "https://product.hstatic.net/1000026716/product/m16-i9_ffcc609fd96a409cbd7dd6e910f364f4.png", "M???t trong nh???ng chi???n binh ?????n t??? nh?? ASUS ???????c c???u t???o t??? nh???ng th??nh ph???n cao c???p nh???t, v?????t tr???i nh???t so v???i th???i ??i???m hi???n t???i l?? laptop gaming Asus ROG Zephyrus M16 GU603ZX K8025W", 15,2, 1));
//        products.add(new Product("Laptop gaming Asus ROG Flow Z13 GZ301ZC LD110W", 48000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-asus-rog-zephyrus-g14-ga402rj-l8030w_dcdbae86d20e47ac8c5a150060f10fd5.jpg", "Khi nh???c ?????n laptop gaming ch???c h???n m???i ng?????i ?????u c?? ???n t?????ng v??? m???t chi???c m??y t??nh x??ch tay v???i ngo???i h??nh h???m h???, ??????? con??? v?? c??ng v?? c??ng ng???u l??i. V???y l?? c??c b???n ch??a bi???t ?????n d??ng ASUS ROG Flow v?? c??c b???n ch??a bi???t ?????n ROG Flow Z13 GZ301ZC LD110W", 30,2, 1));
//        products.add(new Product("Laptop gaming ASUS ROG Zephyrus G14 GA402RJ L8030W", 60000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-asus-rog-zephyrus-g14-ga402rj-l8030w_dcdbae86d20e47ac8c5a150060f10fd5.jpg", "L?? m???t trong nh???ng m???u laptop ???????c c??c game th??? y??u th??ch nh???t hi???n nay, laptop gaming ASUS ROG Zephyrus G14 GA402RJ L8030W kh??ng ch??? s??? h???u hi???u n??ng cao, m?? c??n c?? thi???t k??? v?? c??ng ?????p m???t, ???n t?????ng v?? m??n h??nh ch??n th???t ?????n m???c b???t ng???.",30 ,2, 1));
//        products.add(new Product("Laptop gaming ASUS ROG Flow X13 GV301RC LJ050W", 40000000, "https://product.hstatic.net/1000026716/product/x13_98ce02aaf8be4512baf0ad9a684ebeba.png", "Nhanh - m?????t m?? - linh ho???t l?? nh???ng t??? m?? t??? laptop gaming 13 inch ?????u ti??n c???a ROG s??? h???u thi???t k??? xoay g???p linh ho???t nh??? b???n l??? 360",30, 2, 1));
//
//
//        products.add(new Product("Laptop gaming MSI Raider GE76 12UHS 480VN", 101000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-msi-raider-ge76-12uhs-480vn_e3151bfa17344b7392219e9f05c976ab.jpg", "S??? d???ng b??? vi x??? Intel Core i9 th??? h??? 12 16 nh??n 24 lu???ng cho m???c xung nh???p cao nh???t l??n ?????n 5.2 GHz, MSI GE76 Raider d??? d??ng x??? l?? nhanh c??c t??c v??? n???ng", 15,3, 1));
//        products.add(new Product("Laptop gaming MSI Stealth GS77 12UH 075VN", 80000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-msi-stealth-gs77-12uh-075vn_24e1b58aa3ea4cca9efe853a1ef1dbac.jpg", "L?? s???n ph???m laptop gaming cao c???p ?????n t??? nh?? r???ng MSI, MSI Stealth GS77 12UH 075VN ??em l???i m???t v??? ngo??i g???n g??ng v?? m???ng nh???",5,3, 1));
//        products.add(new Product("Laptop gaming MSI Vector GP66 12UGS 422VN", 51000000, "https://product.hstatic.net/1000026716/product/1_5072c09a14f04da19aeb5a266a5a8335.png", "MSI Vector GP66 12UGS 422VN ???????c trang b??? b??? vi x??? l?? Intel Core  th??? h??? th??? 112 m???i nh???t v?? NVIDIA GeForce RTX 30 series. GeForce RTX 30 series mang l???i s???c m???nh b???c ph?? c??ng c??ng ngh??? Ray Tracing", 3,3, 1));
//        products.add(new Product("Laptop MSI Gaming GP66 Leopard 11UE 643VN", 37500000, "https://product.hstatic.net/1000026716/product/643vn_a1f624a1594148009e7a10c9582533d1.png", "MSI Gaming GP66 Leopard 11UE 643VN ???????c trang b??? b??? vi x??? l?? Intel Core i th??? h??? th??? 11 m???i nh???t v?? NVIDIA GeForce RTX 30 series", 4,3, 1));
//
//
//        products.add(new Product("Laptop gaming Legion 5 Pro 16ITH6H 82JD00BCVN", 40000000, "https://product.hstatic.net/1000026716/product/laptop-gaming-legion-5-pro-16ith6h-82jd00bcvn_e38ad367031a4ea383d364ee8ef63b29.jpg", "Laptop gaming Legion 5 Pro 16ITH6H 82JD00BCVN l?? m???t trong nh???ng m???u laptop gaming m???i nh???t c???a Lenovo. L?? d??ng gaming Legion chuy??n nghi???p m???i Legion 5 Pro d??ng chip x??? l?? Intel H45 m???i nh???t",5, 4, 1));
//        products.add(new Product("Laptop gaming Lenovo Legion 7 16ACHG6 82N600NSVN", 68500000, "https://product.hstatic.net/1000026716/product/gearvn-laptop-gaming-lenovo-legion-7-16achg6-82n600nsvn-1_db9f8fe11b6741cdba47d2b68c64020c.png", "????? ph???c v??? nhu c???u ch??i game tr???c tuy???n tr??n m??y t??nh x??ch tay, Lenovo ???? cho ra m???t s???n ph???m laptop gaming Lenovo Legion 7 16ACHG6 82N600NSVN", 5,4, 1));
//        products.add(new Product("Laptop gaming Legion S7 15ACH6 82K800DPVN", 38000000, "https://product.hstatic.net/1000026716/product/khung-laptop-gaming_1e0ba2d2f7454a59957a6dfc8d151c7f.png", "Laptop gaming Lenovo Legion S7 15ACH6 82K800DPVN l?? s???n ph???m thu???c d??ng m??y t??nh x??ch tay cao c???p c???a Lenovo. S??? h???u thi???t k??? ???n t?????ng v?? hi???u su???t cao", 5,4, 1));
//        products.add(new Product("Laptop Lenovo IdeaPad Gaming 3 15IHU6 82K100KLVN", 22000000, "https://product.hstatic.net/1000026716/product/i5-3050ti_9a41c791aabe4169b5d3d642fa44dbe9.png", "Lenovo ???????c bi???t ?????n l?? m???t trong nh???ng h??ng chuy??n ph??n ph???i ?????n th??? tr?????ng nh???ng d??ng m??y t??nh v???i thi???t k??? m???ng nh???, thanh l???ch t??? c??c d??ng s???n ph???m laptop cho sinh vi??n, doanh nh??n,???", 5,4, 1));
//
//
//        products.add(new Product("Laptop Gaming Dell Alienware M15 R6 70262923", 50000000, "https://product.hstatic.net/1000026716/product/laptop_gaming_dell_alienware_m15_r6_70262923_b5cb854e9a8e47ebba8025cb6a4426a7.jpg", "Alienware m15 R6 ???????c cho l?? ??t tham v???ng h??n so v???i phi??n b???n ti???n nhi???m Ryzen Edition", 3,5, 1));
//        products.add(new Product("Laptop Gaming Dell Alienware M15 70262921", 44200000, "https://product.hstatic.net/1000026716/product/khung-laptop-gaming_6edf1e5348e94c3fa924df00dec7d95f.png", "Dell Alienware M15 70262921, chi???c laptop gaming cao c???p m???i nh???t ???? ???????c ch??o s??n t??? Dell. Thu???c d??ng Alienware si??u kh???ng", 3,5, 1));
//        products.add(new Product("Laptop Gaming Dell G15 5511 70266676", 23100000, "https://product.hstatic.net/1000026716/product/gearvn-laptop-gaming-dell-g15-5511-70266676-1_a1ee0b58a6ee41a58240c38cd3335a74.jpg", "Laptop gaming Dell G15 l?? s???n ph???m n???m trong ph??n kh??c laptop gaming tr??n 20 tri???u v?? l?? th??? h??? ch??i game ti???p theo c???a Dell.", 3,5, 1));
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public List<Product> saveAll(){
//        return repositoryProduct.saveAll(products);
//    }
}
