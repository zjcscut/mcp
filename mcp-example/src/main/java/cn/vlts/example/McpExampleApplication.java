package cn.vlts.example;


import cn.vlts.example.model.entity.Customer;
import cn.vlts.example.model.entity.Foo;
import cn.vlts.example.model.query.result.CustomerAnnoResult;
import cn.vlts.example.model.query.result.CustomerConfigResult;
import cn.vlts.example.model.query.result.CustomerResult;
import cn.vlts.example.repository.CustomerDao;
import cn.vlts.example.repository.FooDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author throwable
 * @version v1
 * @description main entry
 * @since 2023/12/21 16:21
 */
@SpringBootApplication(scanBasePackages = "cn.vlts")
@Slf4j
@RequiredArgsConstructor
public class McpExampleApplication implements CommandLineRunner {

    private final CustomerDao customerDao;

    private final FooDao fooDao;

    public static void main(String[] args) {
        SpringApplication.run(McpExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Foo foo = new Foo();
        foo.setName("bar");
        fooDao.insertSelective(foo);
        Foo bar = fooDao.selectOneById(1L);
        Assert.notNull(bar, "bar = 1");
        Customer customer = new Customer();
        customer.setCustomerName("doge-1");
        customer.setPhone("12345678901");
        customer.setCreateTime(new Date());
        int count = customerDao.insertSelective(customer);
        log.info("插入数据成功,更新记录数:{}", count);
        Customer c = customerDao.selectByPrimaryKey(1L);
        Assert.notNull(c, "id = 1");
        CustomerResult customerResult = customerDao.selectOneById(1L);
        CustomerAnnoResult annoResult = customerDao.selectOneAnnoById(1L);
        CustomerConfigResult configResult = customerDao.selectOneConfigById(1L);
        log.info("customerResult => {}", customerResult);
        log.info("annoResult => {}", annoResult);
        log.info("configResult => {}", configResult);
        Customer updater = new Customer();
        updater.setId(1L);
        updater.setCustomerName("doge-2");
        count = customerDao.updateByPrimaryKeySelective(updater);
        log.info("更新数据成功,更新记录数:{}", count);
        customerResult = customerDao.selectOneById(1L);
        annoResult = customerDao.selectOneAnnoById(1L);
        configResult = customerDao.selectOneConfigById(1L);
        log.info("更新后customerResult => {}", customerResult);
        log.info("更新后annoResult => {}", annoResult);
        log.info("更新后configResult => {}", configResult);
    }
}
