package lesson6;

import lesson6.db.dao.CategoriesMapper;
import lesson6.db.model.Categories;
import lesson6.db.model.CategoriesExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class ExampleMain {

    public static void main( String[] args ) throws IOException {
        SqlSession session = null;
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new
                    SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);
            CategoriesExample example = new CategoriesExample();

            example.createCriteria().andIdEqualTo(1);
            List<Categories> list = categoriesMapper.selectByExample(example);
            System.out.println(categoriesMapper.countByExample(example));

            Categories categories = new Categories();
            categories.setTitle("test");
            categoriesMapper.insert(categories);
            session.commit();

            CategoriesExample example2 = new CategoriesExample();
            example2.createCriteria().andTitleLike("%test%");
            List<Categories> list2 = categoriesMapper.selectByExample(example2);
            Categories categories2 = list2.get(0);
            categories2.setTitle("test100");
            categoriesMapper.updateByPrimaryKey(categories2);
            session.commit();

            categoriesMapper.deleteByPrimaryKey(categories2.getId());
            session.commit();

        } finally {
            session.close();
        }


    }
}