package ru.andnovikov.sportnow.mongobee;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.andnovikov.sportnow.mongobee.changelog.DatabaseChangelog;

@Configuration
public class MongoBeeConfig {

    // static final String DB_NAME = "heroku_88pd0w9v";
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Autowired
    private MongoClient mongo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Mongobee mongobee(Environment environment, ApplicationContext applicationContext) {
        Mongobee runner = new Mongobee(mongo);
        runner.setDbName(database);
        runner.setChangeLogsScanPackage(DatabaseChangelog.class.getPackage().getName());
        runner.setSpringEnvironment(environment);
        runner.setMongoTemplate(mongoTemplate);
        return runner;
    }

}
