package lesson6;

import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lesson6.db.model.ProductsExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateProductTest {
    private static SqlSession session = null;
    private static ProductsMapper productMapper = null;

    @BeforeAll
    static void beforeAll() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new
                SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession();
        productMapper = session.getMapper(ProductsMapper.class);
    }

    @Test
    void createProductInFoodCategoryTest() {
        Products product = createProduct();
        productMapper.insert(product);
        session.commit();

        ProductsExample example = new ProductsExample();
        example.createCriteria();
        List<Products> list = productMapper.selectByExample(example);
        assertTrue(productMapper.countByExample(example) > 0);
    }

    @Test
    void getProductByIdInFoodCategoryTest() {
        ProductsExample example = new ProductsExample();
        List<Products> list = productMapper.selectByExample(example);
        long id = list.get(new Random().nextInt(list.size())).getId();
        Products product = productMapper.selectByPrimaryKey(id);
        assertThat(product.getId(), equalTo(id));
        assertThat(product.getCategory_id(), notNullValue());
        assertThat(product.getPrice(), notNullValue());
        assertThat(product.getTitle(), notNullValue());
    }

    @Test
    void modifyProductInFoodCategoryTest() {
        ProductsExample example = new ProductsExample();
        List<Products> list = productMapper.selectByExample(example);
        long id = list.get(new Random().nextInt(list.size())).getId();
        Products product = productMapper.selectByPrimaryKey(id);
        String title = product.getTitle();
        product.setTitle("anyFood");
        productMapper.updateByPrimaryKey(product);
        session.commit();
        product = productMapper.selectByPrimaryKey(id);
        assertThat(product.getTitle(), equalTo("anyFood"));
        product.setTitle(title);
        productMapper.updateByPrimaryKey(product);
        session.commit();
    }

    @Test
    void getProductsInFoodCategoryTest() throws IOException {
        createProductInFoodCategoryTest();
        ProductsExample example = new ProductsExample();
        List<Products> list = productMapper.selectByExample(example);
        assertTrue(list.size() > 0);
        deleteProductByIdInFoodCategoryTest();
    }

    @Test
    void deleteProductByIdInFoodCategoryTest() {
        ProductsExample example = new ProductsExample();
        List<Products> list = productMapper.selectByExample(example);
        long id = list.get(new Random().nextInt(list.size())).getId();
        int n = productMapper.deleteByPrimaryKey(id);
        session.commit();
        assertThat(n, equalTo(1));
    }

    @AfterAll
    static void tearDown() {
        if (session != null) {
            session.close();
        }
    }

    private Products createProduct() {
        String[] products = new String[]{"Кефир", "Молоко", "Масло", "Творог", "Варенец"};
        Products product = new Products();
        product.setCategory_id(1L);
        product.setTitle(products[new Random().nextInt(5)]);
        product.setPrice(new Random().nextInt(900) + 100);
        return product;
    }
}