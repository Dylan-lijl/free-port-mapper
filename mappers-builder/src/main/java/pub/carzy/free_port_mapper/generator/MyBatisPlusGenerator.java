package pub.carzy.free_port_mapper.generator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * MyBatisPlus代码生成器
 *
 * @author macro
 * @date 2020/8/20
 */
public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir") + "/mappers-builder";
        String moduleName = "";
        // String removePrefix = scanner("去除前缀");
        String removePrefix = "_";
        String[] tableNames = scanner("表名，多个英文逗号分割").split(",");
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(initGlobalConfig(projectPath));
        autoGenerator.setDataSource(initDataSourceConfig());
        autoGenerator.setPackageInfo(initPackageConfig(moduleName));
        autoGenerator.setCfg(initInjectionConfig(autoGenerator, projectPath, moduleName));
        autoGenerator.setTemplate(initTemplateConfig());
        autoGenerator.setStrategy(initStrategyConfig(tableNames, removePrefix));
        autoGenerator.execute();
    }

    /**
     * 读取控制台内容信息
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String next = scanner.next();
            if (StrUtil.isNotEmpty(next)) {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 初始化全局配置
     */
    private static GlobalConfig initGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("Dylan Li");
        globalConfig.setOpen(false);
        globalConfig.setSwagger2(true);
        globalConfig.setBaseResultMap(true);
        globalConfig.setFileOverride(true);
        globalConfig.setSwagger2(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setEntityName("%s");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        return globalConfig;
    }

    /**
     * 初始化数据源配置
     */
    private static DataSourceConfig initDataSourceConfig() {
        Props props = new Props("generator.properties");
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(props.getStr("dataSource.url"));
        dataSourceConfig.setDriverName(props.getStr("dataSource.driverName"));
        dataSourceConfig.setUsername(props.getStr("dataSource.username"));
        dataSourceConfig.setPassword(props.getStr("dataSource.password"));
        //查询出Null字段
        dataSourceConfig.setDbQuery(new MySqlQuery() {
            @Override
            public String[] fieldCustom() {
                return new String[]{"Null", "Default"};
            }
        });
        return dataSourceConfig;
    }

    /**
     * 初始化包配置
     */
    private static PackageConfig initPackageConfig(String moduleName) {
        Props props = new Props("generator.properties");
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent(props.getStr("package.base"));
        packageConfig.setEntity("model");
        return packageConfig;
    }

    /**
     * 初始化模板配置
     */
    private static TemplateConfig initTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        //可以对controller、service、entity模板进行配置
        templateConfig.setController("vms/controller.java.vm");
        templateConfig.setService("vms/service.java.vm");
        templateConfig.setServiceImpl("vms/serviceImpl.java.vm");
        templateConfig.setEntity("vms/entity.java.vm");
        //mapper.xml模板需单独配置
        templateConfig.setXml(null);
        return templateConfig;
    }

    /**
     * 初始化策略配置
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames, String removePrefix) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        //当表名中带*号时可以启用通配符模式
        if (tableNames.length == 1 && tableNames[0].contains("*")) {
            strategyConfig.setLikeTable(new LikeTable(tableNames[0].replace("*", "")));
        } else {
            strategyConfig.setInclude(tableNames);
        }
        if (removePrefix != null && !"".equals(removePrefix)) {
            strategyConfig.setTablePrefix(removePrefix);
        }
        return strategyConfig;
    }

    /**
     * 初始化自定义配置
     */
    private static InjectionConfig initInjectionConfig(AutoGenerator autoGenerator, String projectPath, String moduleName) {
        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(new HashMap<>());
                // 可用于自定义属性
                this.getMap().put("parent", autoGenerator.getPackageInfo().getParent());
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig("/vms/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + moduleName
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        String dtoPackage = projectPath + "/src/main/java/" + autoGenerator.getPackageInfo().getParent().replace(".", "/")+"/dto";
        //-----------------  request ---------------------
        //list请求
        focList.add(new FileOutConfig("/vms/request/listReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "List" + tableInfo.getEntityName() + "Request" + ".java";
            }
        });
        //info请求
        focList.add(new FileOutConfig("/vms/request/infoReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "Info" + tableInfo.getEntityName() + "Request" + ".java";
            }
        });
        //create请求
        focList.add(new FileOutConfig("/vms/request/createReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "Create" + tableInfo.getEntityName() + "Request" + ".java";
            }
        });
        //Update请求
        focList.add(new FileOutConfig("/vms/request/updateReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "Update" + tableInfo.getEntityName() + "Request" + ".java";
            }
        });
        //state请求
        focList.add(new FileOutConfig("/vms/request/stateReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "Update" + tableInfo.getEntityName() + "StateRequest" + ".java";
            }
        });
        //delete请求
        focList.add(new FileOutConfig("/vms/request/deleteReq.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/request/" + "Delete" + tableInfo.getEntityName() + "Request" + ".java";
            }
        });
        //--------------  response --------
        //listRes
        focList.add(new FileOutConfig("/vms/response/listRes.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/response/" + "List" + tableInfo.getEntityName() + "Response" + ".java";
            }
        });
        //infoRes
        focList.add(new FileOutConfig("/vms/response/infoRes.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return dtoPackage + "/response/" + "Info" + tableInfo.getEntityName() + "Response" + ".java";
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }

}
